import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class EventCollection {

    ArrayList<Event> theEventArray;

    //Utilities
    String eventFileName;
    PersonCollection correspondingPersonCollection;
    Scanner userEntry;

    public EventCollection(String efn, Scanner ue) {
        theEventArray = new ArrayList<>();

        //Utilities
        eventFileName = efn;
        correspondingPersonCollection = null;
        userEntry = ue;
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

        //Loading the 1st stored Event.
        if(eventReader.hasNextLine()) {
            Event firstLoadedEvent = new Event(eventReader.nextLine().strip(), this, userEntry);
            firstLoadedEvent.setComparableString();
            theEventArray.add(firstLoadedEvent);
        }

        //Loading any events after the 1st.
        while(eventReader.hasNextLine()) {
            Event loadedEvent = new Event(eventReader.nextLine().strip(), this, userEntry);
            loadedEvent.setComparableString();
            int numberOfLoadedEvents = theEventArray.size();

            /*Small optimization: Since events should be stored in order in the .txt file,
            each new event should be added on at the end of the list. We check for that first.
            More optimizations could be done, but JVM might do those on its own. Not sure.*/
            if(loadedEvent.compareTo(theEventArray.get(numberOfLoadedEvents -1)) >= 0) {
                theEventArray.add(loadedEvent);
            }
            else {
                for(int i = 0; i < numberOfLoadedEvents; i++) {
                    if(loadedEvent.compareTo(theEventArray.get(i)) < 0) {
                        theEventArray.add(i, loadedEvent);
                        break;
                    }
                }
            }

        }//Loop while
        System.out.println("* Event file: Found and loaded");

        eventReader.close();
    }//Method loadEvents

    public void completeSetup(PersonCollection pc) {
        correspondingPersonCollection = pc;
    }//Method completeSetup

//----------------------------------------------------------------
    public String toString() {
        int numberOfEvents = theEventArray.size();

        if(numberOfEvents == 0) return "Events: NONE";
        else if(numberOfEvents == 1) return "Event: 1";
        else return "Events: " + numberOfEvents;
    }//Method toString

    public void removePersonWhereInvited(String p) {
        for(Event e: theEventArray) e.removePersonIfInvited(p);
    }//Method removePersonWhereInvited

    public void updateHandleWhereInvited(String oldHandle, String updatedHandle) {
        for(Event e: theEventArray) e.updateHandleIfInvited(oldHandle, updatedHandle);
    }//Method updateHandleWhereInvited

    public void makeEvent() {

        String initialName;

        System.out.println("Enter a name for the new event (/list to view events, /cancel to return to main menu)");
        do {
            System.out.println("- Enter name: ");
            initialName = userEntry.nextLine().strip();

            if(initialName.toLowerCase().startsWith("/l")) {
                listEvents();
                continue;
            }
            else if(initialName.toLowerCase().startsWith("/c")) {
                System.out.println("Canceling making a new event.");
                return;
            }
        }
        while(!CommonMethods.stringIsSafeWithLength(initialName));

        ArrayList<String> initialStrings = new ArrayList<>(3);
        initialStrings.add(initialName);
        for(int i = 1; i <= 2; i++) initialStrings.add(null);

        Event newEvent = new Event(initialStrings, this, userEntry);
        newEvent.editThisEvent();
        newEvent.setComparableString(); //Should be in editThisEvent() in the end.

        insertEventIntoEventArray(newEvent);

        System.out.println("New event made: " + newEvent);
    }//Method makeEvent

    public void editEvent(String eventIndexString) {}//Method editEvent

    public void insertEventIntoEventArray(Event theEvent) {
        if(theEventArray.isEmpty()) theEventArray.add(theEvent);
        else if(theEvent.compareTo(theEventArray.get(theEventArray.size() -1)) >= 0) {
            theEventArray.add(theEvent);
        }
        else {
            for(int i = 0; i < theEventArray.size(); i++) {
                if(theEvent.compareTo(theEventArray.get(i)) < 0) {
                    theEventArray.add(i, theEvent);
                    break;
                }
            }
        }
    }//Method insertEventIntoEventArray

    public void viewEvent(String enteredIndex) {

        if(CommonMethods.stringIsIntInRange(enteredIndex, 0, theEventArray.size()-1)) {
            int eventIndex = Integer.parseInt(enteredIndex);
            theEventArray.get(eventIndex).viewThisEvent(eventIndex);
        }
        else {
            listEvents();
            System.out.println("\nView event (/cancel to return to main menu)");

            do {
                System.out.print("- Enter index: ");
                enteredIndex = userEntry.nextLine().strip().toLowerCase();
                if(CommonMethods.stringIsIntInRange(enteredIndex, 0, theEventArray.size()-1)) {
                    int eventIndex = Integer.parseInt(enteredIndex);
                    theEventArray.get(eventIndex).viewThisEvent(eventIndex);
                    break;
                }
            }
            while(!enteredIndex.startsWith("/c"));
        }
    }//Method viewEvent

    public void listEvents() {
        int numberOfEvents = theEventArray.size();

        if(numberOfEvents == 0) System.out.println("Events: NONE");
        else if(numberOfEvents == 1) System.out.println("Event (1):");
        else System.out.println("Events (" + numberOfEvents + "):");

        for(int i = 0; i < numberOfEvents; i++) {
            System.out.println(
                    "[" + i + "] " + theEventArray.get(i)
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