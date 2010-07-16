/*
 * Occurs when a user quits IRC. This quit may be accompanied by a message.
 * RFC1459 suggests that there will always be a message, but one can never
 * be certain.
 *
 * ARGS: QUIT [MESSAGE]
 */

package com.packethammer.vaquero.parser.events.basic;

import java.util.Date;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.events.interfaces.IRCHostmaskSourcedEventI;
import com.packethammer.vaquero.parser.events.interfaces.IRCExtendedMessageEventI;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;

public class IRCQuitEvent extends IRCEvent implements IRCHostmaskSourcedEventI,IRCExtendedMessageEventI {
    public IRCQuitEvent() {
    }

    /**
     * Returns the quit message, or null if none was given.
     *
     * @return The message or null if none present.
     */
    public String getMessage() {
        return this.getSecondArgument();
    }
    
    /**
     * Determines if there was a quit message present or not.
     *
     * @return True if there was a quit message, false otherwise.
     */
    public boolean hasQuitMessage() {
        return this.getMessage() != null;
    }
    
    public String toString() {
        return this.getSource().getSimpleRepresentation() + " quits (" + this.getMessage() + ")";
    }
}
