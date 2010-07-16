/*
 * This requests the current time from a server. Please do not confuse it with
 * the CTCP TIME command, primarily used amongst IRC clients. You can target
 * a specific server on the IRC network using this command, or omit the target
 * to get the time of the local server you are on.
 *
 * RFC2812 states that the server parameter may have wildcards in it, but 
 * RFC1459 does not state this.
 *
 * TIME [SERVER]
 */

package com.packethammer.vaquero.outbound.commands.server;

import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.outbound.commands.IRCCommand;

public class IRCTimeCommand extends IRCCommand {
    private String targetServer;
    
    /**
     * Instantiates this TIME command with a target server, which may be in the
     * form of a regular mask if the server supports RFC2812. The server name
     * provided will be queried for its current time.
     */
    public IRCTimeCommand(String targetServer) {
        this();
        this.setTargetServer(targetServer);
    }
    
    /**
     * Instantiates this TIME command without a target server, indicating we 
     * just want the time of the server we are currently connected to.
     */
    public IRCTimeCommand() {
    }
    
    /**
     * Returns the name of the server (or a mask, if we are going by RFC2812's
     * definition) that we are trying to request the time of. This will be null
     * if we aren't specifying a specific server (this implies we want the
     * time of the server we are currently connected to).
     */
    public String getTargetServer() {
        return targetServer;
    }
    
    /**
     * @see #getTargetServer()
     */
    public void setTargetServer(String targetServer) {
        this.targetServer = targetServer;
    }
    
    public IRCRawLine renderForIRC() {
        return IRCRawLine.buildRawLine(false, "TIME", this.getTargetServer());
    }    
   
    public boolean isSendable() {
        return true;
    }
}
