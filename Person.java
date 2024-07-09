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

    public Person(String h, String f, String m, String l, PersonList pl) {
        nameArray = new ArrayList<>(4);
        personalInterests = new HashMap<>();
        myPersonList = pl;

        //Filling the name array.
        nameArray.add(0, h);
        nameArray.add(1, f);
        nameArray.add(2, m);
        nameArray.add(3, l);

        //This one may not be needed if I always send in null instead of empty string.
        for(int i = 0; i <= 3; i ++) {
            if(nameArray.get(i).equalsIgnoreCase("")) {
                nameArray.add(i, null);
            }
        }

        editInterests(myPersonList.userEntry);
        System.out.println("Made new person " + nameArray.get(0) + "!");
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
        personalInterests.put(i, v);
    }//Method setInterest

    public void removeInterest(String i) {
        personalInterests.remove(i);
    }

    public void wipeInterests() {
        personalInterests.clear();
    }//Method wipeInterests

    public void showInterests() {
        System.out.println(nameArray.get(0) + "'s interests:");

        for(int v = 3; v >= 0; v--) {
            for(String key: personalInterests.keySet()) {
                if(personalInterests.get(key) == v) {
                    System.out.println("- " + key + ": " + personalInterests.get(key));
                }
            }
        }

    }//Method showInterests

    public void editInterests(Scanner ue) {
        System.out.println("Editing " + nameArray.get(0) + "'s interests.");
        showInterestEditingOptions();

        String editEntry = "-";
        while(isValidStringWithLength(editEntry)) {

            System.out.print("Personal interest entry: ");
            editEntry = ue.nextLine().strip();
            String[] editArray = editEntry.split(" ");

            if(editEntry.isEmpty()) {
                System.out.println("Done editing " + nameArray.get(0) + "'s interests.");
            }
            else if(editArray[0].toLowerCase().startsWith("m") && editArray.length >= 3) {
                if(isValidStringWithLength(editArray[1]) && isValidNumberValue(editArray[2], 0, 3)) {
                    personalInterests.put(editArray[1], Integer.parseInt(editArray[2]));
                }
            }
            else if(editArray[0].toLowerCase().startsWith("l")) {
                showInterests();
            }
            else if(editArray[0].toLowerCase().startsWith("r") && editArray.length >= 2) {
                if(isValidStringWithLength(editArray[1])) {
                    personalInterests.remove(editArray[1]);
                }
            }
            else if(editArray[0].toLowerCase().startsWith("w")) {
                personalInterests.clear();
            }
            else showInterestEditingOptions();
        }

    }//Method editInterests

    public void showInterestEditingOptions() {
        System.out.println("Editing options:\n" +
                "- Make [interest] [value] (add or update an interest)\n" +
                "- List (lists all interests for this person)\n" +
                "- Remove [interest] (removes an interest if it exists)\n" +
                "- Wipe (removes all interests for this person)\n" +
                "* Enter nothing to finish editing interests");
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
        System.out.println("Test print - person storage string:\n" +
                storageString);

        return storageString;
    }//Method getStorageString

}//Class Person