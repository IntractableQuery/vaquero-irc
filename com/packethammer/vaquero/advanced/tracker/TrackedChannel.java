/**
 * Represents a tracked IRC channel.
 */

package com.packethammer.vaquero.advanced.tracker;

import com.packethammer.vaquero.util.datastore.DataStore;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import com.packethammer.vaquero.util.modes.channel.ChannelMode;

public class TrackedChannel implements Comparable {
    private String name;
    private Vector<UserChannelInformation> userInformation;
    private String topic;
    private Date lastTopicChangeTime;
    private String lastTopicChanger;
    private Date creationTime;
    private HashSet<ChannelMode> channelModes;
    private DataStore store;
    // TODO: not currently supported
    //private ArrayList<ListableModes> listableModes;
    private boolean tracked;
    
    
    /**
     * Creates a new instance of TrackedChannel
     */
    public TrackedChannel() {
        store = new DataStore();
        userInformation = new Vector();
        // instantiate in set-like mode to prevent duplicate modes (a precaution)
        channelModes = new HashSet();
        //listableModes = new ArrayList();
    }

    /**
     * Returns the name of this channel, including the channel type prefix.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this channel.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns information for the users in this channel.
     */
    public Vector<UserChannelInformation> getUserInformation() {
        return userInformation;
    }
    
    /**
     * Gets a user's information by their nickname. This is a case-insensitive
     * search, but network casemapping is NOT taken into effect. Read more
     * on this inside Tracker.
     *
     * @param nickname Their case-insensitive information.
     * @return User information, or null if there is no user by that nickname in this channel.
     * @see #vaquero.advanced.tracker.Tracker.getUser(String)
     */
    public UserChannelInformation getUserInformation(String nickname) {
        for(UserChannelInformation info : this.userInformation) {
            if(info.getUser().getHostmask().getNonNullNickname().equalsIgnoreCase(nickname))
                return info;
        }
        return null;
    }
    
    /**
     * Adds user information to this channel, essentially making the user a member
     * of this channel.
     *
     * @param info The information to add.
     */
    public void addUserInformation(UserChannelInformation info) {
        this.userInformation.add(info);
    }
    
    /**
     * Removes user information from this channel, removing the user from this
     * channel.
     */
    public void removeUserInformation(UserChannelInformation info) {
        this.userInformation.remove(info);
    }

    /**
     * Returns the topic if it is known.
     *
     * @return The topic, or null if none is set.
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Sets this channel's tracked topic.
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /** 
     * Returns the last time the topic was changed, or null if it is unknown.
     * 
     * @return Topic last change time or null if it is unknown/unset.
     */
    public Date getLastTopicChangeTime() {
        return lastTopicChangeTime;
    }

    /**
     * Sets this channel's tracked last topic change time.
     */
    public void setLastTopicChangeTime(Date lastTopicChangeTime) {
        this.lastTopicChangeTime = lastTopicChangeTime;
    }

    /**
     * Returns the name of the entity that last changed the topic. This can be
     * a user nickname, server name, etc.
     *
     * @return Nickname/server, etc. or null if it is unknown.
     */
    public String getLastTopicChanger() {
        return lastTopicChanger;
    }

    /**
     * Sets this channel's tracked last topic changer.
     */
    public void setLastTopicChanger(String lastTopicChanger) {
        this.lastTopicChanger = lastTopicChanger;
    }

    /**
     * Returns the channel's creation time on the server.
     *
     * @return Channel creation time or null if it unknown.
     */
    public Date getCreationTime() {
        return creationTime;
    }

    /**
     * Sets this channel's tracked creation time.
     */
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    /**
     * Returns only numeric 005 ISUPPORT CHANMODES types B, C, and D. This 
     * basically excludes listable modes (such as +b) and user prefix modes
     * such as +o. The idea is to only contain certain modes related directly 
     * to the channel which are always definite.
     */
    public Set<ChannelMode> getChannelModes() {
        return channelModes;
    }

    /*
     * Returns the listable modes for this channel. This would be numeric 005 
     * ISUPPORT CHANMODES type A. The most obvious example is +b.
     *
    public ArrayList<ListableModes> getListableModes() {
        return listableModes;
    }
    
    /**
     * This is a convenience method that returns the +b modes. Note that using
     * it on a network that doesn't support +b may result in null being
     * returned.
     *
     * @return The set of bans or null if the mode +b does not exist.
     *
    public ListableModes getBanList() {
        for(ListableModes lm : listableModes) {
            if(lm.getModeDefinition().getMode().equals(ChannelMode.MODE_BAN)) 
                return lm;
        }
        
        return null;
    }
     */
    
    /**
     * Determines if this channel is currently being tracked. If it is not being
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
     * Sets the tracking status of this channel.
     */
    public void setTracked(boolean tracked) {
        this.tracked = tracked;
    }
    
    /**
     * Returns the data store associated with this channel. The store is emptied
     * when the channel is no longer being tracked.
     */
    public DataStore getStore() {
        return store;
    }
    
    public void destroy() {
        this.store.destroy();
        this.store = null;
        this.userInformation = null;
    }
    
   /**
     * @see #compareTo(Object)
     */
    public boolean equals(Object o) {
        return compareTo(o) == 0;
    }
    
    /**
     * Comparison is based on the name of the channel. This does not take
     * casemapping into account, although ASCII letter casing does not matter
     * (this is case-insensitive).
     */
    public int compareTo(Object o) {
        if(o instanceof TrackedChannel) {
            return this.getName().toLowerCase().compareTo(((TrackedChannel) o).getName().toLowerCase());
        } else {
            throw new RuntimeException("Object not IRCChannel");
        }
    }
}
