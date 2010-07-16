/*
 * Occurs when the IRC server pings us, most generally to determine if we are
 * still alive. The ping message typically contains a message payload that
 * we are to respond with when we PONG the server. It is not quite clear if
 * it is possible to receive a PING without a payload, but in the event that it
 * is, the message should simply be null.
 *
 * Do not confuse this with a client-to-client CTCP ping.
 *
 * ARGS: PING [PAYLOAD]
 */

package com.packethammer.vaquero.parser.events.server;

import java.util.Date;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.events.interfaces.IRCExtendedMessageEventI;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;

public class IRCPingEvent extends IRCEvent implements IRCExtendedMessageEventI {     
    public IRCPingEvent() {
    }
    
    /**
     * Returns the ping message payload, or null if none was given.
     *
     * @return The ping payload or null if none present.
     */
    public String getMessage() {
        return this.getSecondArgument();
    }

    /**
     * Determines if this PING has a payload message.
     *
     * @return True if this PING has a payload, false otherwise
     */
    public boolean hasPayloadMessage() {
        return this.getMessage() != null;
    }
    
    public String toString() {
        return "[PING from " + this.getSource().getSimpleRepresentation() + "] " + this.getMessage();
    }
}
