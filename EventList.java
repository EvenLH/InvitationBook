import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class EventList {

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
    public void makeEvent() {}//Method makeEvent

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

    public boolean isValidInvName(String s) {
        return correspondingPersonList.thePersonMap.containsKey(s);
    }//Method isValidInvName

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