/**
 * RPL_TOPICWHOTIME
 * "<channel> <topic setter> <time set>"
 *
 * This numeric appears to have originated with ircu, but is now sufficiently
 * popular that it may as well be considered your usual IRC numeric.
 *
 * I assume the time is UNIX epoch, although I'm not 100% sure.
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import java.util.Date;
import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;

public class TopicWhoTime extends IRCNumericEvent {
    public TopicWhoTime() {
    }
    
    /**
     * Returns the channel this information is for.
     */
    public String getChannel() {
        return this.getNumericArg(0);
    }
    
    /**
     * Returns the last setter of the topic (usually a nickname, but may be a 
     * server name).
     */
    public String getTopicSetter() {
        return this.getNumericArg(1);
    }
    
    /**
     * Returns the last time this topic was changed.
     */
    public Date getLastChangedTime() {
        // multiply by 1000 so it is in milliseconds (date takes milliseconds)
        return new Date((long) Long.parseLong(this.getNumericArg(2)) * 1000);
    }
    
    public boolean validate() {
        return this.numericArgumentCount() == 3;
    }
    
    public int getHandledNumeric() {
        return this.RPL_TOPICWHOTIME;
    }
    
    public String toString() {
        return super.toString() + ", CHAN:" + this.getChannel() + ", SETTER:" + this.getTopicSetter() + ", TIME:" + this.getLastChangedTime();
    }
}
