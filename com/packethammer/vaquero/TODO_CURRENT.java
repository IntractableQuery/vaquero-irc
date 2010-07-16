/*
 * temp class -- I just jot stuff down in here to keep from forgetting it...
 */

package com.packethammer.vaquero;

public class TODO_CURRENT {
    /*
     * CUR: 
     *
     * probably need to work on a filter for modes to adjust nicknames there,
     * too
     *
     * reconsider the decision on auto-subclassing the channel/user modes
     * that the parser spits out
     *
     * need release timestamp for outbound commands
     *
     * may need subclasses of WHO/WHOX commands for queries that only target
     * nicknames (so they can be auto-adjusted if we use
     * the nickname history thing)
     * 
     *
     * timing for the wait queue needs some base way to force commands to wait
     * x miliseconds before actual release to the timing scheme, so they can
     * be optimized if sent at a rapid rate
     *
     * rebuild the irc numerics before release
     *
     *-------------
     * 
     * channelmode/usermode need a helper class for isOp() etc.
     * 
     * 
     * 
     * Hostmask needs equals/compareTo/hashCode methods
     * 
     * Need server/undernet-specific outbound commands for CPRIVMSG, CNOTICE, 
     * WHOX, etc.
     * 
     * allow the tracker/outbound actions system to take advantage of 
     * server-specific commands if possible. (WHOX, CPRIVMSG, etc.)
     * 
     * precedence must be handled for o/v @/+ with a method in the server context somewhere@!
     * 
     * we have irc/2 numerics list, but this is such a pain
     *
     * Look at making tracked channels/users highly event-based (like the generate their own events when they get opped, etc.)
     * may need a custom collection as discussed previously -- later: still need to consider this
     *
     */
    
    // use this later somewhere:
    
    /**
     * Returns this mode's precedence. This is a value that is directly relative
     * to an IRC server context. The higher it is, the greater precedence it has
     * over other prefix modes. It is meant to be used to determine if this mode's
     * prefix should be shown instead of another when we can only display one prefix
     * character. 
     * 
     * As an example, if a network has user prefix modes 'o' and 'v', with 
     * symbols '@' and '+' repspectively, then just by RFC1459, we know that
     * whatever precedence value 'o' has should be greater than whatever 'v' has.
     * 
     */
    /*
    public int getPrecedence() {
        return precedence;
    }
     */
    
    /**
     * This method is provided because multiple mode definitions may be floating 
     * around in memory while the parser's tracking system may discover modes
     * that have higher presedence than this mode. It would be a poor decision
     * to discard this mode definition and create a new one, since the same modes
     * with different precedence levels would be floating around. Thus, it is 
     * important that this can be changed at any time. Unless you are accessing
     * this from within the tracking system itself, or otherwise have an extremely
     * compelling reason, you should NOT change this value, as it is meant to be
     * modified only by the parsing system.
     *
     * @param val The new precedence level for this prefix mode.
     *
     */
    /*
    public void setPrecedence(int val) {
        this.precedence = val;
    }
     */
    
    /**
     * Determines if this mode's prefix character has precedence over another 
     * mode's character.
     *
     * @return True if this mode has a higher precedence, false otherwise.
     */
    /*
    public boolean hasPrecedenceOver(ChannelNickPrefixModeDefinition mode) {
        return this.getPrecedence() > mode.getPrecedence();
    }
     */
     
}
