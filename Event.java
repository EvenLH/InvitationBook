import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Event {

    ArrayList<String> eventStrings; //Name, type, description
    ArrayList<Integer> eventStartTimeUnits; //Year, month, day, hour, minute.
    HashMap<String, String> invitations; //Handle name, invitation status

    EventList myEventList;

    public Event(String storageString, EventList el) {
        eventStrings = new ArrayList<>(3);
        eventStartTimeUnits = new ArrayList<>(5);
        invitations = new HashMap<>();
        myEventList = el;

        String[] temp = storageString.strip().split(";");

        //Filling start time units
        for(int i = 0; i <= 4; i++) {
            if(temp[i].equalsIgnoreCase("null")) eventStartTimeUnits.add(i, null);
            else eventStartTimeUnits.add(i, Integer.parseInt(temp[i]));
        }

        //Filling event strings
        for(int i = 5; i <= 7; i++) {
            if(temp[i].equalsIgnoreCase("null")) eventStrings.add(i-5, null);
            else eventStrings.add(i-5, temp[i]);
        }

        //Filling invitations
        for(int i = 8; i <= temp.length; i = i+2) {
            invitations.put(temp[i], temp[i+1]);
        }

    }//Method constructor 1

    public Event(ArrayList<String> es, ArrayList<Integer> et, EventList el) {
        eventStrings = new ArrayList<>(3);
        eventStartTimeUnits = new ArrayList<>(5);
        invitations = new HashMap<>();
        myEventList = el;

        //Filling event strings
        for(int i = 0; i <= 2; i++) {
            if(es.get(i).isEmpty())
                eventStrings.add(i, null);
            else eventStrings.add(i, es.get(i));
        }

        //Filling start time units
        for(int i = 0; i <= 4; i++) {
            if(et.get(i) == null)
                eventStrings.add(i, null);
            else eventStrings.add(i, es.get(i));
        }

    }//Method constructor 2

    public String toString() {
        String returnString;

        if(eventStartTimeUnits.get(0) == null) returnString = "????";
        else returnString = String.valueOf(eventStartTimeUnits.get(0));

        for(int i = 1; i <= 2; i++) {
            if(eventStartTimeUnits.get(i) == null) returnString = returnString.concat("-??");
            else returnString = returnString.concat("-" + eventStartTimeUnits.get(i));
        }

        returnString = returnString.concat(eventStrings.get(0));

        return returnString;
    }//Method toString

//----------------------------------------------------------------
    public void setInvitation(String p, String s) {
        if(invitations.containsKey(p)) {
            System.out.println("Updated invitation: " + p + ": " + s);
        }
    }

    public void removeInvitation(String p) {}

    public void wipeInvitations() {}

    public void showInvitations() {}

    public void editInvitations(Scanner ue) {}

//----------------------------------------------------------------
    public boolean hasPersonIgnoreCase(String p) {
        for(String key: invitations.keySet()) {
            if(key.equalsIgnoreCase(p)) return true;
        }

        return false;
    }//Method hasPersonIgnoreCase

    public String getPersonHandleCorrectCase(String p) {
        for(String key: invitations.keySet()) {
            if(key.equalsIgnoreCase(p)) return key;
        }

        return null;
    }//Method getPersonHandleCorrectCase

//----------------------------------------------------------------
    public String getStorageString() {
        String storageString = String.valueOf(eventStartTimeUnits.get(0));

        for(int i = 1; i <= 4; i++) {
            storageString = storageString.concat(";" + eventStartTimeUnits.get(i));
        }

        for(int i = 0; i <= 2; i++) {
            storageString = storageString.concat(";" + eventStrings.get(i));
        }

        for(Map.Entry<String, String> entry: invitations.entrySet()) {
            storageString = storageString.concat(";" + entry.getKey() + ";" + entry.getValue());
        }

        return storageString;
    }

}//Class Event