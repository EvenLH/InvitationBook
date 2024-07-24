import java.util.Scanner;

public class InvitationBook {

    static PersonList thePersonList;
    static EventList theEventList;

    static Scanner userEntry;
    static String[] mainEntryArray;

    public static void main(String[] args) {
        opening();
        readingAndWriting();
        closing();
    }//Method main

//----------------------------------------------------------------

    public static void opening() {
        System.out.println("----------------------------------------------------------------\n" +
                "Opening the Invitation Book:");
        thePersonList = new PersonList("storedPersons.txt");
        theEventList = new EventList("storedEvents.txt");

        userEntry = new Scanner(System.in);
        mainEntryArray = new String[]{"-", "-", "-"};

        thePersonList.setResourcePointers(theEventList, userEntry);
        theEventList.setResourcePointers(thePersonList, userEntry);
    }//Method opening

    public static void readingAndWriting() {
        System.out.println("\n--------------------------------\n" +
                "Reading and writing:\n");

        System.out.println("Main menu commands");
        listCommands();

        String mainEntry = "?";
        while(!mainEntry.toLowerCase().startsWith("/c")) {
            System.out.print("\nMain menu entry: ");
            mainEntry = userEntry.nextLine().strip();
            mainEntryArray = mainEntry.toLowerCase().split(" ");

            if(mainEntry.isEmpty()) listCommands();
            else if(mainEntryArray[0].startsWith("/m")) make();
            else if(mainEntryArray[0].startsWith("/e")) edit();
        }//Loop while
    }//Method readingAndWriting

    public static void closing() {
        System.out.println("\n--------------------------------\n" +
                "Closing the Invitation Book:");
        thePersonList.storePersons();
        theEventList.storeEvents();

        System.out.println("\n----------------------------------------------------------------");
    }//Method closing

//----------------------------------------------------------------

    public static void make() {
        if(mainEntryArray.length >= 2 && mainEntryArray[1].startsWith("p"))
            thePersonList.makePerson();
        else if(mainEntryArray.length >= 2 && mainEntryArray[1].startsWith("e"))
            theEventList.makeEvent();
        else if(mainEntryArray.length >= 2 && mainEntryArray[1].startsWith("i"))
            thePersonList.makeInterest();
        else {
            System.out.println("Valid 'make' entries:\n" +
                    "/make person (make a new person)\n" +
                    "/make event (make a new event)\n" +
                    "/make interest (define a new or existing interest for every person)");
        }
    }//Method make

    public static void edit() {
        if(mainEntryArray.length >= 3 && mainEntryArray[1].startsWith("p") && thePersonList.isExistingPersonHandleIgnoreCase(mainEntryArray[2])) {}
        else if(mainEntryArray.length >= 3 && mainEntryArray[1].startsWith("e") && theEventList.isExistingEventIndex(mainEntryArray[2])) {}
        else if(mainEntryArray.length >= 2 && mainEntryArray[1].startsWith("p")) {}
        else if(mainEntryArray.length >= 2 && mainEntryArray[1].startsWith("e")) {}
        else {}
    }//Method edit

    public static void listCommands() {
        System.out.println("/make person|event|interest\n" +
                "/edit person|event [personHandle|index]");
    }//Method listCommands

}//Class InvitationBook