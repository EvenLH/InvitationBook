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

    public void viewThisPerson(HashMap<String, String> spellingMap) {

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
        for(String interestKey: interestMap.keySet()) {
            String personalInterestString = "[*] " + interestKey + ": ";
            if(spellingMap.containsKey(interestKey)) personalInterestString = "[*] " + spellingMap.get(interestKey) + ": ";
            personalInterestString = personalInterestString.concat(String.valueOf(interestMap.get(interestKey)));

            System.out.println(personalInterestString);
        }
    }//Method viewThisPerson

    @Override
    public int compareTo(Person p) {
        return this.nameArray.get(0).compareTo(p.nameArray.get(0));
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