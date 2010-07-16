/*
 * RPL_LUSERCHANNELS
 * "<integer> :channels formed"
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import com.packethammer.vaquero.parser.StringOperations;
import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;

public class ChannelsFormedReply extends IRCNumericEvent {
    public ChannelsFormedReply() {
    }
    
    /**
     * Gets the number of channels formed.
     */
    public int getChannelsFormed() {
        return Integer.parseInt(this.getNumericFirstArg());
    }
    
    public boolean validate() {
        return this.numericArgumentCount() >= 1 && StringOperations.isInteger(this.getNumericFirstArg());
    }
    
    public int getHandledNumeric() {
        return this.RPL_LUSERCHANNELS;
    }
    
    public String toString() {
        return super.toString() + ", CHANNELSFORMED:" + this.getChannelsFormed();
    }
}