/*
 * This mode prohibits users other than operators from modifying the channel
 * topic. 
 *
 * Derived from rfc1459.
 */

package com.packethammer.vaquero.util.modes.channel;

public class ChanTopicLockMode extends ChannelMode {    
    /**
     * Initializes this channel topic lock mode to determine if it is being
     * given or removed from a channel.
     *
     * @param beingAdded True if this mode should be given, or false if it should be removed.
     */
    public ChanTopicLockMode(boolean beingAdded) {
        super(MODE_TOPICLOCK, beingAdded);
    }
}