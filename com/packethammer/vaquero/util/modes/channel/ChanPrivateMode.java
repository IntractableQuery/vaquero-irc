/*
 * This mode makes a channel private. 
 *
 * Derived from rfc1459.
 */

package com.packethammer.vaquero.util.modes.channel;

public class ChanPrivateMode extends ChannelMode {
    /**
     * Initializes this private mode to determine if it is being
     * given or removed from a channel.
     *
     * @param beingAdded True if this mode should be given, or false if it should be removed.
     */
    public ChanPrivateMode(boolean beingAdded) {
        super(MODE_PRIVATE, beingAdded);
    }
}
