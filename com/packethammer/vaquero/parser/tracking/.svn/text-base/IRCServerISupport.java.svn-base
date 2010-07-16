/*
 * Holds information for numeric 005 RPL_ISUPPORT and allows one to determine
 * what a network supports based on it.
 *
 * Note that this class is designed to return defaulted RFC 1459 information
 * and "reasonable" other default settings if they are currently unknown. In short,
 * using this on an IRC server that is not providing enough or any ISUPPORT
 * information could result in some interesting stuff, especially if it uses
 * nonstandard channel types, modes, etc. If you want to determine if the server
 * actually told of a certain setting, use the is...Known() methods. Some methods
 * do not return defaulted information -- you can check this be referring to their
 * javadoc information.
 *
 * There is a broad range of ISUPPORT information that is either derived from
 * the "draft-brocklesby-irc-isupport-03.txt" draft or just from a server's
 * own specifications. This class is meant to handle most, if not all in the
 * ISUPPORT draft, and some RFC2812-related tokens. Some of the tokens may be
 * very server-specific (such as ircu's WHOX), but they can be very beneficial
 * to the user or channel tracking system, so they are included here at least
 * for the tracker's sake. Other irc-server-specific support may be left up to
 * you to check for existence.
 *
 * @see ServerISupportReply
 */

package com.packethammer.vaquero.parser.tracking;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import com.packethammer.vaquero.util.CasemappingDefinition;
import com.packethammer.vaquero.util.modes.channel.ChannelMode;
import com.packethammer.vaquero.parser.StringOperations;
import com.packethammer.vaquero.parser.tracking.definitions.ChannelModeDefinition;
import com.packethammer.vaquero.parser.tracking.definitions.ChannelNickPrefixModeDefinition;
import com.packethammer.vaquero.parser.tracking.definitions.ChannelTypeDefinition;

public class IRCServerISupport {
    // the following are not static since the constructor still must operate on them.
    // they should be defaulted to RFC 1459 specifications.
    public final Vector<ChannelNickPrefixModeDefinition> DEFAULT_NICKPREFIXMODES;
    public final HashSet<ChannelTypeDefinition> DEFAULT_CHANNELTYPES;
    public final HashSet<ChannelModeDefinition> DEFAULT_CHANNELMODES;
    public final String DEFAULT_CASEMAPPING = "rfc1459";
    
    private HashMap<String, String> iSupport; // For numeric 005 ISUPPORT
    private HashMap<String, String> iSupportPure; // Also for numeric 005 ISUPPORT, but this class has the tendency to pollute the regular iSupport map, so this untouched one is provided
    private Vector<ChannelNickPrefixModeDefinition> nickPrefixModes; // for PREFIX/STATUSMSG -- higher-precedence modes come first
    private HashSet<ChannelTypeDefinition> channelTypes; // for CHANTYPES/MAXCHANNELS/CHANLIMIT
    private HashSet<ChannelModeDefinition> channelModes; // for CHANMODES/MAXBANS/MAXLIST
    private int maxChanModesWithParam = -1; // for MODES
    private int maxNickLength = -1; // for NICKLEN
    private int maxTopicLength = -1; // for TOPICLEN
    private int maxKickMessageLength = -1; // for KICKLEN
    private int maxChannelLength = -1; // for CHANNELLEN
    private int maxTargets = -1; // for MAXTARGETS
    private String networkName; // for NETWORK
    private String caseMapping; // for CASEMAPPING
    private boolean whoXSupported; // for WHOX (ircu)
    private boolean cPrivmsgSupported; // for CPRIVMSG (ircu)
    private boolean cNoticeSupported; // for CNOTICE (ircu)
    private CasemappingDefinition casemappingDefinition; // for CASEMAPPING (yes, a second one)
    
    public IRCServerISupport() {
        iSupport = new HashMap();
        iSupportPure = new HashMap();
        nickPrefixModes = new Vector();
        channelTypes = new HashSet();
        channelModes = new HashSet();
        casemappingDefinition = new CasemappingDefinition();
        
        // add default channel prefixes
        ChannelNickPrefixModeDefinition chanOp = new ChannelNickPrefixModeDefinition(ChannelMode.MODE_OP, '@');
        ChannelNickPrefixModeDefinition chanVoice = new ChannelNickPrefixModeDefinition(ChannelMode.MODE_VOICE, '+');
        DEFAULT_NICKPREFIXMODES = new Vector();
        DEFAULT_NICKPREFIXMODES.add(chanOp);
        DEFAULT_NICKPREFIXMODES.add(chanVoice);
        
        // add default channel modes
        DEFAULT_CHANNELMODES = new HashSet();
        DEFAULT_CHANNELMODES.addAll(DEFAULT_NICKPREFIXMODES);
        DEFAULT_CHANNELMODES.add(new ChannelModeDefinition(ChannelMode.MODE_BAN,                ChannelModeDefinition.MODETYPE_A_NICKADDRESS));
        DEFAULT_CHANNELMODES.add(new ChannelModeDefinition(ChannelMode.MODE_INVITEONLY,         ChannelModeDefinition.MODETYPE_D_NOPARAM));
        DEFAULT_CHANNELMODES.add(new ChannelModeDefinition(ChannelMode.MODE_KEY,                ChannelModeDefinition.MODETYPE_B_ALWAYSPARAM));
        DEFAULT_CHANNELMODES.add(new ChannelModeDefinition(ChannelMode.MODE_LIMIT,              ChannelModeDefinition.MODETYPE_C_SETPARAM));
        DEFAULT_CHANNELMODES.add(new ChannelModeDefinition(ChannelMode.MODE_MODERATED,          ChannelModeDefinition.MODETYPE_D_NOPARAM));
        DEFAULT_CHANNELMODES.add(new ChannelModeDefinition(ChannelMode.MODE_NOEXTERNALMESSAGES, ChannelModeDefinition.MODETYPE_D_NOPARAM));
        DEFAULT_CHANNELMODES.add(new ChannelModeDefinition(ChannelMode.MODE_PRIVATE,            ChannelModeDefinition.MODETYPE_D_NOPARAM));
        DEFAULT_CHANNELMODES.add(new ChannelModeDefinition(ChannelMode.MODE_SECRET,             ChannelModeDefinition.MODETYPE_D_NOPARAM));
        DEFAULT_CHANNELMODES.add(new ChannelModeDefinition(ChannelMode.MODE_TOPICLOCK,          ChannelModeDefinition.MODETYPE_D_NOPARAM));
        
        // add default channel types
        DEFAULT_CHANNELTYPES = new HashSet();
        DEFAULT_CHANNELTYPES.add(new ChannelTypeDefinition('#'));
        DEFAULT_CHANNELTYPES.add(new ChannelTypeDefinition('&'));
        
        // set casemappingDefinition based on default
        casemappingDefinition.setCasemappingConstant(CasemappingDefinition.getCasemappingConstantByName(this.getCaseMapping()));
    }
    
    /**
     * Adds an ISupport token and parses it. The parsing routine is meant to
     * attempt to handle situations where tokens may come out of logical order,
     * but it is still important to add them in the order they are received from
     * the server.
     *
     * @param key The token's name.
     * @param val The value associated with the token. May be null if no value is present.
     */
    public void addISupport(String key, String val) {
        iSupportPure.put(key.toUpperCase(), val);
        parseISupport(key, val);
    }
    
    
    //         +++++++++++++++++++++++++++++++++++
    //         ****** BEGIN PRIVATE METHODS ******
    //         +++++++++++++++++++++++++++++++++++
    
    
    /**
     * Adds a supported property according to numeric 005 RPL_ISUPPORT.
     * It is here that a property might also be parsed and its informational
     * content stored internally for retrieval in logical form.
     *
     * Note that this method should be guaranteed to take keys in any order,
     * even if it is not logical for one to precede the other (such as getting
     * CHANLIMIT before CHANTYPES) and still be able to use them to the fullest
     * without regard to which order they come in.
     *
     * @param key The key name.
     * @param val The value associated with the key. May be null if no value exists.
     */
    private void parseISupport(String key, String val) {
        key = key.toUpperCase();
        iSupport.put(key, val);
        
        if(val != null) {
            // handle keys with values
            if(key.equals("PREFIX")) {
                // format is "(modes)prefixes"
                String strModes = StringOperations.getBetween(val, "(", ")");
                String strPrefixes = val.substring(val.indexOf(")") + 1);
                if(strModes != null && strPrefixes != null) {
                    char[] modes = strModes.toCharArray();
                    char[] prefixes = strPrefixes.toCharArray();
                    for(int x = 0; x < modes.length && x < prefixes.length; x++) {
                        ChannelNickPrefixModeDefinition modeDef = new ChannelNickPrefixModeDefinition(new Character(modes[x]), prefixes[x]);
                        this.nickPrefixModes.add(modeDef);
                        this.channelModes.add(modeDef);
                    }
                }
                
                if(this.iSupport.containsKey("STATUSMSG")) {
                    // we got STATUSMSG before PREFIX, so run it back through parser to endow the prefix modes with STATUSMSG's goodness
                    pushISupportAgain("STATUSMSG");
                }
                
                syncNickPrefixModes();
            } else if(key.equals("CHANTYPES")) {
                // format is simply "prefixes"
                for(Character chanPrefix : val.toCharArray()) {
                    // the following will not take place if the set already has the prefix. we don't care about that, since we only need the prefix in there -- if it's already there, who cares.
                    this.channelTypes.add(new ChannelTypeDefinition(chanPrefix));
                }
                
                // note: we may have gotten a MAXCHANNELS before CHANTYPES, so the join limit for each channel type may not be set yet -- do that if needed
                if(this.iSupport.containsKey("MAXCHANNELS")) {
                    // force MAXCHANNELS back through the parser to setup channels correctly
                    pushISupportAgain("MAXCHANNELS");
                }
            } else if(key.equals("CHANMODES")) {
                // format is "A,B,C,D" where letters signify positions of corresponding mode types
                String[] modes = val.split(",");
                if(modes.length == 4) {
                    // note: the following will not add mode type As that we may have already guessed, but it doesn't matter, since the ones we guesed should contain accurate list limits, assuming they were guessed from MAXLIST/MAXBANS
                    for(Character mode : modes[0].toCharArray())
                        this.channelModes.add(new ChannelModeDefinition(mode, ChannelModeDefinition.MODETYPE_A_NICKADDRESS));
                    
                    for(Character mode : modes[1].toCharArray())
                        this.channelModes.add(new ChannelModeDefinition(mode, ChannelModeDefinition.MODETYPE_B_ALWAYSPARAM));
                    
                    for(Character mode : modes[2].toCharArray())
                        this.channelModes.add(new ChannelModeDefinition(mode, ChannelModeDefinition.MODETYPE_C_SETPARAM));
                    
                    for(Character mode : modes[3].toCharArray())
                        this.channelModes.add(new ChannelModeDefinition(mode, ChannelModeDefinition.MODETYPE_D_NOPARAM));
                }
            } else if(key.equals("MODES")) {
                if(StringOperations.isInteger(val))
                    this.maxChanModesWithParam = Integer.parseInt(val);
            } else if(key.equals("MAXCHANNELS")) {
                // this sets the maximum number of channels we can join of ANY type
                if(StringOperations.isInteger(val)) {
                    int limit = Integer.parseInt(val);

                    for(ChannelTypeDefinition chanType : channelTypes) {
                        chanType.setJoinLimit(limit);
                    }
                }
            } else if(key.equals("CHANLIMIT")) {
                // format is "pfx:num[,pfx:num,...]""
                // ex: #&!+:10
                String[] limits = val.split(",");
                for(String limitExpression : limits) {
                    String[] split = limitExpression.split(":");
                    if(split.length == 2 && StringOperations.isInteger(split[1])) {
                        // we have split out the prefixes:limit, it is in split[]
                        char[] chanPrefixes = split[0].toCharArray();
                        int joinLimit = Integer.parseInt(split[1]);
                        
                        for(Character chanPrefixToLimit : chanPrefixes) {
                            boolean foundChanType = false;
                            for(ChannelTypeDefinition chanType : channelTypes) {
                                if(chanType.getPrefix().equals(chanPrefixToLimit)) {
                                    chanType.setJoinLimit(joinLimit);
                                    foundChanType = true;
                                    break; // leave inner loop, since we successfully matched this chanType with a prefix we were to modify the join limit of
                                }
                            }
                            
                            if(!foundChanType) {
                                // we never found a chanType with a prefix that matches this one, so make one! (this is likely due to not reading in CHANTYPES yet -- we are now inferring channel types)
                                ChannelTypeDefinition chanDef = new ChannelTypeDefinition(chanPrefixToLimit);
                                chanDef.setJoinLimit(joinLimit);
                                this.channelTypes.add(chanDef);
                            }
                        }
                    }
                }
            } else if(key.equals("NICKLEN")) {
                if(StringOperations.isInteger(val))
                    this.maxNickLength = Integer.parseInt(val);
            } else if(key.equals("MAXLIST")) {
                // format is "mode:num[,mode:num,...]"
                String[] limits = val.split(",");
                for(String limitExpression : limits) {
                    String[] split = limitExpression.split(":");
                    if(split.length == 2 && StringOperations.isInteger(split[1])) {
                        // we have split out the prefixes:limit, it is in split[]
                        char[] modes = split[0].toCharArray();
                        int maxLimit = Integer.parseInt(split[1]);
                        
                        for(Character mode : modes) {
                            ensureChannelModeDefinitionExistence(mode, ChannelModeDefinition.MODETYPE_A_NICKADDRESS);
                            getChannelModeByChar(mode).setListLimit(maxLimit);
                        }
                    }
                }
            } else if(key.equals("MAXBANS")) {
                // just recycle and use MAXLIST
                this.parseISupport("MAXLIST", ChannelMode.MODE_BAN + ":" + val);
            } else if(key.equals("NETWORK")) {
                this.networkName = val;
            } else if(key.equals("EXCEPTS")) {
                if(val.length() == 1) {
                    Character exceptionMode = val.toCharArray()[0];
                    ensureChannelModeDefinitionExistence(exceptionMode, ChannelModeDefinition.MODETYPE_A_NICKADDRESS);
                    getChannelModeByChar(exceptionMode).setBanExceptionMode(true);
                }
            } else if(key.equals("INVEX")) {
                if(val.length() == 1) {
                    Character exceptionMode = val.toCharArray()[0];
                    ensureChannelModeDefinitionExistence(exceptionMode, ChannelModeDefinition.MODETYPE_A_NICKADDRESS);
                    getChannelModeByChar(exceptionMode).setInviteExceptionMode(true);
                }
            } else if(key.equals("STATUSMSG")) {
                // TODO: we are going to ignore STATUSMSG for now; it acts damn weird on gamesurge's ircu and doesn't behave as one might expect, probably due to a poor implementation of NOTICE and WALLVOICES -- read more in ChannelNickPrefixModeDefinition
                /*
                for(Character prefixChar : val.toCharArray()) {
                    for(ChannelNickPrefixModeDefinition prefixModeDef : nickPrefixModes) {
                        if(prefixModeDef.getPrefix().equals(prefixChar)) {
                            // we found a mode definition that matches, it is a STATUSMSG mode
                            prefixModeDef.setStatusPrefixMsgMode(true);
                            break;
                        }
                    }
                }
                
                syncNickPrefixModes();
                 */
            } else if (key.equals("CASEMAPPING")) {
                this.caseMapping = val;
                this.casemappingDefinition.setCasemappingConstant(CasemappingDefinition.getCasemappingConstantByName(val));
            } else if(key.equals("ELIST")) {
                // don't know enough about how this works to implement it yet
            } else if(key.equals("TOPICLEN")) {
                if(StringOperations.isInteger(val))
                    this.maxTopicLength = Integer.parseInt(val);
            } else if(key.equals("KICKLEN")) {
                if(StringOperations.isInteger(val))
                    this.maxKickMessageLength = Integer.parseInt(val);
            } else if(key.equals("CHANNELLEN")) {
                if(StringOperations.isInteger(val))
                    this.maxChannelLength = Integer.parseInt(val);
            } else if(key.equals("MAXTARGETS")) {
                if(StringOperations.isInteger(val))
                    this.maxTargets = Integer.parseInt(val);
            }
        } else {
            // handle valueless keys
            if(key.equals("WALLCHOPS")) {
                /* see STATUSMSG
                this.parseISupport("STATUSMSG", "@");
                 */
            } else if(key.equals("WALLVOICES")) {
                /* see STATUSMSG
                this.parseISupport("STATUSMSG", "+");
                 */
            } else if(key.equals("WHOX")) {
                this.whoXSupported = true;
            } else if(key.equals("CPRIVMSG")) {
                this.cPrivmsgSupported = true;
            } else if(key.equals("CNOTICE")) {
                this.cNoticeSupported = true;
            }
        }
    }
    
    /**
     * Puts a key back through the parser method again with its value.
     */
    private void pushISupportAgain(String key) {
        this.parseISupport(key.toUpperCase(), this.iSupport.get(key.toUpperCase()));
    }
    
    /**
     * This will ensure a mode exists in channelModes, or will create it if it
     * does not yet.
     */
    private void ensureChannelModeDefinitionExistence(Character mode, char modeType) {
        ChannelModeDefinition newModeDef = new ChannelModeDefinition(mode, modeType);
        
        if(!getChannelModes().contains(newModeDef)) {
            getChannelModes().add(newModeDef);
        }
    }
    
    /**
     * nickPrefixModes contains modes that are actually a subset of channelModes.
     * This method ensures that channelModes contains the latest nickPrefixModes
     * when called.
     */
    private void syncNickPrefixModes() {
        getChannelModes().removeAll(getNickPrefixModes()); // clear any duplicate, but older prefix definitions
        getChannelModes().addAll(getNickPrefixModes());
    }
    
    
    //         +++++++++++++++++++++++++++++++++++
    //         ****** BEGIN UTILITY METHODS ******
    //         +++++++++++++++++++++++++++++++++++
    
    /**
     * Returns the casemappingDefinition guessed or known to be supported by the server.
     */
    public CasemappingDefinition getCasemappingDefinition() {
        return this.casemappingDefinition;
    }
    
    /**
     * Returns a channel mode definition by its mode character.
     *
     * @return The channel mode definition or null if it is not found.
     */
    public ChannelModeDefinition getChannelModeByChar(Character mode) {
        Iterator<ChannelModeDefinition> i = getChannelModes().iterator();
        while(i.hasNext()) {
            ChannelModeDefinition def = i.next();
            if(def.getMode().equals(mode))
                return def;
        }
        
        return null;
    }
    
    /**
     * Returns a channel type definition by its prefix character.
     *
     * @param prefix The prefix character.
     * @return The channel type definition or null if none found.
     */
    public ChannelTypeDefinition getChannelTypeByPrefix(Character prefix) {
        for(ChannelTypeDefinition def : this.channelTypes) {
            if(def.getPrefix().equals(prefix))
                return def;
        }
        
        return null;
    }
    
    /**
     * Determines if a character is a channel name prefix based on internal known prefixes.
     *
     * @param prefix The potential prefix character.
     * @return True if prefix, false otherwise.
     */
    public boolean isChannelPrefix(Character prefix) {
        return this.getChannelTypeByPrefix(prefix) != null;
    }
    
    //         +++++++++++++++++++++++++++++++++++++++
    //         ****** BEGIN GET/KNOWN ACCESSORS ******
    //         +++++++++++++++++++++++++++++++++++++++
    
    /**
     * Returns a copy of the key=value pairs that this server expresses support
     * for or those which the parser initializes with as defaults.
     *
     * @see ServerISupportReply
     */
    public Map<String, String> getISupport() {
        return (Map) iSupportPure.clone();
    }
    
    /**
     * Returns a copy of the nickname prefix modes (that is, those which
     * modify a user's prefix character in a channel, such as +o).
     */
    public List<ChannelNickPrefixModeDefinition> getNickPrefixModes() {
        if(this.isNickPrefixModesKnown())
            return (List) nickPrefixModes.clone();
        else
            return DEFAULT_NICKPREFIXMODES;
        
    }
    
    /**
     * Determines if we know the nickname prefix modes yet.
     *
     * @return True if known, false otherwise.
     */
    public boolean isNickPrefixModesKnown() {
        return !this.nickPrefixModes.isEmpty();
    }
    
    /**
     * Returns a copy of the channel type definitions.
     */
    public Set<ChannelTypeDefinition> getChannelTypes() {
        if(this.isChannelTypesKnown())
            return (Set) channelTypes.clone();
        else
            return DEFAULT_CHANNELTYPES;
    }
    
    /**
     * Determines if we know the channel types yet.
     *
     * @return True if known, false otherwise.
     */
    public boolean isChannelTypesKnown() {
        return !this.channelTypes.isEmpty();
    }
    
    /**
     * Returns a copy of the supported channel modes (including those that are
     * nickname prefix modes).
     */
    public Set<ChannelModeDefinition> getChannelModes() {
        if(this.isChannelModesKnown())
            return (Set) channelModes.clone();
        else
            return DEFAULT_CHANNELMODES;
    }
    
    /**
     * Determines if we know the channel modes yet.
     *
     * @return True if known, false otherwise.
     */
    public boolean isChannelModesKnown() {
        return !this.channelModes.isEmpty();
    }
    
    /**
     * Determines the maximum amount of modes you can set on a channel that are
     * accompanied by parameters. As an example, if it is 3, you could only
     * add/remove up to 3 operators in a single mode change. This is largely
     * used by vaquero's outbound data system to clean up modes you may attempt
     * to set. Returns -1 if it is unknown.
     *
     * @return Maximum channel modes you may set with a parameter or -1 if unknown.
     */
    public int getMaxChanModesWithParam() {
        return maxChanModesWithParam;
    }
    
    /**
     * @see #getMaxChanModesWithParam()
     */
    public boolean isMaxChanModesWithParamKnown() {
        return this.getMaxChanModesWithParam() > -1;
    }
    
    /**
     * Determines the maximum nickname length allowed by the server. Returns -1 if
     * it is unknown.
     *
     * @return Maximum nickname length or -1 if unknown.
     */
    public int getMaxNickLength() {
        return maxNickLength;
    }
    
    /**
     * @see #getMaxNickLength()
     */
    public boolean isMaxNickLengthKnown() {
        return this.getMaxNickLength() > -1;
    }
    
    /**
     * Determines the maximum topic length allowed by the server. Returns -1 if
     * it is unknown.
     *
     * @return Maximum topic length or -1 if unknown.
     */
    public int getMaxTopicLength() {
        return maxTopicLength;
    }
    
    /**
     * @see #getMaxTopicLength()
     */
    public boolean isMaxTopicLengthKnown() {
        return this.getMaxTopicLength() > -1;
    }
    
    /**
     * Determines the maximum length of a kick comment allowed by the server.
     * Returns -1 if it is unknown.
     *
     * @return Maximum kick comment length or -1 if unknown.
     */
    public int getMaxKickMessageLength() {
        return maxKickMessageLength;
    }
    
    /**
     * @see #getMaxKickMessageLength()
     */
    public boolean isMaxKickMessageLengthKnown() {
        return this.getMaxKickMessageLength() > -1;
    }
    
    /**
     * Returns the maximum channel length allowed by the server. Returns -1 if
     * it is unknown.
     *
     * @return Maximum channel length or -1 if unknown.
     */
    public int getMaxChannelLength() {
        return maxChannelLength;
    }
    
    /**
     * @see #getMaxChannelLength()
     */
    public boolean isMaxChannelLengthKnown() {
        return this.getMaxChannelLength() > -1;
    }
    
    /**
     * Determines the maximum number of targets you may choose in a NOTICE or
     * PRIVMSG (basically, a notice and a message). Returns -1 if it is unknown.
     *
     * @return Maximum number of PRIVMSG/NOTICE targets or -1 if unknown.
     */
    public int getMaxTargets() {
        return maxTargets;
    }
    
    /**
     * @see #getMaxTargets()
     */
    public boolean isMaxTargetsKnown() {
        return this.getMaxTargets() > -1;
    }
    
    /**
     * Returns the general network name that this server is a member of. Returns
     * null if it is unknown.
     *
     * @return Network name or null if unknown.
     */
    public String getNetworkName() {
        return networkName;
    }
    
    /**
     * @see #getNetworkName()
     */
    public boolean isNetworkNameKnown() {
        return this.getNetworkName()  != null;
    }
    
    /**
     * Determines the casemappingDefinition that this IRC server utilizes for string
     * comparisons. Defaults to
     * 
     * @return The server case mapping or null if unknown.
     */
    public String getCaseMapping() {
        if(this.isCaseMappingKnown()) {
            return caseMapping;
        } else {
            return DEFAULT_CASEMAPPING;
        }
    }
    
    /**
     * @see #getCaseMapping()
     */
    public boolean isCaseMappingKnown() {
        return this.caseMapping != null;
    }
    
    /**
     * Determines if this server supports the WHOX command.
     * This command can prove valuable to tracking additional
     * user information when on compatible servers.
     *
     * @return True if WHOX is supported, false otherwise.
     */
    public boolean isWhoXSupported() {
        return whoXSupported;
    }
    
    /**
     * Determines if this server supports CPRIVMSG, a special form of PRIVMSG
     * designed to bypass a server's flooding security in special situations.
     * This command originated on the undernet ircd, ircu. Normally, ircu
     * will begin blocking your PRIVMSGs or NOTICEs if you try to send
     * them to too many different people at once. CPRIVMSG allows you to
     * send a private message to a user that bypasses this throttling mechanism
     * if the two following conditions are met:
     *   1. You are in the same channel as the other user
     *   2. You have operator privileges (+o) in the channel
     * Obviously, this is useful for bots that must send a fairly large
     * volume of messages to multiple users in a channel that it is an
     * operator in. You can find out more information in the ircu /docs/ folder,
     * or quite quickly on google.
     *
     * @return True if CPRIVMSG is supported, false otherwise.
     */
    public boolean isCPrivmsgSupported() {
        return cPrivmsgSupported;
    }
    
    /**
     * This is the exact same as CPRIVMSG, but it is for NOTICEs.
     *
     * @return True if CNOTICE is supported, false otherwise.
     * @see #isCPrivmsgSupported()
     */
    public boolean isCNoticeSupported() {
        return cNoticeSupported;
    }
    
    public String toString() {
        String r = "";
        r += "Casemapping: " + this.getCaseMapping() + "\n";
        r += "Casemapping definition: " + this.getCasemappingDefinition() + "\n";
        r += "Channel modes: " + this.getChannelModes() + "\n";
        r += "Channel types: " + this.getChannelTypes() + "\n";
        r += "ISupport tokens (pure): " + this.getISupport() + "\n";
        r += "Max. channel modes with param.: " + this.getMaxChanModesWithParam() + "\n";
        r += "Max. channel name length: " + this.getMaxChannelLength() + "\n";
        r += "Max. kick message length: " + this.getMaxKickMessageLength() + "\n";
        r += "Max. nickname length: " + this.getMaxNickLength() + "\n";
        r += "Max. targets: " + this.getMaxTargets() + "\n";
        r += "Max. topic length: " + this.getMaxTopicLength() + "\n";
        r += "Network name: " + this.getNetworkName() + "\n";
        r += "Nickname prefix modes: " + this.getNickPrefixModes() + "\n";
        
        return r;
    }
}
