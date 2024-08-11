import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Event implements Comparable<Event> {

    ArrayList<Integer> startTimeUnits;
    ArrayList<String> eventStrings;
    HashMap<String, InvitationState> invitationMap;

    //Utilities
    EventCollection myEventCollection;
    String comparableString;
    Scanner userEntry;

    public Event(String storageString, EventCollection ec) {
        startTimeUnits = new ArrayList<>(5);
        eventStrings = new ArrayList<>(3);
        invitationMap = new HashMap<>();

        myEventCollection = ec;
        userEntry = null;

        String[] temp = storageString.strip().split(";");
        int tIndex = 0;

        //Filling start time units
        while(tIndex <= 4) {
            if(temp[tIndex].equalsIgnoreCase("null"))
                startTimeUnits.add(null);
            else startTimeUnits.add(Integer.parseInt(temp[tIndex]));

            tIndex++;
        }

        //Filling event strings
        while(tIndex <= 7) {
            if(temp[tIndex].equalsIgnoreCase("null"))
                eventStrings.add(null);
            else eventStrings.add(temp[tIndex]);

            tIndex++;
        }

        //Filling invitations
        while(tIndex < temp.length) {
            invitationMap.put(temp[tIndex], InvitationState.valueOf(temp[tIndex+1]));

            tIndex += 2;
        }

    }//Method Event constructor 1

    public Event(String n, EventCollection ec, Scanner ue) {
        startTimeUnits = new ArrayList<>(5);
        eventStrings = new ArrayList<>(3);
        invitationMap = new HashMap<>();

        myEventCollection = ec;
        userEntry = ue;

        for(int i = 0; i <= 4; i++) {
            startTimeUnits.add(null);
            if(i <= 2) eventStrings.add(null);
        }

        eventStrings.set(0, n);

    }//Method Event constructor 2

    public void setResourcePointers(Scanner ue) {
        userEntry = ue;
    }//Method setResourcePointers

//----------------------------------------------------------------
    public String toString() {
        String repString;

        if(startTimeUnits.get(0) == null) repString = "????";
        else repString = String.valueOf(startTimeUnits.get(0));

        for(int i = 1; i <= 2; i++) {
            if(startTimeUnits.get(i) == null) repString += "-??";
            else repString += "-" + startTimeUnits.get(i);
        }

        repString += " " + eventStrings.get(0);

        return repString;
    }//Method toString

    @Override
    public int compareTo(Event e) {
        return this.comparableString.compareTo(e.comparableString);
    }//Method compareTo

    public void setComparableString() {
        if(startTimeUnits.get(0) == null) comparableString = "****";
        else comparableString = String.valueOf(startTimeUnits.get(0));

        for(int i = 1; i <= 4; i++) {
            Integer num = startTimeUnits.get(i);

            if(num == null) comparableString += "**";
            else if(num <= 9) comparableString += "0" + num;
            else comparableString += num;
        }

        comparableString += eventStrings.get(0);
    }//Method setComparableString

//----------------------------------------------------------------
    public String getStorageString() {

        //Storing start time data
        String storageString = String.valueOf(startTimeUnits.get(0));

        for(int i = 1; i <= 4; i++) {
            storageString = storageString.concat(";" + startTimeUnits.get(i));
        }

        //Storing event string data
        for(int j = 0; j <= 2; j++) {
            storageString = storageString.concat(";" + eventStrings.get(j));
        }

        //Storing invitations and their state
        for(Map.Entry<String, InvitationState> invitation: invitationMap.entrySet()) {
            storageString = storageString.concat(
                    ";" + invitation.getKey() + ";" + invitation.getValue()
            );
        }

        //Sending back the finished storage string
        return storageString;
    }//Method getStorageString

//----------------------------------------------------------------
}//Class Event