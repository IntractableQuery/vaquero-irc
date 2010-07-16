/*
 * This modesets a channel key (password).
 *
 * Derived from rfc1459.
 */

package com.packethammer.vaquero.util.modes.channel;

public class ChanKeyMode extends ChannelMode {    
    /** 
     * Initializes this key set mode to determine if it is being given or
     * removed from a channel.
     *
     * @param key The key to give or remove.
     * @param beingAdded True if we are giving this mode, or false if we are removing the mode.
     */
    public ChanKeyMode(String key, boolean beingAdded) {
        super(MODE_KEY, key, beingAdded);
    }
    
    /**
     * Returns the key (password) set by this mode.
     *
     * @return The key.
     */
    public String getKey() {
        return this.getParameter();
    }    
    
    /**
     * Sets the key (password) for this mode.
     *
     * @param key The key.
     */
    public void setKey(String key) {
        this.setParameter(key);
    }
}
