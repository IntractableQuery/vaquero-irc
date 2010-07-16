/**
 * Defines an event that has a source in Hostmask form. Useful for filtering 
 * systems to quickly identify events that may be from a certain IRC user and 
 * take action on them.
 *
 * Technically, all IRCEvents have hostmasks, but this is specifically
 * for situations where the hostmask is *probably* someone other than a server.
 * As with most interfaces in the events classes, it is to help with any
 * sort of event filtering that someone might want to undertake given any
 * IRCEvent instance.
 */

package com.packethammer.vaquero.parser.events.interfaces;

import com.packethammer.vaquero.util.Hostmask;

public interface IRCHostmaskSourcedEventI {
    /**
     * Gets the source.
     * 
     * @return A Hostmask representing the source.
     */
    public Hostmask getSource();
}
