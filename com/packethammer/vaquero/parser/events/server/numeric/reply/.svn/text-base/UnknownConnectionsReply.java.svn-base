/*
 * RPL_LUSERUNKNOWN
 * "<integer> :unknown connection(s)"
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import com.packethammer.vaquero.parser.StringOperations;
import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;

public class UnknownConnectionsReply extends IRCNumericEvent {
    public UnknownConnectionsReply() {
    }
    
    /**
     * Gets the number of unknown connections.
     */
    public int getUnknownConnections() {
        return Integer.parseInt(this.getNumericFirstArg());
    }
    
    public boolean validate() {
        return this.numericArgumentCount() >= 1 && StringOperations.isInteger(this.getNumericFirstArg());
    }
    
    public int getHandledNumeric() {
        return this.RPL_LUSERUNKNOWN;
    }
    
    public String toString() {
        return super.toString() + ", UNKNOWNCONNECTIONS:" + this.getUnknownConnections();
    }
}
