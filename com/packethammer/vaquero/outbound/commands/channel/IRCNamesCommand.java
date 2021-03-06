/*
 * Asks the server for the names list for a given list of channel(s). If the 
 * server is RFC2812 compatible, you can request that this names listing come from 
 * a specific target server within the IRC network.
 *
 * NAMES <CHANNELS> <TARGET SERVER>
 */

package com.packethammer.vaquero.outbound.commands.channel;

import java.util.Collection;
import java.util.List;
import java.util.Vector;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.StringOperations;
import com.packethammer.vaquero.outbound.commands.IRCCommand;
import com.packethammer.vaquero.outbound.commands.interfaces.ChannelsTargetedCommandI;

public class IRCNamesCommand extends IRCCommand implements ChannelsTargetedCommandI {
    private List<String> channels;
    private String targetServer;
    
    /**
     * Initializes this NAMES command with a channel name and target server 
     * to query from. 
     *
     * @param channel The channel to return the names for.
     * @param targetServer The server to target for the request, or null to omit it.
     */
    public IRCNamesCommand(String channel, String targetServer) {
        this();
        this.addChannel(channel);
        this.setTargetServer(targetServer);
    }
    
    /**
     * Initializes this NAMES command with a channel name to query.
     *
     * @param channel The channel to return the names for.
     */
    public IRCNamesCommand(String channel) {
        this(channel, null);
    }
    
    public IRCNamesCommand() {
        channels = new Vector();
    }
    
    /**
     * Adds a target channel to attempt to retrieve the names list from.
     */
    public void addChannel(String channel) {
        this.channels.add(channel);
    }
    
    /**
     * Returns the channel(s) we are querying the names list from.
     */
    public Collection<String> getChannels() {
        return channels;
    }
    
    /** 
     * This is a nonstandard RFC2812 feature. Returns the server we are requesting
     * the names listing from, or null if none is being targeted.
     *
     * @return A server name.
     */
    public String getTargetServer() {
        return targetServer;
    }
    
    /** 
     * This is a nonstandard RFC2812 feature. It sets the server we are requesting
     * the names listing from (set to null if none is being targeted).
     */
    public void setTargetServer(String server) {
        this.targetServer = server;
    }
    
    public IRCRawLine renderForIRC() {
        String channelsString = StringOperations.commaDelimit(this.channels);

        return IRCRawLine.buildRawLine(false, "NAMES", channelsString, this.getTargetServer());
    }    
   
    public boolean isSendable() {
        return !this.getChannels().isEmpty();
    }
}
