/**
 * This defines a class that allows one to send/receive raw lines from an IRC
 * server, and listen for events related to the data transfer starting
 * ("connecting") or ending ("closing").
 */

package com.packethammer.vaquero.net;

import java.util.List;
import java.util.Vector;

public abstract class IRCConnector {
    private IRCLineListener lineListener;
    private List<IRCConnectorListener> listeners;
    
    /** Creates a new instance of IRCConnector */
    public IRCConnector() {
        listeners = new Vector();
    }
    
    /**
     * When this method is called, it is assumed that whatever must be 
     * intialized is initialized, and we can begin sending/receiving data.
     */
    public abstract void begin();
    
    /**
     * This will prematurely terminate the 'connection' that facilitates 
     * transport of the IRC data.
     */
    public abstract void close();
    
    /**
     * This will write data to the remote server, supressing any exceptions
     * encountered in attempting to send the data.
     *
     * @param line The raw line of IRC data to send.
     */
    public abstract void sendLine(String line);

    /**
     * Returns the current line listener associated with this IRC connector.
     *
     * @return The line listener.
     */
    public IRCLineListener getLineListener() {
        return lineListener;
    }

    /**
     * Sets the line listener to use for this IRC connector.
     */
    public void setLineListener(IRCLineListener lineListener) {
        this.lineListener = lineListener;
    }

    /**
     * Returns the current listeners for this IRC connector.
     */
    public List<IRCConnectorListener> getListeners() {
        return listeners;
    }
    
    /**
     * Adds a listener.
     *
     * @param listener The IRC connector listener to add.
     */
    public void addListener(IRCConnectorListener listener) {
        this.listeners.add(listener);
    }
    
    /**
     * Removes a listener (including multiple equivalent listeners if they exist).
     *
     * @param listener The IRC connector listener to remove.
     */
    public void removeListener(IRCConnectorListener listener) {
        while(this.listeners.remove(listener));
    }
}
