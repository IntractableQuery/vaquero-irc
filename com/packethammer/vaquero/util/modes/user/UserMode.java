/*
 * Represents a basic usermode.
 */

package com.packethammer.vaquero.util.modes.user;

import com.packethammer.vaquero.util.modes.StandardMode;
import com.packethammer.vaquero.parser.tracking.definitions.ModeDefinition;

public class UserMode extends StandardMode {
    public static final char MODE_INVISIBLE = 'i';
    public static final char MODE_SERVERNOTICES = 's';
    public static final char MODE_SERVEROPERATOR = 'o';
    public static final char MODE_WALLOPS = 'w';
    
    public UserMode() {        
    }
    
    /**
     * Initializes this channel mode with a channel mode definition.
     *
     * @param modeDefinition The channel mode definition to use.
     */
    public UserMode(ModeDefinition modeDefinition) {
        super(modeDefinition);
    }  
    
    /**
     * Instantiates this mode using a mode character.
     *
     * @param mode The mode to set.
     * @param beingAdded Set to true to add the mode, or false to remove it.
     */
    public UserMode(Character mode, boolean beingAdded) {      
       super(mode, beingAdded);
    }
}
