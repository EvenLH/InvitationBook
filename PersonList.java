import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class PersonList implements CommonValidations {

    HashMap<String, Person> thePersonMap;
    String personFileName;
    EventList correspondingEventList;
    Scanner userEntry;

    public PersonList(String pfn) {
        thePersonMap = new HashMap<>();
        personFileName = pfn;
        correspondingEventList = null;
        userEntry = null;

        loadPersons();

    }//Method constructor

    public String toString() {

        String returnString = "The person list: ";
        int numberOfPersons = thePersonMap.size();

        if(numberOfPersons == 0) returnString += "EMPTY";
        else if(numberOfPersons == 1) returnString += "1 person.";
        else returnString += numberOfPersons + " persons.";

        return returnString;
    }//Method toString

    public void loadPersons() {

        File personFile = new File(personFileName);
        Scanner personReader;

        try {
            personReader = new Scanner(personFile);
        }
        catch(FileNotFoundException fnfe) {
            System.out.println("- Person file - not found.");
            return;
        }

        while(personReader.hasNextLine()) {
            String loadString = personReader.nextLine().strip();
            thePersonMap.put(loadString.split(";")[0],
                    new Person(loadString, this));
        }
        System.out.println("- Person file - found and loaded.");

        personReader.close();
    }//Method loadPersons

    public void setResourcePointers(EventList el, Scanner ue) {
        correspondingEventList = el;
        userEntry = ue;
    }//Method setResourcePointers

//----------------------------------------------------------------
    public void makePerson() {
        ArrayList<String> enteredNames = new ArrayList<>(4);

        System.out.println("Enter one handle name, any number of first names and middle names, and at most one last name.");

        do {
            System.out.print("- Enter handle name: ");
            enteredNames.add(0, userEntry.nextLine().strip());
        }
        while(!isValidPersonHandle(enteredNames.get(0)));

        do {
            System.out.print("- Enter first names: ");
            enteredNames.add(1, userEntry.nextLine().strip());
        }
        while(!isValidString(enteredNames.get(1)));

        do {
            System.out.print("- Enter middle names: ");
            enteredNames.add(2, userEntry.nextLine().strip());
        }
        while(!isValidString(enteredNames.get(2)));

        do {
            System.out.print("- Enter last name: ");
            enteredNames.add(3, userEntry.nextLine().strip());
        }
        while(!(isValidString(enteredNames.get(3)))
                || enteredNames.get(3).contains(" "));

        Person newPerson = new Person(enteredNames, this);
        thePersonMap.put(enteredNames.get(0), newPerson);

        newPerson.editInterests(userEntry);

    }//Method makePerson

    public void makeInterest() {}//Method makeInterest

//----------------------------------------------------------------
    public boolean isValidPersonHandle(String s) {
        if(s == null
        || s.equalsIgnoreCase("null")
        || s.isEmpty()
        || s.contains(" ")) {
            return false;
        }

        for(String key: thePersonMap.keySet()) {
            if(s.equalsIgnoreCase(key)) return false;
        }

        return true;
    }//Method isValidPersonHandle

//----------------------------------------------------------------
    public void storePersons() {
        FileWriter targetFile;

        try {
            targetFile = new FileWriter(personFileName, false);

            for(String key: thePersonMap.keySet()) {
                targetFile.write(thePersonMap.get(key).getStorageString() + "\n");
            }

            targetFile.close();
            System.out.println("- Person storage - complete.");
        }
        catch(IOException ioe) {
            System.out.println("- Person storage - possibly failed or incomplete.\n" +
                    "---" + ioe);
        }

    }//Method storePersons

}//Class PersonList