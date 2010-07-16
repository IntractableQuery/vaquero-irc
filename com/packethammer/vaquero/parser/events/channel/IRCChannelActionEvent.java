/*
 * Represents a channel CTCP ACTION, used in most IRC clients as /me.
 */

package com.packethammer.vaquero.parser.events.channel;

import java.util.Date;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.interfaces.IRCChannelTargetedEventI;
import com.packethammer.vaquero.parser.events.basic.IRCActionEvent;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;

public class IRCChannelActionEvent extends IRCActionEvent implements IRCChannelTargetedEventI {   
    public IRCChannelActionEvent() {
        super();
    }

    /**
     * Returns the channel that this event is occuring in.
     * 
     * @return The channel.
     */
    public String getChannel() {
        return this.getTarget();
    }
    
    public String toString() {
        return "[" + this.getChannel() + "] " + super.toString();
    }
}
