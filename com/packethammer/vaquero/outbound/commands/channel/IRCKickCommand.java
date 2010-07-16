/*
 * Kicks a user from a channel with an optional kick message.
 *
 * Note: There is an option in RFC1459 and a full expansion to this command in 
 * RFC2812 which seems to allow for multiple nicknames and channels to be 
 * specified in the kick, but I do not see any servers thus far that even 
 * support it. For now, it seems a better idea to omit such support, as it can 
 * cause some confusion with the traditional usage of the kick command.
 *
 * KICK <CHANNEL> <NICKNAME> [REASON]
 */

package com.packethammer.vaquero.outbound.commands.channel;

import java.util.ArrayList;
import java.util.Collection;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.outbound.commands.IRCCommand;
import com.packethammer.vaquero.outbound.commands.interfaces.AdjustableNicknameTargetCommandI;
import com.packethammer.vaquero.outbound.commands.interfaces.ChannelsTargetedCommandI;
import com.packethammer.vaquero.outbound.commands.interfaces.ExtendedMessageCommandI;
import com.packethammer.vaquero.outbound.commands.interfaces.NicknamesTargetedCommandI;
import java.util.List;

public class IRCKickCommand extends IRCCommand implements ChannelsTargetedCommandI,NicknamesTargetedCommandI,ExtendedMessageCommandI,AdjustableNicknameTargetCommandI {
    private String channel;
    private String nickname;
    private String reason;
    
    /**
     * Initializes this kick command with the channel, nickname, and reason
     * for the kick.
     *
     * @param channel The channel to kick the nickname from.
     * @param nickname The nickname to kick.
     * @param reason The reason for the kick (may be null if you wish to use none).
     */
    public IRCKickCommand(String channel, String nickname, String reason) {
        this.setChannel(channel);
        this.setNickname(nickname);
        this.setMessage(reason);
    }
    
    /**
     * Initializes this kick command with the channel to kick the given 
     * nickname from.
     *
     * @param channel The channel to kick the nickname from.
     * @param nickname The nickname to kick.
     */
    public IRCKickCommand(String channel, String nickname) {
        this(channel, nickname, null);
    }
    
    public IRCKickCommand() {
    }
    
    /**
     * Sets the channel we are to kick in.
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }
    
    /**
     * Sets the nickname of the person we are to kick.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public void setNicknameTargets(List<String> targets) {
        if(!targets.isEmpty()) 
            this.nickname = targets.get(0);
    }
    
    /**
     * Sets the reason we are to use for the kick (set to null if no reason 
     * is to be provided).
     */
    public void setMessage(String reason) {
        this.reason = reason;
    }
    
    /**
     * Returns the kick reason being used. May be null if none is to be given.
     */
    public String getMessage() {
        return reason;
    }
    
    /**
     * Returns the nickname of the user we are kicking.     
     */    
    public Collection<String> getNicknames() {
        ArrayList<String> a = new ArrayList();
        a.add(nickname);
        return a;
    }
    
    /**
     * Returns the channel we are kicking the user from.
     */    
    public Collection<String> getChannels() {
        ArrayList<String> a = new ArrayList();
        a.add(channel);
        return a;
    }
    
    public IRCRawLine renderForIRC() {
        return IRCRawLine.buildRawLine(this.getMessage() != null, "KICK", channel, nickname, this.getMessage());
    }    
   
    public boolean isSendable() {
        return this.channel != null && this.nickname != null;
    }
}
