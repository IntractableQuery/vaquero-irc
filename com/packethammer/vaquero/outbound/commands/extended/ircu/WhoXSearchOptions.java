/**
 * This class defines what information we are trying to obtain from the server,
 * so that not only can we easily dispatch WHOX queries using it, but also
 * properly handle WHOX replies since it is impossible to parse them unless
 * we know what the original query asked for.
 *
 * This class is used for both sending and receiving WHOX commands. The reason
 * is, these options (at least when search fields are defined) will affect
 * the format of the data that the server gives us. As such, it is important to
 * keep the same set of options we used to send a query around to interpret
 * the reply later.
 */

package com.packethammer.vaquero.outbound.commands.extended.ircu;

import java.util.HashSet;
import java.util.Set;

public class WhoXSearchOptions {
    private HashSet<Character> searchFlags;
    private HashSet<Character> searchFields;
    private int queryTag = -1;
    
    /** Forces the search mask to only be used against nicknames */
    public static final char SEARCHFLAG_NICK = 'n';
    /** Forces the search mask to only be used against idents */
    public static final char SEARCHFLAG_IDENT = 'u';
    /** Forces the search mask to only be used against hostnames */
    public static final char SEARCHFLAG_HOST = 'h';
    /** Forces the search mask to only be used against numeric IP addresses (note: this is the only way to use a mask that uses a bitcount/bitmask IP range search format) */
    public static final char SEARCHFLAG_IP = 'i';
    /** Forces the search mask to only be used against canonic server names */
    public static final char SEARCHFLAG_SERVER = 's';
    /** Forces the search mask to only be used against the user's realname/infotext */
    public static final char SEARCHFLAG_REALNAME = 'r';
    /** Forces the search mask to only be used against the user's account name (Doesn't work on GameSurge) */
    public static final char SEARCHFLAG_ACCOUNT = 'a';
    
    /** This is defined in RFC1459; it returns only users which are server operators */
    public static final char SEARCHFLAG_OPERATORS = 'o';
    /** This supposedly returns join-delayed channel members (this is probably related to mode D/d) (Doesn't work on GameSurge) */
    public static final char SEARCHFLAG_JOINDELAYED = 'd';
    /** If you are a server operator, this flag will allow you to bypass normal security restrictions in the WHO reply to see people's real hosts, etc. */
    public static final char SEARCHFLAG_RESERVED_OPERATORS = 'x';
    
    /** Indicates we want the (first) channel name field returned to us */
    public static final char SEARCHFIELD_CHANNEL = 'c';
    /** Indicates we want the distance in hops (hopcount) field returned to us */
    public static final char SEARCHFIELD_HOPCOUNT = 'd';
    /** Indicates we want the informational flags field returned to us (this contains their away status, operator status, and ALL of their user-prefix modes (unlike regular WHO reply)) */
    public static final char SEARCHFIELD_INFOFLAGS = 'f';
    /** Indicates we want the hostname field returned to us */
    public static final char SEARCHFIELD_HOSTNAME = 'h';
    /** Indicates we want the IP address field returned to us */
    public static final char SEARCHFIELD_IP = 'i';
    /** Indicates we want the idle time field returned to us (ircu 2.10.11+ -- note this is 0 for 'remote users' -- it is more or less useless on GameSurge)*/
    public static final char SEARCHFIELD_IDLETIME = 'l';
    /** Indicates we want the nickname field returned to us */
    public static final char SEARCHFIELD_NICK = 'n';
    /** Indicates we want the realname (infotext) field returned to us */
    public static final char SEARCHFIELD_REALNAME = 'r';
    /** Indicates we want the server name field returned to us */
    public static final char SEARCHFIELD_SERVER = 's';
    /** Indicates we want the query tag (querytype) field returned to us */
    public static final char SEARCHFIELD_QUERYTYPE = 't';
    /** Indicates we want the ident field returned to us (this is supposed to contain the tilde (unresolved ident) prefix if it exists) */
    public static final char SEARCHFIELD_IDENT = 'u';
    /** Indicates we want the account name field returned to us */
    public static final char SEARCHFIELD_ACCOUNTNAME = 'a';
    /** Indicates we want the oplevel field returned to us (not sure what this is -- see ircu docs for more info -- not supported on GameSurge) */
    public static final char SEARCHFIELD_OPLEVEL = 'o';
    
    /**
     * This string contains the characters representing each field in their 
     * natural order as seen in replies. This is useful for parsing replies 
     * from the server. You need to assume that the querytype field will always 
     * come first. 
     *
     * Incidently, this includes all possible fields, should you happen to want
     * to access them.
     */
    public static final String NATURAL_FIELD_ORDER = "" + SEARCHFIELD_QUERYTYPE + SEARCHFIELD_CHANNEL +
        SEARCHFIELD_IDENT + SEARCHFIELD_IP + SEARCHFIELD_HOSTNAME + SEARCHFIELD_SERVER +
        SEARCHFIELD_NICK + SEARCHFIELD_INFOFLAGS + SEARCHFIELD_HOPCOUNT + SEARCHFIELD_IDLETIME +
        SEARCHFIELD_ACCOUNTNAME + SEARCHFIELD_REALNAME;
    
    /**
     * Initializes this class with initial search options instantly.
     *
     * @param flags The flags to use in the query (see SEARCHFLAG_* constants)
     * @param fields The fields to request in the query (see SEARCHFIELD_* constants)
     */
    public WhoXSearchOptions(char[] flags, char[] fields) {
        this();
        
        for(char c : flags)
            this.addSearchFlag(c);
        
        for(char c : fields)
            this.addSearchField(c);
    }
    
    public WhoXSearchOptions() {
        searchFlags = new HashSet();
        searchFields = new HashSet();
    }

    /**
     * Returns the search flags being used in this query.
     */
    public Set<Character> getSearchFlags() {
        return searchFlags;
    }
    
    /**
     * Adds a search flag to the query. These typically will not affect 
     * reply parsing since they only limit your results. At worst, it just
     * won't work if the server doesn't support it.
     */
    public void addSearchFlag(char flag) {
        searchFlags.add(flag);
    }

    /**
     * Returns the search fields we desire in the server reply.
     */
    public Set<Character> getSearchFields() {
        return searchFields;
    }
    
    /**
     * Adds a desired search field to the query. This means the server will reply
     * with that field in its WHOX query response. Beware, some
     * servers may just drop support for the core search fields defined in
     * this class. In that case, you may be unable to receive WHOX replies
     * for that type of query until you stop using that flag.
     *
     * A WHO request with no search fields specified will result in a regular
     * WHO reply taking place. Only when you specify at least one field 
     * will you receive the special WHOX reply.
     */
    public void addSearchField(char flag) {
        searchFields.add(flag);
    }

    /**
     * Returns what is termed the 'querytype' parameter. 
     *
     * @see #setQueryTag(String)
     */
    public int getQueryTag() {
        return queryTag;
    }

    /**
     * Sets what is termed the 'querytype' parameter. It allows you to "tag"
     * a query so that the server replies with it in the reply (this is called
     * the 'querytype' field). This allows you to tie a reply directly back
     * to the original query by giving it some unique name of your choosing.     
     *
     * Note that this automatically adds the SEARCHFIELD_QUERYTYPE field flag.
     *
     * @param queryTag A number between 0 and 999, inclusive.
     * @throws IllegalArgumentException if queryTag is not between 0 and 999.
     */
    public void setQueryTag(int queryTag) {
        if(queryTag < 0 || queryTag > 999) 
            throw new IllegalArgumentException("query tag should be between 0 and 999 inclusive, was " + queryTag);
        this.queryTag = queryTag;
        this.addSearchField(SEARCHFIELD_QUERYTYPE);
    }
    
    /**
     * Renders a WHOX-formatted <OPTIONS> parameter for the search options
     * defined here.
     */
    public String renderSearchOptions() {
        String flags = new String();
        for(Character c : this.getSearchFlags())
            flags += c;
        
        String fields = new String();
        for(Character c : this.getSearchFields())
            fields += c;
        
        String options = flags + "%" + fields;
        if(this.getQueryTag() > -1)
            options += "," + this.getQueryTag();
        
        return options;
    }
}
