/*
 * This is a mode definition. As the name suggests, it is only to describe how
 * a mode behaves. Subclasses may expand on other information that this mode
 * may convey. 
 *
 * Information in this class is immutable.
 */

package com.packethammer.vaquero.parser.tracking.definitions;

public class ModeDefinition implements Comparable {
    private Character mode;
    private boolean channelMode;
    private boolean userMode;
    private boolean removalAccompaniedByParameter;
    private boolean additionAccompaniedByParameter;
            
    /**
     * Initializes this mode definition with base essential information. 
     *
     * @param mode The character that represents this mode.
     * @param channelMode Determinees if this is a channel mode or not.
     * @param userMode Determines if this is a user mode or not.
     * @param removalAccompaniedByParameter Determines if the removal of this mode requires a parameter.
     * @param additionAccompaniedByParameter Determines if the addition of this mode requires a parameter.
     */
    public ModeDefinition(Character mode, boolean channelMode, boolean userMode, boolean removalAccompaniedByParameter, boolean additionAccompaniedByParameter) {
        this.mode = mode;
        this.channelMode = channelMode;
        this.userMode = userMode;
        this.removalAccompaniedByParameter = removalAccompaniedByParameter;
        this.additionAccompaniedByParameter = additionAccompaniedByParameter;
    }

    /**
     * Returns the character that represents this mode.
     *
     * @return The mode's character representation.
     */
    public Character getMode() {
        return mode;
    }

    /**
     * Determines if this is a channel mode or not
     *
     * @return True if this is a channel mode, false otherwise.
     */
    public boolean isChannelMode() {
        return channelMode;
    }

    /**
     * Determines if this is a user mode or not
     *
     * @return True if this is a user mode, false otherwise.
     */
    public boolean isUserMode() {
        return userMode;
    }

    /**
     * Determines if this mode requires a parameter when it is removed.
     *
     * @return True if this requires a parameter on removal, false otherwise.
     */
    public boolean isRemovalAccompaniedByParameter() {
        return removalAccompaniedByParameter;
    }

    /**
     * Determines if this mode requires a parameter when it is added.
     *
     * @return True if this requires a parameter upon being added, false otherwise.
     */
    public boolean isAdditionAccompaniedByParameter() {
        return additionAccompaniedByParameter;
    }
    
    public int compareTo(Object o) {
        if(o instanceof ModeDefinition) {
            ModeDefinition m = (ModeDefinition) o;
            return this.getMode().compareTo(m.getMode());
        } else {
            throw new IllegalArgumentException("object is not of type " + this.getClass().getCanonicalName());
        }
    }
    
    public boolean equals(Object o) {
        return compareTo(o) == 0;
    }
    
    public int hashCode() {
        return this.getMode().hashCode();
    }    
    
    public String toString() {
        return "MODE:" + this.getMode() + " (CHAN:" + this.isChannelMode() + "/USR:" + this.isUserMode() + "/ADD?:" + this.isAdditionAccompaniedByParameter() + "/REM?:" + this.isRemovalAccompaniedByParameter() + ")";
    }
}
