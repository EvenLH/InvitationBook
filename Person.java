import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Person implements Comparable<Person> {

    ArrayList<String> nameArray;
    HashMap<String, Integer> interestMap;

    //Utilities
    PersonCollection myPersonCollection;
    Scanner userEntry;

    public Person(String storageString, PersonCollection pc) {
        nameArray = new ArrayList<>(4);
        interestMap = new HashMap<>();

        myPersonCollection = pc;
        userEntry = null;

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
            interestMap.put(temp[j], Integer.parseInt(temp[j+1]));
        }

    }//Method Person constructor 1

    public Person(String h, PersonCollection pc, Scanner ue) {
        nameArray = new ArrayList<>(4);
        interestMap = new HashMap<>();

        myPersonCollection = pc;
        userEntry = ue;

        nameArray.add(h);
        for(int i = 1; i <= 3; i++)
            nameArray.add(i, null);
    }//Method Person constructor 2

    public void setResourcePointers(Scanner ue) {
        userEntry = ue;
    }//Method setResourcePointers

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

//----------------------------------------------------------------
    @Override
    public int compareTo(Person p) {
        return this.nameArray.get(0).compareTo(p.nameArray.get(0));
    }//Method compareTo

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