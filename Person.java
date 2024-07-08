import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Person {

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

    public Person(String s, String f, String m, String l, PersonList pl) {
        nameArray = new ArrayList<>(4);
        personalInterests = new HashMap<>();
        myPersonList = pl;

        //Filling the name array.
        nameArray.add(0, s);
        nameArray.add(1, f);
        nameArray.add(2, m);
        nameArray.add(3, l);

        //This one may not be needed if I always send in null instead of empty string.
        for(int i = 0; i <= 3; i ++) {
            if(nameArray.get(i).equalsIgnoreCase("")) {
                nameArray.add(i, null);
            }
        }

        //ADD SOMETHING HERE TO ASK USER FOR INTERESTS?



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
    public void setSingleInterest(String i, int v) {
        personalInterests.put(i, v);
    }//Method addInterest

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

        return storageString;
    }//Method getStorageString

}//Class Person