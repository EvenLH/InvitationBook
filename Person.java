import java.util.*;

public class Person implements Comparable<Person> {

    ArrayList<String> nameArray; //Index 0-3: Handle, first, middle, last
    HashMap<String, Integer> interestMap;

    //Utilities
    PersonCollection myPersonCollection;
    Scanner userEntry;

    public Person(String storageString, PersonCollection pc, Scanner ue) {
        nameArray = new ArrayList<>(4);
        interestMap = new HashMap<>();

        //Utilities
        myPersonCollection = pc;
        userEntry = ue;

        String[] temp = storageString.strip().split(";");

        //Filling the name array list
        for(int i = 0; i <= 3; i++) {
            if(temp[i].equalsIgnoreCase("null")) {
                nameArray.add(null);
            }
            else nameArray.add(temp[i]);
        }

        //Filling the interest hash map
        for(int j = 4; j < temp.length; j = j+2) {
            interestMap.put(temp[j].toLowerCase(), Integer.parseInt(temp[j+1]));
        }

    }//Method Person constructor 1

    public Person(ArrayList<String> names, PersonCollection pc, Scanner ue) {
        nameArray = names;
        interestMap = new HashMap<>();

        myPersonCollection = pc;
        userEntry = ue;
    }//Method Person constructor 2

//----------------------------------------------------------------
    public String toString() {
        String repString = nameArray.get(0);

        boolean nameHasBeenAdded = false;
        for(int i = 1; i <= 3; i++) {
            if(!(nameArray.get(i) == null)) {
                if(!nameHasBeenAdded) {
                    repString = repString.concat(" (");
                    nameHasBeenAdded = true;
                }
                else repString = repString.concat(" ");

                repString = repString.concat(nameArray.get(i));
            }
        }
        if(nameHasBeenAdded) repString = repString.concat(")");

        return repString;
    }//Method toString

    public String editThisPerson(Set<String> handleSet) {
        final String handleNameAtStart = nameArray.get(0);

        String editEntryCommand;
        String[] editEntryArray;

        do {
            System.out.print("\n- Edit person entry: ");
            editEntryCommand = userEntry.nextLine();

            //Preparing the command for use.
            if(editEntryCommand.toLowerCase().startsWith("/i")) {

                //Not using this method exactly as it was made for, but it works.
                String[] tempArray = CommonMethods.commandStringToArray(editEntryCommand, 2);

                if(tempArray.length >= 2 && tempArray[1].contains(" ")) {
                    int indexSeparatingNameAndValue = tempArray[1].lastIndexOf(" ");
                    editEntryArray = new String[3];
                    editEntryArray[0] = tempArray[0].toLowerCase();
                    editEntryArray[1] = tempArray[1].substring(0, indexSeparatingNameAndValue);
                    editEntryArray[2] = tempArray[1].substring(indexSeparatingNameAndValue+1);
                }
                else {
                    System.out.println("/interest must be followed by the name of an interest (existing or new), and finally a value (0 to 3).");
                    editEntryArray = new String[]{"?"};
                    continue;
                }

            }
            else if(editEntryCommand.toLowerCase().startsWith("/r")) editEntryArray = CommonMethods.commandStringToArray(editEntryCommand, 3);
            else editEntryArray = CommonMethods.commandStringToArray(editEntryCommand, 2);

            editEntryArray[0] = editEntryArray[0].toLowerCase();

            //Performing the contents of the command.
            if(editEntryArray[0].startsWith("/h")) {
                if(editEntryArray.length == 2
                && CommonMethods.stringIsValidNewHandleName(editEntryArray[1], handleNameAtStart, handleSet)) {
                    nameArray.set(0, editEntryArray[1]);
                    System.out.println("Handle name set: " + nameArray.get(0));
                }
                else
                    System.out.println("Unavailable or invalid new handle name.");
            }

            else if(editEntryArray[0].startsWith("/f")) {
                if(editEntryArray.length == 2
                && CommonMethods.stringIsSafe(editEntryArray[1])) {
                    nameArray.set(1, editEntryArray[1]);
                    System.out.println("First names set: " + nameArray.get(1));
                }
                else if(editEntryArray.length == 2)
                    System.out.println("Can't start with '/', contain ';' or be 'null' in any capitalisation.");
                else
                    System.out.println("To delete first names: /remove first");
            }

            else if(editEntryArray[0].startsWith("/m")) {
                if(editEntryArray.length == 2
                && CommonMethods.stringIsSafe(editEntryArray[1])) {
                    nameArray.set(2, editEntryArray[1]);
                    System.out.println("Middle names set: " + nameArray.get(2));
                }
                else if(editEntryArray.length == 2)
                    System.out.println("Can't contain ';', start with '/', or be 'null' in any capitalisation.");
                else
                    System.out.println("To delete middle names: /remove middle");
            }

            else if(editEntryArray[0].startsWith("/l")) {
                if(editEntryArray.length == 2
                && CommonMethods.stringIsSafeNoSpace(editEntryArray[1])) {
                    nameArray.set(3, editEntryArray[1]);
                    System.out.println("Last name set: " + nameArray.get(3));
                }
                else if(editEntryArray.length == 2)
                    System.out.println("Can't contain ';' or space, start with '/', or be 'null' in any capitalisation.");
                else
                    System.out.println("To delete last name: /remove last");
            }

            else if(editEntryArray[0].startsWith("/i")) {
                if(editEntryArray.length == 3
                && CommonMethods.stringIsSafeWithLength(editEntryArray[1])
                && CommonMethods.stringIsIntInRange(editEntryArray[2], 0, 3)) {
                    String interestNameLowerCase = editEntryArray[1].toLowerCase();
                    interestMap.put(interestNameLowerCase, Integer.parseInt(editEntryArray[2]));
                    myPersonCollection.fillInterestCapitalisation(editEntryArray[1]);
                    System.out.println("Interest set: " + myPersonCollection.getInterestCorrectCase(editEntryArray[1]) +
                            " - " + interestMap.get(interestNameLowerCase));
                }
                else if(editEntryArray.length == 3)
                    System.out.println("Interest names must be at least one character, can't contain ';', start with '/', or be 'null' in any capitalisation.\n" +
                            "Interest values must be a whole number ranging from 0 to 3.");
                else
                    System.out.println("To delete an interest: /remove interest [interest name]");
            }

            else if(editEntryArray[0].startsWith("/r")) {
                if(editEntryArray.length >= 2) editEntryArray[1] = editEntryArray[1].toLowerCase();

                if(editEntryArray.length == 2) {
                    if(editEntryArray[1].startsWith("f")) {
                        nameArray.set(1, null);
                        System.out.println("Removed: First names");
                    }
                    else if(editEntryArray[1].startsWith("m")) {
                        nameArray.set(2, null);
                        System.out.println("Removed: Middle names");
                    }
                    else if(editEntryArray[1].startsWith("l")) {
                        nameArray.set(3, null);
                        System.out.println("Removed: Last name");
                    }
                    else System.out.println("/remove requires you to enter one of these options: First, middle, last or interest.\n" +
                                "If interest, you must enter the name of an interest in this persons interest list.");
                }
                else if(editEntryArray.length == 3
                && editEntryArray[1].startsWith("i")
                && interestMap.containsKey(editEntryArray[2].toLowerCase())) {
                    interestMap.remove(editEntryArray[2].toLowerCase());
                    System.out.println("Removed interest: " + editEntryArray[2].toLowerCase());
                }
                else {
                    System.out.println("/remove requires you to enter one of these options: 'First', 'middle', 'last' or 'interest'.\n" +
                            "If interest, you must enter the name of an interest from this persons interest list.");
                }
            }

            else if(editEntryArray[0].startsWith("/w")) {
                if(editEntryArray.length == 2) {
                    editEntryArray[1] = editEntryArray[1].toLowerCase();

                    if(editEntryArray[1].startsWith("n")) {
                        nameArray.set(1, null);
                        nameArray.set(2, null);
                        nameArray.set(3, null);
                        System.out.println("Removed first names, middle names and last name.\n" +
                                "Note: Handle name can't be deleted.");
                    }
                    else if(editEntryArray[1].startsWith("i")) {
                        interestMap.clear();
                        System.out.println("Removed all interest entries.");
                    }
                    else if(editEntryArray[1].startsWith("b")) {
                        nameArray.set(1, null);
                        nameArray.set(2, null);
                        nameArray.set(3, null);
                        interestMap.clear();
                        System.out.println("Removed first names, middle names, last name and all interest entries.\n" +
                                "Note: Handle name can't be deleted.");
                    }
                }
            }

            else if(editEntryArray[0].startsWith("/v")) {
                viewThisPerson(myPersonCollection.getTheInterestSpellingMap());
            }

            else if(!editEntryArray[0].startsWith("/c")) listCommandsEditPerson();

        }
        while(!editEntryArray[0].startsWith("/c"));

        //The caller must know the current handle name.
        return nameArray.get(0);
    }//Method editThisPerson

    public void listCommandsEditPerson() {
        System.out.println("Commands: Edit person\n" +
                "[C] /handle [handleName]\n" +
                "[C] /first [first names]\n" +
                "[C] /middle [middle names]\n" +
                "[C] /last [lastName]\n" +
                "[C] /interest [interest name] [interestValue]\n" +
                "[C] /remove first|middle|last|interest [interest: interest name]\n" +
                "[C] /wipe names|interests|both\n" +
                "[C] /view\n" +
                "[C] /conclude");
    }//Method listCommandsEditPerson

    public void viewThisPerson(HashMap<String, String> interestSpellingMap) {

        //Printing headline and handle name
        System.out.println("Person: " + nameArray.get(0) + "\n" +
                "Names\n" +
                "[*] Handle: " + nameArray.get(0));

        //Printing actual names
        if(nameArray.get(1) == null) System.out.println("[*] First:");
        else System.out.println("[*] First: " + nameArray.get(1));

        if(nameArray.get(2) == null) System.out.println("[*] Middle:");
        else System.out.println("[*] Middle: " + nameArray.get(2));

        if(nameArray.get(3) == null) System.out.println("[*] Last:");
        else System.out.println("[*] Last: " + nameArray.get(3));

        //Printing interests - headline
        int numberOfInterestEntries = interestMap.size();
        if(numberOfInterestEntries == 0) System.out.println("\nInterests: NONE");
        else if(numberOfInterestEntries == 1) System.out.println("\nInterest (1)");
        else System.out.println("\nInterests (" + numberOfInterestEntries + ")");

        //Printing interests - each interest
        HashSet<String> highlyInterestedSet = new HashSet<>();
        HashSet<String> interestedSet = new HashSet<>();
        HashSet<String> willingSet = new HashSet<>();
        HashSet<String> uninterestedSet = new HashSet<>();

        for(Map.Entry<String, Integer> theEntry: interestMap.entrySet()) {
            switch(theEntry.getValue()) {
                case 0:
                    uninterestedSet.add(theEntry.getKey());
                    break;
                case 1:
                    willingSet.add(theEntry.getKey());
                    break;
                case 2:
                    interestedSet.add(theEntry.getKey());
                    break;
                case 3:
                    highlyInterestedSet.add(theEntry.getKey());
            }
        }

        ArrayList<String> highlyInterestedOrdered = CommonMethods.stringSetToOrderedArrayList(highlyInterestedSet);
        ArrayList<String> interestedOrdered = CommonMethods.stringSetToOrderedArrayList(interestedSet);
        ArrayList<String> willingOrdered = CommonMethods.stringSetToOrderedArrayList(willingSet);
        ArrayList<String> uninterestedOrdered = CommonMethods.stringSetToOrderedArrayList(uninterestedSet);

        if(!highlyInterestedOrdered.isEmpty()) {
            System.out.println("Highly interested:");

            for(int i = 0; i < highlyInterestedOrdered.size(); i++) {
                System.out.println("[3] " + myPersonCollection.getInterestCorrectCase(highlyInterestedOrdered.get(i)));
            }
        }

        if(!interestedOrdered.isEmpty()) {
            System.out.println("Interested:");

            for(int i = 0; i < interestedOrdered.size(); i++) {
                System.out.println("[2] " + myPersonCollection.getInterestCorrectCase(interestedOrdered.get(i)));
            }
        }

        if(!willingOrdered.isEmpty()) {
            System.out.println("Willing:");

            for(int i = 0; i < willingOrdered.size(); i++) {
                System.out.println("[1] " + myPersonCollection.getInterestCorrectCase(willingOrdered.get(i)));
            }
        }

        if(!uninterestedOrdered.isEmpty()) {
            System.out.println("\nUninterested:");
            for(int i = 0; i < uninterestedOrdered.size(); i++) {
                System.out.println("[0] " + myPersonCollection.getInterestCorrectCase(uninterestedOrdered.get(i)));
            }
        }

        //This method could be made a lot shorter if interests were objects in an already ordered ArrayList.

    }//Method viewThisPerson

    public Integer getInterestValue(String interestLowerCase) {
        return interestMap.getOrDefault(interestLowerCase, null);
    }//Method getInterestValue

    public boolean getInterestExistence(String interestLowerCase) {
        return interestMap.containsKey(interestLowerCase);
    }//Method getInterestExistence

    @Override
    public int compareTo(Person p) {
        return this.nameArray.get(0).toLowerCase().compareTo(p.nameArray.get(0).toLowerCase());
    }//Method compareTo

    public HashMap<String, Integer> getInterestMap() {
        return interestMap;
    }//Method getInterestMap

//----------------------------------------------------------------
    public String getStorageString() {

        //Storing name data
        String storageString = nameArray.get(0);

        for(int i = 1; i <= 3; i++) {
            storageString = storageString.concat(";" + nameArray.get(i));
        }

        //Storing interest data
        for(Map.Entry<String, Integer> interest: interestMap.entrySet()) {
            storageString = storageString.concat(
                    ";" + interest.getKey() + ";" + interest.getValue()
            );
        }

        //Returning the finished storage string
        return storageString;
    }//Method getStorageString

//----------------------------------------------------------------
}//Class Person