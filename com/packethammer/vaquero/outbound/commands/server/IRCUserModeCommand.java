/*
 * This represents a mode command that sets a mode on a user.
 */

package com.packethammer.vaquero.outbound.commands.server;

import com.packethammer.vaquero.util.modes.Modes;
import com.packethammer.vaquero.outbound.commands.basic.IRCModeCommand;

public class IRCUserModeCommand extends IRCModeCommand {    
    /**
     * Instantiates this class with base user nickname and modes information.
     *
     * @param user The user's nickname to set the modes on.
     * @param modes The modes to set on the channel/user.
     */
    public IRCUserModeCommand(String user, Modes modes) {
        super(user, modes);
    }
}