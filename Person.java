import java.util.*;

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