/*
 * RPL_WHOXREPLY
 * "[querytype] [channel] [user] [IP] [host] [server] [nick] 
 *  [flags] [hops] [idle] [account] [realname]"
 *
 * This reply only occurs if the original WHO query (in WHOX format) specified
 * fields to return. Otherwise, the original WHO query is only going to generate
 * a normal RPL_WHOREPLY numeric.
 *
 * Note that there is a ton of optional information we must parse out. For this
 * reason, this class requires the original search options to properly extract
 * the information in each field. 
 *
 * This event class can't actually determine if the WHOX reply if valid
 * until it comes time to pull information from it, unlike the other event
 * classes.
 *
 * The RPL_WHOXREPLY numerics are terminated by a normal end-of-who numeric just
 * like RPL_WHOREPLY.
 *
 * Note: the [flags] information is somewhat documented in WhoReply, although
 * this class contains methods to access that flag information without
 * getting your hands dirty.
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import java.util.ArrayList;
import com.packethammer.vaquero.outbound.commands.extended.ircu.WhoXSearchOptions;
import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;
import com.packethammer.vaquero.parser.tracking.IRCServerISupport;
import com.packethammer.vaquero.parser.tracking.definitions.ChannelNickPrefixModeDefinition;
import com.packethammer.vaquero.util.Hostmask;

public class WhoXReply extends IRCNumericEvent {
    private WhoXSearchOptions options; // not from server -- provided by user of this reply
            
    public WhoXReply() {
    }
    
    /**
     * Returns the (first) channel name field associated with this query reply.
     * Returns null if there is no channel specified.
     *
     * @see #getFieldData(char)
     */
    public String getChannel() {
        String chan = this.getFieldData(WhoXSearchOptions.SEARCHFIELD_CHANNEL);
        if(chan.equals("*"))
            return null;
        else
            return chan;
    }
    
    /**
     * Returns the distance in hops (hopcount) field associated with this
     * query reply.
     *
     * @see #getFieldData(char)
     */
    public int getHopcount() {
        return Integer.parseInt(this.getFieldData(WhoXSearchOptions.SEARCHFIELD_HOPCOUNT));
    }
    
    /**
     * Returns the special (information) flags field associated with this query reply.
     * Note that this class contains methods that can be used to extract meaning
     * from these flags.
     *
     * @see #getFieldData(char)
     */
    public String getSpecialFlags() {
        return this.getFieldData(WhoXSearchOptions.SEARCHFIELD_INFOFLAGS);
    }
    
    /**
     * Returns the hostname field associated with this query reply.
     *
     * @see #getFieldData(char)
     */
    public String getHostname() {
        return this.getFieldData(WhoXSearchOptions.SEARCHFIELD_HOSTNAME);
    }
    
    /**
     * Returns the IP address field associated with this query reply.
     *
     * @see #getFieldData(char)
     */
    public String getIP() {
        return this.getFieldData(WhoXSearchOptions.SEARCHFIELD_IP);
    }
    
    /**
     * Returns the idle time field associated with this query reply.
     *
     * @see #getFieldData(char)
     */
    public int getIdleTime() {
        return Integer.parseInt(this.getFieldData(WhoXSearchOptions.SEARCHFIELD_IDLETIME));
    }
    
    /**
     * Returns the nickname field associated with this query reply.
     *
     * @see #getFieldData(char)
     */
    public String getNickname() {
        return this.getFieldData(WhoXSearchOptions.SEARCHFIELD_NICK);
    }
    
    /**
     * Returns the realname (infotext) field associated with this query reply.
     *
     * @see #getFieldData(char)
     */
    public String getRealname() {
        return this.getFieldData(WhoXSearchOptions.SEARCHFIELD_REALNAME);
    }
    
    /**
     * Returns the query tag (querytype) field associated with this query reply.
     *
     * @see #getFieldData(char)
     */
    public String getQueryTag() {
        return this.getFieldData(WhoXSearchOptions.SEARCHFIELD_QUERYTYPE);
    }
    
    /**
     * Returns the ident field associated with this query reply.
     *
     * @see #getFieldData(char)
     */
    public String getIdent() {
        return this.getFieldData(WhoXSearchOptions.SEARCHFIELD_IDENT);
    }
    
    /**
     * Returns the account name field associated with this query reply. Be aware
     * that this returns null if the user has no account name associated with 
     * them.
     *
     * @see #getFieldData(char)
     */
    public String getAccountName() {
        String account = this.getFieldData(WhoXSearchOptions.SEARCHFIELD_ACCOUNTNAME);
        
        if(account.equals("0"))
            return null;
        else 
            return account;
    }
    
    /**
     * Returns the oplevel field associated with this query reply.
     *
     * @see #getFieldData(char)
     */
    public int getOpLevel() {
        return Integer.parseInt(this.getFieldData(WhoXSearchOptions.SEARCHFIELD_OPLEVEL));
    }
    
    /**
     * Returns the server field associated with this query reply.
     *
     * @see #getFieldData(char)
     */
    public String getServer() {
        return this.getFieldData(WhoXSearchOptions.SEARCHFIELD_SERVER);
    }
    
    /**
     * This convenience method takes the nick, ident, and hostname fields
     * and combines them into a fully-formed hostmask. Be sure that
     * you queried for those fields beforehand, or you're in for an explosion.
     * 
     * @see #getNickname();
     * @see #getIdent();
     * @see #getHostname();
     * @return A hostmask (with unresolved ident tilde intact).
     */
    public Hostmask getInfoAsHostmask() {
        return new Hostmask(this.getNickname(), this.getIdent(), this.getHostname());
    }
    
    /**
     * Determines if this user is marked as away or here. 
     * 
     * Depends on the special flags field being returned.
     */
    public boolean isAway() {
        return this.getSpecialFlags().contains("G"); // don't care about H
    }

    /**
     * Determines if we can see this person as a server operator (there can be
     * hidden operators which we won't see in some cases). 
     *
     * Depends on the special flags field being returned.
     */
    public boolean isOperator() {
        return this.getSpecialFlags().contains("*");
    }
    

    /**
     * Returns the prefix modes that this user has in the target channel
     *
     * Depends on the special flags field being returned.
     *
     * @param iSupport The ISUPPORT information for the server we are on, since the very nature of the flags makes it impossible to correctly pull out the prefix without knowing those supported beforehand
     * @return A non-null list of mode definitions that may be empty.
     */
    public ArrayList<ChannelNickPrefixModeDefinition> getChannelUserPrefixModes(IRCServerISupport iSupport) {
        ArrayList<ChannelNickPrefixModeDefinition> defs = new ArrayList();
        
        for(ChannelNickPrefixModeDefinition def : iSupport.getNickPrefixModes()) {
            if(this.getSpecialFlags().contains(def.getPrefix() + "")) {
                defs.add(def);
            }
        }
        
        return defs;
    }
            
    /**
     * Given a field flag, this will return that field's data inside this reply.
     *
     * @param fieldFlag The SERVERFIELD_* constant that we want the data for.
     * @return A string of data at that field location.
     * @throw IllegalStateException if you have not setSearchOptions() yet.
     * @throw IllegalArgumentException if fieldFlag was not valid.
     * @see #setSearchOptions(WhoXSearchOptions)
     */
    public String getFieldData(char fieldFlag) {
        if(options == null) {
            throw new IllegalStateException("You may not access a WHOX reply until you provide the original options that were used for the query; see setSearchOptions()");
        } else if(!WhoXSearchOptions.NATURAL_FIELD_ORDER.contains(fieldFlag + "")) {
            throw new IllegalArgumentException("No such field flag as '" + fieldFlag + "' (valid flags: " + WhoXSearchOptions.NATURAL_FIELD_ORDER + ")"); 
        } else if(!options.getSearchFields().contains(new Character(fieldFlag))) {
            throw new IllegalArgumentException("Field flag '" + fieldFlag + "' not present in options (options have: " + options.getSearchFields() + "). Thus, it is not possible that the reply contains that field.");
        }
            
 
        int index = -1; // index will never be left as -1 because of the above checks that throw exceptions

        for(char curNaturalField : WhoXSearchOptions.NATURAL_FIELD_ORDER.toCharArray()) {
            if(options.getSearchFields().contains(new Character(curNaturalField))) {
                index++;
            }

            if(curNaturalField == fieldFlag) {
                break;
            }
        }
                  
            
        return this.getNumericArg(index);
    }
    
    /**
     * Sets the search options that were used when originally sending this
     * event. This is required if you wish to access the fields within this
     * reply.
     *
     * @param options The exact same search options used when you sent the WHOX query.
     * @throws IllegalStateException If the number of returned fields in this reply does not match the number in the options (this can be due to a field not being supported at the server, so just stop using it if this happens)
     */
    public void setSearchOptions(WhoXSearchOptions options) {
        int numFields = this.numericArgumentCount();
        int numDesiredFields = options.getSearchFields().size();

        if(numFields == numDesiredFields) {
            this.options = options;
        } else {
            throw new IllegalStateException("Original search options requested " + numDesiredFields + " fields from the server, but we got back " + numFields + " from the server. You need to double-check the fields you requested and remove those that the server may not support (or those that are not within the WHOX specification)");
        }
    }
    
    public boolean validate() {
        // well, not much to check for here...
        return this.numericArgumentCount() > 0;
    }
    
    public int getHandledNumeric() {
        return this.RPL_WHOXREPLY;
    }
    
    public String toString() {
        String txt = super.toString();
        if(this.options != null) {
            if(options.getSearchFields().contains(options.SEARCHFIELD_ACCOUNTNAME))
                txt += ", ACCOUNT:" + this.getAccountName();
            if(options.getSearchFields().contains(options.SEARCHFIELD_CHANNEL))
                txt += ", CHAN:" + this.getChannel();
            if(options.getSearchFields().contains(options.SEARCHFIELD_HOPCOUNT))
                txt += ", HOPCOUNT:" + this.getHopcount();
            if(options.getSearchFields().contains(options.SEARCHFIELD_HOSTNAME))
                txt += ", HOST:" + this.getHostname();
            if(options.getSearchFields().contains(options.SEARCHFIELD_IDENT))
                txt += ", IDENT:" + this.getIdent();
            if(options.getSearchFields().contains(options.SEARCHFIELD_NICK))
                txt += ", NICK:" + this.getAccountName();
            if(options.getSearchFields().contains(options.SEARCHFIELD_IDLETIME))
                txt += ", IDLETIME:" + this.getIdleTime();
            if(options.getSearchFields().contains(options.SEARCHFIELD_INFOFLAGS))
                txt += ", FLAGS:" + this.getSpecialFlags();
            if(options.getSearchFields().contains(options.SEARCHFIELD_IP))
                txt += ", IP:" + this.getIP();
            if(options.getSearchFields().contains(options.SEARCHFIELD_OPLEVEL))
                txt += ", OPLVL:" + this.getOpLevel();
            if(options.getSearchFields().contains(options.SEARCHFIELD_QUERYTYPE))
                txt += ", QUERYTAG:" + this.getQueryTag();
            if(options.getSearchFields().contains(options.SEARCHFIELD_SERVER))
                txt += ", SERVER:" + this.getServer();
            if(options.getSearchFields().contains(options.SEARCHFIELD_REALNAME))
                txt += ", REALNAME:" + this.getRealname();
        } else {
            txt += ", [Original WhoX options not set, cannot interpret reply]";
        }
        
        return txt;
    }
}
