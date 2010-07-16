/*
 * Occurs when channel modes change.
 *
 * Note: parsing the modes contained in the underlying parameters of the 
 * IRC event requires knowlege of the properties of modes on the server
 * we are on, which is not available. As such, it is the job of a parser
 * to populate our modes. Children classes should cast up getModes()
 * in their own implementations -- such as channel modes.
 *
 * ARGS: MODE <TARGET> <MODES> [ARGUMENTS THAT ACCOMPANY SET/REMOVED MODES]
 */

package com.packethammer.vaquero.parser.events.basic;

import com.packethammer.vaquero.util.modes.Modes;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.interfaces.IRCUserOrChannelTargetedEventI;

public abstract class IRCModeChangeEvent extends IRCEvent implements IRCUserOrChannelTargetedEventI {
    private Modes modes;

    public IRCModeChangeEvent() {
    }
    
    /**
     * Returns the target of this mode change.
     *
     * @return The target of this mode change.
     */
    public String getTarget() {
        return this.getSecondArgument();
    }
    
    /**
     * Returns the modes being set/removed.
     *
     * @return Modes.
     */
    public Modes getModes() {
        return modes;
    }
    
    /**
     * Sets the list of modes
     *
     * @param modes The list of modes.
     */
    public void setModes(Modes modes) {
        this.modes = modes;
    }
    
    public String toString() {
        return this.getModes().toString();
    }
}
