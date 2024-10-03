import java.util.*;

public class Event implements Comparable<Event> {

    ArrayList<Integer> startTimeUnits;
    ArrayList<String> eventStrings;
    HashMap<String, InvitationState> invitationMap;

    //Utilities
    EventCollection myEventCollection;
    String comparableString;
    Scanner userEntry;

    public Event(String storageString, EventCollection ec, Scanner ue) {
        startTimeUnits = new ArrayList<>(5);
        eventStrings = new ArrayList<>(3);
        invitationMap = new HashMap<>();

        //Utilities
        myEventCollection = ec;
        comparableString = null;
        userEntry = ue;

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

    public Event(ArrayList<String> es, EventCollection ec, Scanner ue) {
        startTimeUnits = new ArrayList<>(5);
        eventStrings = es;
        invitationMap = new HashMap<>();

        myEventCollection = ec;
        comparableString = null;
        userEntry = ue;

        for(int i = 0; i <= 4; i++) {
            startTimeUnits.add(null);
        }

    }//Method Event constructor 2

    public void completeNewEventSetup() {
        setComparableString();
    }//Method completeEventSetup

//----------------------------------------------------------------
    public String toString() {
        String repString = getDateString();

        repString += " " + eventStrings.get(0);

        return repString;
    }//Method toString

    public void updateHandleIfInvited(String oldHandle, String updatedHandle) {
        if(invitationMap.containsKey(oldHandle)) invitationMap.put(updatedHandle, invitationMap.remove(oldHandle));
    }//Method updateHandleIfInvited

    public void removePersonIfInvited(String p) {
        invitationMap.remove(p);
    }//Method removePersonIfInvited

    public String getDateString() {
        String dateString;

        if(startTimeUnits.get(0) == null) dateString = "????";
        else dateString = String.valueOf(startTimeUnits.get(0));

        for(int i = 1; i <= 2; i++) {
            Integer theTimeUnit = startTimeUnits.get(i);
            if(theTimeUnit == null) dateString += "-??";
            else if(theTimeUnit <= 9) dateString += "-0" + theTimeUnit;
            else dateString += "-" + startTimeUnits.get(i);
        }

        return dateString;
    }//Method getDateString

    public String getClockString() {
        String clockString;

        if(startTimeUnits.get(3) == null) clockString = "??";
        else if(startTimeUnits.get(3) <= 9) clockString = "0" + startTimeUnits.get(3);
        else clockString = String.valueOf(startTimeUnits.get(3));

        if(startTimeUnits.get(4) == null) clockString += ".??";
        else if(startTimeUnits.get(4) <= 9) clockString += ".0" + startTimeUnits.get(4);
        else clockString += "." + startTimeUnits.get(4);

        return clockString;
    }

    public boolean editThisEvent() {
        setComparableString();
        final String initialComparisonString = comparableString;

        String editEntryCommand;
        String[] editEntryArray;

        listCommandsEditEvent();
        do {
            System.out.print("\n- Edit event entry: ");
            editEntryCommand = userEntry.nextLine();

            //Preparing the command for use.
            if(editEntryCommand.toLowerCase().startsWith("/h")
            || editEntryCommand.toLowerCase().startsWith("/i")
            || editEntryCommand.toLowerCase().startsWith("/r")) {
                editEntryArray = CommonMethods.commandStringToArray(editEntryCommand, 3);
            }
            else editEntryArray = CommonMethods.commandStringToArray(editEntryCommand, 2);

            editEntryArray[0] = editEntryArray[0].toLowerCase();

            //Performing the contents of the command.
            if(editEntryArray[0].startsWith("/n")) {
                if(editEntryArray.length == 2
                && CommonMethods.stringIsSafeWithLength(editEntryArray[1])) {
                    eventStrings.set(0, editEntryArray[1]);
                    System.out.println("Event name set: " + eventStrings.get(0));
                }
                else if(editEntryArray.length == 2) {
                    System.out.println("Must be at least one character long. Can't start with '/', contain ';' or be 'null' in any capitalisation.");
                }
                else
                    System.out.println("/name requires you to enter a new name for this event.");
            }

            else if(editEntryArray[0].startsWith("/t")) {
                if(editEntryArray.length == 2
                && CommonMethods.stringIsSafeWithLength(editEntryArray[1])) {
                    eventStrings.set(1, editEntryArray[1]);
                    System.out.println("Event type set: " + eventStrings.get(1));
                }
                else if(editEntryArray.length == 2) {
                    System.out.println("Must be at least one character long. Can't start with '/', contain ';' or be 'null' in any capitalisation.");
                }
                else
                    System.out.println("/type requires you to enter a new type for this event.");
            }

            else if(editEntryArray[0].startsWith("/a")) {
                if(editEntryArray.length == 2
                && CommonMethods.stringIsSafeWithLength(editEntryArray[1])) {
                    eventStrings.set(2, editEntryArray[1]);
                    System.out.println("Event description set: " + eventStrings.get(2));
                }
                else if(editEntryArray.length == 2) {
                    System.out.println("Must be at least one character long. Can't start with '/', contain ';' or be 'null' in any capitalisation.");
                }
                else
                    System.out.println("/about requires you to enter a new description for this event.");
            }

            else if(editEntryArray[0].startsWith("/y")) {
                if(editEntryArray.length == 2
                && CommonMethods.stringIsIntInRange(editEntryArray[1], 1000, 9999)) {
                    startTimeUnits.set(0, Integer.parseInt(editEntryArray[1]));
                    System.out.println("Event year set: " + startTimeUnits.get(0));
                }
                else if(editEntryArray.length == 2) {
                    System.out.println("Must be a number from 1000 to 9999.");
                }
                else
                    System.out.println("/year requires you to enter a new start year for this event.");
            }

            else if(editEntryArray[0].startsWith("/m")) {
                if(editEntryArray.length == 2
                && CommonMethods.stringIsIntInRange(editEntryArray[1], 1, 12)) {
                    startTimeUnits.set(1, Integer.parseInt(editEntryArray[1]));
                    System.out.println("Event month set: " + startTimeUnits.get(1));
                }
                else if(editEntryArray.length == 2) {
                    System.out.println("Must be a number from 1 to 12.");
                }
                else
                    System.out.println("/month requires you to enter a new start month for this event.");
            }

            else if(editEntryArray[0].startsWith("/d")) {
                if(editEntryArray.length == 2
                && CommonMethods.stringIsIntInRange(editEntryArray[1], 1, 31)) {
                    startTimeUnits.set(2, Integer.parseInt(editEntryArray[1]));
                    System.out.println("Event day set: " + startTimeUnits.get(2));
                }
                else if(editEntryArray.length == 2) {
                    System.out.println("Must be a number from 1 to 31.");
                }
                else
                    System.out.println("/day requires you to enter a new start day for this event.");
            }

            else if(editEntryArray[0].startsWith("/h")) {
                if(editEntryArray.length == 3) {

                    if(CommonMethods.stringIsIntInRange(editEntryArray[1], 0, 23)
                    && CommonMethods.stringIsIntInRange(editEntryArray[2], 0, 59)) {
                        startTimeUnits.set(3, Integer.parseInt(editEntryArray[1]));
                        startTimeUnits.set(4, Integer.parseInt(editEntryArray[2]));
                    }
                    else System.out.println("Hour must be a number from 0 to 23. If specifying minute, it must be a number from 0 to 59.");
                }
                else if(editEntryArray.length == 2) {

                    if(CommonMethods.stringIsIntInRange(editEntryArray[1], 0, 23)) {
                        startTimeUnits.set(3, Integer.parseInt(editEntryArray[1]));
                        startTimeUnits.set(4, 0);
                    }
                    else System.out.println("Hour must be a number from 0 to 23.");
                }
                else System.out.println("/hour requires you to enter a new start hour for this event. Optionally, you may add another space and specify a minute.");
            }

            else if(editEntryArray[0].startsWith("/i")) {

                if(editEntryArray.length == 3) {
                    String existingHandle = myEventCollection.correspondingPersonCollection.userFindsExistingPersonKey(editEntryArray[1]);
                    InvitationState invState = InvitationState.stringToEnum(editEntryArray[2]);

                    if(existingHandle == null || invState == null) {
                        System.out.println("/invite requires you to enter an existing person handle. If entering an answer, it must be 'attending', 'pending' or 'declined'.");
                        continue;
                    }
                    else {
                        invitationMap.put(existingHandle, invState);
                        System.out.println("Invitation set: " + existingHandle + " - " + invState);
                    }
                }
                else if(editEntryArray.length == 2) {
                    String existingHandle = myEventCollection.correspondingPersonCollection.userFindsExistingPersonKey(editEntryArray[1]);

                    if(existingHandle == null) {
                        System.out.println("/invite requires you to enter an existing person handle. The default answer is none is entered is that the answer is 'pending'.");
                        continue;
                    }
                    else {
                        invitationMap.put(existingHandle, InvitationState.Pending);
                        System.out.println("Invitation set: " + existingHandle + " - Pending");
                    }
                }
                else {
                    System.out.println("/invite requires you to enter an existing person handle. If entering an answer, it must be 'attending', 'pending' or 'declined'.");
                }
            }

            else if(editEntryArray[0].startsWith("/r")) {

                if(editEntryArray.length >= 2) {

                    if(editEntryArray[1].startsWith("t")) {
                        eventStrings.set(1, null);
                        System.out.println("Deleted event type.");
                    }
                    else if(editEntryArray[1].startsWith("a")) {
                        eventStrings.set(2, null);
                        System.out.println("Deleted event description.");
                    }
                    else if(editEntryArray[1].startsWith("y")) {
                        startTimeUnits.set(0, null);
                        System.out.println("Deleted starting year.");
                    }
                    else if(editEntryArray[1].startsWith("m")) {
                        startTimeUnits.set(1, null);
                        System.out.println("Deleted starting month.");
                    }
                    else if(editEntryArray[1].startsWith("d")) {
                        startTimeUnits.set(2, null);
                        System.out.println("Deleted starting day.");
                    }
                    else if(editEntryArray[1].startsWith("h")) {
                        startTimeUnits.set(3, null);
                        startTimeUnits.set(4, null);
                        System.out.println("Deleted starting hour and minute.");
                    }
                    else if(editEntryArray[1].startsWith("i")) {
                        if(editEntryArray.length == 3) {
                            String existingHandle = myEventCollection.correspondingPersonCollection.userFindsExistingPersonKey(editEntryArray[2]);

                            if(existingHandle == null) {
                                System.out.println("For /remove invite, you must also enter an existing person's handle name.\n" +
                                        "Example: /remove invite SigridK");
                            }
                            else {
                                invitationMap.remove(existingHandle);
                                System.out.println("Deleted invitation: " + myEventCollection.correspondingPersonCollection.getPersonsToString(existingHandle));
                            }
                        }
                        else System.out.println("For /remove invite, you must also enter an existing person's handle name.\n" +
                                "Example: /remove invite Erik");
                    }
                }
                else {
                    System.out.println("/remove must be followed by 'type', 'about', 'year', 'month', 'day', 'hour' or 'invite'.\n" +
                            "If 'invite', you must also enter an existing person's handle name.");
                }
            }

            else if(editEntryArray[0].startsWith("/w")) {
                if(editEntryArray.length == 2) {
                    editEntryArray[1] = editEntryArray[1].toLowerCase();

                    if(editEntryArray[1].startsWith("/t")) {
                        wipeEventTexts();
                        System.out.println("Removed event type and description.\n" +
                                "Note: Event name can't be deleted (only changed).");
                    }
                    else if(editEntryArray[1].startsWith("/w")) {
                        wipeEventStartTimes();
                        System.out.println("Removed all event times.");
                    }
                    else if(editEntryArray[1].startsWith("/i")) {
                        wipeEventInvitations();
                        System.out.println("Removed all invitations.");
                    }
                    else if(editEntryArray[1].startsWith("/a")) {
                        wipeEventTexts();
                        wipeEventStartTimes();
                        wipeEventInvitations();
                        System.out.println("Removed event type, description, all event times and all invitations.\n" +
                                "Note: Event name can't be deleted (only changed).");
                    }
                }
            }

            else if(editEntryArray[0].startsWith("/v")) {
                viewThisEvent(null);
            }

            else if(!editEntryArray[0].startsWith("/c")) {
                listCommandsEditEvent();
            }
        }
        while(!editEntryArray[0].startsWith("/c"));

        //Finishing
        //Returning 'true' means it may need to be moved in the event array.
        setComparableString();
        return !initialComparisonString.equals(comparableString);
    }//Method editThisEvent

    public void wipeEventTexts() {
        eventStrings.set(1, null);
        eventStrings.set(2, null);
    }//Method wipeEventTexts

    public void wipeEventStartTimes() {
        for(int i = 0; i <= 4; i++) {
            startTimeUnits.set(i, null);
        }
    }//Method wipeEventStartTimes

    public void wipeEventInvitations() {
        invitationMap.clear();
    }//Method wipeEventInvitations

    public void listCommandsEditEvent() {
        System.out.println("Commands: Edit event\n" +
                "[C] /name [new name]\n" +
                "[C] /type [new type]\n" +
                "[C] /about [new text]\n" +
                "[C] /year [number from 1000 to 9999]\n" +
                "[C] /month [number from 1 to 12]\n" +
                "[C] /day [number from 1 to 31]\n" +
                "[C] /hour [number from 0 to 23] [opt: number from 0 to 59]\n" +
                "[C] /invite [personHandle] opt: Attending|Pending|Declined\n" +
                "[C] /remove type|about|year|month|day|hour|invite [invite: personHandle]\n" +
                "[C] /wipe texts|when|invitations|all\n" +
                "[C] /view\n" +
                "[C] /conclude\n");
    }//Method listCommandsEditEvent

    public void viewThisEvent(Integer myIndex) {
        System.out.println("Event: " + eventStrings.get(0) +
                "\n[*] Name: " + eventStrings.get(0));

        if(eventStrings.get(1) == null) System.out.println("[*] Type:");
        else System.out.println("[*] Type: " + eventStrings.get(1));

        System.out.println("[*] Date: " + getDateString());
        System.out.println("[*] Time: " + getClockString());
        if(myIndex == null) System.out.println("[*] Index: Unknown");
        else System.out.println("[*] Index: " + myIndex);

        if(eventStrings.get(2) != null) System.out.println("\n" + eventStrings.get(2));

        //Printing invited persons - headline
        int numberOfInvitations = invitationMap.size();
        if(numberOfInvitations == 0) return;
        else if(numberOfInvitations == 1) System.out.println("\nInvited person (1)");
        else System.out.println("\nInvited persons (" + numberOfInvitations + ")");

        //Printing invitations - each invitation
        HashSet<String> attendingSet = new HashSet<>();
        HashSet<String> pendingSet = new HashSet<>();
        HashSet<String> declinedSet = new HashSet<>();

        for(Map.Entry<String, InvitationState> invEntry: invitationMap.entrySet()) {
            switch(invEntry.getValue().toString()) {
                case "Attending":
                    attendingSet.add(invEntry.getKey());
                    break;
                case "Pending":
                    pendingSet.add(invEntry.getKey());
                    break;
                case "Declined":
                    declinedSet.add(invEntry.getKey());
                    break;
            }
        }

        ArrayList<String> attendingOrdered = CommonMethods.stringSetToOrderedArrayList(attendingSet);
        ArrayList<String> pendingOrdered = CommonMethods.stringSetToOrderedArrayList(pendingSet);
        ArrayList<String> declinedOrdered = CommonMethods.stringSetToOrderedArrayList(declinedSet);

        if(!attendingOrdered.isEmpty()) {
            System.out.println("Attending (" + attendingOrdered.size() + "):");

            for(int i = 0; i < attendingOrdered.size(); i++) {
                String handleCorrectCase = myEventCollection.correspondingPersonCollection.getPersonsToString(attendingOrdered.get(i));
                System.out.println("[A] " + handleCorrectCase);
            }
        }

        if(!pendingOrdered.isEmpty()) {
            System.out.println("\nAnswer pending (" + pendingOrdered.size() + "):");

            for(int i = 0; i < pendingOrdered.size(); i++) {
                String handleCorrectCase = myEventCollection.correspondingPersonCollection.getPersonsToString(pendingOrdered.get(i));
                System.out.println("[P] " + handleCorrectCase);
            }
        }

        if(!declinedOrdered.isEmpty()) {
            System.out.println("\nDeclined (" + declinedOrdered.size() + "):");

            for(int i = 0; i < declinedOrdered.size(); i++) {
                String handleCorrectCase = myEventCollection.correspondingPersonCollection.getPersonsToString(declinedOrdered.get(i));
                System.out.println("[D] " + handleCorrectCase);
            }
        }

    }//Method viewThisEvent

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