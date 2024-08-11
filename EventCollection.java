import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class EventCollection {

    ArrayList<Event> theEventArray;
    String eventFileName;
    PersonCollection correspondingPersonCollection;
    Scanner userEntry;
    boolean certainlyInChronologicalOrder;

    public EventCollection(String efn) {
        theEventArray = new ArrayList<>();

        //Utilities
        eventFileName = efn;
        correspondingPersonCollection = null;
        userEntry = null;
        certainlyInChronologicalOrder = false;

        loadEvents();
    }//Method EventCollection constructor

    public void loadEvents() {
        File eventFile = new File(eventFileName);
        Scanner eventReader;

        try {
            eventReader = new Scanner(eventFile);
        }
        catch(FileNotFoundException fnfe) {
            System.out.println("* Event file: Not found");
            return;
        }

        while(eventReader.hasNextLine()) {
            theEventArray.add(new Event(eventReader.nextLine().strip(), this));
        }
        System.out.println("* Event file: Found and loaded");

        eventReader.close();
    }//Method loadEvents

    public void setResourcePointers(PersonCollection pc, Scanner ue) {
        correspondingPersonCollection = pc;
        userEntry = ue;
    }//Method setResourcePointers

//----------------------------------------------------------------
    public String toString() {
        int numberOfEvents = theEventArray.size();

        if(numberOfEvents == 0) return "Events: NONE";
        else if(numberOfEvents == 1) return "Event: 1";
        else return "Events: " + numberOfEvents;
    }//Method toString

    public void listEvents() {
        int numberOfEvents = theEventArray.size();

        if(numberOfEvents == 0) System.out.println("Events: NONE");
        else if(numberOfEvents == 1) System.out.println("Event (1):");
        else System.out.println("Events (" + numberOfEvents + "):");

        for(int i = 0; i < numberOfEvents; i++) {
            System.out.println(
                    "* " + theEventArray.get(i)
            );
        }
    }//Method listEvents

//----------------------------------------------------------------
    public void storeEvents() {
        FileWriter storageFile;

        try {
            storageFile = new FileWriter(eventFileName, false);

            for(int i = 0; i < theEventArray.size(); i++) {
                storageFile.write(
                        theEventArray.get(i).getStorageString() + "\n"
                );
            }

            storageFile.close();
            System.out.println("* Event file: Update complete");
        }
        catch(IOException ioe) {
            System.out.println("* Event file: Possibly failed or incomplete update\n" +
                    "   * " + ioe
            );
        }
    }//Method storeEvents

//----------------------------------------------------------------
}//Class EventCollection