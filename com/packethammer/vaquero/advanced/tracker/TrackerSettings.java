/**
 * This class is used by the tracker to determine how to behave. 
 *
 * After you configure this and pass it to the tracker, you should not modify
 * its settings.
 */

package com.packethammer.vaquero.advanced.tracker;

import com.packethammer.vaquero.outbound.commands.extended.ircu.WhoXSearchOptions;

public class TrackerSettings {
    private boolean fullHostmaskTrackingEnabled;
    private boolean ircuAccountTrackingEnabled;
    private boolean whoXEnabled;
    private boolean whoXForced;
    private boolean automaticWhoQueryingEnabled;
    private boolean automaticJoinWhoQueryingEnabled;
    private boolean automaticJoinModeListingEnabled;
    private WhoXSearchOptions whoXSearchOptions;
    private int nicknameTrackingHistoryTime;
    
    /** Creates a new instance of TrackerSettings with default settings */
    public TrackerSettings() {
        setWhoXSearchOptions(new WhoXSearchOptions(new char[] {}, new char[] {
            WhoXSearchOptions.SEARCHFIELD_NICK,
            WhoXSearchOptions.SEARCHFIELD_IDENT,
            WhoXSearchOptions.SEARCHFIELD_HOSTNAME,
            WhoXSearchOptions.SEARCHFIELD_REALNAME,
            WhoXSearchOptions.SEARCHFIELD_ACCOUNTNAME,
            WhoXSearchOptions.SEARCHFIELD_INFOFLAGS,
            WhoXSearchOptions.SEARCHFIELD_CHANNEL
        }));
        
        this.setAutomaticJoinModeListingEnabled(true);
        this.setNicknameTrackingHistoryTime(10);
        this.setWhoXEnabled(true);
        this.setAutomaticWhoQueryingEnabled(true);
    }

    public boolean isFullHostmaskTrackingEnabled() {
        return fullHostmaskTrackingEnabled;
    }

    /**
     * When in full hostmask tracking mode, the tracker will hook all channel
     * events that could contain a user's current hostmask and it will make sure
     * that that user's IRCUser tracking object has the lastest hostmask. 
     *
     * The upside is that if the server is screwy and lets a user's hostmask
     * (most commonly, the host part) change without trying to clue us in, we'll
     * know as soon as the user does something else. Also, if we aren't or haven't
     * received a WHO reply yet, we'll still be able to store a user's hostmask
     * before officially receiving it.
     *
     * The downside is that we hook a lot of events. However, this isn't really
     * an issue unless you are dealing with an ungodly amount of clients that
     * tend to do a lot of stuff at once.
     */
    public void setFullHostmaskTrackingEnabled(boolean fullHostmaskTrackingEnabled) {
        this.fullHostmaskTrackingEnabled = fullHostmaskTrackingEnabled;
    }

    public boolean isIrcuAccountTrackingEnabled() {
        return ircuAccountTrackingEnabled;
    }

    /**
     * This requires that WHOX be supported on the server and that we are allowing
     * it to be used. WHOX allows us to extract the "account name" of a user,
     * which is typically tied to that network's IRC services. When enabled,
     * it will add a key called "IRCU.ACCT" to the user's data store once
     * the user's account name has been discovered.
     */
    public void setIrcuAccountTrackingEnabled(boolean ircuAccountTrackingEnabled) {
        this.ircuAccountTrackingEnabled = ircuAccountTrackingEnabled;
    }

    public boolean isWhoXEnabled() {
        return whoXEnabled;
    }

    /**
     * This allows us to specify whether we wish WHOX to be used. Note that the server
     * must explicitly express support for it in its ISUPPORT information, or
     * it won't be used to begin with.
     */
    public void setWhoXEnabled(boolean whoXEnabled) {
        this.whoXEnabled = whoXEnabled;
    }

    public boolean isWhoXForced() {
        return whoXForced;
    }

    /**
     * This will force the usage of WHOX. It is useful for servers
     * that support WHOX, but do not declare so in their ISUPPORT information.
     * Do not enable it on servers that don't support WHOX.
     */
    public void setWhoXForced(boolean whoXForced) {
        this.whoXForced = whoXForced;
    }

    public boolean isAutomaticWhoQueryingEnabled() {
        return automaticWhoQueryingEnabled;
    }

    /**
     * When enabled, we will automatically perform a WHO request on a channel 
     * when we join it. This allows us to quickly gather the hostmask information
     * for most, if not all of the users in the channel.
     *
     * Please note possible ramifications in assuming that this will guarantee
     * us the hostmasks (and in the case of WHOX being supported, even more
     * information) of every user in the channel. Some servers may limit you
     * to, say, 200 WHO replies, but there may be 250 users in a channel. In
     * this case, you'd be missing hostmasks or other vital information
     * for 50 users.
     *
     * TODO: Consider adding a more advanced WHO query generator to "trick" the
     * server into giving us every user in the channel by using broad 
     * search masks that yield users in the target channel. For now, you can
     * emulate this behavior yourself since the tracker still passively watches
     * for WHO replies.
     */
    public void setAutomaticWhoQueryingEnabled(boolean automaticWhoQueryingEnabled) {
        this.automaticWhoQueryingEnabled = automaticWhoQueryingEnabled;
    }

    public boolean isAutomaticJoinWhoQueryingEnabled() {
        return automaticJoinWhoQueryingEnabled;
    }

    /**
     * When enabled, we will automatically send a WHO request when a user
     * joins a channel we are in (tracking). This is rarely useful and
     * generally serves no advantage except in situations such as where
     * the server supports WHOX (to derive account names) and we want to
     * know a user's account name as soon as we come in contact with them.
     *
     * Note that this could generate a lot of outbound commands if we are
     * in high-traffic channels!
     */
    public void setAutomaticJoinWhoQueryingEnabled(boolean automaticJoinWhoQueryingEnabled) {
        this.automaticJoinWhoQueryingEnabled = automaticJoinWhoQueryingEnabled;
    }

    public WhoXSearchOptions getWhoXSearchOptions() {
        return whoXSearchOptions;
    }

    /**
     * Sets the WHOX search options to use when sending WHOX queries and 
     * handling their replies. Modifying this is highly discouraged.
     *
     * To ensure that the tracker can still make the most use of the query
     * as possible, please leave nickname, ident, hostname, realname, and 
     * infoflags. The tracker automatically adds in the accountname field
     * flag if ircu user tracking is explicitly enabled.
     */
    public void setWhoXSearchOptions(WhoXSearchOptions whoXSearchOptions) {
        this.whoXSearchOptions = whoXSearchOptions;
    }

    public int getNicknameTrackingHistoryTime() {
        return nicknameTrackingHistoryTime;
    }

    /**
     * Sets the time (in minutes) we should track nicknames for. Set to -1
     * if you wish to track nicknames forever (this can consume a lot of memory
     * over time!). 
     *
     * Be aware that certain mechanisms of vaquero rely on the nickname history
     * tracking time to properly perform some operations. Put simply, try to 
     * keep this value over the maximum amount of time a command can sit in 
     * the outbound queue before it is sent, unless you want trouble!
     *
     * Default is 10 minutes.
     */
    public void setNicknameTrackingHistoryTime(int nicknameTrackingHistoryTime) {
        this.nicknameTrackingHistoryTime = nicknameTrackingHistoryTime;
    }

    public boolean isAutomaticJoinModeListingEnabled() {
        return automaticJoinModeListingEnabled;
    }

    /**
     * Determines if we should send a MODE command for each listable mode type
     * on the server to retrieve the entries for that mode. The most obvious
     * example of this in use is listing mode 'b', which is essentially
     * retrieval of the ban list. Turn this on if you want to make sure that
     * you know the bans list or other listable mode type once you join a 
     * channel.
     */
    public void setAutomaticJoinModeListingEnabled(boolean automaticJoinModeListingEnabled) {
        this.automaticJoinModeListingEnabled = automaticJoinModeListingEnabled;
    }

    public String toString() {
        return "WHOXSEARCH:" + this.getWhoXSearchOptions().renderSearchOptions()
         + ", NICKHIST:" + this.getNicknameTrackingHistoryTime()
         + ", JOINMODELISTING:" + this.isAutomaticJoinModeListingEnabled()
         + ", ONJOINWHOQUERYING:" + this.isAutomaticJoinWhoQueryingEnabled()
         + ", AUTOWHOCHANNELONJOIN:" + this.isAutomaticWhoQueryingEnabled()
         + ", FULLHOSTMASK:"  + this.isFullHostmaskTrackingEnabled()
         + ", IRCUACCOUNTTRACKING:" + this.isIrcuAccountTrackingEnabled()
         + ", WHOXENABLED:" + this.isWhoXEnabled()
         + ", WHOXFORCED:" + this.isWhoXForced();
    }
    
}
