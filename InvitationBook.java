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
            else if(mainEntryArray[0].startsWith("/m")) make();
            else if(mainEntryArray[0].startsWith("/e")) edit();
            else if(mainEntryArray[0].startsWith("/r")) remove();
            else if(mainEntryArray[0].startsWith("/v")) view();
            else if(mainEntryArray[0].startsWith("/w")) wipe();
            else if(mainEntryArray[0].startsWith("/l")) list();
            else if(mainEntryArray[0].startsWith("/a")) about();
            else if(!mainEntryArray[0].startsWith("/c")) listCommands();
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
        else if(mainEntryArray[1].startsWith("i")) thePersonCollection.manageInterest();
        else if(mainEntryArray[1].startsWith("e")) theEventCollection.makeEvent();
        else viewCommandMake();

        CommonMethods.returnToMainPrint();
    }//Method make

    public static void viewCommandMake() {
        System.out.println("View command: MAKE\n" +
                "This command lets you add a new person or event. You can also add or manage an interest.\n" +
                "The /make command must be followed by a space and one of these words:\n" +
                "[*] person\n" +
                "[*] interest\n" +
                "[*] event\n"
        );
    }//Method viewCommandMake

    public static void edit() {
        if(mainEntryArray.length <= 1) viewCommandEdit();
        else if(mainEntryArray[1].startsWith("p")) {
            if(mainEntryArray.length == 2) thePersonCollection.editPerson(null);
            else thePersonCollection.editPerson(mainEntryArray[2]);
        }
        else if(mainEntryArray[1].startsWith("e")) {
            if(mainEntryArray.length == 2) theEventCollection.editEvent(null);
            else theEventCollection.editEvent(mainEntryArray[2]);
        }
        else viewCommandEdit();

        CommonMethods.returnToMainPrint();
    }//Method edit

    public static void viewCommandEdit() {
        System.out.println("View command: EDIT\n" +
                "This command lets you edit a person or event.\n" +
                "The /edit command must be followed by a space and one of these options:\n" +
                "[*] person\n" +
                "[*] event\n" +
                "The chosen option may be followed by a space and a value:\n" +
                "[*] For 'person': The handle name of an existing person.\n" +
                "[*] For 'event': The index of an existing event.\n" +
                "Examples:\n" +
                "[*] /remove person\n" +
                "[*] /remove person Zeus"
        );
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
                "[*] /remove person Zeus"
        );
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

    public static void wipe() {
        if(mainEntryArray.length <= 1) viewCommandWipe();
        else if(mainEntryArray[1].startsWith("p")) thePersonCollection.wipePersons();
        else if(mainEntryArray[1].startsWith("i")) thePersonCollection.wipeInterests();
        else if(mainEntryArray[1].startsWith("e")) theEventCollection.wipeEvents();
        else viewCommandWipe();

        CommonMethods.returnToMainPrint();
    }//Method wipe

    public static void viewCommandWipe() {
        System.out.println("View command: WIPE\n" +
                "This command deletes all persons, interests or events.\n" +
                "The /wipe command should be followed by a space and one of these words:\n" +
                "[*] persons\n" +
                "[*] interests\n" +
                "[*] events\n" +
                "Example:\n" +
                "[*] /wipe events"
        );
    }//Method viewCommandWipe

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

    public static void about() {
        if(mainEntryArray.length <= 1) viewCommandAbout();
        else if(mainEntryArray[1].startsWith("t")
        || mainEntryArray[1].startsWith("b")) {
            System.out.println("The Invitation Book\n" +
                    "The Invitation Book is a simple event and invitation planner. The user can add persons and events\n" +
                    "to the program. Then they can manage what people have been invited to their events, and what each\n" +
                    "person has answered. The program looks for .txt files to load saved data from when it starts. When\n" +
                    "shutting down, it updates those same files with new data.\n\n" +
                    "[*] Program version: 0.?\n" +
                    "[*] Version date: ?\n" +
                    "[*] Author: Even Lindell Heggø\n" +
                    "[*] Programming language: Java\n"
            );
        }
        else if(mainEntryArray[1].startsWith("u")) {
            System.out.println("Usage\n" +
                    "The user interacts with the program by entering commands and values.\n\n" +
                    "Values are pieces of information you enter for storage and future use. They must conform to certain\n" +
                    "criteria, depending on what kind of information you are entering. If you are entering something\n" +
                    "that doesn't begin with a '/', it is a value. Values are sometimes entered as part of a command.\n\n" +
                    "Commands are the main way to interact with the program. Each command starts with a command word,\n" +
                    "and may then be followed by entering options and/or values, depending in the command. If you are\n" +
                    "entering something that begins with a '/', it is a command.\n" +
                    "[*] Command words: Starts with a '/' and then a word.\n" +
                    "    Example: /close\n" +
                    "[*] Options: Enter one word from a selection, with possibilities separated by '|'.\n" +
                    "    Example: person|interest|event\n" +
                    "[*] Values: Shown in [], with a description of what you need to enter inside. Values may be\n" +
                    "            optional, or only required in certain cases. If so, this is shown inside the [], before\n" +
                    "            a ':'." +
                    "    Example: [number from 1 to 12]\n" +
                    "    Example: [opt: handle|interest name|index]\n\n" +
                    "Both command words and options can be shortened to their initial letter, and it will still work.\n\n" +
                    "Complete examples:" +
                    "[E] /close\n" +
                    "    The /close command doesn't require any options or values to be entered.\n" +
                    "[E] /view person|interest|event|command [opt: handle|interest name|index|command]\n" +
                    "    The /view command must be followed by one of four options. You may then enter a value\n" +
                    "    corresponding to the option you selected if you wish to. It might look like one of these:\n" +
                    "    /view person\n" +
                    "    /view person IsaacN\n" +
                    "    /v p IsaacN\n"
            );
        }
        else if(mainEntryArray[1].startsWith("p")) {
            System.out.println("Persons\n\n" +
                    "Persons consist of values: Names and interests. Only handle names are mandatory.\n" +
                    "\n" +
                    "Person\n" +
                    "[*] Handle name: Each person must have exactly one handle name (no spaces). This is the name you\n" +
                    "    use to specify a person.\n" +
                    "[*] First names: A person can have any number of first names.\n" +
                    "[*] Middle names: A person can have any number of middle names.\n" +
                    "[*] Last name: A person can have up to one last name (no spaces).\n" +
                    "[*] Interests: A person can have any number of interest entries.\n" +
                    "    [*] Interest entry: [interest name] [interest level from 0 to 3]"
            );
        }
        else if(mainEntryArray[1].startsWith("i")) {
            System.out.println("Interests\n" +
                    "Persons have interests.\n" +
                    "\n" +
                    "A person has a list of interests. Each interest entry must have a name for the interest that is at\n" +
                    "least one character long. It must also have an interest level. The interest level says how\n" +
                    "interested that person is in that activity." +
                    "\n" +
                    "Interest levels:\n" +
                    "[3] Highly interested\n" +
                    "[2] Interested\n" +
                    "[1] Willing\n" +
                    "[0] Uninterested\n" +
                    "\n" +
                    "You can also define how a given interest is capitalised, so that it always looks good however you write it."
            );
        }
        else if(mainEntryArray[1].startsWith("e")) {}
        else viewCommandAbout();

        CommonMethods.returnToMainPrint();
    }//Method about

    public static void viewCommandAbout() {
        System.out.println("Command: ABOUT\n" +
                "This command shows information about a subject.\n" +
                "The /about command should be followed by a space and one of these words:\n" +
                "[*] theBook\n" +
                "[*] usage\n" +
                "[*] persons\n" +
                "[*] interests\n" +
                "[*] events\n" +
                "Example:\n" +
                "[*] /about theBook"
        );
    }//Method viewCommandAbout

    public static void viewCommand(String enteredCommand) {
        //I'll write this method last.
        System.out.println("Method not implemented: viewCommand(String enteredCommand)");
    }//Method viewCommand

    public static void listCommands() {
        System.out.println("Main menu commands:\n" +
                "[C] /make person|interest|event\n" +
                "[C] /edit person|event [opt: handle|index]\n" +
                "[C] /remove person|interest|event [opt: handle|interest name|index]\n" +
                "[C] /view person|interest|event|command [opt: handle|interest name|index|command]\n" +
                "[C] /wipe persons|interests|events\n" +
                "[C] /list persons|interests|events|commands\n" +
                "[C] /about theBook|usage|persons|interests|events\n" +
                "[C] /close"
        );
    }//Method listCommands

//----------------------------------------------------------------
}//Class InvitationBook