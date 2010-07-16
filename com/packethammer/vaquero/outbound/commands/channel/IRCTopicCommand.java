/*
 * This is a topic command, which can either be used to get a channel topic
 * or set a channel topic. When the new topic argument is not present, it will
 * retrieve the channel's topic.
 *
 * TOPIC <channel> [ <topic> ]
 */

package com.packethammer.vaquero.outbound.commands.channel;

import java.util.ArrayList;
import java.util.Collection;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.outbound.commands.IRCCommand;
import com.packethammer.vaquero.outbound.commands.interfaces.ChannelsTargetedCommandI;
import com.packethammer.vaquero.outbound.commands.interfaces.ExtendedMessageCommandI;

public class IRCTopicCommand extends IRCCommand implements ChannelsTargetedCommandI,ExtendedMessageCommandI {
    private String channel;
    private String message;
    
    /** 
     * Instantiates this topic command with just a channel, which indicates we
     * are just querying for the target.
     *
     * @param channel The channel to get the topic of.
     */
    public IRCTopicCommand(String channel) {
        this(channel, null);
    }    
    
    /** 
     * Instantiates this topic command with a channel and the new topic to
     * set for that channel.
     *
     * @param channel The channel to change the topic of.
     * @param topic The topic to use. Set to null to simply query for the topic.
     */
    public IRCTopicCommand(String channel, String topic) {
        this.channel = channel;
        this.message = topic;
    }    
    
    /**
     * Returns the channel this topic is being changed or queried for in.
     */
    public Collection<String> getChannels() {
        ArrayList<String> channels = new ArrayList();
        channels.add(channel);
        return channels;
    }
  
    /**
     * Sets the channel topic to be set. Set to null to indicate that we are
     * simply querying for the current channel topic.
     *
     * @param message The topic to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * Returns the topic being set or null if this is just a request for the 
     * topic.
     */
    public String getMessage() {
        return message;
    }
    
    public IRCRawLine renderForIRC() {
        return IRCRawLine.buildRawLine(this.getMessage() != null, "TOPIC", channel, this.getMessage());
    }    
   
    public boolean isSendable() {
        return this.channel != null;
    }
}
