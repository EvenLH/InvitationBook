import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Person implements CommonValidations {

    ArrayList<String> nameArray; //Handle name, first, middle, last
    HashMap<String, Integer> personalInterests; //Interest name, values integers 0 to 3

    PersonList myPersonList;

    public Person(String storageString, PersonList pl) {
        nameArray = new ArrayList<>(4);
        personalInterests = new HashMap<>();
        myPersonList = pl;

        String[] temp = storageString.strip().split(";");

        //Filling the name array.
        for(int i = 0; i <= 3; i++) {
            if(temp[i].equalsIgnoreCase("null")) {
                nameArray.add(i, null);
            }
            else nameArray.add(i, temp[i]);
        }

        //Filling the interest map.
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
            if(en.get(i).isEmpty()) {
                nameArray.add(i, null);
            }
            else {
                nameArray.add(i, en.get(i));
            }
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
    public void setInterest(String i, int v) {
        if(personalInterests.containsKey(i)) {
            System.out.println("Updated interest: " + i + " " + v);
        }
        else if(hasInterestIgnoreCase(i)) {
            personalInterests.remove(getInterestCorrectCase(i));
            System.out.println("Updated interest: " + i + " " + v);
        }
        else {
            System.out.println("Added interest: " + i + " " + v);
        }

        personalInterests.put(i, v);
    }//Method setInterest

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

    public void showInterests() {
        if(personalInterests.isEmpty()) {
            System.out.println(nameArray.get(0) + " has no interests.");
        }
        else {
            System.out.println(nameArray.get(0) + "'s interests:");

            for(int v = 3; v >= 0; v--) {
                for(String key: personalInterests.keySet()) {
                    if(personalInterests.get(key) == v) {
                        System.out.println("- " + key + ": " + personalInterests.get(key));
                    }
                }
            }
        }

    }//Method showInterests

    public void editInterests(Scanner ue) {
        showInterestEditingOptions();

        System.out.print("Personal interest entry: ");
        String editEntry = ue.nextLine().strip();
        String[] editArray = editEntry.split(" ");

        while(!editEntry.toLowerCase().startsWith("c")) {

            if(editArray.length >= 3
            && editEntry.toLowerCase().startsWith("m")
            && isValidStringWithLength(editArray[1])
            && isValidNumberValue(editArray[editArray.length-1], 0, 3)) {
                StringBuilder madeInterestBuilder = new StringBuilder(editArray[1]);
                for(int i = 2; i < editArray.length-1; i++) {
                    madeInterestBuilder.append(" ").append(editArray[i]);
                }

                setInterest(madeInterestBuilder.toString(), Integer.parseInt(editArray[editArray.length-1]));
            }
            else if(editArray.length >= 2
            && editEntry.toLowerCase().startsWith("r")
            && isValidStringWithLength(editArray[1])) {
                StringBuilder namedInterestBuilder = new StringBuilder(editArray[1]);
                for(int i = 2; i < editArray.length; i++) {
                    namedInterestBuilder.append(" ").append(editArray[i]);
                }

                removeInterest(namedInterestBuilder.toString());
            }
            else if(editArray.length > 0 && editEntry.toLowerCase().startsWith("l")) {
                showInterests();
            }
            else if(editArray.length > 0 && editEntry.toLowerCase().startsWith("w")) {
                personalInterests.clear();
                System.out.println(nameArray.get(0) + "'s interests have been wiped.");
            }
            else showInterestEditingOptions();

            System.out.print("Personal interest entry: ");
            editEntry = ue.nextLine().strip();
            editArray = editEntry.split(" ");
        }//Loop while

    }//Method editInterests

    public void showInterestEditingOptions() {
        System.out.println("Editing options:\n" +
                "- Make [interest] [value] (add or update an interest)\n" +
                "- Remove [interest] (removes an interest if it exists)\n" +
                "- List (lists all interests for this person)\n" +
                "- Wipe (removes all interests for this person)\n" +
                "- Close (concludes interest entry for this person)\n" +
                "- Options (shows these options)");
    }

//----------------------------------------------------------------

//----------------------------------------------------------------
    public String getStorageString() {
        String storageString = nameArray.get(0);

        for(int i = 1; i <= 3; i++) {
            storageString = storageString.concat(";" + nameArray.get(i));
        }

        for(Map.Entry<String, Integer> entry: personalInterests.entrySet()) {
            storageString = storageString.concat(";" + entry.getKey() + ";" + entry.getValue());
        }

        //TEST PRINT
        System.out.println("Test print - person storage string: " + storageString);

        return storageString;
    }//Method getStorageString

}//Class Person