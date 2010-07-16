/*
 * Defines a channel KICK.
 *
 * ARGS: KICK <CHANNEL> <TARGET NICKNAME> [OPTIONAL REASON]
 */

package com.packethammer.vaquero.parser.events.channel;

import java.util.Date;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.interfaces.IRCChannelTargetedEventI;
import com.packethammer.vaquero.parser.events.interfaces.IRCExtendedMessageEventI;
import com.packethammer.vaquero.parser.events.interfaces.IRCHostmaskSourcedEventI;
import com.packethammer.vaquero.parser.events.interfaces.IRCNicknameTargetedEventI;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;

public class IRCKickEvent extends IRCEvent implements IRCChannelTargetedEventI,IRCHostmaskSourcedEventI,IRCExtendedMessageEventI,IRCNicknameTargetedEventI {   

    public IRCKickEvent() {
    }
    
    /**
     * Returns the channel that this event is occuring in.
     * 
     * @return The channel.
     */
    public String getChannel() {
        return this.getSecondArgument();
    }
    
    /**
     * Returns the kick reason, or null if none was present.
     *
     * @return The reason or null.
     */
    public String getMessage() {
        return this.getArg(3);
    }
    
    /**
     * Determines if this kick had a message that accompanied it.
     * Some servers try to force a kick message no matter what, but don't
     * become reliant on that. RFC-2812 makes an example of a KICK that has
     * no message.
     */
    public boolean hasKickMessage() {
        return this.getMessage() != null;
    }
    
    /**
     * Returns the nickname of the person being kicked.
     *
     * @return Nickname of person being kicked.
     */
    public String getTarget() {
        return this.getArg(2);
    }
    
    public String toString() {
        return "[" + this.getChannel() + "] " + this.getSource().getNickname() + " kicks " + this.getTarget() + " (" + this.getMessage() + ")";
    }
}