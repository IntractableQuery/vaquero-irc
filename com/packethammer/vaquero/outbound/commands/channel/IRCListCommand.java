/*
 * Given a list of channel(s), this command sends a LIST request. The reply is
 * typically a numeric that provides channel name, size, and topic. It is not
 * mentioned in RFC1459 or RFC2812, but sending this command without any 
 * arguments at all will typically return the entire visible channel list for
 * the entire IRC network. To do this, just don't specify a channel or server
 * target.
 *
 * Also note that the implementation of this varies greatly across various 
 * types of servers; some servers allow special parameters to be used for
 * filtering the channels list. This is particularly useful for discovering
 * hidden users (at least by looking at user count) on some channels.
 *
 * LIST <CHANNELS> <TARGET SERVER>
 */

package com.packethammer.vaquero.outbound.commands.channel;

import java.util.Collection;
import java.util.List;
import java.util.Vector;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.StringOperations;
import com.packethammer.vaquero.outbound.commands.IRCCommand;
import com.packethammer.vaquero.outbound.commands.interfaces.ChannelsTargetedCommandI;

public class IRCListCommand extends IRCCommand implements ChannelsTargetedCommandI {    
    private List<String> channels;
    private String targetServer;
    
    /**
     * Initializes this LIST command with a channel name and target server 
     * to query from. 
     *
     * @param channel The channel to return the listing for.
     * @param targetServer The server to target for the request, or null to omit it.
     */
    public IRCListCommand(String channel, String targetServer) {
        this();
        this.addChannel(channel);
        this.setTargetServer(targetServer);
    }
    
    /**
     * Initializes this LIST command with a channel name and target server 
     * to query from. 
     *
     * @param channel The channel to return the listing for.
     */
    public IRCListCommand(String channel) {
        this(channel, null);
    }
    
    /**
     * Initializes an empty LIST command, which in empty state will generally 
     * result in the server replying with the entire visible channel list for
     * the irc network.
     */
    public IRCListCommand() {
        channels = new Vector();
    }
    
    /**
     * Adds a target channel to attempt to retrieve the listing for.
     */
    public void addChannel(String channel) {
        this.channels.add(channel);
    }
    
    /**
     * Returns the channel(s) we are trying to list, assuming we are listing
     * specific channels.
     */
    public Collection<String> getChannels() {
        return channels;
    }
    
    /** 
     * Returns the server we are requesting channel listing from, or null if 
     * none is being targeted.
     *
     * @return A server name.
     */
    public String getTargetServer() {
        return targetServer;
    }
    
    /** 
     * Sets the server we are requesting channel listing from (use null if 
     * none is being targeted).
     */
    public void setTargetServer(String server) {
        this.targetServer = server;
    }
    
    public IRCRawLine renderForIRC() {
        String channelsString = StringOperations.commaDelimit(this.channels);
        if(channelsString.length() == 0)
            channelsString = null; // make empty if none

        return IRCRawLine.buildRawLine(false, "LIST", channelsString, this.getTargetServer());
    }    
   
    public boolean isSendable() {
        return true; // even parameterless, the LIST is fine
    }
}
