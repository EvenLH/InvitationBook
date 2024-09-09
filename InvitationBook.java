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
        mainEntryArray = new String[3];

        thePersonCollection = new PersonCollection("storedPersons.txt", "storedInterests.txt", userEntry);
        theEventCollection = new EventCollection("storedEvents.txt", userEntry);

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
            mainEntryArray = CommonMethods.commandStringToArray(userEntry.nextLine(), 3);
            if(mainEntryArray.length >= 1) mainEntryArray[0] = mainEntryArray[0].toLowerCase();
            if(mainEntryArray.length >= 2) mainEntryArray[1] = mainEntryArray[1].toLowerCase();

            //Handling main menu commands from the user.
            if(mainEntryArray.length == 0) listCommands();
            else if(mainEntryArray[0].startsWith("/c")) {}
            else if(mainEntryArray[0].startsWith("/m")) make();
            else if(mainEntryArray[0].startsWith("/e")) edit();
            else if(mainEntryArray[0].startsWith("/r")) remove();
            else if(mainEntryArray[0].startsWith("/v")) view();
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
    public static void make() {
        if(mainEntryArray.length <= 1) viewCommandMake();
        else if(mainEntryArray[1].startsWith("p")) thePersonCollection.makePerson();
        else if(mainEntryArray[1].startsWith("i")) System.out.println("Not yet written: make() for interest");
        else if(mainEntryArray[1].startsWith("e")) theEventCollection.makeEvent();
        else viewCommandMake();

        CommonMethods.returnToMainPrint();
    }//Method make

    public static void viewCommandMake() {
        System.out.println("Not yet written: viewCommandMake()");
    }//Method viewCommandMake

    public static void edit() {
        if(mainEntryArray.length <= 1) viewCommandEdit();
        else if(mainEntryArray[1].startsWith("p")) {
            if(mainEntryArray.length == 2) thePersonCollection.editPerson(null);
            else thePersonCollection.editPerson(mainEntryArray[2]);
        }
        else if(mainEntryArray[1].startsWith("i")) {
            if(mainEntryArray.length == 2) System.out.println("Not yet written: edit() for interest");
            else System.out.println("Not yet written: edit() for interest");
        }
        else if(mainEntryArray[1].startsWith("e")) {
            if(mainEntryArray.length == 2) theEventCollection.editEvent(null);
            else theEventCollection.editEvent(mainEntryArray[2]);
        }
        else viewCommandEdit();

        CommonMethods.returnToMainPrint();
    }//Method edit

    public static void viewCommandEdit() {
        System.out.println("Not yet written: viewCommandEdit()");
    }//Method viewCommandEdit

    public static void remove() {
        if(mainEntryArray.length <= 1) viewCommandRemove();
        else if(mainEntryArray[1].startsWith("p")) {
            if(mainEntryArray.length == 2) thePersonCollection.removePerson(null);
            else thePersonCollection.removePerson(mainEntryArray[2]);
        }
        else if(mainEntryArray[1].startsWith("i")) {
            if(mainEntryArray.length == 2) thePersonCollection.removeInterest(null);
            else thePersonCollection.removeInterest(mainEntryArray[2]);
        }
        else if(mainEntryArray[1].startsWith("e")) {
            if(mainEntryArray.length == 2) theEventCollection.removeEvent(null);
            else theEventCollection.removeEvent(mainEntryArray[2]);
        }
        else viewCommandRemove();

        CommonMethods.returnToMainPrint();
    }//Method remove

    public static void viewCommandRemove() {
        System.out.println("View command: REMOVE\n" +
                "This command deletes a person, interest or event.\n" +
                "The /remove command should be followed by a space and one of these words:\n" +
                "[*] person\n" +
                "[*] interest\n" +
                "[*] event\n" +
                "The chosen word may be followed by a space and an entry:\n" +
                "[*] For 'person': The handle name of an existing person.\n" +
                "[*] For 'interest': The name of an interest.\n" +
                "[*] For 'event': The index of an existing event.\n" +
                "Examples:\n" +
                "[*] /remove person\n" +
                "[*] /remove person Zeus");
    }//Method viewCommandRemove

    public static void view() {
        if(mainEntryArray.length <= 1) viewCommandView();
        else if(mainEntryArray[1].startsWith("p")) {
            if(mainEntryArray.length == 2) thePersonCollection.viewPerson(null);
            else thePersonCollection.viewPerson(mainEntryArray[2]);
        }
        else if(mainEntryArray[1].startsWith("i")) {
            if(mainEntryArray.length == 2) thePersonCollection.viewInterest(null);
            else thePersonCollection.viewInterest(mainEntryArray[2]);
        }
        else if(mainEntryArray[1].startsWith("e")) {
            if(mainEntryArray.length == 2) theEventCollection.viewEvent(null);
            else theEventCollection.viewEvent(mainEntryArray[2]);
        }
        else if(mainEntryArray[1].startsWith("c")) {
            if(mainEntryArray.length == 2) viewCommand(null);
            else viewCommand(mainEntryArray[2]);
        }
        else viewCommandView();

        CommonMethods.returnToMainPrint();
    }//Method view

    public static void viewCommandView() {
        System.out.println("Command: VIEW\n" +
                "This command shows all information about a given entry." +
                "The /view command should be followed by a space and one of these words:\n" +
                "[*] person\n" +
                "[*] interest\n" +
                "[*] event\n" +
                "[*] command\n" +
                "The chosen word may be followed by a space and an entry:\n" +
                "[*] For 'person': The handle name of an existing person.\n" +
                "[*] For 'interest': The name of an existing interest.\n" +
                "[*] For 'event': The index of an existing event.\n" +
                "[*] For 'command': The name of an existing command.\n" +
                "Examples:\n" +
                "[*] /view person\n" +
                "[*] /view person Odin"
        );
    }//Method viewCommandView

    public static void list() {
        if(mainEntryArray.length <= 1) viewCommandList();
        else if(mainEntryArray[1].startsWith("p")) thePersonCollection.listPersons();
        else if(mainEntryArray[1].startsWith("i")) thePersonCollection.listInterests();
        else if(mainEntryArray[1].startsWith("e")) theEventCollection.listEvents();
        else if(mainEntryArray[1].startsWith("c")) listCommands();
        else viewCommandList();

        CommonMethods.returnToMainPrint();
    }//Method list

    public static void viewCommandList() {
        System.out.println("Command: LIST\n" +
                "This command lists all entries of a given type.\n" +
                "The /list command should be followed by a space and one of these words:\n" +
                "[*] persons\n" +
                "[*] interests\n" +
                "[*] events\n" +
                "[*] commands\n" +
                "Example:\n" +
                "[*] /list events"
        );
    }//Method viewCommandList

    public static void viewCommand(String enteredCommand) {
        //I'll write this method last.
        System.out.println("Method not implemented: viewCommand(String enteredCommand)");
    }//Method viewCommand

    public static void listCommands() {
        System.out.println("Main menu commands:\n" +
                "[C] /make person|interest|event\n" +
                "[C] /edit person|interest|event [opt: handle|interest name|index]\n" +
                "[C] /remove person|interest|event [opt: handle|interest name|index]\n" +
                "[C] /view person|interest|event|command [opt: handle|interest name|index|command]\n" +
                "[C] /list persons|interests|events|commands"
        );
    }//Method listCommands

//----------------------------------------------------------------
}//Class InvitationBook