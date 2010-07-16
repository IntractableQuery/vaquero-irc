/**
 * ERR_RESTRICTED
 * "<reason>"
 *
 * Sent by the server to a user upon connection to indicate the restricted 
 * nature of the connection (i.e. usermode +r).
 *
 * TODO: I am not sure if this affects the connection sequence or not. Vaquero is
 * not exactly RFC2812-friendly anyway -- it only coexists with it. This numeric
 * may break any sort of query-based processing.
 */

package com.packethammer.vaquero.parser.events.server.numeric.error;

public class RestrictedNicknameError extends IRCNumericErrorEvent {    
    /** Creates a new instance of RestrictedNicknameError */
    public RestrictedNicknameError() {
    }

    public String getReason() {
        return this.getNumericArg(0);
    }
    
    public boolean validate() {
        return this.numericArgumentCount() == 1;
    }
    
    public int getHandledNumeric() {
        return this.ERR_RESTRICTED;
    }
}
