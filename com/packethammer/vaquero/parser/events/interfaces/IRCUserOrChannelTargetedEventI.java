/*
 * Defines an event that may target either a user or a channel. This interface
 * is not applied on events that only target either a nickname, or only target
 * a channel. Those sort of events have their own interfaces. This interface is
 * only for events where it can be either. Note that the class hierarchy of some
 * event types may result in something such as a channel message still having
 * inherited this event type -- this is due to the fact its parent class is the
 * one that is ambigious, not that this is an umbrella that also always covers
 * nickname/channel-specific events.
 */

package com.packethammer.vaquero.parser.events.interfaces;

public interface IRCUserOrChannelTargetedEventI {
    /**
     * Returns the nickname or channel that is being targeted by this event.
     *
     * @return Nickname or channel.
     */
    public String getTarget();
}
