/*
 * ERR_UNAVAILRESOURCE
 * "<nick/channel/service> <reason>"
 * 
 * Return when the target is unable to be reached temporarily, eg. a delay 
 * mechanism in play, or a service being offline.
 */

package com.packethammer.vaquero.parser.events.server.numeric.error;

public class UnavailableResourceError extends IRCNumericErrorEvent {    
    /** Creates a new instance of UnavailableResourceError */
    public UnavailableResourceError() {
    }
    
    /**
     * Returns the nickname/channel/service that cannot be reached.
     */
    public String getUnavailableEntity() {
        return this.getNumericArg(0);
    }
    
    public String getReason() {
        return this.getNumericArg(1);
    }
    
    public boolean validate() {
        return this.numericArgumentCount() == 2;
    }
    
    public int getHandledNumeric() {
        return this.ERR_UNAVAILRESOURCE;
    }
    
    public String toString() {
        return super.toString() + ", ENTITY:" + this.getUnavailableEntity();
    }
}
