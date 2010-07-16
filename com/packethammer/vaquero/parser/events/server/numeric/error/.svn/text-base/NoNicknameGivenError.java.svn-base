/**
 * ERR_NONICKNAMEGIVEN
 * "<reason>"
 *
 * Returned when a nickname parameter expected for a command isn't found.
 */

package com.packethammer.vaquero.parser.events.server.numeric.error;

public class NoNicknameGivenError extends IRCNumericErrorEvent {    
    /**
     * Creates a new instance of NoNicknameGivenError
     */
    public NoNicknameGivenError() {
    }
    
    public String getReason() {
        return this.getNumericArg(0);
    }
    
    public boolean validate() {
        return this.numericArgumentCount() == 1;
    }
    
    public int getHandledNumeric() {
        return this.ERR_NONICKNAMEGIVEN;
    }
}
