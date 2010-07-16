/*
 * This represents a mode command that sets modes on a channel.
 */

package com.packethammer.vaquero.outbound.commands.channel;

import java.util.ArrayList;
import java.util.Collection;
import com.packethammer.vaquero.util.modes.Modes;
import com.packethammer.vaquero.outbound.commands.basic.IRCModeCommand;
import com.packethammer.vaquero.outbound.commands.interfaces.ChannelsTargetedCommandI;

public class IRCChannelModeCommand extends IRCModeCommand implements ChannelsTargetedCommandI {
    /**
     * Instantiates this class with base channel and modes information.
     *
     * @param channel The channel to set the modes on.
     * @param modes The modes to set on the channel/user.
     */
    public IRCChannelModeCommand(String channel, Modes modes) {
        super(channel, modes);
    }
    
    /**
     * Instantiates this class with the target channel. By not adding modes,
     * you will cause the server to send back a numeric reply with the current
     * modes for the channel.
     *
     * @param channel The channel to set the modes on.
     */
    public IRCChannelModeCommand(String channel) {
        super(channel, null);
    }
    
    /**
     * Returns the channel that the mode is being set on. 
     */
    public Collection<String> getChannels() {
        ArrayList<String> a = new ArrayList();
        a.add(this.getTarget());        
        return a;
    }
}
