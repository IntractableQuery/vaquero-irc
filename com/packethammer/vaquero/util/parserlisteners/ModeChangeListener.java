/**
 * Defines a class that listens for user/channel mode change events created by
 * ModeChangeHandler.
 *
 * @see vaquero.util.parserlisteners.ModeChangeHandler
 */

package com.packethammer.vaquero.util.parserlisteners;

import com.packethammer.vaquero.util.Hostmask;
import com.packethammer.vaquero.util.modes.channel.ChannelMode;
import com.packethammer.vaquero.util.modes.user.UserMode;

public abstract class ModeChangeListener {
    /**
     * Occurs when a user is given mode 'o' (channel operator status).
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     * @param nickname The user this mode is operating on.
     */
    public void onOp(String channel, Hostmask setter, String nickname) {
        
    }
    
    /**
     * Occurs when a user loses mode 'o' (channel operator status).
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     * @param nickname The user this mode is operating on.
     */
    public void onDeop(String channel, Hostmask setter, String nickname) {
        
    }
    
    /**
     * Occurs when a user is given mode 'v' (channel voice status).
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     * @param nickname The user this mode is operating on.
     */
    public void onVoice(String channel, Hostmask setter, String nickname) {
        
    }
    
    /**
     * Occurs when a user loses mode 'v' (channel voice status).
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     * @param nickname The user this mode is operating on.
     */
    public void onDevoice(String channel, Hostmask setter, String nickname) {
        
    }
    
    /**
     * Occurs when a user is given mode 'h' (channel half-operator status).
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     * @param nickname The user this mode is operating on.
     */
    public void onHalfop(String channel, Hostmask setter, String nickname) {
        
    }
    
    /**
     * Occurs when a user loses mode 'h' (channel half-operator status).
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     * @param nickname The user this mode is operating on.
     */
    public void onDehalfop(String channel, Hostmask setter, String nickname) {
        
    }
       
    /**
     * Occurs when a channel is given mode 'm' (moderated status).
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     */
    public void onChannelSetModerated(String channel, Hostmask setter) {
        
    }
    
    /**
     * Occurs when a channel loses mode 'm' (moderated status).
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     */
    public void onChannelUnsetModerated(String channel, Hostmask setter) {
        
    }
    
    /**
     * Occurs when a channel is given mode 'p' (private status).
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     */
    public void onChannelSetPrivate(String channel, Hostmask setter) {
        
    }
    
    /**
     * Occurs when a channel loses mode 'p' (private status).
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     */
    public void onChannelUnsetPrivate(String channel, Hostmask setter) {
        
    }
    
    /**
     * Occurs when a channel is given mode 's' (secret status).
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     */
    public void onChannelSetSecret(String channel, Hostmask setter) {
        
    }
    
    /**
     * Occurs when a channel loses mode 's' (secret status).
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     */
    public void onChannelUnsetSecret(String channel, Hostmask setter) {
        
    }
    
    /**
     * Occurs when a channel is given mode 'i' (invite-only status).
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     */
    public void onChannelSetInviteOnly(String channel, Hostmask setter) {
        
    }
    
    /**
     * Occurs when a channel loses mode 'i' (invite-only status).
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     */
    public void onChannelUnsetInviteOnly(String channel, Hostmask setter) {
        
    }
    
    /**
     * Occurs when a channel is given mode 't' (topic only settable by operators status).
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     */
    public void onChannelSetTopicOps(String channel, Hostmask setter) {
        
    }
    
    /**
     * Occurs when a channel loses mode 't' (topic only settable by operators status).
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     */
    public void onChannelUnsetTopicOps(String channel, Hostmask setter) {
        
    }
    
    /**
     * Occurs when a channel is given mode 'n' (no-outside-message status).
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     */
    public void onChannelSetNoOutsideMessages(String channel, Hostmask setter) {
        
    }
    
    /**
     * Occurs when a channel loses mode 'n' (no-outside-message status).
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     */
    public void onChannelUnsetNoOutsideMessages(String channel, Hostmask setter) {
        
    }
    
    
    /**
     * Occurs when a channel is given mode 'l' (channel capacity limit).
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     * @param limit The user limit for the channel.
     */
    public void onChannelSetLimit(String channel, Hostmask setter, int limit) {
        
    }
    
    /**
     * Occurs when a channel loses mode 'l' (channel capacity limit).
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     */
    public void onChannelUnsetLimit(String channel, Hostmask setter) {
        
    }
        
    /**
     * Occurs when a channel is given mode 'k' (channel passkey).
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     * @param key The passkey set.
     */
    public void onChannelSetKey(String channel, Hostmask setter, String key) {
        
    }
    
    /**
     * Occurs when a channel loses mode 'k' (channel passkey).
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     * @param key The passkey removed.
     */
    public void onChannelUnsetKey(String channel, Hostmask setter, String key) {
        
    }
    
    
    /**
     * Occurs when a ban takes place in a channel.
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     * @param hostmask The ban hostmask being set.
     */
    public void onBan(String channel, Hostmask setter, String hostmask) {
        
    }
    
    /**
     * Occurs when a ban is removed from a chananel.
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     * @param hostmask The ban hostmask being removed.
     */
    public void onUnban(String channel, Hostmask setter, String hostmask) {
        
    }
    
    /**
     * Occurs when your user client receives mode 'i' (invisible status).
     */
    public void onSetInvisible() {
        
    }
    
    /**
     * Occurs when your user client receives mode 's' (server notices receival status).
     */
    public void onSetServerNotices() {
        
    }
    
    /**
     * Occurs when your user client receives mode 'w' (wallops receival status).
     */
    public void onSetWallops() {
        
    }
    
    /**
     * Occurs when your user client receives mode 'o' (server operator status).
     */
    public void onSetServerOperator() {
        
    }
    
    /**
     * Occurs when your user client receives mode 'x' (real-host cloaked status).
     *
     * This mode is used across a variety of IRCds to indicate your real hostname
     * (address) is now hidden from the general public view.
     */
    public void onSetHostnameCloaked() {
        
    }
    
    /** 
     * Occurs when a channel mode is set (added to a channel).
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     * @param mode The mode being added.
     */
    public void onChannelModeAdded(String channel, Hostmask setter, ChannelMode mode) {
        
    }
    
    /** 
     * Occurs when a channel mode is unset (being removed from a channel).
     *
     * @param channel The channel this mode is taking place in.
     * @param setter The hostmask of the user changing this mode.
     * @param mode The mode being removed.
     */
    public void onChannelModeRemoved(String channel, Hostmask setter, ChannelMode mode) {
        
    }
    
    /** 
     * Occurs when a user mode is set on us (added to us).
     *
     * @param mode The mode being added.
     */
    public void onUserModeAdded(UserMode mode) {
        
    }
    
    /** 
     * Occurs when a user mode being removed from us (this is fairly rare).
     *
     * @param mode The mode being removed.
     */
    public void onUserModeRemoved(UserMode mode) {
        
    }
}
