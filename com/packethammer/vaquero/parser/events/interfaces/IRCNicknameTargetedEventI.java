/*
 * Defines an event that targets a nickname. Note that it is entirely possible
 * that a channel and nickname can be targeted in the same event -- an example
 * of this is a KICK.
 */

package com.packethammer.vaquero.parser.events.interfaces;

public interface IRCNicknameTargetedEventI {
    /**
     * Returns the nickname that is being targeted by this event.
     *
     * @return Nickname.
     */
    public String getTarget();
}
