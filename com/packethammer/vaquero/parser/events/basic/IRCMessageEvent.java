/*
 * Holds information directly related to PRIVMSG, except for the target
 * of the message, as this can be either a channel message or a true private
 * message to just us. Those two cases are handled by sub-events.
 *
 * ARGS: PRIVMSG <TARGET> <MESSAGE>
 */

package com.packethammer.vaquero.parser.events.basic;

import java.util.Date;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.events.interfaces.IRCHostmaskSourcedEventI;
import com.packethammer.vaquero.parser.events.interfaces.IRCExtendedMessageEventI;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.interfaces.IRCUserOrChannelTargetedEventI;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;

public abstract class IRCMessageEvent extends IRCEvent implements IRCHostmaskSourcedEventI,IRCExtendedMessageEventI,IRCUserOrChannelTargetedEventI {
    private boolean ctcp;
    private String target;
    
    public IRCMessageEvent() {        
    }

    /**
     * Determines if this message contains a client-to-client protocol
     * message.
     *
     * @return True if CTCP, false otherwise.
     */
    public boolean isCTCP() {
        return ctcp;
    }

    /**
     * Sets if this is a client-to-client protocol message or not.
     *
     * @param ctcp True if this is a CTCP, false otherwise.
     */
    public void setCTCP(boolean ctcp) {
        this.ctcp = ctcp;
    }
    
    /**
     * Returns the raw channel message (if it is a CTCP, CTCP formatting
     * is left intact).
     *
     * @return The channel message.
     */
    public String getMessage() {
        return this.getArg(2);
    }
    
    /**
     * Gets the target (channel/nickname) of this message.
     *
     * @return Target of the message.
     */
    public String getTarget() {
        return this.getSecondArgument();
    }    
    
    public String toString() {
        return "<" + this.getSource().getNickname() + "> " + this.getMessage();  
    }
}
