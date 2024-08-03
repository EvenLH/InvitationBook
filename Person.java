import java.util.*;

public class Person implements CommonValidations {

    ArrayList<String> nameArray; //Handle name, first, middle, last
    HashMap<String, Integer> personalInterests; //Interest name, values integers 0 to 3

    PersonList myPersonList;

    public Person(String storageString, PersonList pl) {
        nameArray = new ArrayList<>(4);
        personalInterests = new HashMap<>();
        myPersonList = pl;

        String[] temp = storageString.strip().split(";");

        //Filling the name array list.
        for(int i = 0; i <= 3; i++) {
            if(temp[i].equalsIgnoreCase("null")) {
                nameArray.set(i, null);
            }
            else nameArray.set(i, temp[i]);
        }

        //Filling the interest hash map.
        for(int i = 4; i < temp.length; i = i+2) {
            personalInterests.put(temp[i], Integer.parseInt(temp[i+1]));
        }

    }//Method constructor 1

    public Person(ArrayList<String> en, PersonList pl) {
        nameArray = new ArrayList<>(4);
        personalInterests = new HashMap<>();
        myPersonList = pl;

        //Filling the name array.
        for(int i = 0; i <= 3; i++) {
            if(en.get(i).isEmpty())
                nameArray.set(i, null);
            else nameArray.set(i, en.get(i));
        }

    }//Method constructor 2

    public String toString() {
        String returnString = nameArray.get(0);
        boolean noNameHasBeenAdded = true;

        for(int i = 1; i <= 3; i++) {
            if(!(nameArray.get(i) == null)) {
                if(noNameHasBeenAdded) {
                    returnString = returnString.concat(" -");
                    noNameHasBeenAdded = false;
                }

                returnString = returnString.concat(" " + nameArray.get(i));
            }
        }

        return returnString;
    }//Method toString

//----------------------------------------------------------------
    public void setName(String t, String n) {

        if(t.toLowerCase().startsWith("h")) {
            if(myPersonList.isValidNewPersonHandle(n)) nameArray.set(0, n);
            else System.out.println("Handle names must\n" +
                        "* Have length (contain at least one character)\n" +
                        "* Be unique (and the only differences can't be capitalisation)\n" +
                        "* Not be exactly 'null', start with '/', or contain ';' or any spaces.");
        }
        else if(t.toLowerCase().startsWith("f")) {
            if(isValidString(n)) nameArray.set(1, n);
            else System.out.println("First and middle names must\n" +
                        "* Not be exactly 'null', start with '/', or contain ';'.");
        }
        else if(t.toLowerCase().startsWith("m")) {
            if(isValidString(n)) nameArray.set(2, n);
            else System.out.println("First and middle names must\n" +
                    "* Not be exactly 'null', start with '/', or contain ';'.");
        }
        else if(t.toLowerCase().startsWith("l")) {
            if(isValidString(n)
            && !(n.contains(" "))) nameArray.set(3, n);
            else System.out.println("Last names must\n" +
                    "* Not be exactly 'null', start with '/', or contain ';' or any spaces.");
        }
        else listNameChangeOptions();
    }//Method setName

    public void removeNameExceptHandle(String n) {
        String selection = n.toLowerCase();
        if(n.startsWith("f")) nameArray.set(1, null);
        else if(n.startsWith("m")) nameArray.set(2, null);
        else if(n.startsWith("l")) nameArray.set(3, null);
        else System.out.println("Name field to be emptied must be 'first', 'middle' or 'last'.");
    }//Method removeNameExceptHandle

    public void wipeNamesExceptHandle() {
        nameArray.set(1, null);
        nameArray.set(2, null);
        nameArray.set(3, null);
    }//Method wipeNamesExceptHandle

    public void listNames() {
        System.out.println("Name information for " + nameArray.get(0) + "\n" +
                "* Handle name:  " + nameArray.get(0) + "\n" +
                "* First names:  " + nameArray.get(1) + "\n" +
                "* Middle names: " + nameArray.get(2) + "\n" +
                "* Last name:    " + nameArray.get(3));
    }//Method listNames

    public String editNames(Scanner ue) {
        String oldHandle = nameArray.get(0);
        listNameChangeOptions();

        String commandEntry;
        String[] commandArray;
        do {
            System.out.print("- Enter edit command: ");
            commandEntry = ue.nextLine().strip();
            commandArray = commandEntry.split(" ");

            if(commandEntry.toLowerCase().startsWith("/m")
            && commandArray.length >= 3) {
                String nameString = commandArray[2];
                for(int i = 3; i < commandArray.length; i++) {
                    nameString = nameString.concat(" " + commandArray[i]);
                }

                setName(commandArray[1], nameString);
            }//Conditional for /make
            else if(commandEntry.toLowerCase().startsWith("/r")
            && commandArray.length >= 2) {
                removeNameExceptHandle(commandArray[1]);
            }//Conditional for /remove
            else if(commandEntry.toLowerCase().startsWith("/w")) {
                wipeNamesExceptHandle();
            }//Conditional for /wipe
            else if(commandEntry.toLowerCase().startsWith("/l")) {
                listNames();
            }//Conditional for /list
            else if(commandEntry.toLowerCase().startsWith("/c")) {
                System.out.println("Completed name editing for " + nameArray.get(0) + ".");
            }//Conditional for /close
            else {
                listNameChangeOptions();
            }//Conditional for /options
        }
        while(!commandEntry.toLowerCase().startsWith("/c"));

        //Telling the calling location whether the handle name was edited or not.
        if(oldHandle.equals(nameArray.get(0))) return null;
        return nameArray.get(0);
    }//Method editNames

    public void listNameChangeOptions() {
        System.out.println("Editing names:\n" +
                "/make handle|first|middle|last [newName] (update a name category)\n" +
                "/remove first|middle|last (removes all names in a category)\n" +
                "/wipe (keeps handle, but removes all other names)\n" +
                "/list (lists all names for this person)\n" +
                "/close (concludes name editing)\n" +
                "/options (shows these options)");
    }//Method listNameChangeOptions

    public void setInterest(String i, int v) {
        if(personalInterests.containsKey(i)) {
            System.out.println("Updated interest: " + i + " - " + v);
        }
        else if(hasInterestIgnoreCase(i)) {
            personalInterests.remove(getInterestCorrectCase(i));
            System.out.println("Updated interest: " + i + " - " + v);
        }
        else {
            System.out.println("Added interest: " + i + " - " + v);
        }

        personalInterests.put(i, v);
    }//Method setInterest

    public void removeInterest(String i) {
        String discoveredInterest = getInterestCorrectCase(i);

        if(discoveredInterest == null) {
            System.out.println("No such interest: " + i);
        }
        else {
            personalInterests.remove(discoveredInterest);
            System.out.println("Removed interest: " + discoveredInterest);
        }

    }//Method removeInterest

    public void wipeInterests() {
        personalInterests.clear();
    }//Method wipeInterests

    public void listInterests() {
        if(personalInterests.isEmpty()) {
            System.out.println(nameArray.get(0) + " has no interests.");
        }
        else {
            System.out.println(nameArray.get(0) + "'s interests:");

            for(int v = 3; v >= 0; v--) {
                for(String handleName: personalInterests.keySet()) {
                    if(personalInterests.get(handleName) == v) {
                        System.out.println("* " + handleName + ": " + personalInterests.get(handleName));
                    }
                }
            }
        }

    }//Method listInterests

    public void editInterests(Scanner ue) {
        showInterestEditingOptions();

        System.out.print("- Personal interest entry: ");
        String editEntry = ue.nextLine().strip();
        String[] editArray = editEntry.split(" ");

        while(!editEntry.toLowerCase().startsWith("/c")) {

            if(editArray.length >= 3
            && editEntry.toLowerCase().startsWith("/m")
            && isValidStringWithLength(editArray[1])
            && isValidIntInRange(editArray[editArray.length-1], 0, 3)) {
                StringBuilder madeInterestBuilder = new StringBuilder(editArray[1]);
                for(int i = 2; i < editArray.length-1; i++) {
                    madeInterestBuilder.append(" ").append(editArray[i]);
                }

                setInterest(madeInterestBuilder.toString(), Integer.parseInt(editArray[editArray.length-1]));
            }
            else if(editArray.length >= 2
            && editEntry.toLowerCase().startsWith("/r")
            && isValidStringWithLength(editArray[1])) {
                StringBuilder namedInterestBuilder = new StringBuilder(editArray[1]);
                for(int i = 2; i < editArray.length; i++) {
                    namedInterestBuilder.append(" ").append(editArray[i]);
                }

                removeInterest(namedInterestBuilder.toString());
            }
            else if(editArray.length >= 1
            && editEntry.toLowerCase().startsWith("/l")) {
                listInterests();
            }
            else if(editArray.length >= 1
            && editEntry.toLowerCase().startsWith("/w")) {
                personalInterests.clear();
                System.out.println(nameArray.get(0) + "'s interests have been wiped.");
            }
            else showInterestEditingOptions();

            System.out.print("- Personal interest entry: ");
            editEntry = ue.nextLine().strip();
            editArray = editEntry.split(" ");
        }//Loop while

    }//Method editInterests

    public void showInterestEditingOptions() {
        System.out.println("Editing interests:\n" +
                "- /make [interest] [value] (add or update an interest)\n" +
                "- /remove [interest] (removes an interest)\n" +
                "- /list (lists all interests for this person)\n" +
                "- /wipe (removes all interests for this person)\n" +
                "- /close (concludes interest entry)\n" +
                "- /options (shows these options)");
    }//Method showInterestEditingOptions

//----------------------------------------------------------------
public boolean hasInterestIgnoreCase(String i) {
    for(String key: personalInterests.keySet()) {
        if(key.equalsIgnoreCase(i)) return true;
    }

    return false;
}//Method hasInterest

    public String getInterestCorrectCase(String i) {
        for(String key: personalInterests.keySet()) {
            if(key.equalsIgnoreCase(i)) return key;
        }

        return null;
    }//Method getInterestString

//----------------------------------------------------------------
    public String getStorageString() {
        String storageString = nameArray.get(0);

        for(int i = 1; i <= 3; i++) {
            storageString = storageString.concat(";" + nameArray.get(i));
        }

        for(Map.Entry<String, Integer> entry: personalInterests.entrySet()) {
            storageString = storageString.concat(";" + entry.getKey() + ";" + entry.getValue());
        }

        return storageString;
    }//Method getStorageString

}//Class Person