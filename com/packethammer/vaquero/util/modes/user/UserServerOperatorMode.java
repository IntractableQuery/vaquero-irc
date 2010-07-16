/*
 * Marks a user as a (server) operator.
 *
 * Derived from rfc1459.
 */

package com.packethammer.vaquero.util.modes.user;

public class UserServerOperatorMode extends UserMode {
    public UserServerOperatorMode() {
        this.setMode(UserMode.MODE_SERVEROPERATOR);
    }    
}
