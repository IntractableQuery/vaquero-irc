/*
 * Represents a channel ban.
 *
 * Derived from rfc1459.
 */

package com.packethammer.vaquero.util.modes.channel;

import com.packethammer.vaquero.util.Hostmask;

public class ChanBanMode extends ChannelHostmaskParameterMode {    
    /**
     * Initializes this ban mode with a hostmask to add or remove the
     * ban on.
     *
     * @param nick The user's nickname.
     * @param adding Determines if we are giving or removing this mode.
     */
    public ChanBanMode(Hostmask hostmask, boolean adding) {
        super(MODE_BAN, hostmask, adding);
    }
}
