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

        thePersonCollection = new PersonCollection("storedPersons.txt");
        theEventCollection = new EventCollection("storedEvents.txt");

        userEntry = new Scanner(System.in);
        mainEntryArray = new String[]{"?"};

        thePersonCollection.setResourcePointers(theEventCollection, userEntry);
        theEventCollection.setResourcePointers(thePersonCollection, userEntry);
    }//Method open

    public static void readAndWrite() {}//Method readAndWrite

    public static void close() {
        System.out.println("\n----------------\n" +
                "Invitation Book closing");
        userEntry.close();
        thePersonCollection.storePersons();
        theEventCollection.storeEvents();

        System.out.println("\n----------------------------------------------------------------");
    }//Method close

//----------------------------------------------------------------
//----------------------------------------------------------------
}//Class InvitationBook