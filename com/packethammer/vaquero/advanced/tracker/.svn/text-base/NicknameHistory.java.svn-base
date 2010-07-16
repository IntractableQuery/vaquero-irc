/**
 * This class contains information for a nickname that began usage at
 * a specific time period and ended usage at a likewise specific time period 
 * (or, it may not have ended yet). 
 */

package com.packethammer.vaquero.advanced.tracker;

import java.util.Date;

public class NicknameHistory {
    private Date usageStartTime;
    private Date usageStopTime;
    private String nickname;
    private TrackedUser user;
    
    /**
     * Initializes this nickname history with the usage start time, nickname
     * in use, and user it is tied to. 
     */
    public NicknameHistory(Date usageStartTime, String nickname, TrackedUser user) {
        this.usageStartTime = usageStartTime;
        this.nickname = nickname;
        this.user = user;
    }

    /**
     * Returns the time that this nickname came into use.
     */
    public Date getUsageStartTime() {
        return usageStartTime;
    }

    /**
     * @see #getUsageStartTime()
     */
    public void setUsageTime(Date usageStartTime) {
        this.usageStartTime = usageStartTime;
    }

    /**
     * Returns the nickname being used in this time period.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @see #getNickname()
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Returns the tracked IRC user that this nickname history is for.
     */
    public TrackedUser getUser() {
        return user;
    }

    /**
     * @see #getUser()
     */
    public void setUser(TrackedUser user) {
        this.user = user;
    }

    /**
     * Returns the time that this nickname was no longer in use at. Will be
     * null if this is the most current nickname, and has not ended usage
     * yet.
     */
    public Date getUsageStopTime() {
        return usageStopTime;
    }

    /**
     * @see #getUsageStopTime()
     */
    public void setUsageStopTime(Date usageStopTime) {
        this.usageStopTime = usageStopTime;
    }
    
    /**
     * Returns true if this nickname is still in use, which implies that
     * usageStopTime is null.
     */
    public boolean isStillInUse() {
        return this.getUsageStopTime() == null;
    }
    
    public String toString() {
        return "NICKNAME:" + this.getNickname() + " (" + this.getUsageStartTime() + " -> " + this.getUsageStopTime() + ")";
    }    
}
