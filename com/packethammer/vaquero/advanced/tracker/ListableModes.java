/**
 * NOT IN USE.
 * todo: Work on this, at least for +b
 *
 * This is a list of modes in a channel of the type "listable" (ISUPPORT mode
 * type A). 
 *
 * An example of its usage is storing the bans list for a channel.
 */

package com.packethammer.vaquero.advanced.tracker;

import java.util.HashSet;
import java.util.Set;
import com.packethammer.vaquero.parser.tracking.definitions.ChannelModeDefinition;
import com.packethammer.vaquero.util.modes.channel.ChannelMode;

public class ListableModes {
    private ChannelModeDefinition modeDefinition;
    private HashSet<ChannelMode> modes;
    private boolean definite;
    
    /** Creates a new instance of ListableModes */
    public ListableModes() {
        modes = new HashSet();
    }

    /**
     * Returns the mode definition for this list of modes.
     */
    public ChannelModeDefinition getModeDefinition() {
        return modeDefinition;
    }

    public void setModeDefinition(ChannelModeDefinition modeDefinition) {
        this.modeDefinition = modeDefinition;
    }

    /**
     * Returns the actual modes listing. These should all be the same type mode
     * as is defined by the channel mode definition.
     */
    public Set<ChannelMode> getModes() {
        return modes;
    }

    /**
     * Determines if this set of modes is actually the full set of modes set in
     * the channel. An example of a situation where it is indefinite would be
     * like the following:
     * 
     * We join a channel, but do not try to list the bans with MODE #chan +b.
     * However, we record additional bans made. Thus, we know some of the bans,
     * but not all of them. However, if we had done a MODE #chan +b at some
     * point and gotten a reply, we would have a definite list of modes.
     */
    public boolean isDefinite() {
        return definite;
    }

    /**
     * @see #isDefinite()
     */
    public void setDefinite(boolean definite) {
        this.definite = definite;
    }
    
}
