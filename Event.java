import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Event implements CommonValidations {

    ArrayList<String> eventStrings; //Name, type, description
    ArrayList<Integer> eventStartTimeUnits; //Year, month, day, hour, minute.
    HashMap<String, InvitationStatus> invitations; //Handle name, invitation status

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
        for(int i = 8; i < temp.length; i = i+2) {
            invitations.put(temp[i], InvitationStatus.valueOf(temp[i+1]));
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
                eventStartTimeUnits.add(i, null);
            else eventStartTimeUnits.add(i, et.get(i));
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

        returnString = returnString.concat(" " + eventStrings.get(0));

        return returnString;
    }//Method toString

//----------------------------------------------------------------
    public void setInvitation(String p, String s) {
        if(invitations.containsKey(p)) {
            System.out.println("Updated invitation status: " + p + ": " + s);
        }
        else if(personHandleIsInvitedIgnoreCase(p)) {
            invitations.remove(getPersonHandleCorrectCase(p));
            System.out.println("Updated invitation status: " + p + ": " + s);
        }
        else {
            System.out.println("Added invitation status: " + p + ": " + s);
        }

        invitations.put(p, InvitationStatus.getInvStatusEnum(s));
    }//Method setInvitations

    public void removeInvitation(String p) {
        String invitedPerson = getPersonHandleCorrectCase(p);

        if(invitedPerson == null) {
            System.out.println("No such person is invited: " + p);
        }
        else {
            invitations.remove(invitedPerson);
            System.out.println("Removed invitation: " + invitedPerson);
        }

    }//Method removeInterest

    public void wipeInvitations() {
        invitations.clear();
    }//Method wipeInvitation

    public void showInvitations() {
        if(invitations.isEmpty()) {
            System.out.println(this + " has no invited persons.");
        }
        else {
            System.out.println("Invitations to " + this + ":");

            for(InvitationStatus status: InvitationStatus.values()) {
                for(String key: invitations.keySet()) {
                    if(invitations.get(key) == status) {
                        System.out.println("- " + status + ": " + key);
                    }
                }
            }
        }

    }//Method showInvitations

    public void editInvitations(Scanner ue) {
        showInvitationEditingOptions();

        System.out.print("- Invitation entry: ");
        String editEntry = ue.nextLine().strip();
        String[] editArray = editEntry.split(" ");

        while(!editEntry.toLowerCase().startsWith("/c")) {

            if(editArray.length >= 3
            && editEntry.toLowerCase().startsWith("/m")
            && myEventList.correspondingPersonList.isExistingPersonHandleIgnoreCase(editArray[1])
            && InvitationStatus.isValidInvStatusIgnoreCase(editArray[2])) {
                setInvitation(editArray[1], editArray[2]);
            }
            else if(editArray.length >= 2
            && editEntry.toLowerCase().startsWith("/r")
            && myEventList.correspondingPersonList.isExistingPersonHandleIgnoreCase(editArray[1])) {
                removeInvitation(editArray[1]);
            }
            else if(editArray.length >= 1
            && editEntry.toLowerCase().startsWith("/l")) {
                showInvitations();
            }
            else if(editArray.length >= 1
            && editEntry.toLowerCase().startsWith("/w")) {
                invitations.clear();
                System.out.println(this + "'s invitations have been wiped.");
            }
            else showInvitationEditingOptions();

            System.out.print("- Invitation entry: ");
            editEntry = ue.nextLine().strip();
            editArray = editEntry.split(" ");
        }//Loop while

    }//Method editInvitations

    public void showInvitationEditingOptions() {
        System.out.println("Editing options:\n" +
                "- /make [personHandle] [status] (add or update an invitation)\n" +
                "- /remove [personHandle] (removes an invitation)\n" +
                "- /list (lists all invitations for this event)\n" +
                "- /wipe (removes all invitations for this event)\n" +
                "- /close (concludes invitation entry)\n" +
                "- /options (shows these options)");
    }//Method showInvitationEditingOptions

//----------------------------------------------------------------
    public boolean personHandleIsInvitedIgnoreCase(String p) {
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

        //Making the part of the string that stores time info.
        for(int i = 1; i <= 4; i++) {
            storageString = storageString.concat(";" + eventStartTimeUnits.get(i));
        }

        //Making the part of the string that stores name, type and description.
        for(int i = 0; i <= 2; i++) {
            storageString = storageString.concat(";" + eventStrings.get(i));
        }

        //Making the part of the string that stores invitation info.
        for(Map.Entry<String, InvitationStatus> entry: invitations.entrySet()) {
            storageString = storageString.concat(";" + entry.getKey() + ";" + entry.getValue());
        }

        return storageString;
    }//Method getStorageString

}//Class Event