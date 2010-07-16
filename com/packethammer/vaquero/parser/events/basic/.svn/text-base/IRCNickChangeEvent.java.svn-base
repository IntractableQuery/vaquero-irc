/*
 * Occurs when a user changes their nickname from one nickname to another.
 * Their original nickname is contained within the source hostmask.
 *
 * ARGS: NICK <NEW NICKNAME>
 */

package com.packethammer.vaquero.parser.events.basic;

import java.util.Date;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.events.interfaces.IRCHostmaskSourcedEventI;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;

public class IRCNickChangeEvent extends IRCEvent implements IRCHostmaskSourcedEventI {
    public IRCNickChangeEvent() {
    }
    
    /**
     * Returns the new nickname of the user changing their nickname.
     *
     * @return The user's new nickname.
     */
    public String getNewNickname() {
        return this.getSecondArgument();
    }
    
    public String toString() {
        return this.getSource().getSimpleRepresentation() + " changes nickname to " + this.getNewNickname();
    }
}
