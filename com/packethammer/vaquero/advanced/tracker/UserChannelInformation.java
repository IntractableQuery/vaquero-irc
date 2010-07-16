/**
 * This represents information pertaining to a user inside of a channel. It is
 * the special in-between place between a just a user and just a channel.
 */

package com.packethammer.vaquero.advanced.tracker;

import java.util.HashSet;
import java.util.Set;
import com.packethammer.vaquero.parser.tracking.definitions.ChannelNickPrefixModeDefinition;
import com.packethammer.vaquero.util.datastore.DataStore;

public class UserChannelInformation {
    private TrackedUser user;
    private TrackedChannel channel;
    private HashSet<ChannelNickPrefixModeDefinition> prefixModes;
    private HashSet<Character> witnessedModes;
    private DataStore store;
    private boolean channelPrefixModesDefinite;
    
    /** Creates a new instance of UserChannelInformation */
    public UserChannelInformation() {
        // instantiate the modes in set-like mode, so that we can't accidently give them two +o's or something
        store = new DataStore();
        prefixModes = new HashSet();
        witnessedModes = new HashSet();
    }

    /**
     * Returns the user that this information is associated with.
     */
    public TrackedUser getUser() {
        return user;
    }

    public void setUser(TrackedUser user) {
        this.user = user;
    }

    /**
     * Returns the channel that this information is associated with.
     */
    public TrackedChannel getChannel() {
        return channel;
    }

    public void setChannel(TrackedChannel channel) {
        this.channel = channel;
    }

    /**
     * Returns the prefixing modes this user has in the channel. Be aware that this may
     * not actually contain all modes the user has in most cases. For more 
     * information, see isModesDefinite().
     *
     * @see #isChannelPrefixModesDefinite()
     */
    public Set<ChannelNickPrefixModeDefinition> getPrefixModes() {
        return prefixModes;
    }
    
    /**
     * Calling this method removes this information from the user and channel it
     * is associated with, effectively removing a user from that channel.
     */
    public void destroy() {
        this.store.destroy();
        this.store = null;
        this.user = null;
        this.channel = null;
        this.prefixModes = null;
    }
    
    /**
     * Returns the data store associated with this user-channel information. 
     * The store is emptied when the user in the channel is no longer being
     * tracked.
     */
    public DataStore getStore() {
        return store;
    }
    
    /**
     * Determines if the user-prefix channel modes (ie: +o, +v, etc.) are 
     * fully known. The typical case where they are unknown/indefinite is 
     * when we first join a channel and only get a NAMES list to work with.
     * Someone can have +ov, but we'd only see +o (since we'd see @nickname).
     * This is true by default if we've tracked this user from the time they
     * first joined the channel.
     *
     * If you are concerned with making sure you know all of the user-prefix
     * modes for sure, see if the network you are on supports WHOX and that
     * you are performing an auto-WHO every time you join a new channel. If so,
     * the tracker should automatically determine all of the modes using WHOX,
     * although limits on WHO replies can still make it difficult to do this
     * on channels with large user populations.
     *
     * @return True if we know all the user prefix modes, or false if there may be more.
     */
    public boolean isChannelPrefixModesDefinite() {
        return channelPrefixModesDefinite;
    }

    public void setChannelPrefixModesDefinite(boolean channelPrefixModesDefinite) {
        this.channelPrefixModesDefinite = channelPrefixModesDefinite;
    }

    /**
     * Returns a set of the mode characters we've seen this user receive by
     * MODES or some other means. This helps the tracker in determining if
     * we may already know all the user prefix modes this user has.
     */
    public HashSet<Character> getWitnessedModes() {
        return witnessedModes;
    }
}
