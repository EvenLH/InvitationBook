import java.util.Scanner;

public class InvitationBook {

    static PersonList thePersonList;
    static EventList theEventList;

    static Scanner userWrites;
    static String[] entryArray;

    public static void main(String[] args) {
        opening();
        readingAndWriting();
        closing();
    }//Method main

//----------------------------------------------------------------

    public static void opening() {
        System.out.println("----------------------------------------------------------------\n" +
                "Welcome to the Invitation Book!\n\n" +
                "Opening:");
        thePersonList = new PersonList("storedPersons.txt");
        theEventList = new EventList("storedEvents.txt");

        thePersonList.setCorrespondingEventList(theEventList);
        theEventList.setCorrespondingPersonList(thePersonList);
    }//Method opening

    public static void readingAndWriting() {
        System.out.println("\n--------------------------------\n" +
                "Reading and writing:\n");

        System.out.println("Main menu commands");
        listCommands();

        userWrites = new Scanner(System.in);
        entryArray = new String[]{"-", "-", "-"};

        while(!entryArray[0].toLowerCase().startsWith("c")) {
            System.out.print("\nMain menu entry: ");
            entryArray = userWrites.nextLine().strip().toLowerCase().split(" ");

            if(entryArray.length == 0) {}
            else if(entryArray[0].startsWith("m")) make();
        }//Loop while
    }//Method readingAndWriting

    public static void closing() {
        System.out.println("\n--------------------------------\n" +
                "Closing:");

        thePersonList.storePersons();
        theEventList.storeEvents();

        System.out.println("\n----------------------------------------------------------------");
    }//Method closing

//----------------------------------------------------------------

    public static void make() {}//Method make

    public static void listCommands() {
        System.out.println("- Make person|event|interest\n");
    }//Method listCommands

}//Class InvitationBook