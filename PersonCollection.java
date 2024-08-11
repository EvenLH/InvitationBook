import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class PersonCollection {

    HashMap<String, Person> thePersonMap;
    String personFileName;
    EventCollection correspondingEventCollection;
    Scanner userEntry;

    public PersonCollection(String pfn) {
        thePersonMap = new HashMap<>();

        //Utilities
        personFileName = pfn;
        correspondingEventCollection = null;
        userEntry = null;

        loadPersons();
    }//Method PersonCollection constructor

    public String toString() {
        int numberOfPersons = thePersonMap.size();
        if(numberOfPersons == 0) return "Persons: NONE";

        String returnString;
        if(numberOfPersons == 1) returnString = "Person (1):";
        else returnString = "Persons (" + numberOfPersons + "):";

        for(String handle: thePersonMap.keySet()) {
            returnString += "\n" + thePersonMap.get(handle);
        }

        return returnString;
    }//Method toString

    public void loadPersons() {
        File personFile = new File(personFileName);
        Scanner personReader;

        try {
            personReader = new Scanner(personFile);
        }
        catch(FileNotFoundException fnfe) {
            System.out.println("* Person file: Not found");
            return;
        }

        while(personReader.hasNextLine()) {
            String loadString = personReader.nextLine().strip();
            thePersonMap.put(loadString.split(";")[0],
                    new Person(loadString, this));
        }
        System.out.println("* Person file: Found and loaded");

        personReader.close();
    }//Method loadPersons

    public void setResourcePointers(EventCollection ec, Scanner ue) {
        correspondingEventCollection = ec;
        userEntry = ue;

        for(String handle: thePersonMap.keySet()) {
            thePersonMap.get(handle).setResourcePointers(ue);
        }
    }//Method setResourcePointers

//----------------------------------------------------------------
    public void storePersons() {
        FileWriter storageFile;

        try {
            storageFile = new FileWriter(personFileName, false);

            for(String key: thePersonMap.keySet()) {
                storageFile.write(
                        thePersonMap.get(key).getStorageString() + "\n"
                );
            }

            storageFile.close();
            System.out.println("* Person file: Update complete");
        }
        catch(IOException ioe) {
            System.out.println("* Person file: Possibly failed or incomplete update\n" +
                    "   *" + ioe
            );
        }
    }//Method storePersons

//----------------------------------------------------------------
}//Class PersonCollection