/*
 * Occurs when our user mode changes.
 */

package com.packethammer.vaquero.parser.events.server;

import com.packethammer.vaquero.util.modes.user.UserMode;
import com.packethammer.vaquero.parser.events.basic.IRCModeChangeEvent;
import com.packethammer.vaquero.parser.events.interfaces.IRCNicknameTargetedEventI;
import com.packethammer.vaquero.util.modes.Modes;

public class IRCUserModeChangeEvent extends IRCModeChangeEvent implements IRCNicknameTargetedEventI {           
    public IRCUserModeChangeEvent() {
    }    
    
    /**
     * Returns user modes being modified.
     *
     * @return The user modes being modified.
     */
    public Modes<UserMode> getUserModes() {
        return this.getModes().getAsUserModes();
    }
    
    public String toString() {
        return "[USERMODE CHANGE] " + super.toString();
    }
}
