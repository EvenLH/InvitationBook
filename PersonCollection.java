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
            if(CommonMethods.stringIsValidNewHandleName(enteredHandle, handleNameSet)) {
                System.out.println("\nThere is no such person: " + enteredHandle);
            }
            System.out.println("View person from the list above (/cancel to return to main menu)");

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

    public void viewInterest(String enteredInterest) {

        /*If the entered interest string could possibly represent an interest,
        check whether any person actually has an entry for it.
        If any of them does, call viewExistingInterest and then return to main menu.*/
        if(CommonMethods.stringIsSafeWithLength(enteredInterest)) {
            String enteredInterestLowerCase = enteredInterest.toLowerCase();

            for(Person p: thePersonMap.values()) {
                if(p.getInterestExistence(enteredInterestLowerCase)) {
                    viewExistingInterest(enteredInterestLowerCase);
                    return;
                }
            }
        }

        /*If no valid and existing interest entry was entered, we show the user all existing interests,
        then ask them to enter one of them (any case).
        This could possibly be written cleaner, but testing seems to show that it works as intended.
        Probably not important.*/
        listInterests();
        System.out.println("View interest from the list above (/cancel to return to main menu)");

        String enteredInterestLowerCase;
        do {
            System.out.print("- Enter interest: ");
            enteredInterestLowerCase = userEntry.nextLine().strip().toLowerCase();

            if(!enteredInterestLowerCase.startsWith("/c")) {
                for(Person p: thePersonMap.values()) {
                    if(p.getInterestExistence(enteredInterestLowerCase)) {
                        viewExistingInterest(enteredInterestLowerCase);
                        return;
                    }
                }
            }
        }
        while(!enteredInterestLowerCase.startsWith("/c"));
    }//Method viewInterest

    public void viewExistingInterest(String existingInterestLowerCase) {

        ArrayList<String> int3Ordered = new ArrayList<>();
        ArrayList<String> int2Ordered = new ArrayList<>();
        ArrayList<String> int1Ordered = new ArrayList<>();
        ArrayList<String> int0Ordered = new ArrayList<>();
        ArrayList<String> notFoundOrdered = new ArrayList<>();

        for(Map.Entry<String, Person> interestedPerson: thePersonMap.entrySet()) {
            Integer interestValue = interestedPerson.getValue().getInterestValue(existingInterestLowerCase);

            if(interestValue == null) {
                CommonMethods.stringIntoOrderedArrayList(interestedPerson.getKey(), notFoundOrdered);
                continue;
            }

            switch(interestValue) {
                case 3:
                    CommonMethods.stringIntoOrderedArrayList(interestedPerson.getKey(), int3Ordered);
                    break;
                case 2:
                    CommonMethods.stringIntoOrderedArrayList(interestedPerson.getKey(), int2Ordered);
                    break;
                case 1:
                    CommonMethods.stringIntoOrderedArrayList(interestedPerson.getKey(), int1Ordered);
                    break;
                case 0:
                    CommonMethods.stringIntoOrderedArrayList(interestedPerson.getKey(), int0Ordered);
                    break;
            }
        }

        //Printing headline
        System.out.println("Interest: " + theInterestSpellingMap.getOrDefault(existingInterestLowerCase, existingInterestLowerCase));

        //Printing information
        if(!int3Ordered.isEmpty()) {
            int numberOfInterestedPersons = int3Ordered.size();
            System.out.println("Highly interested (" + numberOfInterestedPersons + "):");
            for(int i = 0; i < numberOfInterestedPersons; i++) {
                System.out.println("[3] " + thePersonMap.get(int3Ordered.get(i)));
            }
        }

        if(!int2Ordered.isEmpty()) {
            int numberOfInterestedPersons = int2Ordered.size();
            System.out.println("Interested (" + numberOfInterestedPersons + "):");
            for(int i = 0; i < numberOfInterestedPersons; i++) {
                System.out.println("[2] " + thePersonMap.get(int2Ordered.get(i)));
            }
        }

        if(!int1Ordered.isEmpty()) {
            int numberOfInterestedPersons = int1Ordered.size();
            System.out.println("Willing (" + numberOfInterestedPersons + "):");
            for(int i = 0; i < numberOfInterestedPersons; i++) {
                System.out.println("[1] " + thePersonMap.get(int1Ordered.get(i)));
            }
        }

        if(!int0Ordered.isEmpty()) {
            int numberOfUninterestedPersons = int0Ordered.size();
            System.out.println("\nUninterested (" + numberOfUninterestedPersons + "):");
            for(int i = 0; i < numberOfUninterestedPersons; i++) {
                System.out.println("[0] " + thePersonMap.get(int0Ordered.get(i)));
            }
        }

        if(!notFoundOrdered.isEmpty()) {
            int numberOfUnknownPersons = notFoundOrdered.size();
            System.out.println("\nUnknown interest (" + numberOfUnknownPersons + "):");
            for(int i = 0; i < numberOfUnknownPersons; i++) {
                System.out.println("[?] " + thePersonMap.get(notFoundOrdered.get(i)));
            }
        }
    }//Method viewExistingInterest

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

    public String getInterestCorrectCase(String i) {
        return theInterestSpellingMap.getOrDefault(i, i);
    }//Method getInterestCorrectCase

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