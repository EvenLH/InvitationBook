import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class PersonCollection {

    HashMap<String, Person> thePersonMap;
    HashMap<String, String> theInterestSpellingMap;

    //Utilities
    String personFileName;
    String interestFileName;
    EventCollection correspondingEventCollection;
    Scanner userEntry;

    public PersonCollection(String pfn, String ifn, Scanner ue) {
        thePersonMap = new HashMap<>();
        theInterestSpellingMap = new HashMap<>();

        //Utilities
        personFileName = pfn;
        interestFileName = ifn;
        correspondingEventCollection = null;
        userEntry = ue;
    }//Method PersonCollection constructor

    public void loadPersons() {
        File personFile = new File(personFileName);
        Scanner personReader;

        try {
            personReader = new Scanner(personFile);
        }
        catch(FileNotFoundException fnfe) {
            System.out.println("* Person file: Not found");
            return;
        }

        while(personReader.hasNextLine()) {
            String loadString = personReader.nextLine().strip();
            thePersonMap.put(loadString.split(";")[0].strip().toLowerCase(),
                    new Person(loadString, this, userEntry));
        }
        System.out.println("* Person file: Found and loaded");

        personReader.close();
    }//Method loadPersons

    public void loadInterests() {
        File interestFile = new File(interestFileName);
        Scanner interestReader;

        try {
            interestReader = new Scanner(interestFile);
        }
        catch(FileNotFoundException fnfe) {
            System.out.println("* Interest file: Not found");
            return;
        }

        while(interestReader.hasNextLine()) {
            String loadString = interestReader.nextLine().strip();
            theInterestSpellingMap.put(loadString.toLowerCase(), loadString);
        }
        System.out.println("* Interest file: Found and loaded");

        interestReader.close();
    }//Methods loadInterests

    public void completeSetup(EventCollection ec) {
        correspondingEventCollection = ec;
    }//Method completeSetup

    //----------------------------------------------------------------
    public String toString() {
        int numberOfPersons = thePersonMap.size();

        if(numberOfPersons == 0) return "Persons: NONE";
        else if(numberOfPersons == 1) return "Person: 1";
        else return "Persons: " + numberOfPersons;
    }//Method toString

    public void viewPerson(String enteredHandle) {

        Set<String> handleNameSet = thePersonMap.keySet();

        if(CommonMethods.keyExistsIgnoreCase(enteredHandle, handleNameSet)) {
            thePersonMap.get(enteredHandle).viewThisPerson(theInterestSpellingMap);
        }
        else {
            listPersons();
            System.out.println("\nView person (/cancel to return to main menu)");

            do {
                System.out.print("- Enter handle: ");
                enteredHandle = userEntry.nextLine().strip().toLowerCase();
                if(CommonMethods.keyExistsIgnoreCase(enteredHandle, handleNameSet)) {
                    thePersonMap.get(enteredHandle).viewThisPerson(theInterestSpellingMap);
                    break;
                }
            }
            while(!enteredHandle.startsWith("/c"));
        }

    }//Method viewPerson

    public void viewInterest(String enteredInterest) {}//Method viewInterest

    public void listPersons() {
        int numberOfPersons = thePersonMap.size();

        //Printing the list headline
        if(numberOfPersons == 0) System.out.println("Persons: NONE");
        else if(numberOfPersons == 1) System.out.println("Person (1):");
        else System.out.println("Persons (" + numberOfPersons + "):");

        //Printing each person
        ArrayList<String> sortedKeyArray = CommonMethods.stringSetToOrderedArrayList(thePersonMap.keySet());
        for(int kIndex = 0; kIndex < sortedKeyArray.size(); kIndex++) {
            System.out.println("[P] " + thePersonMap.get(sortedKeyArray.get(kIndex)));
        }
    }//Method listPersons

    public String getPersonsToString(String p) {
        return thePersonMap.get(p).toString();
    }//Method getPersonsToString

    public void listInterests() {
        HashMap<String, Integer> tempCountMap = new HashMap<>();

        //Determining number of willing participants in each interest
        for(Person person: thePersonMap.values()) {
            for(Map.Entry<String, Integer> interestEntry: person.getInterestMap().entrySet()) {

                tempCountMap.putIfAbsent(interestEntry.getKey(), 0);

                if(interestEntry.getValue() >= 1) {
                    tempCountMap.merge(interestEntry.getKey(), 1, Integer::sum);
                }

            }
        }

        //Printing the list headline
        int numberOfInterests = tempCountMap.size();
        if(numberOfInterests == 0) System.out.println("Interests: NONE");
        else System.out.println("Interests (with number of willing, interested or highly interested people):");

        //Printing each interest
        ArrayList<String> sortedKeyArray = CommonMethods.stringSetToOrderedArrayList(tempCountMap.keySet());
        for(int kIndex = 0; kIndex < sortedKeyArray.size(); kIndex++) {
            String lowerCaseKey = sortedKeyArray.get(kIndex);
            String printCaseKey = lowerCaseKey;
            if(theInterestSpellingMap.containsKey(lowerCaseKey)) printCaseKey = theInterestSpellingMap.get(lowerCaseKey);

            if(tempCountMap.get(lowerCaseKey) == 0) System.out.println("[I] " + printCaseKey + ": NONE");
            else if(tempCountMap.get(lowerCaseKey) == 1) System.out.println("[I] " + printCaseKey + ": 1 person");
            else System.out.println("[I] " + printCaseKey + ": " + tempCountMap.get(lowerCaseKey) + " persons");
        }

    }//Method listInterests

//----------------------------------------------------------------
    public void storePersons() {
        FileWriter storageFile;

        try {
            storageFile = new FileWriter(personFileName, false);

            for(String key: thePersonMap.keySet()) {
                storageFile.write(
                        thePersonMap.get(key).getStorageString() + "\n"
                );
            }

            storageFile.close();
            System.out.println("* Person file: Update complete");
        }
        catch(IOException ioe) {
            System.out.println("* Person file: Possibly failed or incomplete update\n" +
                    "   * " + ioe
            );
        }
    }//Method storePersons

    public void storeInterests() {
        FileWriter storageFile;

        try {
            storageFile = new FileWriter(interestFileName, false);

            for(String key: theInterestSpellingMap.keySet()) {
                storageFile.write(theInterestSpellingMap.get(key) + "\n");
            }

            storageFile.close();
            System.out.println("* Interest file: Update complete");
        }
        catch(IOException ioe) {
            System.out.println("* Interest file: Possibly failed or incomplete update\n" +
                    "   * " + ioe
            );
        }
    }//Method storeInterests

//----------------------------------------------------------------
}//Class PersonCollection