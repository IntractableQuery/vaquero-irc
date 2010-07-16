/*
 * IRCNoticeEvent.java
 *
 * Represents a basic IRC NOTICE, which is very similar to a PRIVMSG, but
 * is designed protocol-wise to be safe for bots and such, since it will
 * never generate a reply from the server, although that is more relevant to 
 * actually sending one. The main difference between this and a PRIVMSG 
 * parsing-wise is that a notice can target ops, voices, etc. in a channel.
 * However, as with most things in the RFCs strewn around, servers aren't
 * required or guaranteed to let you even use this feature, or even 
 * show the target of the message as "@#chan" instead of "#chan". In either case,
 * this class does not attempt to store that information, it just 
 * leaves the target intact. It is the job of subclasses, such as an 
 * IRCChannelNoticeEvent to contain that information.
 *
 * ARGS: NOTICE [TARGET SPECIFIER PREFIX]<TARGET> <MESSAGE>
 */

package com.packethammer.vaquero.parser.events.basic;

import java.util.Date;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.events.interfaces.IRCHostmaskSourcedEventI;
import com.packethammer.vaquero.parser.events.interfaces.IRCExtendedMessageEventI;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.interfaces.IRCUserOrChannelTargetedEventI;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;

public abstract class IRCNoticeEvent extends IRCEvent implements IRCHostmaskSourcedEventI,IRCExtendedMessageEventI,IRCUserOrChannelTargetedEventI {    
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
        return "-" + this.getSource().getSimpleRepresentation() + "- " + this.getMessage();
    }
}
