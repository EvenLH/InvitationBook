public interface CommonValidations {

    default boolean stringIsInt(String s) {
        try {
            Integer.parseInt(s);
        }
        catch(NumberFormatException nfe) {
            return false;
        }

        return true;
    }//Method stringIsInt

    //Used to validate entries for
    //year, month, day, hour, minute, as well as interest level.
    default boolean isValidIntInRange(String s, int min, int max) {
        if(!stringIsInt(s)) return false;

        int value = Integer.parseInt(s);
        if(value < min || max < value) return false;

        return true;
    }//Method isValidNumberValue

    //Used to validate entries for
    //first, middle and last name, as well as event type and description.
    default boolean isValidString(String s) {
        if(s == null
        || s.equalsIgnoreCase("null")
        || s.startsWith("/")
        || s.contains(";")) {
            return false;
        }

        return true;
    }//Method isValidString

    //Used to validate entries for
    //interest name, as well as event name.
    default boolean isValidStringWithLength(String s) {
        if(!isValidString(s)
        || s.isEmpty()) {
            return false;
        }

        return true;
    }//Method isValidStringWithLength

}//Interface CommonValidations