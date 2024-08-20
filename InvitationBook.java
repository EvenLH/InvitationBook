import java.util.Scanner;

public class InvitationBook {

    static PersonCollection thePersonCollection;
    static EventCollection theEventCollection;

    static Scanner userEntry;
    static String[] mainEntryArray;

    public static void main(String[] args) {
        open();
        readAndWrite();
        close();
    }//Method main

//----------------------------------------------------------------
    public static void open() {
        System.out.println("----------------------------------------------------------------\n" +
                "Invitation Book opening");

        userEntry = new Scanner(System.in);
        mainEntryArray = new String[]{"?"};

        thePersonCollection = new PersonCollection("storedPersons.txt", "storedInterests.txt", userEntry);
        theEventCollection = new EventCollection("storedEvents.txt", userEntry);

        thePersonCollection.loadPersons();
        thePersonCollection.loadInterests();
        theEventCollection.loadEvents();

        thePersonCollection.completeSetup(theEventCollection);
        theEventCollection.completeSetup(thePersonCollection);
    }//Method open

    public static void readAndWrite() {
        System.out.println("\n----------------\n" +
                "Reading and writing\n");

        listCommands();

        do {
            //Taking main menu commands from the user.
            System.out.print("\n- Main menu entry: ");
            mainEntryArray =
                    userEntry.nextLine().strip().toLowerCase().split(" ");

            //Handling main menu commands from the user.
            if(mainEntryArray.length == 0) listCommands();
            else if(mainEntryArray[0].startsWith("/c")) {}
            else if(mainEntryArray[0].startsWith("/m")) make();
            else if(mainEntryArray[0].startsWith("/e")) edit();
            else if(mainEntryArray[0].startsWith("/l")) list();
            else listCommands();
        }
        while(!mainEntryArray[0].startsWith("/c"));
    }//Method readAndWrite

    public static void close() {
        System.out.println("\n----------------\n" +
                "Invitation Book closing");
        userEntry.close();
        thePersonCollection.storePersons();
        thePersonCollection.storeInterests();
        theEventCollection.storeEvents();

        System.out.println("\n----------------------------------------------------------------");
    }//Method close

//----------------------------------------------------------------
    public static void make() {}//Method make

    public static void edit() {}//Method edit

    public static void list() {
        if(mainEntryArray.length <= 1) {}
        else if(mainEntryArray[1].startsWith("p")) thePersonCollection.listPersons();
        else if(mainEntryArray[1].startsWith("i")) thePersonCollection.listInterests();
        else if(mainEntryArray[1].startsWith("e")) theEventCollection.listEvents();
        else if(mainEntryArray[1].startsWith("c")) listCommands();
        else {}
    }//Method list

    public static void listCommands() {
        System.out.println("Main menu commands:\n" +
                "[C] /make person|interest|event\n" +
                "[C] /edit person|interest|event [opt: handle|interest name|index]\n" +
                "[C] /list persons|interests|events|commands"
        );
    }//Method listCommands

//----------------------------------------------------------------
}//Class InvitationBook