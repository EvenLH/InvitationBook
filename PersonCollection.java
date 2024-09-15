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

    public void manageInterest() {

        String selectedInterestLowerCase;
        boolean userIsStillDeciding = true;

        System.out.println("Enter a new or existing interest (/list to see all interests, /cancel to return to main menu).");
        do {
            System.out.print("- Enter interest: ");
            selectedInterestLowerCase = userEntry.nextLine().strip().toLowerCase();

            if(selectedInterestLowerCase.startsWith("/l")) {
                listInterests();
                continue;
            }
            else if(selectedInterestLowerCase.startsWith("/c")) {
                return;
            }
            else if(CommonMethods.stringIsSafeWithLength(selectedInterestLowerCase)) {
                if(interestExists(selectedInterestLowerCase)) {
                    System.out.println("You have selected an existing interest. Continue managing this interest?");
                }
                else {
                    System.out.println("You have entered a new interest. Continue making a new interest?");
                }
            }
            else {
                System.out.println("That is not a valid interest name.");
                continue;
            }

            System.out.print("- Enter answer (yes / no): ");
            String ans = userEntry.nextLine().strip().toLowerCase();
            if(ans.startsWith("y")) userIsStillDeciding = false;
        }
        while(userIsStillDeciding);

        if(interestExists(selectedInterestLowerCase)) editInterest(selectedInterestLowerCase);
        else makeInterest(selectedInterestLowerCase);
    }//Method manageInterest

    public void makeInterest(String newInterestNameLowerCase) {

        System.out.println("Making interest: " + newInterestNameLowerCase + "\n" +
                "Commands:\n" +
                "[C] /spelling [interest name how you want it capitalised, or 'clear']\n" +
                "[C] /all [number 0-3, or 'clear'] (gives all persons the interest with this interest level. 'Clear' if you don't want to do this after all.)\n" +
                "[C] /examine (shows you the values you have currently entered)" +
                "[C] /conclude (applies changes and returns to main menu)\n"
        );

        String newSpelling = null;
        Integer sweepingValue = null;

        String commandEntry;
        String[] commandArray;
        do {
            System.out.print("- Enter command: ");
            commandEntry = userEntry.nextLine().strip();
            commandArray = CommonMethods.commandStringToArray(commandEntry, 2);

            if(commandEntry.toLowerCase().startsWith("/s")) {
                if(commandArray.length <= 1) System.out.println("/spelling must be followed by the name of the interest, except that you should use the capitalisation you want.");
                else if(commandArray[1].equalsIgnoreCase(newInterestNameLowerCase)) {
                    newSpelling = commandArray[1];
                }
                else if(commandArray[1].toLowerCase().startsWith("c")) {
                    newSpelling = null;
                }
                else System.out.println("/spelling must be followed by the name of the interest, except that you should use the capitalisation you want.");
            }
            else if(commandEntry.toLowerCase().startsWith("/a")) {
                if(commandArray.length <= 1) System.out.println("/all must be followed by a number from 0 to 3, or the 'cancel' option.");
                else if(CommonMethods.stringIsIntInRange(commandArray[1], 0, 3)) {
                    sweepingValue = Integer.parseInt(commandArray[1]);
                }
                else if(commandArray[1].toLowerCase().startsWith("c")) {
                    sweepingValue = null;
                }
                else System.out.println("/all must be followed by a number from 0 to 3, or the 'cancel' option.");
            }
            else if(commandEntry.toLowerCase().startsWith("/e")) {
                if(newSpelling == null) System.out.println("[*] Capitalisation: " + newInterestNameLowerCase);
                else System.out.println("[*] Capitalisation: " + newSpelling);
                if(sweepingValue == null) System.out.println("No persons will receive a value for this interest.");
                else System.out.println("[*] Interest value set for all persons: " + sweepingValue);
            }
            else if(!commandEntry.toLowerCase().startsWith("/c")) {
                System.out.println("Making interest: " + newSpelling + "\n" +
                        "Commands:\n" +
                        "[C] /spelling [interest name how you want it capitalised, or 'clear']\n" +
                        "[C] /all [number 0-3, or 'clear'] (gives all persons the interest with this interest level. 'Cancel' if you don't want to do this after all.)\n" +
                        "[C] /examine (shows you the values you have currently entered)" +
                        "[C] /conclude (applies any changes and returns to main menu)\n"
                );
            }
        }
        while(!commandEntry.toLowerCase().startsWith("/c"));

        if(newSpelling != null) theInterestSpellingMap.put(newInterestNameLowerCase, newSpelling);

        if(sweepingValue != null) {
            for(Person p: thePersonMap.values()) {
                p.addInterest(newInterestNameLowerCase, sweepingValue);
            }
        }

        System.out.println("Finished making interest: " + theInterestSpellingMap.getOrDefault(newInterestNameLowerCase, newInterestNameLowerCase));
    }//Method makeInterest

    public void editInterest(String existingInterestNameLowerCase) {

        System.out.println("Managing interest: " + theInterestSpellingMap.get(existingInterestNameLowerCase) + "\n" +
                "Commands:\n" +
                "[C] /spelling [interest name how you want it capitalised, or 'clear']\n" +
                "[C] /all [number 0-3, or 'clear'] (gives all persons the interest with this interest level. 'Clear' if you don't want to do this after all.)\n" +
                "[C] /defined [number 0-3, or 'clear'] (applies only to the persons who already have the interest.)\n" +
                "[C] /undefined [number 0-3, or 'clear'] (applies only to the persons who don't have the interest.)\n" +
                "[C] /examine (shows you the values you have currently entered)" +
                "[C] /conclude (applies changes and returns to main menu)\n"
        );

        String newSpelling = null;
        Integer alreadyDefinedNewValue = null;
        Integer undefinedNewValue = null;

        String commandEntry;
        String[] commandArray;
        do {
            System.out.print("- Enter command: ");
            commandEntry = userEntry.nextLine().strip();
            commandArray = CommonMethods.commandStringToArray(commandEntry, 2);

            if(commandEntry.toLowerCase().startsWith("/s")) {
                if(commandArray.length <= 1) System.out.println("/spelling must be followed by the name of the interest, except that you should use the capitalisation you want.");
                else if(commandArray[1].equalsIgnoreCase(existingInterestNameLowerCase)) {
                    newSpelling = commandArray[1];
                }
                else if(commandArray[1].toLowerCase().startsWith("c")) {
                    newSpelling = null;
                }
                else System.out.println("/spelling must be followed by the name of the interest, except that you should use the capitalisation you want.");
            }
            else if(commandEntry.toLowerCase().startsWith("/a")) {
                if(commandArray.length <= 1) System.out.println("/all must be followed by a number from 0 to 3, or the 'cancel' option.");
                else if(CommonMethods.stringIsIntInRange(commandArray[1], 0, 3)) {
                    alreadyDefinedNewValue = Integer.parseInt(commandArray[1]);
                    undefinedNewValue = Integer.parseInt(commandArray[1]);
                }
                else if(commandArray[1].toLowerCase().startsWith("c")) {
                    alreadyDefinedNewValue = null;
                    undefinedNewValue = null;
                }
                else System.out.println("/all must be followed by a number from 0 to 3, or the 'cancel' option.");
            }
            else if(commandEntry.toLowerCase().startsWith("/d")) {
                if(commandArray.length <= 1) System.out.println("/defined must be followed by a number from 0 to 3, or the 'cancel' option.");
                else if(CommonMethods.stringIsIntInRange(commandArray[1], 0, 3)) {
                    alreadyDefinedNewValue = Integer.parseInt(commandArray[1]);
                }
                else if(commandArray[1].toLowerCase().startsWith("c")) {
                    alreadyDefinedNewValue = null;
                }
                else System.out.println("/defined must be followed by a number from 0 to 3, or the 'cancel' option.");
            }
            else if(commandEntry.toLowerCase().startsWith("/u")) {
                if(commandArray.length <= 1) System.out.println("/undefined must be followed by a number from 0 to 3, or the 'cancel' option.");
                else if(CommonMethods.stringIsIntInRange(commandArray[1], 0, 3)) {
                    undefinedNewValue = Integer.parseInt(commandArray[1]);
                }
                else if(commandArray[1].toLowerCase().startsWith("c")) {
                    undefinedNewValue = null;
                }
                else System.out.println("/undefined must be followed by a number from 0 to 3, or the 'cancel' option.");
            }
            else if(commandEntry.toLowerCase().startsWith("/e")) {
                if(newSpelling == null) System.out.println("[*] Capitalisation: " + existingInterestNameLowerCase);
                else System.out.println("[*] Capitalisation: " + newSpelling);
                if(alreadyDefinedNewValue == null) System.out.println("No existing interest entries will have their value changed for this interest.");
                else System.out.println("[*] Interest value changed for all persons who already had the interest: " + alreadyDefinedNewValue);
                if(undefinedNewValue == null) System.out.println("No new persons will receive a value for this interest.");
                else System.out.println("[*] Interest value set for all persons who did not have the interest: " + undefinedNewValue);
            }
            else if(!commandEntry.toLowerCase().startsWith("/c")) {
                System.out.println("Managing interest: " + newSpelling + "\n" +
                        "Commands:\n" +
                        "[C] /spelling [interest name how you want it capitalised, or 'clear']\n" +
                        "[C] /all [number 0-3, or 'clear'] (gives all persons the interest with this interest level. 'Clear' if you don't want to do this after all.)\n" +
                        "[C] /defined [number 0-3, or 'clear'] (applies only to the persons who already have the interest.)\n" +
                        "[C] /undefined [number 0-3, or 'clear'] (applies only to the persons who don't have the interest.)\n" +
                        "[C] /examine (shows you the values you have currently entered)" +
                        "[C] /conclude (applies changes and returns to main menu)\n"
                );
            }
        }
        while(!commandEntry.toLowerCase().startsWith("/c"));

        if(newSpelling != null) theInterestSpellingMap.put(existingInterestNameLowerCase, newSpelling);

        if(alreadyDefinedNewValue != null && alreadyDefinedNewValue == undefinedNewValue) {
            for(Person p: thePersonMap.values()) {
                p.addInterest(existingInterestNameLowerCase, alreadyDefinedNewValue);
            }
        }
        else {
            if(alreadyDefinedNewValue != null) {
                for(Person p: thePersonMap.values()) {
                    if(p.hasInterest(existingInterestNameLowerCase)) {
                        p.addInterest(existingInterestNameLowerCase, alreadyDefinedNewValue);
                    }
                }
            }

            if(undefinedNewValue != null) {
                for(Person p: thePersonMap.values()) {
                    if(!p.hasInterest(existingInterestNameLowerCase)) {
                        p.addInterest(existingInterestNameLowerCase, undefinedNewValue);
                    }
                }
            }
        }

        System.out.println("Finished managing interest: " + theInterestSpellingMap.getOrDefault(existingInterestNameLowerCase, existingInterestNameLowerCase));
    }//Method editInterest

    public void removeInterest(String enteredInterestName) {
        String existingInterestLowerCase = userFindsExistingInterestKey(enteredInterestName);

        if(existingInterestLowerCase == null) return;

        //Removing the interest from each person.
        for(Person p: thePersonMap.values()) {
            p.removeAnInterest(existingInterestLowerCase);
        }

        //Removing the interest capitalisation.
        String correctCase = theInterestSpellingMap.remove(existingInterestLowerCase);

        if(correctCase == null) {
            System.out.println("Interest removed: " + existingInterestLowerCase);
        }
        else System.out.println("Interest removed: " + correctCase);
    }//Method removeInterest

    public void viewInterest(String enteredInterest) {

        /*If the entered interest string could possibly represent an interest,
        check whether any person actually has an entry for it.
        If any of them does, call viewExistingInterest and then return to main menu.*/
        if(CommonMethods.stringIsSafeWithLength(enteredInterest)) {
            String enteredInterestLowerCase = enteredInterest.toLowerCase();

            for(Person p: thePersonMap.values()) {
                if(p.hasInterest(enteredInterestLowerCase)) {
                    viewExistingInterest(enteredInterestLowerCase);
                    return;
                }
            }
        }

        /*If no existing interest entry was entered, we show the user all existing interests,
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
                    if(p.hasInterest(enteredInterestLowerCase)) {
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

    public void wipePersons() {
        if(thePersonMap.isEmpty()) {
            System.out.println("There were no persons to delete.");
            return;
        }

        correspondingEventCollection.wipeInvitationsAllEvents();
        thePersonMap.clear();
        System.out.println("Removed all persons and all their event invitations.");
    }//Method wipePersons

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

    public void wipeInterests() {
        for(Person p: thePersonMap.values()) {
            p.wipePersonalInterests();
        }

        theInterestSpellingMap.clear();
        System.out.println("Removed all interests.");
    }//Method wipeInterests

    //Let's look at this method. What if spelling exists, but no one has the interest?
    public void listInterests() {
        HashMap<String, Integer> tempCountMap = new HashMap<>();

        //Ensuring interests are included when a capitalisation is defined for them, even if no one has that interest.
        for(String iCaseKey: theInterestSpellingMap.keySet()) {
            tempCountMap.put(iCaseKey, 0);
        }

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

    public String userFindsExistingInterestKey(String i) {
        String iLower;

        if(i != null) {
            iLower = i.toLowerCase();

            if(interestExists(iLower)) return iLower;
        }

        System.out.println("Enter existing interest (/list to see all interests, /cancel to return to main menu)");
        do {
            System.out.print("- Enter interest: ");
            iLower = userEntry.nextLine().strip().toLowerCase();

            if(iLower.startsWith("/l")) listInterests();
            else if(iLower.startsWith("/c")) return null;
            else if(!interestExists(iLower)) {
                System.out.print("Non-existing or invalid interest name.");
            }
        }
        while(!interestExists(iLower));

        return iLower;
    }//Method userFindsExistingInterestKey

    public boolean interestExists(String interestNameLowerCase) {
        if(theInterestSpellingMap.containsKey(interestNameLowerCase)) return true;

        for(Person p: thePersonMap.values()) {
            if(p.hasInterest(interestNameLowerCase)) return true;
        }

        return false;
    }//Method interestExists

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