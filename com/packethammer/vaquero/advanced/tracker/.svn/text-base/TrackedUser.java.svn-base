/**
 * Represents a tracked IRC user.
 */

package com.packethammer.vaquero.advanced.tracker;

import java.util.List;
import java.util.Vector;
import com.packethammer.vaquero.util.Hostmask;
import com.packethammer.vaquero.util.datastore.DataStore;

public class TrackedUser implements Comparable {
    private Hostmask hostmask;
    private String realname;
    private boolean tracked;
    private Boolean serverOperator;
    private Boolean away;
    private Vector<UserChannelInformation> channelInformation;
    private Vector<NicknameHistory> nicknameHistory; // newest nickname is at index 0
    private DataStore store;
    
    /**
     * Creates a new instance of TrackedUser
     */
    public TrackedUser() {
        channelInformation = new Vector();
        hostmask = new Hostmask();
        store = new DataStore();
        nicknameHistory = new Vector();
    }

    /**
     * Returns this user's hostmask. Unknown components of it will be null.
     *
     * @return Non-null hostmask that may have null components.
     */
    public Hostmask getHostmask() {
        return hostmask;
    }

    /**
     * Sets this user's hostmask.
     */
    public void setHostmask(Hostmask hostmask) {
        this.hostmask = hostmask;
    }

    /**
     * Determines if this user is currently being tracked. If it is not being
     * tracked, the information within is no longer reliable and is likely
     * already cleared out. Do not access the object once it is no longer
     * tracked.
     *
     * @return True if tracked, false otherwise.
     */
    public boolean isTracked() {
        return tracked;
    }

    /**
     * Sets the tracking status of this user.
     */
    public void setTracked(boolean tracked) {
        this.tracked = tracked;
    }

    /**
     * Returns the channel status information for each channel this user is 
     * known to be in.
     *
     * @return List of channels this user in known to be in.
     */
    public List<UserChannelInformation> getChannelInformation() {
        return (List) this.channelInformation.clone();
    }
    
    /**
     * Gets a channel's information by its name. This is a case-insensitive
     * search, but network casemapping is NOT taken into effect. Read more
     * on this inside Tracker.
     *
     * @param channel Their case-insensitive channel name (including channel type prefix).
     * @return Channel information, or null if there is no channel by that name that we see this user in.
     * @see #vaquero.advanced.tracker.Tracker.getUser(String)
     */
    public UserChannelInformation getChannelInformation(String channel) {
        for(UserChannelInformation info : this.channelInformation) {
            if(info.getChannel().getName().equalsIgnoreCase(channel))
                return info;
        }
        return null;
    }
    
    /**
     * Adds channel status information, implying we are in that channel.
     *
     * @param info The channel information to add.
     */
    public void addChannelInformation(UserChannelInformation info) {
        this.channelInformation.add(info);
    }
    
    /**
     * Removes our channel information for some channel using equals()
     */
    public void removeChannelInformation(UserChannelInformation info) {
        this.channelInformation.remove(info);
    }
    

    /**
     * Returns this user's realname (infotext) or null if it is not yet known.
     *
     * @return User's realname or null if unknown at this time.
     */
    public String getRealname() {
        return realname;
    }

    /**
     * Sets this user's realname.
     */
    public void setRealname(String realname) {
        this.realname = realname;
    }

    /**
     * Returns the data store associated with this user. The store is emptied
     * when the user is no longer being tracked.
     */
    public DataStore getStore() {
        return store;
    }
    
    /**
     * Returns a reference to this user's nickname history. The lowest-index
     * element(s) refer to the most recent nicknames, with the topmost being
     * the current nickname.
     *
     * The length of time that a user's nickname history is kept is controlled
     * in the tracker's settings. It basically guarantees that nicknames that
     * existed in the time period defined in the tracker's settings will be
     * in this history list.
     */
    public Vector<NicknameHistory> getNicknameHistory() {
        return nicknameHistory;
    }
    
    /**
     * Clears this object of as many references as possible and performs general
     * clean-up. Garbage collection of this object is soon expected.
     */
    public void destroy() {
        channelInformation = null;
        nicknameHistory = null;
        hostmask = null;
        realname = null;
        store.destroy();
        store = null;
        tracked = false; // just in case...
    }
    
    /**
     * @see #compareTo(Object)
     */
    public boolean equals(Object o) {
        return compareTo(o) == 0;
    }
    
    /**
     * Comparison is based on the hostmasks of the users. This does not take
     * casemapping into account.
     */
    public int compareTo(Object o) {
        if(o instanceof TrackedUser) {
            return this.getHostmask().compareTo(((TrackedUser) o).getHostmask());
        } else {
            throw new RuntimeException("Object not IRCUser");
        }
    }

    /**
     * Determines if this user is marked as a server operator. Returns null
     * if we don't know yet.
     */
    public Boolean isServerOperator() {
        return serverOperator;
    }

    /**
     * @see #isServerOperator()
     */ 
    public void setServerOperator(Boolean serverOperator) {
        this.serverOperator = serverOperator;
    }

    /**
     * Determines if this user is currently marked as away. Note that this
     * information can change at any time on the server, so it isn't too
     * reliable unless you force it to update in some way. Returns null if
     * we don't know yet.
     */
    public Boolean isAway() {
        return away;
    }

    /**
     * @see #isAway()
     */
    public void setAway(Boolean away) {
        this.away = away;
    }
}
