/*
 * Occurs when we are invited to a channel. Note that only the person who 
 * is being invited to a channel gets this event; that means that every time
 * we see it, it is ONLY for us. The server may generate numerics for other
 * users that inform them of our being invited to a given channel, but they
 * don't get this same event!
 *
 * ARGS: INVITE <NICK> <CHANNEL>
 */

package com.packethammer.vaquero.parser.events.personal;

import java.util.Date;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.interfaces.IRCChannelTargetedEventI;
import com.packethammer.vaquero.parser.events.interfaces.IRCNicknameTargetedEventI;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;

public class PersonalChannelInvite extends IRCEvent implements IRCChannelTargetedEventI,IRCNicknameTargetedEventI{
    public PersonalChannelInvite() {
    }
    
    /**
     * Returns the channel we are being invited to.
     */
    public String getChannel() {
        return this.getArg(2);
    }
    
    /**
     * Returns the nicknamed being invited to the channel (this is basically
     * always us!)
     *
     * @return Our nickname.
     */
    public String getTarget() {
        return this.getSecondArgument();
    }
    
    public String toString() {
        return this.getSource().getSimpleRepresentation() + " invites you to join " + this.getChannel();
    }
 }
