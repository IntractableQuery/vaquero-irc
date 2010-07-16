/*
 * This sends a NOTICE to one or more channels with a given message. 
 *
 * This class does not directly support noticing to just voices/ops, etc., which
 * is a feature present in some IRC servers. The behavior of such special NOTICEs
 * is not too predictable, so they are of little use. The server you are on
 * should offer alternate commands though, such as WALLCHOPS and WALLVOICES,
 * which are more predictable in behavior and nicely laid aside as additional
 * commands.
 */

package com.packethammer.vaquero.outbound.commands.channel;

import java.util.Collection;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.StringOperations;
import com.packethammer.vaquero.outbound.commands.basic.MultitargetMessage;
import com.packethammer.vaquero.outbound.commands.interfaces.ChannelsTargetedCommandI;

public class IRCChannelNoticeCommand extends MultitargetMessage implements ChannelsTargetedCommandI {
    /**
     * Instantiates a notice command with a target channel and message.
     *
     * @param channel The channel to notice.
     * @param message The message to send.
     */
    public IRCChannelNoticeCommand(String channel, String message) {
        this(message);
        this.addChannel(channel);
    }
    
    /**
     * Instantiates a notice command with a message, but no target(s).
     *
     * @param message The message to send.
     */
    public IRCChannelNoticeCommand(String message) {
        this();
        this.setMessage(message);
    }
    
    public IRCChannelNoticeCommand() {
        super();
    }
    
    /**
     * Returns the channels we are noticing.
     *
     * @return Channels being targeted.
     */
    public Collection<String> getChannels() {
        return super.getTargets();
    }
    
    /**
     * Adds a channel to the target list. Use this instead of addTarget().
     */
    public void addChannel(String channel) {
        this.addTarget(channel);
    }
    
    /**
     * DO NOT USE THIS METHOD. THROWS EXCEPTION BY DEFAULT.
     */
    public void addTarget(String target) {
        throw new IllegalStateException("Do not use this method in this subclass; use addChannel");
    }
    
    public IRCRawLine renderForIRC() {
        String targets = StringOperations.commaDelimit(this.getChannels());
        
        return IRCRawLine.buildRawLine(true, "NOTICE", targets, this.getMessage());
    }    
   
    public boolean isSendable() {
        return !this.getChannels().isEmpty() && this.getMessage() != null;
    }
}

