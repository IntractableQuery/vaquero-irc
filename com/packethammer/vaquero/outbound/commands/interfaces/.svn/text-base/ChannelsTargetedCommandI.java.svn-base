/*
 * Defines a command in which one or more channels can be targeted. It is not
 * necessary that the command always support more than one channel. It can simply
 * support one.
 */

package com.packethammer.vaquero.outbound.commands.interfaces;

import java.util.Collection;

public interface ChannelsTargetedCommandI {
    /**
     * Returns the channel(s) on which this command is being performed. Guaranteed
     * to not return null, but the collection may be empty.
     *
     * @return The channels.
     */
    public Collection<String> getChannels();
    
}
