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

    public void completeSetup(EventCollection ec) {
        correspondingEventCollection = ec;

        loadPersons();
        loadInterests();
    }//Method completeSetup

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

//----------------------------------------------------------------
    public String toString() {
        int numberOfPersons = thePersonMap.size();

        if(numberOfPersons == 0) return "Persons: NONE";
        else if(numberOfPersons == 1) return "Person: 1";
        else return "Persons: " + numberOfPersons;
    }//Method toString

    public void makePerson() {

        String newHandle;

        System.out.println("Enter an eligible handle name for the new person (/list to view persons, /cancel to return to main menu)");
        do {
            System.out.print("- Enter handle: ");
            newHandle = userEntry.nextLine().strip();

            if(newHandle.toLowerCase().startsWith("/l")) {
                listPersons();
                continue;
            }
            else if(newHandle.toLowerCase().startsWith("/c")) {
                System.out.println("Canceling making a new person.");
                return;
            }
        }
        while(!CommonMethods.stringIsValidNewHandleName(newHandle, null, thePersonMap.keySet()));

        ArrayList<String> newNames = new ArrayList<>(4);
        newNames.add(newHandle);
        for(int i = 1; i <= 3; i++) newNames.add(null);

        Person newPerson = new Person(newNames, this, userEntry);
        String newKeyAfterEdit = newPerson.editThisPerson(thePersonMap.keySet());

        thePersonMap.put(newKeyAfterEdit.toLowerCase(), newPerson);
        System.out.println("New person made: " + newPerson);
    }//Method makePerson

    public void editPerson(String enteredHandle) {
        final String existingPersonKey = userFindsExistingPersonKey(enteredHandle); //Always lower case unless null.

        if(existingPersonKey == null) return;
        String newKeyAfterEdit = thePersonMap.get(existingPersonKey).editThisPerson(thePersonMap.keySet()).toLowerCase();

        if(newKeyAfterEdit.equals(existingPersonKey)) {
            System.out.println("Person edited: " + thePersonMap.get(existingPersonKey));
        }
        else {
            thePersonMap.put(newKeyAfterEdit, thePersonMap.remove(existingPersonKey));
            correspondingEventCollection.updateHandleWhereInvited(existingPersonKey, newKeyAfterEdit);
            System.out.println("Person edited: " + thePersonMap.get(newKeyAfterEdit));
        }
    }//Method editPerson

    public void removePerson(String enteredHandle) {
        String existingPersonKey = userFindsExistingPersonKey(enteredHandle);

        if(existingPersonKey == null) return;

        correspondingEventCollection.removePersonWhereInvited(existingPersonKey);
        System.out.println("Removed person: " + thePersonMap.remove(existingPersonKey));
    }//Method removePerson

    public void viewPerson(String enteredHandle) {
        String existingPersonKey = userFindsExistingPersonKey(enteredHandle);

        if(existingPersonKey == null) return;

        thePersonMap.get(existingPersonKey).viewThisPerson(theInterestSpellingMap);
    }//Method viewPerson

    public void removeInterest(String enteredInterestName) {
        String interestNameLowerCase = enteredInterestName.toLowerCase();

        //Removing the interest from each person.
        boolean someoneHadThisInterest = false;
        for(Person p: thePersonMap.values()) {

            if(!(someoneHadThisInterest) && p.getInterestExistence(interestNameLowerCase)) {
                someoneHadThisInterest = true;
            }

            p.removeAnInterest(interestNameLowerCase);
        }

        //Removing the interest capitalisation.
        String correctCap = theInterestSpellingMap.remove(interestNameLowerCase);

        //Giving feedback to user.
        if(correctCap != null) {
            System.out.println("Interest removed: " + correctCap);
        }
        else if(someoneHadThisInterest) {
            System.out.println("Interest removed: " + interestNameLowerCase);
        }
        else System.out.println("Interest didn't exist: " + enteredInterestName);
    }//Method removeInterest

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
                CommonMethods.stringInsertedIntoOrderedArrayList(interestedPerson.getKey(), notFoundOrdered);
                continue;
            }

            switch(interestValue) {
                case 3:
                    CommonMethods.stringInsertedIntoOrderedArrayList(interestedPerson.getKey(), int3Ordered);
                    break;
                case 2:
                    CommonMethods.stringInsertedIntoOrderedArrayList(interestedPerson.getKey(), int2Ordered);
                    break;
                case 1:
                    CommonMethods.stringInsertedIntoOrderedArrayList(interestedPerson.getKey(), int1Ordered);
                    break;
                case 0:
                    CommonMethods.stringInsertedIntoOrderedArrayList(interestedPerson.getKey(), int0Ordered);
                    break;
            }
        }

        //Printing headline
        System.out.println("Interest: " + theInterestSpellingMap.getOrDefault(existingInterestLowerCase, existingInterestLowerCase));

        //Printing information
        if(!int3Ordered.isEmpty()) {
            int numberOfInterestedPersons = int3Ordered.size();
            System.out.println("\nHighly interested (" + numberOfInterestedPersons + "):");
            for(int i = 0; i < numberOfInterestedPersons; i++) {
                System.out.println("[3] " + thePersonMap.get(int3Ordered.get(i)));
            }
        }

        if(!int2Ordered.isEmpty()) {
            int numberOfInterestedPersons = int2Ordered.size();
            System.out.println("\nInterested (" + numberOfInterestedPersons + "):");
            for(int i = 0; i < numberOfInterestedPersons; i++) {
                System.out.println("[2] " + thePersonMap.get(int2Ordered.get(i)));
            }
        }

        if(!int1Ordered.isEmpty()) {
            int numberOfInterestedPersons = int1Ordered.size();
            System.out.println("\nWilling (" + numberOfInterestedPersons + "):");
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

    public HashMap<String, String> getTheInterestSpellingMap() {
        return theInterestSpellingMap;
    }

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
        if(!CommonMethods.keyExistsIgnoreCase(p, thePersonMap.keySet())) {
            return "No person found.";
        }
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

    public boolean interestSpellingKeyExistsIgnoreCase(String k) {
        return theInterestSpellingMap.containsKey(k.toLowerCase());
    }//Method interestSpellingKeyExistsIgnoreCase

    public void fillInterestCapitalisation(String s) {
        theInterestSpellingMap.putIfAbsent(s.toLowerCase(), s);
    }//Method fillInterestCapitalisation

    public String getInterestCorrectCase(String i) {
        String iLower = i.toLowerCase();
        return theInterestSpellingMap.getOrDefault(iLower, iLower);
    }//Method getInterestCorrectCase

    public String userFindsExistingPersonKey(String h) {
        Set<String> handleSet = thePersonMap.keySet();

        if(handleSet.isEmpty()) {
            System.out.println("There are no persons in the person list.");
            return null;
        }
        else if(CommonMethods.keyExistsIgnoreCase(h, handleSet)) {
            return h.toLowerCase();
        }

        System.out.println("Enter existing handle name (/list to see all handles, /cancel to return to main menu)");
        do {
            System.out.print("- Enter handle: ");
            h = userEntry.nextLine().strip().toLowerCase();

            if(h.startsWith("/l")) listPersons();
            else if(h.startsWith("/c")) return null;
            else if(!handleSet.contains(h)) {
                System.out.println("Non-existing or invalid handle.");
            }
        }
        while(!CommonMethods.keyExistsIgnoreCase(h, handleSet));

        return h;
    }//Method userFindsExistingPersonKey

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