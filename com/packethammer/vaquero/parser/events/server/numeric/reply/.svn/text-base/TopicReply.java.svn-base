/**
 * RPL_TOPIC
 * "<channel> <topic>"
 *
 * Sent automatically when you join a channel, although if there is no channel
 * topic, you won't receive this numeric. Can also be sent when you
 * send TOPIC <channel>. 
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;

public class TopicReply extends IRCNumericEvent {    
    public TopicReply() {
    }
    
    /**
     * Returns the channel this topic is for.
     */
    public String getChannel() {
        return this.getNumericArg(0);
    }
    
    /**
     * Returns the topic for the channel.
     */
    public String getTopic() {
        return this.getNumericArg(1);
    }
    
    public boolean validate() {
        return this.numericArgumentCount() == 2;
    }
    
    public int getHandledNumeric() {
        return this.RPL_TOPIC;
    }
    
    public String toString() {
        return super.toString() + ", CHAN:" + this.getChannel() + ", TOPIC:" + this.getTopic();
    }
}
