public enum InvitationStatus {
    Accepted,
    Invited,
    Declined;

    public static boolean isValidInvStatusIgnoreCase(String s) {
        for(InvitationStatus invStatus: values()) {
            if(invStatus.toString().equalsIgnoreCase(s)) {
                return true;
            }
        }

        return false;
    }//Method isValidInvStatusIgnoreCase

    public static InvitationStatus getInvStatusEnum(String s) {
        for(InvitationStatus invStatus: values()) {
            if(invStatus.toString().equalsIgnoreCase(s)) {
                return invStatus;
            }
        }

        return null;
    }//Method getInvStatusStringCorrectCase

}//Enum InvitationStatus