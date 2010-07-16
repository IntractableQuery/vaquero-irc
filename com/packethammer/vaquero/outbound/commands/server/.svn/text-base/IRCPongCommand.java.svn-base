/**
 * 
 */

package com.packethammer.vaquero.outbound.commands.server;

import com.packethammer.vaquero.outbound.commands.IRCCommand;
import com.packethammer.vaquero.util.protocol.IRCRawLine;

public class IRCPongCommand extends IRCCommand {
    private String payload;
    
    /**
     * Initializes this pong command with the reply payload.
     *
     * @param payload The data to reply with.
     */
    public IRCPongCommand(String payload) {
        this.setPayload(payload);
    }
    
    /**
     * Initializes this pong command without a reply payload.
     */
    public IRCPongCommand() {
        
    }
    
    /**
     * Returns the payload associated with this pong command.
     */
    public String getPayload() {
        return payload;
    }

    /**
     * Sets the payload to send with this PONG command.
     */
    public void setPayload(String payload) {
        this.payload = payload;
    }
    
    public IRCRawLine renderForIRC() {
        return IRCRawLine.buildRawLine(false, "PONG", this.getPayload());
    }
   
    public boolean isSendable() {
        return true;
    }

}
