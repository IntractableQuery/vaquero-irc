/**
 * The task of the command helper is fairly simple; it just provides methods
 * for common operations (such as messaging users, channels, etc.) and 
 * automatically sends them using a command manager.
 *
 * This is slightly less painful than manually creating the new commands 
 * yourself, although you're still free to do so.
 */

package com.packethammer.vaquero.util;

import com.packethammer.vaquero.outbound.CommandManager;
import com.packethammer.vaquero.outbound.commands.basic.IRCMessageNickCommand;
import com.packethammer.vaquero.outbound.commands.basic.IRCNoticeNickCommand;
import com.packethammer.vaquero.outbound.commands.channel.ChannelKeyPair;
import com.packethammer.vaquero.outbound.commands.channel.IRCChannelMessageCommand;
import com.packethammer.vaquero.outbound.commands.channel.IRCChannelModeCommand;
import com.packethammer.vaquero.outbound.commands.channel.IRCChannelNoticeCommand;
import com.packethammer.vaquero.outbound.commands.channel.IRCInviteCommand;
import com.packethammer.vaquero.outbound.commands.channel.IRCJoinCommand;
import com.packethammer.vaquero.outbound.commands.channel.IRCKickCommand;
import com.packethammer.vaquero.outbound.commands.channel.IRCPartCommand;
import com.packethammer.vaquero.outbound.commands.channel.IRCTopicCommand;
import com.packethammer.vaquero.outbound.commands.server.IRCNickCommand;
import com.packethammer.vaquero.util.modes.Modes;
import com.packethammer.vaquero.util.modes.channel.ChanBanMode;
import com.packethammer.vaquero.util.modes.channel.ChanOperatorMode;
import com.packethammer.vaquero.util.modes.channel.ChanVoiceMode;
import com.packethammer.vaquero.util.modes.channel.ChannelMode;

public class CommandHelper {
    private CommandManager outbound;
    
    /**
     * Initializes this command helper with a command manager to use for 
     * sending commands.
     *
     * @param outbound The command manger to use. 
     */
    public CommandHelper(CommandManager outbound) {
        this.outbound = outbound;
    }
    
    /**
     * Sends a message to one or more channels.
     *
     * @param channels The channels to send the message to.
     * @param message The message to send.
     */
    public void msgChans(String[] channels, String message) {
        IRCChannelMessageCommand msg = new IRCChannelMessageCommand(message);
        for(String chan : channels)
            msg.addChannel(chan);
        outbound.sendCommand(msg);
    }
    
    /**
     * Sends a message to a channel.
     *
     * @param channel The channel to send the message to.
     * @param message The message to send.
     */
    public void msgChan(String channel, String message) {
        this.msgChans(new String[] { channel }, message);
    }
    
    /**
     * Sends a message to one or more nicknames.
     *
     * @param nicknames The nicknames to send the message to.
     * @param message The message to send.
     */
    public void msgNicks(String[] nicknames, String message) {
        IRCMessageNickCommand msg = new IRCMessageNickCommand(message);
        for(String nick : nicknames)
            msg.addNick(nick);
        outbound.sendCommand(msg);
    }
    
    /**
     * Sends a message to a nickname.
     *
     * @param nickname The nickname to send the message to.
     * @param message The message to send.
     */
    public void msgNick(String nickname, String message) {
        this.msgNicks(new String[] { nickname }, message);
    }
    
    /**
     * Sends a notice to one or more channels.
     *
     * @param channels The channels to notice.
     * @param message The notice message.
     */
    public void noticeChans(String[] channels, String message) {
        IRCChannelNoticeCommand notice = new IRCChannelNoticeCommand(message);
        for(String chan : channels) 
            notice.addChannel(chan);
        outbound.sendCommand(notice);            
    }
    
    /**
     * Sends a notice to a channel.
     *
     * @param channel The channel to notice.
     * @param message The notice message.
     */
    public void noticeChan(String channel, String message) {
       this.noticeChans(new String[] { channel }, message);          
    }
    
    /**
     * Sends a notice to one or more nicknames.
     *
     * @param nicknames The nicknames to notice.
     * @param message The notice message.
     */
    public void noticeNicks(String[] nicknames, String message) {
        IRCNoticeNickCommand notice = new IRCNoticeNickCommand(message);
        for(String nick : nicknames) 
            notice.addNick(nick);
        outbound.sendCommand(notice);            
    }
    
    /**
     * Sends a notice to a nickname.
     *
     * @param nickname The nickname to notice.
     * @param message The notice message.
     */
    public void noticeNick(String nickname, String message) {
       this.noticeNicks(new String[] { nickname }, message);          
    }
    
    /**
     * Joins one or more channels using channel-key pairs.
     *
     * @param channels The channel(s) to join.
     */
    public void join(ChannelKeyPair[] channels) {
        IRCJoinCommand join = new IRCJoinCommand();
        for(ChannelKeyPair chan : channels) {
            join.addChannel(chan.getChannel(), chan.getKey());
        }
        outbound.sendCommand(join);
    }
    
    /**
     * Joins one or more channels.
     *
     * @param channels The channel(s) to join.
     */
    public void join(String[] channels) {
        ChannelKeyPair[] chans = new ChannelKeyPair[channels.length];
        for(int x = 0; x < channels.length; x++) {
            chans[x] = new ChannelKeyPair(channels[x]);
        }
        this.join(chans);
    }
    
    /**
     * Joins a channel using the channel-key pair.
     *
     * @param channel The channel to join.
     */
    public void join(ChannelKeyPair channel) {
        this.join(new ChannelKeyPair[] { channel });
    }
    
    /**
     * Joins a channel.
     *
     * @param channel The channel to join.
     */
    public void join(String channel) {
        this.join(new String[] { channel });
    }
    
    /**
     * Joins a channel given the channel name and key.
     *
     * @param channel The channel to join.
     * @param key The key to use to join the channel.
     */
    public void join(String channel, String key) {
        this.join(new ChannelKeyPair[] { new ChannelKeyPair(channel, key) });
    }
    
    /**
     * Parts (leaves) one or more channels with a parting message.
     *
     * @param channels The channels to leave.
     * @param message The message to include in the part. Set to null if none.
     */
    public void part(String[] channels, String message) {
        IRCPartCommand part = new IRCPartCommand();
        part.setMessage(message);
        for(String chan : channels)
            part.addPart(chan);
        outbound.sendCommand(part);
    }
    
    /**
     * Parts (leaves) a channel with a parting message.
     *
     * @param channel The channel to leave.
     * @param message The message to include in the part. Set to null if none.
     */
    public void part(String channel, String message) {
        this.part(new String[] { channel }, message);
    }
    
    /**
     * Parts (leaves) one or more channels.
     *
     * @param channels The channels to leave.
     */
    public void part(String[] channels) {
        this.part(channels, null);
    }
    
    /**
     * Parts (leaves) a channel.
     *
     * @param channel The channel to leave.
     */
    public void part(String channel) {
        this.part(channel, null);
    }
    
    /**
     * Kicks a user from a channel with a reason.
     *
     * @param channel The channel to kick the user from.
     * @param nickname The user's nickname.
     * @param reason The reason to kick the user (set to null to use none).
     */
    public void kick(String channel, String nickname, String reason) {
        IRCKickCommand kick = new IRCKickCommand(channel, nickname, reason);
        outbound.sendCommand(kick);        
    }
    
    /**
     * Kicks a user from a channel without a reason.
     *
     * @param channel The channel to kick the user from.
     * @param nickname The user's nickname.
     */
    public void kick(String channel, String nickname) {
        this.kick(channel, nickname, null);  
    }
    
    /**
     * Changes the topic of a channel.
     *
     * @param channel The channel to change the topic of.
     * @param newTopic The new topic for the channel.
     */
    public void changeTopic(String channel, String newTopic) {
        IRCTopicCommand topic = new IRCTopicCommand(channel, newTopic);
        outbound.sendCommand(topic);
    }
    
    /**
     * Changes our nickname.
     *
     * @param newNick The new nickname to attempt to switch to.
     */
    public void changeNick(String newNickname) {
        IRCNickCommand nick = new IRCNickCommand(newNickname);
        outbound.sendCommand(nick);
    }
    
    /**
     * Invites a user by their nickname to a channel.
     *
     * @param nickname The user's nickname to invite.
     * @param channel The channel to invite the user to.
     */
    public void invite(String nickname, String channel) {
        IRCInviteCommand invite = new IRCInviteCommand(nickname, channel);
        outbound.sendCommand(invite);
    }
    
    /**
     * Changes the modes on a channel.
     *
     * @param channel The channel to apply the modes on.
     * @param modes The modes to change.
     */
    public void mode(String channel, Modes<ChannelMode> modes) {
        IRCChannelModeCommand mode = new IRCChannelModeCommand(channel, modes);
        outbound.sendCommand(mode);
    }
    
    /**
     * Changes a mode on a channel.
     *
     * @param channel The channel to apply the modes on.
     * @param mode The mode to set.
     */
    public void mode(String channel, ChannelMode mode) {
        Modes<ChannelMode> modes = new Modes();
        modes.addMode(mode);
        this.mode(channel, modes);
    }
    
    /**
     * Gives operator status to a nickname on a channel.
     *
     * @param channel The channel to set this mode in.
     * @param nickname The nickname to set this mode on.
     */
    public void op(String channel, String nickname) {
        this.mode(channel, new ChanOperatorMode(nickname, true));
    }
    
    /**
     * Takes operator status from a nickname on a channel.
     *
     * @param channel The channel to set this mode in.
     * @param nickname The nickname to set this mode on.
     */
    public void deop(String channel, String nickname) {
        this.mode(channel, new ChanOperatorMode(nickname, false));
    }
    
    /**
     * Gives voice status to a nickname on a channel.
     *
     * @param channel The channel to set this mode in.
     * @param nickname The nickname to set this mode on.
     */
    public void voice(String channel, String nickname) {
        this.mode(channel, new ChanVoiceMode(nickname, true));
    }
    
    /**
     * Takes voice status from a nickname on a channel.
     *
     * @param channel The channel to set this mode in.
     * @param nickname The nickname to set this mode on.
     */
    public void devoice(String channel, String nickname) {
        this.mode(channel, new ChanVoiceMode(nickname, false));
    }
    
    /**
     * Bans a given hostmask in a channel
     *
     * @param channel The channel to set this mode in.
     * @param hostmask The hostmask (possibly containing wildcards) to ban.
     */
    public void ban(String channel, Hostmask hostmask) {
        this.mode(channel, new ChanBanMode(hostmask, true));
    }
    
    /**
     * Unbans a given hostmask in a channel
     *
     * @param channel The channel to set this mode in.
     * @param hostmask The hostmask (possibly containing wildcards) to unban.
     */
    public void unban(String channel, Hostmask hostmask) {
        this.mode(channel, new ChanBanMode(hostmask, false));
    }
}
