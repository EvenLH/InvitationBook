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
            thePersonMap.put(loadString.split(";")[0],
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

    public void listPersons() {
        int numberOfPersons = thePersonMap.size();

        //Printing the list headline
        if(numberOfPersons == 0) System.out.println("Persons: NONE");
        else if(numberOfPersons == 1) System.out.println("Person (1):");
        else System.out.println("Persons (" + numberOfPersons + "):");

        //Printing each person
        ArrayList<String> sortedKeyArray = sortKeys(thePersonMap.keySet());
        for(int kIndex = 0; kIndex < sortedKeyArray.size(); kIndex++) {
            System.out.println("[P] " + thePersonMap.get(sortedKeyArray.get(kIndex)));
        }
    }//Method listPersons

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
        ArrayList<String> sortedKeyArray = sortKeys(tempCountMap.keySet());
        for(int kIndex = 0; kIndex < sortedKeyArray.size(); kIndex++) {
            String lowerCaseKey = sortedKeyArray.get(kIndex);
            String printCaseKey = lowerCaseKey;
            if(theInterestSpellingMap.containsKey(lowerCaseKey)) printCaseKey = theInterestSpellingMap.get(lowerCaseKey);

            if(tempCountMap.get(lowerCaseKey) == 0) System.out.println("[I] " + printCaseKey + ": NONE");
            else if(tempCountMap.get(lowerCaseKey) == 1) System.out.println("[I] " + printCaseKey + ": 1 person");
            else System.out.println("[I] " + printCaseKey + ": " + tempCountMap.get(lowerCaseKey) + " persons");
        }

    }//Method listInterests

    public ArrayList<String> sortKeys(Set<String> ks) {
        int numberOfElements = ks.size();
        ArrayList<String> sortedKeys = new ArrayList<>(numberOfElements);

        for(String key: ks) {
            if(sortedKeys.isEmpty()) sortedKeys.add(key);
            else if(key.compareTo(sortedKeys.get(sortedKeys.size() -1)) >= 0) sortedKeys.add(key);
            else {
                for(int i = 0; i < sortedKeys.size(); i++) {
                    if(key.compareTo(sortedKeys.get(i)) < 0) {
                        sortedKeys.add(i, key);
                        break;
                    }
                }//Loop for (inner)
            }
        }//Loop for (outer)

        return sortedKeys;
    }//Method sortKeys

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