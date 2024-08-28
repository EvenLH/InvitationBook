import java.util.ArrayList;
import java.util.Set;

public abstract class CommonMethods {

    public static String removeExtraSpaces(String s) {
        if(s == null) return null;

        s = s.strip();
        while(s.contains("  ")) {
            s = s.replace("  ", " ");
        }

        return s;
    }//Method removeExtraSpaces

    public static String[] commandStringToArray(String s, int maxSize) {
        if(s == null || s.isBlank()) return new String[0];

        String[] initialArray = removeExtraSpaces(s).toLowerCase().split(" ");
        if(initialArray.length <= maxSize) return initialArray;

        String[] correctedArray = new String[maxSize];
        System.arraycopy(initialArray, 0, correctedArray, 0, maxSize);
        for(int i = maxSize; i < initialArray.length; i++)
            correctedArray[maxSize -1] = correctedArray[maxSize -1].concat(" " + initialArray[i]);

        return correctedArray;
    }//Method removeExtraSpacesFromString

    public static ArrayList<String> stringSetToOrderedArrayList(Set<String> stringSet) {
        ArrayList<String> orderedStrings = new ArrayList<>(stringSet.size());

        for(String currentString: stringSet) {
            if(orderedStrings.isEmpty()) orderedStrings.add(currentString);
            else if(currentString.compareTo(orderedStrings.get(orderedStrings.size() -1)) >= 0) orderedStrings.add(currentString);
            else {
                for(int i = 0; i < orderedStrings.size(); i++) {
                    if(currentString.compareTo(orderedStrings.get(i)) < 0) {
                        orderedStrings.add(i, currentString);
                        break;
                    }
                }//Loop for (inner)
            }
        }//Loop for (outer)

        return orderedStrings;
    }//Method stringSetToOrderedArrayList

    public static boolean stringIsInt(String s) {
        try {
            Integer.parseInt(s);
        }
        catch(NumberFormatException nfe) {
            return false;
        }

        return true;
    }//Method stringIsInt

    public static boolean stringIsIntInRange(String s, int min, int max) {
        if(!stringIsInt(s)) return false;

        int number = Integer.parseInt(s);
        return min <= number && number <= max;
    }//Method stringIsIntInRange

    public static boolean stringIsSafe(String s) {
        if(s == null
        || s.equalsIgnoreCase("null")
        || s.startsWith("/")
        || s.contains(";"))
            return false;

        return true;
    }//Method stringIsSafe

    public static boolean stringIsSafeWithLength(String s) {
        return stringIsSafe(s) && !s.isEmpty();
    }//Method stringIsSafeWithLength

    public static boolean stringIsSafeNoSpace(String s) {
        return stringIsSafe(s) && !s.contains(" ");
    }//Method stringIsSafeNoSpace

    public static boolean keyExistsIgnoreCase(String s, Set<String> keys) {
        if(s == null) return false;

        String candidateKey = s.toLowerCase();
        for(String k: keys) {
            if(k.equals(candidateKey)) return true;
        }

        return false;
    }//Method keyExistsIgnoreCase

    public static boolean stringIsValidNewHandleName(String s, Set<String> keys) {
        return stringIsSafeWithLength(s) && !s.contains(" ") && !keyExistsIgnoreCase(s, keys);
    }//Method stringIsValidNewHandleName

    public static void returnToMainPrint() {
        System.out.print("\nReturned to main menu.");
    }//Method returnToMainPrint

}//Class CommonMethods