import java.util.ArrayList;
import java.util.Scanner;

public class InvitationBook {

    static PersonCollection thePersonCollection;
    static EventCollection theEventCollection;

    static Scanner userEntry;
    static String[] mainEntryArray;
    static ArrayList<String> allMainCommandsArray;

    public static void main(String[] args) {
        open();
        readAndWrite();
        close();
    }//Method main

//----------------------------------------------------------------
    public static void open() {
        System.out.println("----------------------------------------------------------------\n" +
                "Invitation Book opening\n\n" +
                "Add yourself and your friends, make events, and add people to their invitation lists!\n");

        userEntry = new Scanner(System.in);
        mainEntryArray = new String[3];
        allMainCommandsArray = new ArrayList<>(8);
        allMainCommandsArray.add("m"); allMainCommandsArray.add("e");
        allMainCommandsArray.add("r"); allMainCommandsArray.add("v");
        allMainCommandsArray.add("w"); allMainCommandsArray.add("l");
        allMainCommandsArray.add("a"); allMainCommandsArray.add("c");

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
        System.out.println("Command: MAKE\n" +
                "This command lets you add a new person or event. You can also add or manage an interest.\n" +
                "The /make command must be followed by a space and one of these options:\n" +
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
        System.out.println("Command: EDIT\n" +
                "This command lets you edit a person or event.\n" +
                "The /edit command must be followed by a space and one of these options:\n" +
                "[*] person\n" +
                "[*] event\n" +
                "The chosen option may be followed by a space and a value:\n" +
                "[*] For 'person': The handle name of an existing person.\n" +
                "[*] For 'event': The index of an existing event.\n" +
                "Examples:\n" +
                "[*] /edit person\n" +
                "[*] /edit person Zeus"
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
                "The /remove command must be followed by a space and one of these options:\n" +
                "[*] person\n" +
                "[*] interest\n" +
                "[*] event\n" +
                "The chosen option may be followed by a space and a value:\n" +
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
                "The /view command must be followed by a space and one of these options:\n" +
                "[*] person\n" +
                "[*] interest\n" +
                "[*] event\n" +
                "[*] command\n" +
                "The chosen word may be followed by a space and an entry:\n" +
                "[*] For 'person': The handle name of an existing person.\n" +
                "[*] For 'interest': The name of an existing interest.\n" +
                "[*] For 'event': The index of an existing event.\n" +
                "[*] For 'command': The name of an existing command, without a '/'.\n" +
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
                "The /wipe command must be followed by a space and one of these options:\n" +
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
                "The /list command must be followed by a space and one of these options:\n" +
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
                    "to the program. They can then manage which persons are invited to what events, and other aspects of\n" +
                    "the persons and events they have added. The program uses .txt files to store data when not running.\n" +
                    "\n" +
                    "[*] Version: 1.0\n" +
                    "[*] Date: 22024-10-03\n" +
                    "[*] Author: Even Lindell Heggø\n" +
                    "[*] Programming language: Java"
            );
        }
        else if(mainEntryArray[1].startsWith("u")) {
            System.out.println("Usage\n" +
                    "The user interacts with the program through the keyboard. Anything the user enters is either a\n" +
                    "value (information, data) or a command. The program will let you know whether it is ready to\n" +
                    "receive only a value, only a command, or either one.\n" +
                    "\n" +
                    "[*] Values: Never starts with a '/'. There are basic rules all values must follow, and some values\n" +
                    "    have additional requirements.\n" +
                    "[*] Commands: Always starts with a '/'. Some commands allow or require the user to enter options\n" +
                    "    and/or values with the command."
            );
        }
        else if(mainEntryArray[1].startsWith("v")) {
            System.out.println("Values\n" +
                    "Values are pieces of information you enter into the program, like someones first name, or how\n" +
                    "interested they are in football.\n" +
                    "\n" +
                    "All values must follow these rules:\n" +
                    "[*] They can't be exactly the words 'null' or 'cancel', in any capitalisation.\n" +
                    "[*] They can't begin with '/'.\n" +
                    "[*] They can't contain ';'.\n" +
                    "\n" +
                    "Some types of data have additional requirements. For example, if you are entering a month, it must\n" +
                    "be a whole number from 1 to 12. The program will guide you if you make a mistake, so there is no\n" +
                    "need to learn all the requirements by heart.");
        }
        else if(mainEntryArray[1].startsWith("c")) {
            System.out.println("Commands\n" +
                    "Commands tell the program what you want to do. Commands contain one or more term. The first term is\n" +
                    "always a command word, and each of the remaining terms will be either options or values.\n" +
                    "\n" +
                    "Command terms can be:\n" +
                    "[*] A command word: The command starts with a '/' and a command word.\n" +
                    "    Example: /close\n" +
                    "[*] An option: This is one word from a selection, with each possibility separated by a '|'.\n" +
                    "    Example: person|interest|event\n" +
                    "[*] A value: Some commands allow or require you to enter a value with the command. When a command\n" +
                    "    lets you enter a value, it is show as [] with a description inside.\n" +
                    "    Example: [first name]\n" +
                    "\n" +
                    "There may be words telling you something about how to use an option or a value. In particular, the\n" +
                    "term 'opt' means that entering the option or value in question is optional. Both command words and\n" +
                    "command options can be shortened to their initial letter. Values must always be written out.\n" +
                    "\n" +
                    "Example:\n" +
                    "[*] /view person|interest|event|command [opt: handle|interest name|index|command]\n" +
                    "    Example entry: /view person\n" +
                    "    Example entry: /view person AdaL\n" +
                    "    Example entry: /v p AdaL");
        }
        else if(mainEntryArray[1].startsWith("p")) {
            System.out.println("Persons\n" +
                    "Persons have names and interests.\n\n" +
                    "Names\n" +
                    "[*] Handle\n" +
                    "[*] First\n" +
                    "[*] Middle\n" +
                    "[*] Last\n\n" +
                    "Interests:\n" +
                    "[*] Interest name & interest level\n" +
                    "[*] Interest name & interest level\n" +
                    "\n" +
                    "All persons must have a handle name. This is the name you use when you want to manage an existing\n" +
                    "person. All other values are optional.\n" +
                    "\n" +
                    "A person may have any number of first names and middle names (separated by spaces when entered),\n" +
                    "but at most one last name. Interests have an interest name (which may contain spaces), as well as\n" +
                    "an interest level from 0 to 3.\n" +
                    "\n" +
                    "Interest levels:\n" +
                    "[3] Highly interested\n" +
                    "[2] Interested\n" +
                    "[1] Willing\n" +
                    "[0] Uninterested\n" +
                    "\n" +
                    "Example person!\n" +
                    "Person: SigvaldF\n" +
                    "Names\n" +
                    "[*] Handle: SigvaldF\n" +
                    "[*] First: Sigvald\n" +
                    "[*] Middle:\n" +
                    "[*] Last: Fjellstuen\n\n" +
                    "Interests (5)\n" +
                    "Highly interested:\n" +
                    "[3] Animal husbandry\n" +
                    "[3] Swing dancing\n" +
                    "Interested:\n" +
                    "[2] Mathematics\n" +
                    "Willing:\n" +
                    "[1] Cooking\n\n" +
                    "Uninterested:\n" +
                    "[0] Travel");
        }
        else if(mainEntryArray[1].startsWith("i")) {
            System.out.println("Interests\n" +
                    "Persons can have interests, where each interest is associated with an interest level from 0 to 3.\n" +
                    "\n" +
                    "Interest levels:\n" +
                    "[3] Highly interested\n" +
                    "[2] Interested\n" +
                    "[1] Willing\n" +
                    "[0] Uninterested\n" +
                    "\n" +
                    "It is also possible to define a capitalisation for a given interest. For example, we can define\n" +
                    "that 'west coast swing' should always be shown as \"West Coast Swing'.");
        }
        else if(mainEntryArray[1].startsWith("e")) {
            System.out.println("Events\n" +
                    "\n" +
                    "[*] Name\n" +
                    "[*] Type\n" +
                    "[*] Description\n" +
                    "[*] Starting year\n" +
                    "[*] Starting month\n" +
                    "[*] Starting day\n" +
                    "[*] Starting hour\n" +
                    "[*] Starting minute\n" +
                    "[*] Index\n" +
                    "[*] Invitations:\n" +
                    "    [*] Person - invitationState\n" +
                    "    [*] Person - invitationState\n" +
                    "\n" +
                    "All events must have a name. They also always have a number (index) in the event list. This number\n" +
                    "is set by the program, and is the way you select which event you wish to interact with. All other\n" +
                    "values are optional.\n" +
                    "\n" +
                    "Name, type and description require you to enter text. The starting times are all numbers.\n" +
                    "Invitations consist of the handle name of a person and an invitation state.\n" +
                    "\n" +
                    "Invitation states:\n" +
                    "[*] Attending\n" +
                    "[*] Pending\n" +
                    "[*] Declined\n" +
                    "\n" +
                    "Example event:\n" +
                    "[*] Name: Constitution day\n" +
                    "[*] Type: National day\n" +
                    "[*] Description: Celebrating constitution day all day!\n" +
                    "[*] Starting year: 2025\n" +
                    "[*] Starting month: 05\n" +
                    "[*] Starting day: 17\n" +
                    "[*] Starting hour: 05\n" +
                    "[*] Starting minute: 30\n" +
                    "[*] Index: 17\n" +
                    "[*] Invitations:\n" +
                    "    [*] LarsK - Attending\n" +
                    "    [*] Sigurd - Attending\n" +
                    "    [*] BigJens - Pending");
        }
        else viewCommandAbout();

        CommonMethods.returnToMainPrint();
    }//Method about

    public static void viewCommandAbout() {
        System.out.println("Command: ABOUT\n" +
                "This command shows information about a subject.\n" +
                "The /about command must be followed by a space and one of these options:\n" +
                "[*] theBook\n" +
                "[*] usage\n" +
                "[*] values\n" +
                "[*] commands\n" +
                "[*] persons\n" +
                "[*] interests\n" +
                "[*] events\n" +
                "Example:\n" +
                "[*] /about theBook"
        );
    }//Method viewCommandAbout

    public static void viewCommandClose() {
        System.out.println("Command: CLOSE\n" +
                "The /close command stores all current information about persons, interests and events. Then it closes\n" +
                "the program.");
    }//Method viewCommandClose

    public static void viewCommand(String enteredCommand) {
        String existingCommandWordLowerMinimal = userFindsExistingCommand(enteredCommand);

        if(existingCommandWordLowerMinimal == null) return;

        if(existingCommandWordLowerMinimal.startsWith("m")) viewCommandMake();
        else if(existingCommandWordLowerMinimal.startsWith("e")) viewCommandEdit();
        else if(existingCommandWordLowerMinimal.startsWith("r")) viewCommandRemove();
        else if(existingCommandWordLowerMinimal.startsWith("v")) viewCommandView();
        else if(existingCommandWordLowerMinimal.startsWith("w")) viewCommandWipe();
        else if(existingCommandWordLowerMinimal.startsWith("l")) viewCommandList();
        else if(existingCommandWordLowerMinimal.startsWith("a")) viewCommandAbout();
        else if(existingCommandWordLowerMinimal.startsWith("c")) viewCommandClose();
    }//Method viewCommand

    public static void listCommands() {
        System.out.println("Main menu commands:\n" +
                "[C] /make person|interest|event\n" +
                "[C] /edit person|event [opt: handle|index]\n" +
                "[C] /remove person|interest|event [opt: handle|interest name|index]\n" +
                "[C] /view person|interest|event|command [opt: handle|interest name|index|command]\n" +
                "[C] /wipe persons|interests|events\n" +
                "[C] /list persons|interests|events|commands\n" +
                "[C] /about theBook|usage|values|commands|persons|interests|events\n" +
                "[C] /close"
        );
    }//Method listCommands

    public static String userFindsExistingCommand(String c) {
        String cLower;

        if(c != null && !(c.isEmpty())) {
            cLower = c.toLowerCase().substring(0, 1);

            if(allMainCommandsArray.contains(cLower)) return cLower;
        }

        System.out.println("Enter existing command word without a '/' (/list to see all interests, /cancel to return to main menu)");
        do {
            System.out.print("- Enter command word: ");
            cLower = userEntry.nextLine().strip().toLowerCase();

            if(cLower.startsWith("/l")) listCommands();
            else if(cLower.startsWith("/c")) return null;
            else if(cLower.isEmpty()
            || !allMainCommandsArray.contains(cLower.substring(0, 1))) {
                System.out.println("Non-existing, invalid or empty command word.");
            }
        }
        while(!allMainCommandsArray.contains(cLower.substring(0, 1)));

        return cLower.substring(0, 1);
    }//Method userFindsExistingCommand

//----------------------------------------------------------------
}//Class InvitationBook