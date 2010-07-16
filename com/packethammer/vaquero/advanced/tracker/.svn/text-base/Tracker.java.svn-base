/**
 * The advanced tracker is designed to give you a lot of control over user 
 * and channel information. It's quite simple: everything the client can 
 * currently "see" (users, channels, etc.) is recorded here. This can consume
 * quite a bit more memory than the basic parsing system, so it's labled as
 * "advanced." Memory-saving options are available, but beware of using
 * this on particularly large channels.
 *
 * To operate, the tracker hooks events from a regular IRCParser and uses this
 * information to update its internal state. It is important that the tracker
 * be allowed to hook events before anything else hooks them from the irc parser.
 * Otherwise, you may end up trying to use the tracker after some IRCParser event
 * without the tracker having yet properly updated its internal tracking state!
 *
 * The tracker requires an instance of the advanced dispatch command manager 
 * so that it can perform query-oriented operations such as requesting
 * WHO lists, etc.
 */

package com.packethammer.vaquero.advanced.tracker;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import com.packethammer.vaquero.advanced.dispatcher.Dispatcher;
import com.packethammer.vaquero.advanced.dispatcher.querying.WhoXQueryListener;
import com.packethammer.vaquero.outbound.CommandManager;
import com.packethammer.vaquero.outbound.commands.basic.IRCWhoCommand;
import com.packethammer.vaquero.outbound.commands.channel.IRCChannelModeCommand;
import com.packethammer.vaquero.outbound.commands.extended.ircu.IRCWhoXCommand;
import com.packethammer.vaquero.outbound.commands.extended.ircu.WhoXSearchOptions;
import com.packethammer.vaquero.parser.IRCEventListener;
import com.packethammer.vaquero.parser.IRCParser;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.basic.IRCNickChangeEvent;
import com.packethammer.vaquero.parser.events.basic.IRCQuitEvent;
import com.packethammer.vaquero.parser.events.channel.IRCChannelActionEvent;
import com.packethammer.vaquero.parser.events.channel.IRCChannelCTCPEvent;
import com.packethammer.vaquero.parser.events.channel.IRCChannelMessageEvent;
import com.packethammer.vaquero.parser.events.channel.IRCChannelModeChangeEvent;
import com.packethammer.vaquero.parser.events.channel.IRCChannelNoticeEvent;
import com.packethammer.vaquero.parser.events.channel.IRCJoinEvent;
import com.packethammer.vaquero.parser.events.channel.IRCKickEvent;
import com.packethammer.vaquero.parser.events.channel.IRCPartEvent;
import com.packethammer.vaquero.parser.events.channel.IRCTopicChangeEvent;
import com.packethammer.vaquero.parser.events.server.numeric.reply.ChannelCreationReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.ChannelModeReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.NamesReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.TopicReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.TopicWhoTime;
import com.packethammer.vaquero.parser.events.server.numeric.reply.UserHostReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.WhoReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.WhoXReply;
import com.packethammer.vaquero.parser.tracking.definitions.ChannelModeDefinition;
import com.packethammer.vaquero.parser.tracking.definitions.ChannelNickPrefixModeDefinition;
import com.packethammer.vaquero.util.Hostmask;
import com.packethammer.vaquero.util.modes.Modes;
import com.packethammer.vaquero.util.modes.channel.ChannelMode;

public class Tracker {   
    /** This is the keyname used for a user store when storing a user's account name, assuming we are using WHOX with account-tracking enabled in settings */
    public static final String USERKEY_IRCU_ACCOUNT = "IRCU.ACCT";
    
    private IRCParser parser;
    private Dispatcher outbound;
    private TrackerSettings settings;
    
    private HashMap<String, TrackedUser> trackedUsers; // key = lowercase nickname
    private HashMap<String, TrackedChannel> trackedChannels; // key = lowercase channel 
    private HashMap<String,ArrayList<NicknameHistory>> nicknameHistories; // key = nickname (not in lowercase)
    
    /**
     * Initializes the tracker with a parser and command manager to use.
     *
     * @param parser The parser to hook events from for advanced tracking. 
     * @param outbound The advanced dispatch command manager to use.
     * @param settings The tracker settings to use.
     * @param trackingMode See TRACKING_MODE_* constants for the tracking mode.
     */
    public Tracker(IRCParser parser, Dispatcher outbound, TrackerSettings settings) {
        this.parser = parser;
        this.outbound = outbound; 
        this.settings = settings;
        
        this.trackedUsers = new HashMap();
        this.trackedChannels = new HashMap();
        this.nicknameHistories = new HashMap();
        
        // act on settings
        if(settings.isIrcuAccountTrackingEnabled())
            settings.getWhoXSearchOptions().addSearchField(WhoXSearchOptions.SEARCHFIELD_ACCOUNTNAME);
        
        hookTrackingEvents();
    }    

    /**
     * Returns the parser this tracker is using.
     */
    public IRCParser getParser() {
        return parser;
    }

    /** 
     * Returns the command manager this tracker is using.
     */
    public CommandManager getOutbound() {
        return outbound;
    }
    
    /**
     * Returns your own tracked user information. This will return null if you
     * are currently not tracked. This occurs when you are not currently in
     * a channel. If you want to access your own information before joining
     * a channel, use the parser's server context.
     *
     * @return Yourself as a tracked user.
     */
    public TrackedUser getMe() {
        return this.getUser(parser.getServerContext().getMe().getNickname());
    }
    
    /**
     * Returns a user by their nickname. Please note that this is case-insensitive,
     * but it does NOT take network casemapping into account. This means that if
     * a server considers ` and ~ to be the same (same as 'a' and 'A' are the
     * same on basically every IRC server), that this method would not work
     * as you might expect. In short, please use the exact nickname of the user
     * you want to get the information for, although the ASCII letters can be
     * lowercase/uppercase without causing trouble.
     *
     * @param nickname The user's current nickname.
     * @return The tracked IRC user, or null if such a user does not exist.
     */
    public TrackedUser getUser(String nickname) {
        return this.trackedUsers.get(nickname.toLowerCase());
    }
    
    /**
     * Returns a channel by its name (including the channel type prefix). Note
     * that while this is case-insensitive in the traditional sense, that there
     * are some casemapping limitations in place that are better described in 
     * getUser().
     *
     * @param channel The name of the channel.
     * @return The tracked channel, or null if such a channel does not exist.
     * @see #getUser(String)
     */
    public TrackedChannel getChannel(String channel) {
        return this.trackedChannels.get(channel.toLowerCase());
    }
    
    /**
     * Returns the settings that this tracker is operating with.
     *
     * @return This tracker's settings.
     */
    public TrackerSettings getSettings() {
        return this.settings;
    }
    
    /**
     * Given an EXACT nickname, this method will find all usages of it in our
     * known nickname history time period. This is useful for finding
     * the exact time period when a certain nickname was used, eventually
     * allowing you to narrow things down to a specific IRC user.
     *
     * The returned list is a reference to the internal listing, so do
     * not modify it.
     *
     * @param nickname The EXACT nickname to search with.
     * @return A non-null list of nickname history for a given nickname (the list will be empty if no history is present in our tracking time period)
     */
    public ArrayList<NicknameHistory> getNicknameHistory(String nickname) {
        ArrayList<NicknameHistory> list = this.nicknameHistories.get(nickname);
        if(list == null)
            list = new ArrayList();
        
        return list;
    }
    
    /**
     * Given an EXACT nickname and a point in time that you knew that nickname was in
     * use, this will return a nickname history node which will allow you to
     * access the actual tracked IRC user.
     *
     * The most obvious use is to find a tracked IRC user by their past nickname(s).
     *
     * @param nickname The EXACT nickname to find a result with.
     * @param time Some point in time when this nickname should have been in use.
     * @return The nickname history for that nickname for that point in time, or null if no such nickname existed at that time or if we are no longer tracking the user with that nickname.
     */
    public NicknameHistory getNicknameHistoryAtTime(String nickname, Date time) {
        ArrayList<NicknameHistory> history = this.getNicknameHistory(nickname);
        for(NicknameHistory nicknameUsage : history) {
            if(nicknameUsage.getUsageStartTime().before(time) && 
                    (nicknameUsage.isStillInUse() || nicknameUsage.getUsageStopTime().after(time))) {
                return nicknameUsage;
            }
        }
        
        return null;
    }
            
    /**
     * Hooks events essential to tracking state.
     */
    private void hookTrackingEvents() {
        // track joins
        parser.getEventDistributor().addHardEventListener(IRCJoinEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                IRCJoinEvent j = (IRCJoinEvent) e;
                
                if(parser.getServerContext().isMe(j.getSource().getNickname())) {
                    // we just joined the channel
                    
                    // add the channel since we won't be tracking it yet
                    createNewChannel(j.getChannel());
                    
                    // dispatch a MODES request, since this is very nice information to have
                    outbound.sendCommand(new IRCChannelModeCommand(j.getChannel()));
                    
                    // do we find listable modes?
                    if(getSettings().isAutomaticJoinModeListingEnabled()) {
                        for(ChannelModeDefinition def : getParser().getServerContext().getISupport().getChannelModes()) {
                            if(def.isListable()) {
                                Modes<ChannelMode> sendModes = new Modes();
                                ChannelMode mode = new ChannelMode(def);
                                mode.setAdding(true);
                                sendModes.addMode(mode);
                                outbound.sendCommand(new IRCChannelModeCommand(j.getChannel(), sendModes));
                             }
                        }
                    }
                    
                    if(settings.isAutomaticWhoQueryingEnabled()) {
                        dispatchWho(j.getChannel());
                    }
                }
                
                // now we process the user (who may be us)
                UserChannelInformation info = addUserToChannel(j.getSource(), j.getChannel());
                // from the time a user joins a channel, we can see all modes set on them
                info.setChannelPrefixModesDefinite(true);
                
                if(settings.isAutomaticJoinWhoQueryingEnabled()) {
                    // we need to WHO this user (yeah, we might have it from channel WHO request earlier if we are the ones joining, but it is possible we won't even get a WHO reply related to ourselves from that -- we shall err on safe side)
                    dispatchWho(j.getSource().getNickname());
                }
            }
        });
        
        // track parts
        parser.getEventDistributor().addHardEventListener(IRCPartEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                IRCPartEvent p = (IRCPartEvent) e;
                removeUserFromChannel(p.getSource().getNickname(), p.getChannel());
            }
        });
        
        // track kicks
        parser.getEventDistributor().addHardEventListener(IRCKickEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                IRCKickEvent k = (IRCKickEvent) e;
                removeUserFromChannel(k.getTarget(), k.getChannel());
            }
        });
        
        // track quits
        parser.getEventDistributor().addHardEventListener(IRCQuitEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                IRCQuitEvent q = (IRCQuitEvent) e;
                userQuit(q.getSource());
            }
        });
        
        // track nick changes
        parser.getEventDistributor().addHardEventListener(IRCNickChangeEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                IRCNickChangeEvent n = (IRCNickChangeEvent) e;
                nicknameChanged(n.getSource(), n.getNewNickname());
            }
        });
        
        // track topic changes
        parser.getEventDistributor().addHardEventListener(IRCTopicChangeEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                IRCTopicChangeEvent t = (IRCTopicChangeEvent) e;
                TrackedChannel chan = getChannel(t.getChannel());
                if(chan != null) {
                    chan.setTopic(t.getMessage());
                    chan.setLastTopicChangeTime(new Date());
                    if(t.getSource().getNickname() != null)
                        chan.setLastTopicChanger(t.getSource().getNickname());
                    else
                        chan.setLastTopicChanger(t.getSource().getShortHostmask());                    
                }
            }
        });
        
        // track TopicWhoTime to get the topic setter and last topic set time
        parser.getEventDistributor().addHardEventListener(TopicWhoTime.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                TopicWhoTime t = (TopicWhoTime) e;
                TrackedChannel chan = getChannel(t.getChannel());
                if(chan != null) {
                    chan.setLastTopicChanger(t.getTopicSetter());
                    chan.setLastTopicChangeTime(t.getLastChangedTime());
                }
            }
        });
        
        // track channel creation time
        parser.getEventDistributor().addHardEventListener(ChannelCreationReply.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                ChannelCreationReply r = (ChannelCreationReply) e;
                TrackedChannel chan = getChannel(r.getChannel());
                if(chan != null) {
                    chan.setCreationTime(r.getCreationTime());
                }
            }
        });
        
        // track channel mode changes
        parser.getEventDistributor().addHardEventListener(IRCChannelModeChangeEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                IRCChannelModeChangeEvent m = (IRCChannelModeChangeEvent) e;
                updateChannelModes(m.getChannel(), m.getChannelModes());
            }
        });
        
        // track the numeric mode reply for a channel
        parser.getEventDistributor().addHardEventListener(ChannelModeReply.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                ChannelModeReply r = (ChannelModeReply) e;
                updateChannelModes(r.getChannel(), r.getModes());
            }
        });
        
        // track topic replies
        parser.getEventDistributor().addHardEventListener(TopicReply.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                TopicReply t = (TopicReply) e;
                TrackedChannel chan = getChannel(t.getChannel());
                if(chan != null) {
                    chan.setTopic(t.getTopic());                
                }
            }
        });
        
        // TODO: if query processor is made for NAMES, this needs to use it
        // track NAMES replies
        parser.getEventDistributor().addHardEventListener(NamesReply.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                NamesReply r = (NamesReply) e;
                Map<String,ChannelNickPrefixModeDefinition> map = r.getNicknames(getParser().getServerContext().getISupport());
                for(String nickname : map.keySet()) {
                    ChannelNickPrefixModeDefinition mode = map.get(nickname);
                    
                    // make sure we don't already have this user tracked in the target channel (we only want to use NAMES to add new initial users)
                    boolean makeNew = true;
                    TrackedUser user = getUser(nickname);
                    if(user != null) {
                        UserChannelInformation info = user.getChannelInformation(r.getChannel());
                        if(info != null) {
                            // this user is A) tracked and B) in the channel we got the reply for -- ignore it
                            makeNew = false;
                        }
                    }
                    
                    if(makeNew) {
                        UserChannelInformation info = addUserToChannel(new Hostmask(nickname, null, null), r.getChannel());
                        info.setChannelPrefixModesDefinite(false); // we only saw them in a names list; we can't know their modes for sure yet
                        // do they have a mode we need to add?
                        if(mode != null) {
                            info.getWitnessedModes().add(mode.getMode());
                            info.getPrefixModes().add(mode);

                            // always check!
                            if(arePrefixModesFullyAccountedFor(info.getWitnessedModes()))
                                info.setChannelPrefixModesDefinite(true);
                        }
                    }
                }
            }
        });
        
        // track WHO replies (this allows us to update our internal state based on WHO replies we may not even have initiated ourselves)
        parser.getEventDistributor().addHardEventListener(WhoReply.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                WhoReply reply = (WhoReply) e;
                // NOTE: reply may be for a specific user, not a channel listing reply
                
                // update hostmask information for user
                updateHostmask(reply.getInfoAsHostmask());

                // get the user
                TrackedUser user = getUser(reply.getNickname());  
                if(user != null) {                     
                    // set realname
                    user.setRealname(reply.getRealname());
                    
                    // update oper/away status
                    user.setAway(reply.isAway());
                    user.setServerOperator(reply.isOperator());
                    
                    // if we are getting this info for a channel, deal with that
                    if(reply.getChannel() != null) {
                        // get channel-user information for the user in the channel
                        UserChannelInformation info = user.getChannelInformation(reply.getChannel());

                        // the reply may have a single channel mode definition, add it to them if it's there
                        ChannelNickPrefixModeDefinition modeDef = reply.getChannelUserPrefix(parser.getServerContext().getISupport());
                        if(modeDef != null) {
                            info.getWitnessedModes().add(modeDef.getMode());
                            info.getPrefixModes().add(modeDef);
                            
                            // Do we know all the nick-prefix modes ever set on this user yet?
                            if(arePrefixModesFullyAccountedFor(info.getWitnessedModes())) {
                                info.setChannelPrefixModesDefinite(true);
                            }
                        }
                    }
                }
            }
        });
        
        // this listens for USERHOST information
        parser.getEventDistributor().addDynamicEventListener(UserHostReply.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                UserHostReply userhost = (UserHostReply) e;                
                updateHostmask(userhost.getAsHostmask());
                
                TrackedUser user = getUser(userhost.getNickname());
                if(user != null) {
                    user.setAway(userhost.isAway());
                    user.setServerOperator(userhost.isServerOperator());
                }
            }
        });
        
        // optional tracking: full hostmask tracking
        if(settings.isFullHostmaskTrackingEnabled()) {
            // set up the listener that will keep updating hostmasks for likely-relevant users
            IRCEventListener listener = new IRCEventListener() {
                public void onEvent(IRCEvent e) {
                    updateHostmask(e.getSource());
                }
            };
            
            // now, hook all the events which could possibly come from some user we are tracking (that is, a lot of channel events)
            parser.getEventDistributor().addHardEventListener(IRCChannelActionEvent.class, listener);
            parser.getEventDistributor().addHardEventListener(IRCChannelCTCPEvent.class, listener);
            parser.getEventDistributor().addHardEventListener(IRCChannelMessageEvent.class, listener);
            parser.getEventDistributor().addHardEventListener(IRCChannelModeChangeEvent.class, listener);
            parser.getEventDistributor().addHardEventListener(IRCChannelNoticeEvent.class, listener);
            parser.getEventDistributor().addHardEventListener(IRCJoinEvent.class, listener);
            parser.getEventDistributor().addHardEventListener(IRCKickEvent.class, listener);
            parser.getEventDistributor().addHardEventListener(IRCPartEvent.class, listener);
            parser.getEventDistributor().addHardEventListener(IRCTopicChangeEvent.class, listener);            
        }
    }
    
    /**
     * This helper method dispatches a WHO request against a target of our 
     * choice. It will automatically determine if we are to use a regular
     * WHO or a WHOX.
     */
    private void dispatchWho(String searchMask) {
        if(settings.isWhoXForced() || (settings.isWhoXEnabled() && parser.getServerContext().getISupport().isWhoXSupported())) {
            // perform a WHOX query and store the results as we receive them
            outbound.performWhoXQuery(new IRCWhoXCommand(searchMask, this.getSettings().getWhoXSearchOptions()), new WhoXQueryListener() {
                public void onReply(WhoXReply reply) {
                    // NOTE: remember, this reply can be for a specific user, or as part of a channel user list
                    
                    // update hostmask information for user
                    updateHostmask(reply.getInfoAsHostmask());
                    
                    // get the user
                    TrackedUser user = getUser(reply.getNickname());  
                    if(user != null) {                     
                        // set realname
                        user.setRealname(reply.getRealname());
                        
                        // update away/oper status -- located in infoflags, so we should have 'em
                        user.setServerOperator(reply.isOperator());
                        user.setAway(reply.isAway());
                        
                        // are we supposed to pull the ircu account field?
                        if(getSettings().isIrcuAccountTrackingEnabled() && reply.getAccountName() != null) {
                            user.getStore().store(USERKEY_IRCU_ACCOUNT, reply.getAccountName());
                        }

                        // if we are getting this info for a channel, deal with that
                        if(reply.getChannel() != null) {
                            // get channel-user information for the user in the channel
                            UserChannelInformation info = user.getChannelInformation(reply.getChannel());

                            // update user nick-prefix modes for channel
                            ArrayList<ChannelNickPrefixModeDefinition> modes = reply.getChannelUserPrefixModes(parser.getServerContext().getISupport());
                            for(ChannelNickPrefixModeDefinition def : modes) {
                                info.getWitnessedModes().add(def.getMode());
                                info.getPrefixModes().add(def);
                            }

                            // we just updated all nick-prefix modes, so those are definite
                            info.setChannelPrefixModesDefinite(true);
                        }
                    }
                }
            });
        } else {
            // send regular WHO -- we don't need to perform a query, since we passively monitor WHO replies anyway.
            outbound.sendCommand(new IRCWhoCommand(searchMask));
        }
    }
    
    /**
     * This takes a set of mode characters and compares them to the current
     * known user prefix modes in ISUPPORT and returns true if the modes
     * in ISUPPORT are all represented in the set of characters provided
     * (thus proving that if the given set of characters is the modes we've
     * seen set on a user, that we know all that user's information definitely).
     */
    private boolean arePrefixModesFullyAccountedFor(Set<Character> modes) {
        List<ChannelNickPrefixModeDefinition> modeDefs = this.getParser().getServerContext().getISupport().getNickPrefixModes();
        for(ChannelNickPrefixModeDefinition def : modeDefs) {
            if(!modes.contains(def.getMode())) {
                // the modes set is missing a required mode, they don't work
                return false;
            }
        }
        
        // if they passed, they work!
        return true;
    }
    
    /**
     * This takes a hostmask that is likely one belonging to someone we are
     * currently tracking and finds the user (by nickname) it is associated with
     * and updates that user's hostmask with this "new" one (although it may
     * be same as before). If the user is not tracked, we just ignore it.
     *
     * This assumes a full hostmask is being passed; don't give incomplete ones.
     */
    private void updateHostmask(Hostmask hostmask) {
        TrackedUser user = this.getUser(hostmask.getNickname());
        if(user != null) {
            user.setHostmask(hostmask);
        }
    }
    
    /**
     * Called when a user changes their nickname.
     */
    private void nicknameChanged(Hostmask sourceHostmask, String newNickname) {
        String originalNick = sourceHostmask.getNickname();
        
        TrackedUser user = this.getUser(sourceHostmask.getNickname());
        if(user != null) {
            // First, remove their entry in the map
            trackedUsers.remove(originalNick.toLowerCase());
            
            // Create new nickname history data for current nickname
            Date nickChangeTime = new Date();
            NicknameHistory history = new NicknameHistory(nickChangeTime, newNickname, user);
            
            // Add to nickname history map
            this.addNicknameHistory(history);
            
            // assign new nickname, of course
            user.getHostmask().setNickname(newNickname);
            
            // add them back to the map
            trackedUsers.put(user.getHostmask().getNickname().toLowerCase(), user);
        }
    }
    
    /**
     * Adds a new user to the internal map, ensuring their nickname history
     * entry is properly configured.
     */
    private TrackedUser createNewUser(Hostmask hostmask) {
        TrackedUser user = new TrackedUser();
        user.setTracked(true);
        user.setHostmask(hostmask);
        
        // add to tracked map
        this.trackedUsers.put(user.getHostmask().getNickname().toLowerCase(), user);

        // add their current nickname into the nickname history as a current nickname
        NicknameHistory history = new NicknameHistory(new Date(), hostmask.getNickname(), user);
        this.addNicknameHistory(history);
        
        return user;
    }
    
    /**
     * Adds nickname history to a user and the internal nickname histories map.
     */
    private void addNicknameHistory(NicknameHistory history) {
        ArrayList<NicknameHistory> currentHistory = this.nicknameHistories.get(history.getNickname());
        if(currentHistory == null)
            currentHistory = new ArrayList();
        
        TrackedUser user = history.getUser();
        
        // First, determine if we need to remove one or more existing nickname
        // histories if they've expired
        if(!user.getNicknameHistory().isEmpty()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date()); // make sure set to current time
            cal.add(Calendar.MINUTE, -1 * this.settings.getNicknameTrackingHistoryTime()); // subtract the number of minutes to give a time in the past that is the expiration point
            Date expirationTime = cal.getTime();            
                    
            ListIterator<NicknameHistory> i = user.getNicknameHistory().listIterator(user.getNicknameHistory().size());
            while(i.hasPrevious()) {
                NicknameHistory current = i.previous();
                if(current.getUsageStartTime().before(expirationTime)) {
                    // the current name history at the end of the list is expired, remove it
                    i.remove();
                    
                    // likewise, remove from out lookup map
                    ArrayList<NicknameHistory> historyList = this.nicknameHistories.get(current.getNickname());
                    if(historyList != null)
                        historyList.remove(current);
                } else {
                    // we've reached name history that is not expired; the rest is fine
                    break;
                }
            }
        }
        
        // First, see if the user has a current nickname we need to "close up"
        if(!user.getNicknameHistory().isEmpty()) {
            // The current nickname just stopped at the time the new nickname is introduced
            user.getNicknameHistory().get(0).setUsageStopTime(history.getUsageStartTime());
        }
        
        // Put history at topmost position, seeing as this is newest nickname
        user.getNicknameHistory().insertElementAt(history, 0);
        currentHistory.add(history);
        
        this.nicknameHistories.put(history.getNickname(), currentHistory);
    }
    
    /**
     * Removes nickname history from the internal nickname histories map. 
     * The history being removed must be the same reference as what is currently 
     * in the map.
     */
    private void removeNicknameHistory(NicknameHistory history) {
        ArrayList<NicknameHistory> currentHistory = this.nicknameHistories.get(history.getNickname());
        if(currentHistory != null) {
            currentHistory.remove(history);
        }
    }
    
    /**
     * Adds a user to a channel given the user's hostmask and the channel
     * they joined. Returns the user-channel information we just added.
     *
     * WARNING: may be called for a NAMES reply, not just JOIN!
     */
    private UserChannelInformation addUserToChannel(Hostmask hostmask, String channel) {
        // Pull user
        TrackedUser user = this.getUser(hostmask.getNickname());
        if(user == null) {
            // make a new user...
            user = this.createNewUser(hostmask);
        }
        
        // always a good habit to force hostmask update...
        user.setHostmask(hostmask);
        
        // Now, pull channel -- note that if we witnessed the join, we must already be in the channel, so it is implied it is tracked
        TrackedChannel chan = this.getChannel(channel);
        if(chan == null) {
            throw new IllegalStateException(hostmask.toString() + " joined " + channel + ", but we aren't tracking that channel!");
        }
        
        // Create the information linking that user with the channel
        UserChannelInformation info = new UserChannelInformation();
        info.setChannel(chan);
        info.setUser(user);
        
        // Now, we link the channel and user back to the info that links them
        user.addChannelInformation(info);
        chan.addUserInformation(info);
        
        return info;
    }
    
    /**
     * Removes a user from a channel, given their nickname and the channel
     * to remove them from.
     */
    private void removeUserFromChannel(String nickname, String channel) {
        TrackedUser user = this.getUser(nickname);
        TrackedChannel chan = this.getChannel(channel);
        
        if(user == null || chan == null)
            throw new IllegalStateException("Trying to remove '" + nickname + "' from '" + channel + "', but we aren't tracking that " + (user == null ? "user" : "channel"));
        
        // first, pull the info that links the user with the channel
        UserChannelInformation info = user.getChannelInformation(chan.getName());
        
        // remove the info from the user and chan
        user.removeChannelInformation(info);
        chan.removeUserInformation(info);
        
        // now, wipe that info that once linked them
        info.destroy();
        
        // time to drop user?
        cleanupCheck(user);
        
        if(this.getParser().getServerContext().isMe(nickname)) {
            // we are the one being removed from the channel! we should delete the channel's information since we can no longer track it
            this.trackedChannels.remove(channel.toLowerCase());
            
            // remove all channel-user info, and make sure each user has the channel removed from them too!
            for(UserChannelInformation i : chan.getUserInformation()) {
                // we must perform a regular user removal, much like above when it's not just us
                i.getUser().removeChannelInformation(i);
                cleanupCheck(i.getUser());
                i.destroy();
            }
            
            // finally, destroy channel
            chan.destroy();
        }
    }
    
    /**
     * Checks a user to make sure we don't need to stop tracking them. The case
     * where we should stop is when we no longer share any channels with them,
     * meaning we don't "see" them anymore.
     */
    private void cleanupCheck(TrackedUser user) {
        if(user.getChannelInformation().isEmpty()) {
            // it is indeed time to drop this person
            
            // delete all their nickname history lookup entries
            for(NicknameHistory history : user.getNicknameHistory()) {
                ArrayList<NicknameHistory> historyList = this.nicknameHistories.get(history.getNickname());
                if(historyList != null) {
                    Iterator<NicknameHistory> i = historyList.iterator();
                    while(i.hasNext()) {
                        if(i.next().equals(history)) {
                            i.remove();
                        }
                    }
                }
                
                if(historyList.isEmpty())
                    this.nicknameHistories.remove(history.getNickname());
            }
            
            // clean up user
            user.setTracked(false);
            this.trackedUsers.remove(user.getHostmask().getNickname().toLowerCase());
            user.destroy();
        }
    }
    
    /**
     * Occurs when a user quits the IRC server, meaning we need to remove them.
     */
    private void userQuit(Hostmask hostmask) {
        // it is implied that the user is in at least one channel for us to have
        // seen them quit, so we just start removing the user from each
        // channel they are in, which will have the side effect of also removing
        // them from our tracked user list at the end.
        TrackedUser user = this.getUser(hostmask.getNickname());
        
        if(user == null)
            throw new IllegalStateException("Handling quit for user '" + hostmask.toString() + "', but user is not being tracked!");
        
        // We need to copy the channels list off the user to avoid concurrent modification problems as we remove the channels
        ArrayList<UserChannelInformation> channels = new ArrayList();
        channels.addAll(user.getChannelInformation());
        for(UserChannelInformation info : channels) {
            this.removeUserFromChannel(user.getHostmask().getNickname(), info.getChannel().getName());
        }
    }
    
    /**
     * Creates a new tracked channel.
     */
    private TrackedChannel createNewChannel(String name) {
        TrackedChannel chan = new TrackedChannel();
        chan.setName(name);
        
        // add to tracked channels
        this.trackedChannels.put(chan.getName().toLowerCase(), chan);
        
        
        // populate the channel with empty listable channel modes derived from ISUPPORT
        Set<ChannelModeDefinition> modeDefs = this.getParser().getServerContext().getISupport().getChannelModes();
        for(ChannelModeDefinition def : modeDefs) {
            if(def.getModeType() == ChannelModeDefinition.MODETYPE_A_NICKADDRESS) {
                ListableModes listable = new ListableModes();
                listable.setModeDefinition(def);
                listable.setDefinite(false); // don't know yet
            }
        }
        
        return chan;
    }
    
    /**
     * This will update the modes for a channel, which may affect users.
     */
    private void updateChannelModes(String channel, Modes<ChannelMode> modes) {
        List<ChannelMode> modeList = modes.getModes();
        TrackedChannel chan = this.getChannel(channel);
        if(chan != null) {
            for(ChannelMode mode : modeList) {
                /*
                if(mode.getModeDefinition().getModeType() == ChannelModeDefinition.MODETYPE_A_NICKADDRESS) {
                    // this mode is operating on the internal channel list
                    for(ListableModes listableModes : chan.getListableModes()) {
                        if(listableModes.getModeDefinition().equals(mode.getModeDefinition())) {
                            if(mode.isBeingAdded()) {
                                listableModes.getModes().add(mode);
                            } else {
                                listableModes.getModes().remove(mode);
                            }
                            break;
                        }                            
                    }
                } else */ if(mode.getModeDefinition() instanceof ChannelNickPrefixModeDefinition) {
                    ChannelNickPrefixModeDefinition definition = (ChannelNickPrefixModeDefinition) mode.getModeDefinition();
                    // it's a user prefix mode
                    String nickname = mode.getParameter();
                    TrackedUser user = this.getUser(nickname);
                    if(user != null) {
                        UserChannelInformation info = user.getChannelInformation(channel);
                        
                        // we now have the user's info for this channel
                        info.getWitnessedModes().add(mode.getMode());
                        if(mode.isBeingAdded()) {
                            info.getPrefixModes().add(definition);
                        } else {
                            info.getPrefixModes().remove(definition);
                        }
                        
                        // check and see if we saw all their modes yet
                        if(arePrefixModesFullyAccountedFor(info.getWitnessedModes()))
                            info.setChannelPrefixModesDefinite(true);
                    }
                } else {
                    // it is a non-user-prefix non-listable channel mode
                    if(mode.isBeingAdded()) {
                        chan.getChannelModes().add(mode);
                    } else {
                        // do it manually to make sure it's done right
                        Iterator<ChannelMode> i = chan.getChannelModes().iterator();
                        while(i.hasNext()) {
                            if(i.next().getMode().equals(mode.getMode())) 
                                i.remove();
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Prints debugging information to a print stream. This will essentially 
     * print the entire state of the tracker.
     */
    public void debugDump(PrintStream out) {
        String space = "     ";
        String format = space + "%-25s %s";
        
        out.println("-> Tracker dump --------------------------");
        out.println("Using settings:");
        out.println(settings.toString());
        
        out.println();
        out.println();
        out.println();
        
        out.println("-> User dump --------------------------");
        for(TrackedUser user : this.trackedUsers.values()) {
            out.println(String.format(format, "Hostmask", user.getHostmask()));
            out.println(String.format(format, "Away/Oper", user.isAway() + "/" + user.isServerOperator()));
            out.println(String.format(format, "Nickname history:", ""));
            out.println(String.format(format, "DataStore", user.getStore().toString()));
            for(NicknameHistory history : user.getNicknameHistory()) {
                out.println(String.format(space + format, history.getNickname(), history.toString()));
            }
            out.println(String.format(format, "Channels", ""));
            for(UserChannelInformation info : user.getChannelInformation()) {
                out.println(String.format(space + format, "Info", info.getChannel().getName()));
                String prefixModes = (info.getPrefixModes() != null ? "" : "None.");
                for(ChannelNickPrefixModeDefinition mode : info.getPrefixModes()) {
                    prefixModes += mode.getPrefix();
                }
                out.println(String.format(space + space + format, "Prefix Modes", prefixModes));
                out.println(String.format(space + space + format, "Witnessed modes", info.getWitnessedModes()));                
                out.println(String.format(space + space + format, "Prefix Modes Definite", info.isChannelPrefixModesDefinite()));
            }
            
            out.println("____________________________________");
        }
        
        out.println();
        out.println();
        out.println();
        
        out.println("-> Channel dump --------------------------");
        for(TrackedChannel chan : this.trackedChannels.values()) {
            out.println(String.format(format, "Channel", chan.getName()));
            out.println(String.format(format, "Modes", new Modes<ChannelMode>(chan.getChannelModes()).renderModes()));
            out.println(String.format(format, "Creation time", chan.getCreationTime()));
            out.println(String.format(format, "Topic", chan.getTopic()));
            out.println(String.format(format, "Topic last set by", chan.getLastTopicChanger()));
            out.println(String.format(format, "Topic last set at", chan.getLastTopicChangeTime()));
            out.println(String.format(format, "DataStore", chan.getStore().toString()));
            /*
            out.println(String.format(format, "Listable modes set", ""));
            
            
            for(ListableModes list : chan.getListableModes()) {
                out.println(String.format(space+format, "Mode", "+" + list.getModeDefinition().getMode()));
                out.println(String.format(space+format, "List is definite", list.isDefinite()));
                out.println(String.format(space+format, "Modes as set", list.getModes()));
            }
             */
            
            out.println(String.format(format, "Users", ""));
            for(UserChannelInformation info : chan.getUserInformation()) {
                out.println(String.format(space+format, info.getUser().getHostmask().getNickname(), info.getPrefixModes()));
                out.println(String.format(space+space+format, "-> DataStore", info.getStore()));
            }
            
            out.println("____________________________________");
        }
        
        out.println("-> User nickname history lookup dump --------------------------");
        for(String nickname : this.nicknameHistories.keySet()) {
            out.println(String.format(format, nickname, ""));
            for(NicknameHistory history : this.nicknameHistories.get(nickname)) {
                out.println(String.format(space+format, history.toString(), ""));
            }
        }
     }
}
