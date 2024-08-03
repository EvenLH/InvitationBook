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

        System.out.println("Enter one handle name, any number of first names and middle names, and at most one last name.\n" +
                "Commands:\n" +
                "/cancel (stops making this person, and returns you to the main menu)");

        String enteredString;

        do {
            System.out.print("- Enter handle name: ");
            enteredString = userEntry.nextLine().strip();
            if(enteredString.toLowerCase().startsWith("/c")) return;
        }
        while(!isValidNewPersonHandle(enteredString));
        enteredNames.add(0, enteredString);

        do {
            System.out.print("- Enter first names: ");
            enteredString = userEntry.nextLine().strip();
            if(enteredString.toLowerCase().startsWith("/c")) return;
        }
        while(!isValidString(enteredString));
        enteredNames.add(1, enteredString);

        do {
            System.out.print("- Enter middle names: ");
            enteredString = userEntry.nextLine().strip();
            if(enteredString.toLowerCase().startsWith("/c")) return;
        }
        while(!isValidString(enteredString));
        enteredNames.add(2, enteredString);

        do {
            System.out.print("- Enter last name: ");
            enteredString = userEntry.nextLine().strip();
            if(enteredString.toLowerCase().startsWith("/c")) return;
        }
        while(!(isValidString(enteredString))
                || enteredString.contains(" "));
        enteredNames.add(3, enteredString);

        Person newPerson = new Person(enteredNames, this);
        thePersonMap.put(enteredNames.get(0), newPerson);
        System.out.println("Person made: " + newPerson);

        newPerson.editInterests(userEntry);

    }//Method makePerson

    public void makeInterest(String i) {

        //Should interests of only different capitalisations be possible?
        //If not, need to change this and some methods in the person class,
        //and make validation methods.

        String interestName = i;

        //If not already provided, user enters the name of an interest.
        if(!isValidStringWithLength(interestName)) {
            System.out.println("Enter an interest, or /conclude (return to main menu without making any changes)");

            do {
                System.out.print("- Enter interest name: ");
                interestName = userEntry.nextLine().strip();
                if(interestName.toLowerCase().startsWith("/c")) return;
            }
            while(!isValidStringWithLength(interestName));
        }

        //User enters interest level from 0 to 3 for each person.
        System.out.println("Enter interest value (0 to 3) for each person, or /conclude (keep changes made and return to main menu)");
        for(String handle: thePersonMap.keySet()) {
            String interestLevel;

            do {
                System.out.print("- Enter interest level for " + handle + ": ");
                interestLevel = userEntry.nextLine().strip();
                if(interestLevel.toLowerCase().startsWith("/c")) return;
            }
            while(!isValidIntInRange(interestLevel, 0, 3));

            thePersonMap.get(handle).setInterest(interestName, Integer.parseInt(interestLevel));
        }

        System.out.println("All persons have received a value for their interest in " + interestName + ".");
    }//Method makeInterest

    public void editPerson(String p) {

        String currentHandle = getPersonHandleCorrectCase(p);

        //If not already given, asking user for the handle of the person to be edited.
        if(!isExistingPersonHandleIgnoreCase(currentHandle)) {
            System.out.println("Persons:");
            listPersons();

            System.out.println("Enter the handle of a person, or /cancel (return to main menu)");
            do {
                System.out.print("- Enter handle name: ");
                currentHandle = userEntry.nextLine().strip();
                if(currentHandle.toLowerCase().startsWith("/c")) return;
            }
            while(!isExistingPersonHandleIgnoreCase(currentHandle));
        }

        Person personObject = thePersonMap.get(currentHandle);

        //Editing handle and names
        String newHandle = personObject.editNames(userEntry);

        //If handle was changed, we need to tell other data structures about it.
        if(!(currentHandle.equals(newHandle))) {
            updateHandleOutsidePerson(currentHandle, newHandle);
        }

        //Editing interests
        personObject.editInterests(userEntry);

        System.out.println("Done editing " + personObject + ". Returning to main menu.");
    }//Method editPerson

    public void updateHandleOutsidePerson(String oldHandle, String newHandle) {

        //Making the actual change.
        thePersonMap.put(newHandle, thePersonMap.remove(oldHandle));

        //Updating handle everywhere else.
        correspondingEventList.updateHandleWhereInvited(oldHandle, newHandle);

    }//Method updateHandleOutsidePerson

    public void listPersons() {
        for(String key: thePersonMap.keySet()) {
            System.out.println("- " + thePersonMap.get(key));
        }
    }//Method listPersons

//----------------------------------------------------------------
    public boolean isValidNewPersonHandle(String s) {
        if(isValidStringWithLength(s)
        || s.contains(" ")) {
            return false;
        }

        for(String key: thePersonMap.keySet()) {
            if(s.equalsIgnoreCase(key)) return false;
        }

        return true;
    }//Method isValidNewPersonHandle

    public boolean isExistingPersonHandleIgnoreCase(String s) {
        for(String key: thePersonMap.keySet()) {
            if(key.equalsIgnoreCase(s)) return true;
        }

        return false;
    }//Method isExistingPersonHandleIgnoreCase

    public String getPersonHandleCorrectCase(String s) {
        for(String key: thePersonMap.keySet()) {
            if(key.equalsIgnoreCase(s)) return key;
        }

        return null;
    }//Method getPersonHandleCorrectCase

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