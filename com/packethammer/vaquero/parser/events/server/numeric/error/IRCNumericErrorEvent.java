/**
 * ERR_ numerics subclass this class.
 */

package com.packethammer.vaquero.parser.events.server.numeric.error;

import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;

public abstract class IRCNumericErrorEvent extends IRCNumericEvent {
    /**
     * Typically, a numeric error is accompanied by a plaintext reason for
     * its cause. This will return that reason, or null if none is provided
     * with that numeric.
     *
     * @return The reason for the error, or null if no reason is given.
     */
    public abstract String getReason();
    
    public String toString() {
        return super.toString() + ", REASON:" + this.getReason();
    }
}
