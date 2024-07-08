import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class PersonList {

    HashMap<String, Person> thePersonMap;
    String personFileName;
    EventList correspondingEventList;

    public PersonList(String personFile) {
        thePersonMap = new HashMap<>();
        personFileName = personFile;
        correspondingEventList = null;

        loadPersons(personFile);

    }//Method constructor

    public String toString() {

        String returnString = "The person list: ";
        int numberOfPersons = thePersonMap.size();

        if(numberOfPersons == 0) returnString += "EMPTY";
        else if(numberOfPersons == 1) returnString += "1 person.";
        else returnString += numberOfPersons + " persons.";

        return returnString;
    }//Method toString

    public void loadPersons(String fileName) {

        File personFile = new File(fileName);
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

    public void setCorrespondingEventList(EventList el) {
        correspondingEventList = el;
    }//Method setCorrespondingEventList

//----------------------------------------------------------------
    public void makePerson() {}//Method makePerson

//----------------------------------------------------------------
    public boolean isValidPersonHandle(String s) {
        if(s == null
        || s.equalsIgnoreCase("null")
        || s.isEmpty()) {
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