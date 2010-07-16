/*
 * This mode limits the maximum number of users who can be in a channel.
 *
 * Derived from rfc1459.
 */

package com.packethammer.vaquero.util.modes.channel;

public class ChanLimitMode extends ChannelIntegerParameterMode {
    /**
     * Initializes this channel join limit mode with the limit to set. It is
     * implied that this mode is being given to the channel.
     *
     * @param limit The user limit for the channel.
     */
    public ChanLimitMode(int limit) {
        super(MODE_LIMIT, limit, true);
    } 
    
    /**
     * Initializes this channel join limit mode for removing the limit mode.
     */
    public ChanLimitMode() {
        super(MODE_LIMIT, false);
    } 
    
    /**
     * Returns the user limit set by this mode. Note that removal of a user limit
     * does NOT contain the original user limit, so attempting to access this at
     * that time could cause quite an explosion. Please check beforehand if the
     * mode is being set or removed.
     *
     * @return The limit being set.
     */
    public int getLimit() {
        return this.getIntegerValue();
    }
    
    /**
     * Sets the user limit.
     *
     * @param limit The user limit to set.
     */
    public void setLimit(int limit) {
        super.setIntegerValue(limit);
    }
}
