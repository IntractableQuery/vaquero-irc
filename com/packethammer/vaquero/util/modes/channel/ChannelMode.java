/*
 * Represents a basic channel mode.
 */

package com.packethammer.vaquero.util.modes.channel;

import com.packethammer.vaquero.parser.tracking.definitions.ChannelModeDefinition;
import com.packethammer.vaquero.util.modes.StandardMode;

public class ChannelMode extends StandardMode {
    public static final char MODE_BAN = 'b';
    public static final char MODE_KEY = 'k';
    public static final char MODE_INVITEONLY = 'i';
    public static final char MODE_LIMIT = 'l';
    public static final char MODE_MODERATED = 'm';
    public static final char MODE_NOEXTERNALMESSAGES = 'n';
    public static final char MODE_PRIVATE = 'p';
    public static final char MODE_SECRET = 's';
    public static final char MODE_TOPICLOCK = 't';
    public static final char MODE_OP = 'o';
    public static final char MODE_VOICE = 'v';

    public ChannelMode() {        
    }
    
    public ChannelMode(Character mode, String parameter, boolean beingAdded) {      
       super(mode, parameter, beingAdded);
    }
    
    public ChannelMode(Character mode, boolean beingAdded) {      
       super(mode, beingAdded);
    }
    
    /**
     * Initializes this channel mode with a channel mode definition.
     *
     * @param modeDefinition The channel mode definition to use.
     */
    public ChannelMode(ChannelModeDefinition modeDefinition) {
        super(modeDefinition);
    }    
    
    /**
     * Returns this mode instance's channel mode definition, assuming it has one.
     *
     * @return The channel mode definition or null if there was none.
     */
    public ChannelModeDefinition getModeDefinition() {
        return (ChannelModeDefinition) super.getModeDefinition();
    }
   
}
