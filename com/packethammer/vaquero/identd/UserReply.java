/**
 * This is a reponse to an ident query, where it was successful and we are
 * returning the user on the system.
 *
 * This assumes we are only dealing with US-ASCII usernames.
 */

package com.packethammer.vaquero.identd;

public class UserReply extends AbstractIdentReply {
    private String operatingSystem;
    private String user;
    
    /**
     * Initializes the ident reply with the port on us, the port on them that
     * is being checked, and the user name. The operating system is set to UNIX.
     *
     * @param ourPort The port on us being checked.
     * @param theirPort The port on them being checked.
     * @param user The username to reply with.
     */
    public UserReply(int ourPort, int theirPort, String user) {
       this(ourPort, theirPort, user, null);
    }
    
    /**
     * Initializes the ident reply with the port on us, the port on them that
     * is being checked, the username, and operating system to use.
     *
     * @param ourPort The port on us being checked.
     * @param theirPort The port on them being checked.
     * @param user The username to reply with.
     * @param operatingSystem The operation system to reply with. Set to null to default to UNIX.
     */
    public UserReply(int ourPort, int theirPort, String user, String operatingSystem) {
        super(ourPort, theirPort);
        this.user = user;
        if(operatingSystem == null)
            this.operatingSystem = "UNIX";
        else
            this.operatingSystem = operatingSystem;
    }
    
    public String getReponseType() {
        return "ERROR";
    }
    
    public String getAdditionalInfo() {
        return this.operatingSystem + " : " + this.user;
    }
}
