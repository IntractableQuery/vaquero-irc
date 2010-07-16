/**
 * RPL_ENDOFNAMES
 * "<channel> <info text>"
 *
 * Terminates a NAMES list.
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;

public class EndOfNamesReply extends IRCNumericEvent {
    
    /** Creates a new instance of EndOfNamesReply */
    public EndOfNamesReply() {
    }
    
    /**
     * Returns the name of the channel that has a NAMES reply ending.
     */
    public String getChannel() {
        return this.getNumericArg(0);
    }
    
    /**
     * Returns something to the effect of "end of NAMES list."
     */
    public String getInfo() {
        return this.getNumericArg(1);
    }
    
    public boolean validate() {
        return this.numericArgumentCount() == 2;
    }
    
    public int getHandledNumeric() {
        return this.RPL_ENDOFNAMES;
    }
    
    public String toString() {
        return super.toString() + ", CHAN:" + this.getChannel() + ", INFO:" + this.getInfo();
    }
}
