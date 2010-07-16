/*
 * Occurs when the server responds to a PING we send. It is typically accompanied
 * by a payload response. 
 *
 * Note: This may not be a correct representation of a PONG. The RFCs are not
 * too clear when it comes to a client receiving a PONG as opposed to a linked
 * server communicating with another server. 
 *
 * ARGS: PONG [REPLY PAYLOAD]
 */

package com.packethammer.vaquero.parser.events.server;

import java.util.Date;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.interfaces.IRCExtendedMessageEventI;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;

public class IRCPongEvent extends IRCEvent implements IRCExtendedMessageEventI {     
    public IRCPongEvent() {
    }    
    
    /**
     * Returns the pong message payload reply, or null if none was given.
     *
     * @return The pong payload or null if none present.
     */
    public String getMessage() {
        return this.getSecondArgument();
    }

    /**
     * Determines if this PONG has a payload message.
     *
     * @return True if this PING has a payload, false otherwise
     */
    public boolean hasPayloadMessage() {
        return this.getMessage() != null;
    }
    
    public String toString() {
        return "[PONG from " + this.getSource().getSimpleRepresentation() + "] " + this.getMessage();
    }
}
