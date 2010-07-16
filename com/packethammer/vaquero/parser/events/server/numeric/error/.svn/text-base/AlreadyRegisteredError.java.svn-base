/*  ERR_ALREADYREGISTERED
 * "<reason>"
 *
 * Returned by the server to any link which attempts to register again. Typically
 * occurs when you try to send PASS or USER a second time (these commands can
 * only be sent once).
 */

package com.packethammer.vaquero.parser.events.server.numeric.error;

public class AlreadyRegisteredError extends IRCNumericErrorEvent {    
    /** Creates a new instance of AlreadyRegisteredError */
    public AlreadyRegisteredError() {
    }
    
    public String getReason() {
        return this.getNumericArg(0);
    }
    
    public boolean validate() {
        return this.numericArgumentCount() == 1;
    }
    
    public int getHandledNumeric() {
        return this.ERR_ALREADYREGISTERED;
    }
}
