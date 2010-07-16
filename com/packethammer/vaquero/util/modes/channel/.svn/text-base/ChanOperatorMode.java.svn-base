/*
 * Represents channel operator status.
 *
 * Derived from rfc1459.
 */

package com.packethammer.vaquero.util.modes.channel;

public class ChanOperatorMode extends ChannelNicknameParameterMode {
    /**
     * Initializes this channel operator mode with a nickname to operate on
     * and if we are giving or removing their operator status in the channel.
     *
     * @param nick The user's nickname.
     * @param adding Determines if we are giving or removing this mode.
     */
    public ChanOperatorMode(String nick, boolean adding) {
        super(MODE_OP, nick, adding);
    }
}
