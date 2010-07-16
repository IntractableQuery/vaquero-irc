/*
 * Represents a change of modes within a channel by a user.
 */

package com.packethammer.vaquero.parser.events.channel;

import com.packethammer.vaquero.util.modes.Modes;
import com.packethammer.vaquero.parser.events.interfaces.IRCChannelTargetedEventI;
import com.packethammer.vaquero.parser.events.interfaces.IRCHostmaskSourcedEventI;
import com.packethammer.vaquero.parser.events.basic.IRCModeChangeEvent;
import com.packethammer.vaquero.util.modes.channel.ChannelMode;

public class IRCChannelModeChangeEvent extends IRCModeChangeEvent implements IRCChannelTargetedEventI,IRCHostmaskSourcedEventI{
    public IRCChannelModeChangeEvent() {
    }
    
    /**
     * Gets the channel where the event takes place.
     *
     * @return The channel.
     */
    public String getChannel() {
        return this.getTarget();
    }
    
    /**
     * Returns the modes set. 
     *
     * @return The list of modes changed.
     */
    public Modes<ChannelMode> getChannelModes() {
        return super.getModes().getAsChannelModes();
    }
    
    public String toString() {
        return "[" + this.getChannel() + "] " + this.getSource().getSimpleRepresentation() + " changes modes: " + this.getChannelModes();
    }
    
}
