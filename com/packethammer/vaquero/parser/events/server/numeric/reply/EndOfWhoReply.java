/**
 * RPL_ENDOFWHO
 * "<name> :<info>"
 *
 * The name parameter is typically the original query we used to retrieve
 * the WHO listing.
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;

public class EndOfWhoReply extends IRCNumericEvent {    
    /**
     * Creates a new instance of EndOfWhoReply
     */
    public EndOfWhoReply() {
    }
    
    /**
     * Returns the 'name' of the original WHO query. This is typically
     * the query mask we used, although that isn't guaranteed.
     */
    public String getName() {
        return this.getNumericArg(0);
    }
    
    /**
     * Returns a string of information associated with the end of this WHO reply.
     */
    public String getInformation() {
        return this.getNumericArg(1);
    }
    
    public boolean validate() {
        return this.numericArgumentCount() == 2;
    }
    
    public int getHandledNumeric() {
        return this.RPL_ENDOFWHO;
    }
    
    public String toString() {
        return super.toString() + " NAME:" + this.getName() + " INFO:" + this.getInformation();
    }
}
