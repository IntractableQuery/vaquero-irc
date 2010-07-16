/**
 * RPL_WHOISCHANNELS
 * "<nick> :<channels delimited by spaces, with mode prefixes"
 *
 * Part of WHOIS reply.
 *
 * Internal parsing warning: some servers put mode prefix symbols on the
 * channel names that are not in ISUPPORT.
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import java.util.LinkedHashMap;
import java.util.Map;
import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;
import com.packethammer.vaquero.parser.tracking.IRCServerISupport;
import com.packethammer.vaquero.parser.tracking.definitions.ChannelNickPrefixModeDefinition;
import com.packethammer.vaquero.parser.tracking.definitions.ChannelTypeDefinition;

public class WhoisChannelsReply extends IRCNumericEvent {
    
    public WhoisChannelsReply() {
    }
    
    /**
     * Returns the nickname of the user who we're getting this channel list for.
     */
    public String getNickname() {
        return this.getNumericArg(0);
    }
    
    /**
     * Returns the raw list of channels including their mode symbol prefix.
     */
    public String[] getRawChannels() {
        return this.getNumericArg(1).split(" ");
    }
    
    /**
     * Given an ISUPPORT tracker instance, this will return a map of channel
     * names to the mode the user appears to have in that channel. Be aware
     * that there may be a mode prefix symbol that is not included in
     * ISUPPORT (some servers have a '<' prefix for (user?)mode 'd'). This
     * will ignore that mode prefix, since it's impossible to parse it.
     *
     * @param iSupport The ISUPPORT tracker to use in parsing these channels.
     * @return A non-empty map of channel names to the mode held in them (or, null if the user had no mode).
     */
    public Map<String,ChannelNickPrefixModeDefinition> getChannels(IRCServerISupport iSupport) {
        Map<String,ChannelNickPrefixModeDefinition> map = new LinkedHashMap();
        for(String rawChannel : this.getRawChannels()) {
            // first, find where the channel name begins
            int channelNameStart = -1;
            for(ChannelTypeDefinition def : iSupport.getChannelTypes()) {
                int loc = rawChannel.indexOf(def.getPrefix() + "");
                if(channelNameStart == -1 || loc < channelNameStart) {
                    channelNameStart = loc;
                }
            }
            
            if(channelNameStart > -1) {
                String chanName = rawChannel.substring(channelNameStart);
                String prefixedString = rawChannel.substring(0, channelNameStart);
                
                ChannelNickPrefixModeDefinition modeDefinition = null;
                if(prefixedString.length() > 0) {
                    // try to find mode prefix 
                    for(ChannelNickPrefixModeDefinition def : iSupport.getNickPrefixModes()) {
                        if(new Character(prefixedString.charAt(0)).equals(def.getPrefix())) {
                            modeDefinition = def;
                            break;
                        }
                    }
                    
                    // store 
                    map.put(chanName, modeDefinition);
                }
                
            } else {
                throw new IllegalStateException("While parsing raw channel name '" + rawChannel + "', unable to find start of channel name using ISUPPORT information (this server is probably using channel types that it isn't telling us about)");
            }
        }
        return map;
    }
    
    public boolean validate() {
        return this.numericArgumentCount() == 2;
    }
    
    public int getHandledNumeric() {
        return this.RPL_WHOISCHANNELS;
    }
    
    public String toString() {
        String chans = "";
        for(String chan : this.getRawChannels()) {
            chans += chan + " ";
        }
        return super.toString() + ", NICK:" + this.getNickname() + ", CHANS:" + chans;
    }
}
