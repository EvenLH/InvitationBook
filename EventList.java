import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class EventList implements CommonValidations {

    ArrayList<Event> theEventArray;
    String eventFileName;
    boolean guaranteedInChronologicalOrder;
    PersonList correspondingPersonList;
    Scanner userEntry;

    String[] possibleInvitationStatuses;

    public EventList(String eventFile) {
        theEventArray = new ArrayList<>();
        eventFileName = eventFile;
        guaranteedInChronologicalOrder = false; //Will be true when loaded?
        correspondingPersonList = null;
        userEntry = null;

        possibleInvitationStatuses = new String[]{"Accepted", "Invited", "Declined"};

        loadEvents(eventFile);

        //nameArray.trimToSize(); Maybe for the eventList somewhere.

    }//Method constructor

    public String toString() {
        String returnString = "The event list: ";
        int numberOfEvents = theEventArray.size();

        if(numberOfEvents == 0) returnString += "EMPTY";
        else if(numberOfEvents == 1) returnString += "1 event.";
        else returnString += numberOfEvents + " events.";

        return returnString;
    }

    public void loadEvents(String fileName) {

        File eventFile = new File(fileName);
        Scanner eventReader;

        try {
            eventReader = new Scanner(eventFile);
        }
        catch(FileNotFoundException fnfe) {
            System.out.println("- Event file - not found.");
            return;
        }

        while(eventReader.hasNextLine()) {
            theEventArray.add(new Event(eventReader.nextLine().strip(), this));
        }
        System.out.println("- Event file - found and loaded.");

        eventReader.close();
    }//Method loadEvents

    public void setResourcePointers(PersonList pl, Scanner ue) {
        correspondingPersonList = pl;
        userEntry = ue;
    }//Method setResourcePointers

//----------------------------------------------------------------
    public void makeEvent() {

        ArrayList<String> enteredTexts = new ArrayList<>(3);
        ArrayList<Integer> validatedTimeInts = new ArrayList<>(5);

        System.out.println("Enter event name. Type, description and all the time fields may be left blank.\n" +
                "Commands:\n" +
                "/cancel (stops making this event, and returns you to the main menu)");

        String enteredString;

        //Entering strings: Name, type (optionally), description (optionally).
        do {
            System.out.print("- Enter event name: ");
            enteredString = userEntry.nextLine().strip();
            if(enteredString.toLowerCase().startsWith("/c")) return;
        }
        while(!isValidStringWithLength(enteredString));
        enteredTexts.add(0, enteredString);

        do {
            System.out.print("- Enter event type: ");
            enteredString = userEntry.nextLine().strip();
            if(enteredString.toLowerCase().startsWith("/c")) return;
        }
        while(!isValidString(enteredString));
        enteredTexts.add(1, enteredString);

        do {
            System.out.print("- Enter event description: ");
            enteredString = userEntry.nextLine().strip();
            if(enteredString.toLowerCase().startsWith("/c")) return;
        }
        while(!isValidString(enteredString));
        enteredTexts.add(2, enteredString);

        //Entering numbers: Year, month, day, hour, minute (all optionally).
        do {
            System.out.print("- Enter starting year: ");
            enteredString = userEntry.nextLine().strip();
            if(enteredString.toLowerCase().startsWith("/c")) return;
        }
        while(!isValidNumberValue(enteredString, 1000, 9999));
        validatedTimeInts.add(0, Integer.parseInt(enteredString));

        do {
            System.out.print("- Enter starting month: ");
            enteredString = userEntry.nextLine().strip();
            if(enteredString.toLowerCase().startsWith("/c")) return;
        }
        while(!isValidNumberValue(enteredString, 1, 12));
        validatedTimeInts.add(1, Integer.parseInt(enteredString));

        do {
            System.out.print("- Enter starting day: ");
            enteredString = userEntry.nextLine().strip();
            if(enteredString.toLowerCase().startsWith("/c")) return;
        }
        while(!isValidNumberValue(enteredString, 1, 31));
        validatedTimeInts.add(2, Integer.parseInt(enteredString));

        do {
            System.out.print("- Enter starting hour: ");
            enteredString = userEntry.nextLine().strip();
            if(enteredString.toLowerCase().startsWith("/c")) return;
        }
        while(!isValidNumberValue(enteredString, 0, 23));
        validatedTimeInts.add(3, Integer.parseInt(enteredString));

        do {
            System.out.print("- Enter starting minute: ");
            enteredString = userEntry.nextLine().strip();
            if(enteredString.toLowerCase().startsWith("/c")) return;
        }
        while(!isValidNumberValue(enteredString, 0, 59));
        validatedTimeInts.add(4, Integer.parseInt(enteredString));

        //Instantiation and storage
        Event newEvent = new Event(enteredTexts, validatedTimeInts, this);
        theEventArray.add(newEvent);
        System.out.println("Event made: " + newEvent);

        //Letting the user add invitations.
        newEvent.editInvitations(userEntry);

    }//Method makeEvent

//----------------------------------------------------------------
    public boolean isValidEventArrayIndex(String s) {
        if(s == null || s.isEmpty()) return false;

        char[] c = s.toCharArray();
        for(int i = 0; i < c.length; i++) {
            if(!Character.isDigit(c[0])) return false;
        }

        int indexValue = Integer.parseInt(s);
        if(indexValue < 0 || theEventArray.size() <= indexValue) return false;
        if(theEventArray.get(indexValue) == null) return false;

        return true;
    }//Method isValidEventArrayIndex

    public boolean isExistingPersonHandle(String s) {
        return correspondingPersonList.thePersonMap.containsKey(s);
    }//Method isExistingPersonHandle

    public boolean isValidInvStatus(String s) {
        if(s == null) return false;

        for(int i = 0; i < possibleInvitationStatuses.length; i++) {
            if(s.equalsIgnoreCase(possibleInvitationStatuses[i])) {
                return true;
            }
        }

        return false;
    }//Method isValidInvStatus

//----------------------------------------------------------------
    public void storeEvents() {

        FileWriter targetFile;

        try {
            targetFile = new FileWriter(eventFileName, false);

            for(int i = 0; i < theEventArray.size(); i++) {
                targetFile.write(theEventArray.get(i).getStorageString() + "\n");
            }

            targetFile.close();
            System.out.println("- Event storage - complete.");
        }
        catch(IOException ioe) {
            System.out.println("- Event storage - possibly failed or incomplete.\n" +
                    "---" + ioe);
        }
    }//Method storeEvents

}//Class EventList