/**
 * Represents a reply from the ident server.
 */

package com.packethammer.vaquero.identd;

/**
 *
 * @author iron
 */
public abstract class AbstractIdentReply {
    private static final String CRLF = "\015\012";
        
    private int ourPort;
    private int theirPort;
    
    /**
     * Initializes the ident reply with the port on us and the port on them that
     * is being checked.
     *
     * @param ourPort The port on us being checked.
     * @param theirPort The port on them being checked.
     */
    public AbstractIdentReply(int ourPort, int theirPort) {
        this.ourPort = ourPort;
        this.theirPort = theirPort;
    }
    
    /**
     * Returns a raw reply, including the end-of-line sequence.
     */
    public String getReply() {
        return this.ourPort + " : " + this.theirPort + " : " + this.getReponseType() + " : " + this.getAdditionalInfo() + CRLF;
    }
    
    /**
     * Returns the response type (USERID/ERROR, typically).
     */
    public abstract String getReponseType();
    
    /**
     * Returns the additional information related to this response.
     */
    public abstract String getAdditionalInfo();

    /**
     * Returns the port on us being checked.
     */
    public int getOurPort() {
        return ourPort;
    }

    /**
     * Returns the port on them being checked.
     */
    public int getTheirPort() {
        return theirPort;
    }
}
