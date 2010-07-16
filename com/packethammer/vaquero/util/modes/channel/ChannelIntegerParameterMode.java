/*
 * Represents a channel mode who's parameter is an integer.
 */

package com.packethammer.vaquero.util.modes.channel;

public class ChannelIntegerParameterMode extends ChannelMode {
    public ChannelIntegerParameterMode(char mode, int number, boolean beingAdded) {
        this(mode, beingAdded);
        this.setIntegerValue(number);
    }

    public ChannelIntegerParameterMode(char mode, boolean beingAdded) {
        super(mode, null, beingAdded);
    }
    
    /**
     * Returns this mode's integer value.
     *
     * @return The integer parameter.
     */
    public int getIntegerValue() {
        return Integer.parseInt(this.getParameter());
    }

    /**
     * Sets this mode's integer value.
     *
     * @param integerValue The new integer parameter to use.
     */
    public void setIntegerValue(int integerValue) {
        super.setParameter("" + this.getIntegerValue());
    }
    
    /**
     * Sets this mode's parameter.
     *
     * @param parameter The mode's parameter.
     */
    public void setParameter(String parameter) {
        this.setIntegerValue(Integer.parseInt(parameter));
    }
}
