/*
 * Allows a user to listen to wallops messages (messages sent to all server 
 * operators).
 *
 * Derived from rfc1459.
 */

package com.packethammer.vaquero.util.modes.user;

public class UserWallopsMode extends UserMode {
    public UserWallopsMode() {
        this.setMode(UserMode.MODE_WALLOPS);
    }
}
