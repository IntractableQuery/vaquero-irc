/*
 * RPL_LUSEROP
 * "<integer> :operator(s) online"
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import com.packethammer.vaquero.parser.StringOperations;
import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;

public class OperatorsOnlineReply extends IRCNumericEvent {
    public OperatorsOnlineReply() {
    }
    
    /**
     * Gets the number of operators online.
     */
    public int getOperatorsOnline() {
        return Integer.parseInt(this.getNumericFirstArg());
    }
    
    public boolean validate() {
        return this.numericArgumentCount() >= 1 && StringOperations.isInteger(this.getNumericFirstArg());
    }
    
    public int getHandledNumeric() {
        return this.RPL_LUSEROP;
    }
    
    public String toString() {
        return super.toString() + ", OPERATORSONLINE:" + this.getOperatorsOnline();
    }
}
