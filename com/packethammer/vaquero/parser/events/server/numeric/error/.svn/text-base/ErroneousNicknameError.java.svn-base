/**
 * ERR_ERRONEOUSNICKNAME
 * <nick> <reason>
 *
 * Returned after receiving a NICK message which contains a nickname which is 
 * considered invalid, such as it's reserved ('anonymous') or contains characters 
 * considered invalid for nicknames. 
 */

package com.packethammer.vaquero.parser.events.server.numeric.error;

public class ErroneousNicknameError extends IRCNumericErrorEvent {    
    /** Creates a new instance of ErroneousNicknameError */
    public ErroneousNicknameError() {
    }
    
    /**
     * Returns the nickname we tried to use that was illegal.
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
        return this.ERR_ERRONEOUSNICKNAME;
    }
    
    public String toString() {
        return super.toString() + ", NICKNAME:" + this.getNickname();
    }
}
