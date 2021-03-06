/*
 * This command allows you to leave an IRC channel.
 *
 * Note: The parting message is not in RFC1459, but rather, RFC2812. The server
 * you are on may not support it, although most do.
 *
 * PART <CHANNEL(S)> [REASON]
 */

package com.packethammer.vaquero.outbound.commands.channel;

import java.util.Collection;
import java.util.HashSet;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.outbound.commands.IRCCommand;
import com.packethammer.vaquero.parser.StringOperations;
import com.packethammer.vaquero.outbound.commands.interfaces.ChannelsTargetedCommandI;

public class IRCPartCommand extends IRCCommand implements ChannelsTargetedCommandI {    
    private HashSet<String> channels;
    private String message;
    
    /**
     * Initializes this part command with a single channel we are to leave and
     * the parting message. The reason may be null.
     *
     * @param channel The channel to leave.
     * @param reason The reason for leaving it.
     */
    public IRCPartCommand(String channel, String reason) {
        this();
        this.addPart(channel);
        this.message = reason;
    }
    
    /**
     * Initializes this part command with a single channel we are to leave.
     */
    public IRCPartCommand(String channel) {
        this(channel, null);
    }
    
    public IRCPartCommand() {
        channels = new HashSet();
    }
    
    /**
     * Returns a collection of the channels we are to part.
     */
    public Collection<String> getChannels() {
        return channels;
    }
    
    /**
     * Adds a channel to part from. 
     *
     * @param channel The channel to leave.
     */
    public void addPart(String channel) {
        this.channels.add(channel);
    }
    
    /**
     * Sets the parting message (reason) for leaving the channel(s). Note this
     * is not an RFC2812 feature, so it may not be supported on your server.
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * Returns the parting message.
     */
    public String getMessage() {
        return message;
    }
    
    public IRCRawLine renderForIRC() {
        String partString = StringOperations.commaDelimit(this.channels);

        return IRCRawLine.buildRawLine(this.getMessage() != null, "PART", partString, this.getMessage());
    }    
   
    public boolean isSendable() {
        return !this.getChannels().isEmpty();
    }
}
