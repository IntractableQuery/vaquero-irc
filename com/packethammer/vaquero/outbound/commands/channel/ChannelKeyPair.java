/*
 * Simply represents a channel and a key (password) to associate with it.
 */

package com.packethammer.vaquero.outbound.commands.channel;

public class ChannelKeyPair {
    private String channel;
    private String key;
    
    /**
     * Creates a new instance of ChannelKeyPair with channel and key information.
     */
    public ChannelKeyPair(String channel, String key) {
        this.channel = channel;
        this.key = key;
    }
    
    /**
     * Creates a new instance of ChannelKeyPair with channel information.
     */
    public ChannelKeyPair(String channel) {
        this(channel, null);
    }
    
    public ChannelKeyPair() {
    }

    /**
     * Returns the channel
     */
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * Returns the key for the channel. May be null.
     */
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
}
