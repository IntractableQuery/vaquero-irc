/*
 * Occurs when the server reports a fatal error of some sort. This should 
 * always be followed by the server closing the connection. It is typical
 * to receive an ERROR after getting killed, but it is not guaranteed.
 *
 * ARGS: ERROR <MESSAGE>
 */

package com.packethammer.vaquero.parser.events.server;

import java.util.Date;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;

public class IRCErrorEvent extends IRCEvent {
    public IRCErrorEvent() {
    }
    
    /**
     * Returns the error message provided by the server.
     */
    public String getErrorMessage() {
        return this.getSecondArgument();
    }
    
    public String toString() {
        return "[SERVER ERROR] " + this.getErrorMessage();
    }
}
