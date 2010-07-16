/*
 * Defines a channel PRIVMSG that is actually a client-to-client protocol
 * message.
 */

package com.packethammer.vaquero.parser.events.channel;

import java.util.Date;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.interfaces.IRCChannelTargetedEventI;
import com.packethammer.vaquero.parser.events.basic.IRCCTCPEvent;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;

public class IRCChannelCTCPEvent extends IRCCTCPEvent implements IRCChannelTargetedEventI {   
    public IRCChannelCTCPEvent() {
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
