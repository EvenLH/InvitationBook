public interface CommonValidations {

    //Used to validate entries for
    //year, month, day, hour, minute, as well as interest level.
    default boolean isValidNumberValue(String s, int min, int max) {
        if(s == null || s.isEmpty()) return false;

        for(char c: s.toCharArray()) {
            if(!Character.isDigit(c)) return false;
        }

        int value = Integer.parseInt(s);
        if(value < min || max < value) return false;

        return true;
    }//Method isValidNumberValue

    //Used to validate entries for
    //first, middle and last name, as well as event type and description.
    default boolean isValidString(String s) {
        if(s == null || s.equalsIgnoreCase("null")) {
            return false;
        }

        return true;
    }//Method isValidString

    //Used to validate entries for
    //interest name, as well as event name.
    default boolean isValidStringWithLength(String s) {
        if(s == null || s.equalsIgnoreCase("null") || s.isEmpty()) {
            return false;
        }

        return true;
    }//Method isValidStringWithLength

}//Interface CommonValidations