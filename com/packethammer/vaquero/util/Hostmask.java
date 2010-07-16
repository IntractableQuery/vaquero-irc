/**
 * Represents the most common form of user identification on an IRC server; 
 * the hostmask. A hostmask is in the form "nickname!ident@hostname"
 * This class can parse and properly encapsulate its 3 components. Be aware that 
 * hostmasks are not always necessarily in said format; messages coming directly 
 * from an IRC server itself can go so far as to simply be "hostname"
 * (an example of this is an IRC server itself messaging a channel or setting a mode)
 *
 * Note: A hostmask may have originally referred to the mask that is used
 * against a user's information in the form nickm!ident@host (such as the mask 
 * '*!*@someisp??.*'). Sorry, I don't mean to confuse you, but this is class
 * does not specifically represent such a mask, just a user's nick, ident, 
 * and hostname information (although, it can certainly be used as a 
 * mask for nick!ident@host data). Common naming for any "nick!ident@host" 
 * has been "hostmask" for a while, even though there's usually no mask in it!
 */

package com.packethammer.vaquero.util;

public class Hostmask implements Comparable {
    private String nickname;
    private String ident;
    private String host;
    
    public Hostmask() {
        this(null, null, null);
    }
    
    /**
     * Creates a new hostmask from the three important integral components.
     * Passing null for any of these parameters is valid, as it indicates that
     * the value is unknown (this will be accounted for when generating string
     * representations of this object). 
     *
     * @param nick The nickname.
     * @param ident The ident.
     * @param host The hostname.
     */
    public Hostmask(String nick, String ident, String host) {
        this.nickname = nick;
        this.ident = ident;
        this.host = host;
    }

    /**
     * Returns the nickname component of this hostmask. Returns null if no
     * nickname exists in the hostmask. The nickname is casemapped.
     * 
     * @return Nickname.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Returns the ident (called "login" by pircbot) component of this
     * hostmask. Returns null if no ident exists in the hostmask.
     * 
     * @return Ident.
     */
    public String getIdent() {
        return ident;
    }

    /**
     * Returns the hostname component of this hostmask. Returns null if no
     * hostname exists in the hostmask.
     * 
     * @return Remote hostname.
     */
    public String getHost() {
        return host;
    }
    
    public void setNickname(String nick) {
        this.nickname = nick;
    }
    
    public void setIdent(String ident) {
        this.ident = ident;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    
    /** 
     * Returns an empty string instead of null if there is no nickname set. 
     * Otherwise, you get back the nickname.
     *
     * @return Hostmask nickname.
     */
    public String getNonNullNickname() {
        // returns an abitrary casemapping -- it doesn't really matter anyway.
        return (getNickname() == null) ? "" : getNickname();
    }
    
    /** 
     * Returns an empty string instead of null if there is no ident set. 
     * Otherwise, you get back the ident.
     *
     * @return Hostmask ident.
     */
    public String getNonNullIdent() {
        return (getIdent() == null) ? "" : getIdent();
    }
    
    /** 
     * Returns an empty string instead of null if there is no hostname set. 
     * Otherwise, you get back the hostname.
     *
     * @return Hostmask hostname.
     */    public String getNonNullHost() {
        return (getHost() == null) ? "" : getHost();
    }

    /**
     * Parses a string of text to extract the hostmask. A hostmask can be in one
     * of the following 3 forms:
     * 
     * nickname!ident@hostname
     * ident@hostname
     * hostname
     * 
     * Passing an invalid hostmask will result in an IllegalArgumentException.
     * 
     * @param hostmask The hostmask to parse.
     */
    public static Hostmask parseHostmask(String hostmask) throws IllegalArgumentException {
        String nick = "";
        String ident = "";
        String host = "";
        
        // parse
        hostmask = hostmask.trim();
        String[] atSplit = hostmask.split("@");
        if(atSplit.length == 1) {
            // format is just "hostmask"
            host = atSplit[0];
        } else if(atSplit.length == 2) {
            // format is definitely "?????@hostmask"
            host = atSplit[1];
            String[] bangSplit = atSplit[0].split("!");
            if(bangSplit.length == 2) {
                // format is "nick!ident@hostmask"
                nick = bangSplit[0];
                ident = bangSplit[1];                
            } else if (bangSplit.length == 1) {
                // format is "ident@hostmask"
                ident = bangSplit[0];
                
            } else {
                // there is less or more than one "!" in the string
                throw new IllegalArgumentException("Invalid hostmask: " + hostmask);
            }
        } else {
            // there is less or more than one "@" in the string, this is not good
            throw new IllegalArgumentException("Invalid hostmask: " + hostmask);
        }
        
        if(nick.length() == 0)
            nick = null;
        if(ident.length() == 0)
            ident = null;
        if(host.length() == 0)
            host = null;
        
        return new Hostmask(nick, ident, host);
    }

    /**
     * Returns this hostmask as a valid hostmask string. It will return it in one
     * of the following forms, depending on internal data known:
     * 
     * nick!ident@host
     * ident@host
     * host
     * ???      (this is only if we cannot apply one of the logical forms)
     * 
     * @return The friendly/logical hostmask format.
     */
    public String getShortHostmask() {
        if(this.getHost() != null) {
            if(this.getIdent() != null) {
                if(this.getNickname() != null) {
                    // all is known
                    return this.getHostmask();
                } else {
                    // only ident and host is known
                    return this.getIdentAtHost();
                }
            } else {
                // ident unknown, so it is just a hostmask
                return this.getHost();
            }
        } else {
            // we have a non-logical hostmask
            return "???";
        }
    }

    /**
     * Returns this hostmask in the form nick!ident@hostname -- if one of the 3 
     * values is null, it will be empty in the output. As an example, if only the 
     * hostname "google.com" is known, then it will return "!@google.com". This 
     * method is obviously best for matching hostmasks against broad hostmasks
     * that may utilize wildcard matching.
     *
     * @return The hostmask in fullest form.
     */
    public String getHostmask() {
        return this.getNonNullNickname() + "!" + this.getIdentAtHost();
    }
    
    /**
     * Returns the full hostmask. This is the same as calling getHostmask().
     *
     * @return The hostmask in fullest form.
     * @see #getHostmask()
     */
    public String getFullHostmask() {
        return this.getHostmask();
    }
    
    /**
     * Returns this hostmask in ident@hostname form.
     *
     * @return A string representing this partial hostmask.
     */
    public String getIdentAtHost() {
        return this.getNonNullIdent() + "@" + this.getNonNullHost();
    }
    
    /**
     * A hostmask is a server hostmask if only the hostname is present.
     *
     * @return True if this is a server hostmask, false otherwise.
     */
    public boolean isServerHostmask() {
        return getNickname() == null && getIdent() == null && getHost() != null;
    }
    
    /**
     * Determines if this is a fully-formed hostmask with nickname, ident, and hostname 
     * known.
     *
     * @return True if this hostmask is fully formed, false otherwise.
     */
    public boolean isFullyFormedHostmask() {
        if(getNickname() != null && getIdent() != null && getHost() != null) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * This returns a string that is most ideal for representing a hostmask source
     * in something such as a chat GUI. If the user nickname is known, that is
     * what is returned. Otherwise, the host is returned, since it is entirely
     * possible a server is the actual origin of the hostmask. This is best
     * used for logging and other such human-read activities.
     *
     * @return The hostmask's nickname or host, or null if neither exist.
     */
    public String getSimpleRepresentation() {
        if(this.getNickname() != null)
            return this.getNickname().toString();
        else if(this.getHost() != null)
            return this.getHost();
        else
            return null;
    }
    
    /**
     * @see #compareTo(Object)
     */
    public boolean equals(Object o) {
        return compareTo(o) == 0;
    }
    
    /**
     * Hostmasks are compared by going through the nick, ident, then hostname
     * using String's compareTo method.
     *
     * Be aware that this does not take into account a network's casemapping, 
     * so it may not be ideal for usage in the context of an IRC server.
     */
    public int compareTo(Object o) {
        if(o instanceof Hostmask) {
            Hostmask h = (Hostmask) o;
            int r = compareTwoStrings(this.getNickname(), h.getNickname());
            if(r == 0) {
                r = compareTwoStrings(this.getIdent(), h.getIdent());
                if(r == 0) {
                    r = compareTwoStrings(this.getHost(), h.getHost());
                    return r;
                }
            } 
                
            return r;
        } else {
            throw new RuntimeException("Object is not Hostmask");
        }
    }
    
    /* Compares two strings, allowing one to be null.  */
    private int compareTwoStrings(String str1, String str2) {
        if(str1 == null && str2 == null)
            return 0;
        else if(str1 != null && str2 != null) 
            return str1.compareTo(str2);
        else
            return -1;
    }
    
    public String toString() {
        return getHostmask();
    }
}
