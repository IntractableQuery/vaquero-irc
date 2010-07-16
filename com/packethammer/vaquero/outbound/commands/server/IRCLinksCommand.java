/*
 * This queries a server (or the immediate server) for a list of servers linked
 * to it. Using it without parameters typically results in a list of all servers
 * linked to the server you are on.
 *
 * LINKS [<SERVER MASK> [FROM SERVER MASK]]
 */

package com.packethammer.vaquero.outbound.commands.server;

import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.outbound.commands.IRCCommand;

public class IRCLinksCommand extends IRCCommand {
    private String serversMatchingMask;
    private String fromServerMatchingMask;
    
    /**
     * Initializes this links command with a server mask to use for the LINKS
     * reply and a from-server mask which will be used to find the first 
     * available server matching the from-server mask and request the LINKS
     * reply from it.
     * 
     * @param serversMatchingMask The mask to use for matching servers for the LINKS reply.
     * @param fromServerMatchingMask The mask to use for locating a server to send the LINKS request to. May be null if none is desired.
     */
    public IRCLinksCommand(String serversMatchingMask, String fromServerMatchingMask) {
        this();
        this.serversMatchingMask = serversMatchingMask;
        this.fromServerMatchingMask = fromServerMatchingMask;
    }
    
    /**
     * Initializes this links command with a server mask to use for the LINKS
     * reply.
     *
     * @param serversMatchingMask The mask to use for matching servers for the LINKS reply.
     */
    public IRCLinksCommand(String serversMatchingMask) {
        this(serversMatchingMask, null);
    }
    
    /**
     * Initializes this LINKS command without parameters, which normally will
     * result in a reply containing all found linked servers relative to the
     * server you are currently connected to.
     */
    public IRCLinksCommand() {
    }

    /**
     * Returns the mask for the servers we are trying to get links information
     * for. Returns null if we are not trying to query for a particular server
     * name (this implies we want all).
     */
    public String getServersMatchingMask() {
        return serversMatchingMask;
    }

    /**
     * Sets the mask to be used for determining which servers to include in the 
     * LINKS reply. May be set to null if we just want all of the servers. However,
     * if we are using a from-server matching mask, this CANNOT be null.
     */
    public void setServersMatchingMask(String serversMatchingMask) {
        this.serversMatchingMask = serversMatchingMask;
    }

    /**
     * This mask is used to find the first matching server, and then request
     * the LINKS as seen by it.
     */
    public String getFromServerMatchingMask() {
        return fromServerMatchingMask;
    }

    /**
     * Sets the server mask that is used to find the first matching server, then
     * request the LINKS from it.
     */
    public void setFromServerMatchingMask(String fromServerMatchingMask) {
        this.fromServerMatchingMask = fromServerMatchingMask;
    }
    
    public IRCRawLine renderForIRC() {
        return IRCRawLine.buildRawLine(false, "LINKS", this.getServersMatchingMask(), this.getFromServerMatchingMask());
    }
   
    public boolean isSendable() {
        if(this.getFromServerMatchingMask() != null)
            return this.getServersMatchingMask() != null;
        else
            return true;
    }
}
