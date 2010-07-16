/*
 * Events that have an extended message attached to them should implement this class.
 * Examples include PRIVMSG, NOTICE, PART, QUIT, etc.
 */

package com.packethammer.vaquero.parser.events.interfaces;

public interface IRCExtendedMessageEventI {
    /**
     * Returns the message associated with this event.
     *
     * @return The message.
     */
    public String getMessage();
}
