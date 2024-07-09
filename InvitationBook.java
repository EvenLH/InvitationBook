import java.util.Scanner;

public class InvitationBook {

    static PersonList thePersonList;
    static EventList theEventList;

    static Scanner userEntry;
    static String[] entryArray;

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
        entryArray = new String[]{"-", "-", "-"};

        thePersonList.setResourcePointers(theEventList, userEntry);
        theEventList.setResourcePointers(thePersonList, userEntry);
    }//Method opening

    public static void readingAndWriting() {
        System.out.println("\n--------------------------------\n" +
                "Reading and writing:\n");

        System.out.println("Main menu commands");
        listCommands();

        String mainEntry = "?";
        while(!mainEntry.toLowerCase().startsWith("c")) {
            System.out.print("\nMain menu entry: ");
            mainEntry = userEntry.nextLine().strip();
            entryArray = mainEntry.toLowerCase().split(" ");

            if(mainEntry.isEmpty()) listCommands();
            else if(entryArray[0].startsWith("m")) make();
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
        if(entryArray.length >= 2 && entryArray[1].startsWith("p")) thePersonList.makePerson();
        else if(entryArray.length >= 2 && entryArray[1].startsWith("e")) theEventList.makeEvent();
        else if(entryArray.length >= 2 && entryArray[1].startsWith("i")) thePersonList.makeInterest();
        else {
            System.out.println("Valid 'make' entries:\n" +
                    "Make person (make a new person)\n" +
                    "Make event (make a new event)\n" +
                    "Make interest (define a new or existing interest for every person)");
        }
    }//Method make

    public static void listCommands() {
        System.out.println("- Make person|event|interest\n");
    }//Method listCommands

}//Class InvitationBook