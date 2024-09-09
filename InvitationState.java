public enum InvitationState {
    Attending,
    Pending,
    Declined;

    public static InvitationState stringToEnum(String s) {
        if(s == null
        || s.isEmpty()) return Pending;

        for(InvitationState e: InvitationState.values()) {

            if(s.substring(0, 1).equalsIgnoreCase(e.toString().substring(0, 1))) {
                return e;
            }
        }

        return null;
    }

}//Enum InvitationState