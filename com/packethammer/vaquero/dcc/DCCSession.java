/**
 * This defines a class that is a DCC session of some type with a remote
 * host.
 */

package com.packethammer.vaquero.dcc;

import java.net.Socket;

public abstract class DCCSession {
    /** Corresponds with a DCC CHAT request, where we are the one initiating the chat */
    public static final int TYPE_CHATSEND = 0;
    /** Corresponds with a DCC CHAT request, where we are the one receiving the chat */
    public static final int TYPE_CHATRECEIVE = 1;
    /** Corresponds with a DCC SEND request, where we are the one sending the file */
    public static final int TYPE_FILESEND = 2;
    /** Corresponds with a DCC SEND request, where we are the one receiving the file */
    public static final int TYPE_FILERECEIVE = 3;
    
    private int type;
    private Socket socket;
    private boolean finished;
    
    /**
     * Initializes this DCC session with the type of session it is and the 
     * socket to use for the session.
     *
     * @param socket The socket to use.
     * @param type The type of this DCC session (see TYPE_* constants).
     */
    public DCCSession(Socket socket, int type) {
        this.socket = socket;
        this.type = type;
    }

    /**
     * Returns the type of this DCC session (see TYPE_* constants).
     */
    public int getType() {
        return type;
    }

    /**
     * Determines if we initiated this connection, or if a remote host did.
     */
    public boolean isInitiatedByUs() {
        return this.getType() == TYPE_FILESEND || this.getType() == TYPE_CHATSEND;
    }
    
    /**
     * Returns the socket that is being used for this DCC chat session.
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Determines if this DCC session is finished.
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Set to true if this DCC session is finished.
     */
    public void setFinished(boolean finished) {
        this.finished = finished;
    }
    
    public String toString() {
        return "TYPE:" + this.getType() + ", FROM-US:" + this.isInitiatedByUs() + ", LOCALPORT:" + this.getSocket().getLocalPort() + " REMOTE:" + this.getSocket().getRemoteSocketAddress();
    }
}
