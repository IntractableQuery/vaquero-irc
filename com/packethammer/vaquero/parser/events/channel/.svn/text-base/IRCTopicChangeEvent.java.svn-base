/*
 * Occurs when someone changes the topic of a channel.
 *
 * ARGS: TOPIC <CHANNEL> <TOPIC...>
 */

package com.packethammer.vaquero.parser.events.channel;

import java.util.Date;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.events.interfaces.IRCHostmaskSourcedEventI;
import com.packethammer.vaquero.parser.events.interfaces.IRCExtendedMessageEventI;
import com.packethammer.vaquero.parser.events.interfaces.IRCChannelTargetedEventI;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;

public class IRCTopicChangeEvent extends IRCEvent implements IRCHostmaskSourcedEventI,IRCExtendedMessageEventI,IRCChannelTargetedEventI { 
    public IRCTopicChangeEvent() {
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
     * Returns the new topic.
     *
     * @return The new topic.
     */
    public String getMessage() {
        return this.getArg(2);
    }
    
    /**
     * Convenience method to get the new topic. Same as getMessage().
     *
     * @return The topic.
     */
    public String getTopic() {
        return this.getMessage();
    }
    
    public String toString() {
        return "[" + this.getChannel() + "] " + this.getSource().getSimpleRepresentation() + " changes topic to '" + this.getTopic() + "'";
    }
}
