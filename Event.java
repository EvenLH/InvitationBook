import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Event {

    ArrayList<Integer> startTimeUnits; //Year, month, day, hour, minute.
    ArrayList<String> eventStrings; //Name, type, description
    HashMap<String, String> invitations; //Handle name, invitation status

    EventList myEventList;

    public Event(String storageString, EventList el) {
        startTimeUnits = new ArrayList<>(5);
        eventStrings = new ArrayList<>(3);
        invitations = new HashMap<>();
        myEventList = el;

        String[] temp = storageString.strip().split(";");

        //Filling start time units
        for(int i = 0; i <= 4; i++) {
            if(temp[i].equalsIgnoreCase("null")) startTimeUnits.add(i, null);
            else startTimeUnits.add(i, Integer.parseInt(temp[i]));
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

    public Event(Integer y, Integer m, Integer d, Integer h, Integer min, String en, EventList el) {
        startTimeUnits = new ArrayList<>(5);
        eventStrings = new ArrayList<>(3);
        invitations = new HashMap<>();
        myEventList = el;

        //Filling start time units
        startTimeUnits.add(0, y);
        startTimeUnits.add(1, m);
        startTimeUnits.add(2, d);
        startTimeUnits.add(3, h);
        startTimeUnits.add(4, min);

        //Filling event strings
        eventStrings.add(0, en);
        eventStrings.add(1, null);
        eventStrings.add(2, null);

        //ADD SOMETHING HERE TO ASK USER FOR TYPE, DESCRIPTION?

        //ADD SOMETHING HERE TO ASK USER FOR INVITATIONS?

    }//Method constructor 2

    public String toString() {
        String returnString;

        if(startTimeUnits.get(0) == null) returnString = "????";
        else returnString = String.valueOf(startTimeUnits.get(0));

        for(int i = 1; i <= 2; i++) {
            if(startTimeUnits.get(i) == null) returnString = returnString.concat("-??");
            else returnString = returnString.concat("-" + startTimeUnits.get(i));
        }

        returnString = returnString.concat(eventStrings.get(0));

        return returnString;
    }//Method toString

//----------------------------------------------------------------

    public String getStorageString() {
        String storageString = String.valueOf(startTimeUnits.get(0));

        for(int i = 1; i <= 4; i++) {
            storageString = storageString.concat(";" + startTimeUnits.get(i));
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