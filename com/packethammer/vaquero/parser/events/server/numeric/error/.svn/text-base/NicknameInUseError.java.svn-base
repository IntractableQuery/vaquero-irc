/**
 * ERR_NICKNAMEINUSE
 * "<attempted nickname> <reason>
 *
 * Returned by the NICK command when the given nickname is already in use.
 */

package com.packethammer.vaquero.parser.events.server.numeric.error;

public class NicknameInUseError extends IRCNumericErrorEvent {    
    /** Creates a new instance of NicknameInUseError */
    public NicknameInUseError() {
    }
        
    /**
     * Returns the nickname that was in use.
     */
    public String getNickname() {
        return this.getNumericArg(0);
    }
    
    public String getReason() {
        return this.getNumericArg(1);
    }
    
    public boolean validate() {
        return this.numericArgumentCount() == 2;
    }
    
    public int getHandledNumeric() {
        return this.ERR_NICKNAMEINUSE;
    }
    
    public String toString() {
        return super.toString() + ", NICKNAME:" + this.getNickname();
    }
}
