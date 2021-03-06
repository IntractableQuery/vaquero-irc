/*
 * Sends a PRIVMSG to one or more channels with a given message.
 *
 * PRIVMSG <TARGETS> <MESSAGE>
 */

package com.packethammer.vaquero.outbound.commands.channel;

import java.util.Collection;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.StringOperations;
import com.packethammer.vaquero.outbound.commands.basic.MultitargetMessage;
import com.packethammer.vaquero.outbound.commands.interfaces.ChannelsTargetedCommandI;

public class IRCChannelMessageCommand extends MultitargetMessage implements ChannelsTargetedCommandI {
    /**
     * Instantiates a message command with a target channel and message.
     *
     * @param channel The channel to message.
     * @param message The message to send.
     */
    public IRCChannelMessageCommand(String channel, String message) {
        this(message);
        this.addChannel(channel);
    }
    
    /**
     * Instantiates a message command with a message, but no target(s).
     *
     * @param message The message to send.
     */
    public IRCChannelMessageCommand(String message) {
        this();
        this.setMessage(message);
    }
    
    public IRCChannelMessageCommand() {
        super();
    }
    
    /**
     * Returns the channels we are messaging.
     *
     * @return Channels being targeted.
     */
    public Collection<String> getChannels() {
        return super.getTargets();
    }
    
    /**
     * Adds a channel to the target list. 
     */
    public void addChannel(String channel) {
        super.addTarget(channel);
    }
    
    /**
     * Adds a channel to the target list. 
     */
    public void addTarget(String channel) {
        this.addChannel(channel);
    }
    
    public IRCRawLine renderForIRC() {
        String targets = StringOperations.commaDelimit(this.getChannels());
        
        return IRCRawLine.buildRawLine(true, "PRIVMSG", targets, this.getMessage());
    }    
   
    public boolean isSendable() {
        return !this.getChannels().isEmpty() && this.getMessage() != null;
    }
}

