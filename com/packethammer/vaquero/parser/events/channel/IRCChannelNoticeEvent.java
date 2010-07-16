/*
 * IRCChannelNoticeEvent.java
 *
 * Represents a channel notice, which is slightly more special than a regular
 * channel message or a private notice, in that it is entirely possible that 
 * only ops, voices, etc. are being sent the message instead of the general 
 * channel population. I've only found mention of this support in numeric 005 ISUPPORT documentation
 * (there are no formal RFCs, but a draft), although it is certainly
 * in use on various networks.
 *
 * Note: Please do not rely on the targeting of user prefixes for anything 
 * important. You can even believe it is a lie; ircu (at least in one configuration
 * I've witnessed) will ALWAYS use @#channel even when sending WALLVOICES.
 * Also, behavior between sending a NOTICE @#channel and WALLCHOPS is different.
 * Put simply, don't complain if weird stuff happens!
 * 
 *
 * http://www.irc.org/tech_docs/005.html (see STATUSMSG/WALLCHOPS)
 *
 */

package com.packethammer.vaquero.parser.events.channel;

import java.util.Date;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.interfaces.IRCChannelTargetedEventI;
import com.packethammer.vaquero.parser.tracking.definitions.ChannelNickPrefixModeDefinition;
import com.packethammer.vaquero.parser.events.basic.IRCNoticeEvent;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;

public class IRCChannelNoticeEvent extends IRCNoticeEvent implements IRCChannelTargetedEventI {
    private ChannelNickPrefixModeDefinition targetedUsers;
    
    public IRCChannelNoticeEvent() {
    }
    
    /**
     * Returns the channel that this event is occuring in. If there was a prefix
     * that indicated a mode type being targeted, it will be stripped off
     * (unlike getTarget()).
     * 
     * @return The channel.
     */
    public String getChannel() {
        if(this.targetsSpecificModeType()) {
            return this.getTarget().substring(1); // chop off the target
        } else {
            return this.getTarget();
        }
    }
    
    /**
     * Determines if this channel notice targets all users or just a specific type.
     *
     * NOTE: This may not behave the way you think it does. Please read the
     * class summary javadoc.
     *
     * @return True if this targets all users, false if it targets a certain type.
     */
    public boolean targetsAll() {
        return this.getTargetedUsers() == null;
    }
    
    /**
     * Determines if this channel notice targets a specific type of user or all of them.
     *
     * NOTE: This may not behave the way you think it does. Please read the
     * class summary javadoc.
     *
     * @return True if it targets a certain type of user, false if this targets all.
     */
    public boolean targetsSpecificModeType() {
        return !this.targetsAll();
    }

    /**
     * Returns the mode definition for the users which are being targeted
     * by this channel notice. If all users are being targeted, this will return null.
     *
     * NOTE: This may not behave the way you think it does. Please read the
     * class summary javadoc.
     *
     * @return The mode definition of targeted users, or false otherwise.
     */
    public ChannelNickPrefixModeDefinition getTargetedUsers() {
        return targetedUsers;
    }

    /**
     * Sets the targeted users mode definition. Set to null if no users are targeted.
     *
     * @param targetedUsers The user mode type targeted by this notice.
     */
    public void setTargetedUsers(ChannelNickPrefixModeDefinition targetedUsers) {
        this.targetedUsers = targetedUsers;
    }
    
    public String toString() {
        return "[" + this.getChannel() + " - targets:" + (this.targetsAll() ? "all" : this.getTargetedUsers().getMode()) + "] " + super.toString();
    }
}
