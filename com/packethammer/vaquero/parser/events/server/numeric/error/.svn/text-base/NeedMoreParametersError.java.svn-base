/**
 * ERR_NEEDMOREPARAMS
 * "<command> <reason>"
 *
 * Returned by the server by any command which requires more parameters 
 * than the number of parameters given.
 */

package com.packethammer.vaquero.parser.events.server.numeric.error;

import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;

public class NeedMoreParametersError extends IRCNumericErrorEvent {
    /** Creates a new instance of NeedMoreParametersError */
    public NeedMoreParametersError() {
    }
    
    /**
     * Returns the name of the command that you just sent that caused this
     * error.
     */
    public String getCommand() {
        return this.getNumericArg(0);
    }
    
    public String getReason() {
        return this.getNumericArg(1);
    }
    
    public boolean validate() {
        return this.numericArgumentCount() == 2;
    }
    
    public int getHandledNumeric() {
        return this.ERR_NEEDMOREPARAMS;
    }
    
    public String toString() {
        return super.toString() + ", COMMAND:" + this.getCommand();
    }
}
