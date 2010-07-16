/*
 * Occurs when a user joins a channel.
 *
 * ARGS: JOIN <CHANNEL>
 */

package com.packethammer.vaquero.parser.events.channel;

import java.util.Date;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.events.interfaces.IRCChannelTargetedEventI;
import com.packethammer.vaquero.parser.events.interfaces.IRCHostmaskSourcedEventI;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;

public class IRCJoinEvent extends IRCEvent implements IRCChannelTargetedEventI,IRCHostmaskSourcedEventI {
    public IRCJoinEvent() {        
    }

    /**
     * Gets the channel where the event takes place.
     *
     * @return The channel.
     */
    public String getChannel() {
        return this.getSecondArgument();
    }
    
    public String toString() {
        return "[" + this.getChannel() + "] " + this.getSource().getSimpleRepresentation() + " joins";
    }
}
