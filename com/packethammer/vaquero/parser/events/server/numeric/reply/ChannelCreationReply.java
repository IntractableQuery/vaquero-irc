/**
 * RPL_CREATIONTIME
 * "<channel> <time>"
 *
 * This supposedly originated with bahamut, but is now fairly popular
 * with other ircds. Time should be from UNIX epoch in seconds.
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import java.util.Date;
import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;

public class ChannelCreationReply extends IRCNumericEvent {
    
    /** Creates a new instance of ChannelCreationReply */
    public ChannelCreationReply() {
    }
    
    /**
     * Returns the channel this creation time is for.
     */
    public String getChannel() {
        return this.getNumericArg(0);
    }
    
    /**
     * Returns the channel's creation time.
     */
    public Date getCreationTime() {
        // multiply by 1000 so it is in milliseconds (date takes milliseconds)
        return new Date((long) Long.parseLong(this.getNumericArg(1)) * 1000);
    }
    
    public boolean validate() {
        return this.numericArgumentCount() == 2;
    }
    
    public int getHandledNumeric() {
        return this.RPL_CREATIONTIME;
    }
    
    public String toString() {
        return super.toString() + ", CHAN:" + this.getChannel() + ", TIME:" + this.getCreationTime();
    }
}
