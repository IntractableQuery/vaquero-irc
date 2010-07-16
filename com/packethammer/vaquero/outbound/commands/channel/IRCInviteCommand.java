/*
 * Using this command, you can invite someone to join a channel. It typically
 * requires that you have operator status or the equivalent.
 *
 * INVITE <NICKNAME> <CHANNEL>
 */

package com.packethammer.vaquero.outbound.commands.channel;

import java.util.ArrayList;
import java.util.Collection;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.outbound.commands.IRCCommand;
import com.packethammer.vaquero.outbound.commands.interfaces.AdjustableNicknameTargetCommandI;
import com.packethammer.vaquero.outbound.commands.interfaces.ChannelsTargetedCommandI;
import com.packethammer.vaquero.outbound.commands.interfaces.NicknamesTargetedCommandI;
import java.util.List;

public class IRCInviteCommand extends IRCCommand implements ChannelsTargetedCommandI,NicknamesTargetedCommandI,AdjustableNicknameTargetCommandI {
    private String channel;
    private String invitedNickname;
    
    /**
     * Instantiates this INVITE command with the user to invite to the given 
     * channel.
     *
     * @param channel The channel to invite the user to.
     * @param nickname The user to invite to the channel.
     */
    public IRCInviteCommand(String channel, String nickname) {
        this();
        this.setChannel(channel);
        this.setInvitedNickname(nickname);
    }
    
    public IRCInviteCommand() {        
    }
    
    /** 
     * Sets the nickname of the user we are inviting to the channel.
     */
    public void setInvitedNickname(String nickname) {
        this.invitedNickname = nickname;
    }
    
    /**
     * Sets the channel we are inviting the user to.
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }
    
    /**
     * Returns the nickname of the user we are inviting.     
     */    
    public Collection<String> getNicknames() {
        ArrayList<String> a = new ArrayList();
        a.add(invitedNickname);
        return a;
    }
    
    /**
     * Returns the channel we are inviting the user to.     
     */    
    public Collection<String> getChannels() {
        ArrayList<String> a = new ArrayList();
        a.add(channel);
        return a;
    }
    
    public void setNicknameTargets(List<String> targets) {
        if(!targets.isEmpty())
            this.invitedNickname = targets.get(0);
    }
    
    public IRCRawLine renderForIRC() {
        return IRCRawLine.buildRawLine(false, "INVITE", invitedNickname, channel);
    }    
   
    public boolean isSendable() {
        return this.channel != null && this.invitedNickname != null;
    }
}
