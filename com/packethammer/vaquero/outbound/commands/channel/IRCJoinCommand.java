/*
 * Represents a JOIN command to join an IRC channel. This class will try to 
 * preserve the order you wish to join channels in, but the order is not 
 * guaranteed, especially since joining multiple key'd channels neccesitates
 * moving the key'd channels to the front of the JOIN query.
 *
 * Note: All channels that we are attempting to join using a channel key should
 * be at the start of the JOIN command.
 *
 * JOIN <channel>{,<channel>} [<key>{,<key>}]
 */

package com.packethammer.vaquero.outbound.commands.channel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.outbound.commands.IRCCommand;
import com.packethammer.vaquero.parser.StringOperations;
import com.packethammer.vaquero.outbound.commands.interfaces.ChannelsTargetedCommandI;

public class IRCJoinCommand extends IRCCommand implements ChannelsTargetedCommandI {
    private List<ChannelKeyPair> channels;
    
    /**
     * Creates a new join command with only a single channel specified for the
     * join.
     *
     * @param channel The channel to join.
     */
    public IRCJoinCommand(String channel) {
        this(channel, null);
    }
    
    /**
     * Creates a new join command with only a single channel and its key 
     * specified for the join.
     *
     * @param channel The channel to join.
     * @param key The channel key to use when attempting to join.
     */
    public IRCJoinCommand(String channel, String key) {
        this();
        this.addChannel(channel, key);
    }
            
    /**
     * Creates a new join command instance that lacks any channels.
     */
    public IRCJoinCommand() {
        this.channels = new Vector();
    }

    /**
     * Returns the channel-key pair used for joining channels. This is a direct
     * reference to the internal list.
     */
    public Collection<String> getChannels() {
        ArrayList<String> joinList = new ArrayList();
        for(ChannelKeyPair join : channels) {
            joinList.add(join.getChannel());
        }
        
        return joinList;
    }

    /** 
     * Adds a channel to the join list.
     *
     * @param channel The channel name to join.
     */
    public void addChannel(String channel) {
       this.addChannel(channel, null);
   }

    /** 
     * Adds a channel to the join list with its key.
     *
     * @param channel The channel name to join.
     * @param key The key to use when attemping to join the channel.
     */
    public void addChannel(String channel, String key) {
        // force key'd channels to front
        if(key != null) {
            channels.add(0, new ChannelKeyPair(channel, key));
        } else {
            channels.add(new ChannelKeyPair(channel, key));
        }
    }

    public IRCRawLine renderForIRC() {
        ArrayList<String> joinList = new ArrayList();
        ArrayList<String> keyList = new ArrayList();
        
        for(ChannelKeyPair join : channels) {
            joinList.add(join.getChannel());
            
            if(join.getKey() != null) {
                keyList.add(join.getKey());
            }
        }
        
        String joinString = StringOperations.commaDelimit(joinList);
        String keyString = StringOperations.commaDelimit(keyList);
        
        return IRCRawLine.buildRawLine(false, "JOIN", joinString, keyString.length() != 0 ? keyString : null);
    }
    
   
    public boolean isSendable() {
        return !this.getChannels().isEmpty();
    }
}
