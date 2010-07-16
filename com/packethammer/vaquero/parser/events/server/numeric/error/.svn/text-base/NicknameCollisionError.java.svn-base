/** 
 * ERR_NICKCOLLISION
 * "<nick> <reason>"
 *
 * Returned by a server to a client when it detects a nickname collision.
 */

package com.packethammer.vaquero.parser.events.server.numeric.error;

public class NicknameCollisionError extends IRCNumericErrorEvent {
    
    /** Creates a new instance of NicknameCollisionError */
    public NicknameCollisionError() {
    }
    
    /**
     * Returns the nickname that we collided with. 
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
        return this.ERR_NICKCOLLISION;
    }
    
    public String toString() {
        return super.toString() + ", NICKNAME:" + this.getNickname();
    }
}
