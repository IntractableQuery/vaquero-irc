/*
 * Occurs when a user leaves a channel.
 *
 * ARGS: PART <CHANNEL> [PARTING MESSAGE...]
 */


package com.packethammer.vaquero.parser.events.channel;

import java.util.Date;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.events.interfaces.IRCChannelTargetedEventI;
import com.packethammer.vaquero.parser.events.interfaces.IRCHostmaskSourcedEventI;
import com.packethammer.vaquero.parser.events.interfaces.IRCExtendedMessageEventI;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;

public class IRCPartEvent extends IRCEvent implements IRCChannelTargetedEventI,IRCHostmaskSourcedEventI,IRCExtendedMessageEventI {
    public IRCPartEvent() {        
    }    
    
    /**
     * Gets the channel where the event takes place.
     *
     * @return The channel.
     */
    public String getChannel() {
        return this.getSecondArgument();
    }

    /**
     * Returns the parting message, or null if none was present.
     *
     * @return The message or null.
     */
    public String getMessage() {
        return this.getArg(2);
    }
    
    /**
     * Determines if there was a parting message present or not.
     *
     * @return True if there was a parting message, false otherwise.
     */
    public boolean hasPartingMessage() {
        return this.getMessage() != null;
    }
    
    public String toString() {
        return "[" + this.getChannel() + "] " + this.getSource().getSimpleRepresentation() + " parts (" + this.getMessage() + ")";
    }
}
