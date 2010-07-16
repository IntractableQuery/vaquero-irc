/*
 * This mode makes a channel invite-only. 
 *
 * Derived from rfc1459.
 */

package com.packethammer.vaquero.util.modes.channel;

public class ChanInviteOnlyMode extends ChannelMode {
    /**
     * Initializes this invite-only mode to determine if it is being
     * given or removed from a channel.
     *
     * @param beingAdded True if this mode should be given, or false if it should be removed.
     */
    public ChanInviteOnlyMode(boolean beingAdded) {
        super(MODE_INVITEONLY, beingAdded);
    }
}
