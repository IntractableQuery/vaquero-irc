/**
 * RPL_WHOREPLY
 * "<channel> <user> <host> <server> <nick>
 * ( "H" / "G" > ["*"] [ ( "@" / "+" ) ]
 * :<hopcount> <real name>"
 *
 * Documentation in the RFC(s) is a bit lacking for this reply in its 
 * usual RFC1459/2812 usage when it comes to the information contained
 * between '<nick>' and ':<hopcount>'. I'll do my best to explain it:
 * G = User is currently marked as being away
 * H = User is not marked away (they are "here")
 * * = We see this user as being a server operator
 * @/+/etc. = The ("highest") symbol corresponding to the channel mode the user has in the channel that the user is shown in
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;
import com.packethammer.vaquero.parser.tracking.IRCServerISupport;
import com.packethammer.vaquero.parser.tracking.definitions.ChannelNickPrefixModeDefinition;
import com.packethammer.vaquero.util.Hostmask;

public class WhoReply extends IRCNumericEvent {
    private int hopcount;
    private String realname;
    
    /** Creates a new instance of WhoReply */
    public WhoReply() {
    }
    
    /**
     * Determines if this user is marked as away or here.
     */
    public boolean isAway() {
        return this.getSpecialFlags().contains("G"); // don't care about H
    }

    /**
     * Determines if we can see this person as a server operator (there can be
     * hidden operators which we won't see in some cases).
     */
    public boolean isOperator() {
        return this.getSpecialFlags().contains("*");
    }

    /**
     * Returns a prefix mode definition representing the topmost mode that
     * has a prefix character that the user currently has in the channel.
     *
     * @param iSupport The ISUPPORT information for the server we are on, since the very nature of the flags makes it impossible to correctly pull out the prefix without knowing those supported beforehand
     * @return Mode definition or null if the user has no topmost mode (or this reply isn't even for a channel)
     */
    public ChannelNickPrefixModeDefinition getChannelUserPrefix(IRCServerISupport iSupport) {
        for(ChannelNickPrefixModeDefinition def : iSupport.getNickPrefixModes()) {
            if(this.getSpecialFlags().contains(def.getPrefix() + "")) {
                // found match
                return def;
            }
        }
        
        return null;
    }

    /**
     * Returns the number of hops (server distance) from us to this user. 
     */
    public int getHopcount() {
        return hopcount;
    }

    /**
     * Returns this user's realname/infoline information.
     */
    public String getRealname() {
        return realname;
    }
    
    /**
     * Returns the channel that this information will be associated with for
     * this user. Returns null if this information is not associated with
     * a channel.
     */
    public String getChannel() {
        String chan = this.getNumericArg(0);
        if(chan.equals("*")) {
            // * = no channel
            return null;
        } else {
            return chan;
        }
    }
    
    /**
     * Returns this user's ident (or otherwise known as "user login", although
     * this is an ambigious term).
     */
    public String getIdent() {
        return this.getNumericArg(1);
    }
    
    /**
     * Returns the user's host.
     */
    public String getHost() {
        return this.getNumericArg(2);
    }
    
    /**
     * Returns the name of the server that this user is on.
     */
    public String getServerName() {
        return this.getNumericArg(3);
    }
    
    /**
     * Returns the nickname of this user.
     */
    public String getNickname() {
        return this.getNumericArg(4);
    }
    
    /**
     * This convenience method returns this user's nick/ident/host information 
     * in hostmask form. Beware, it is not actually transported to us like this, 
     * so it is possible it may differ from their actual hostmask, although 
     * I've not seen a server yet that causes this sort of trouble.
     */
    public Hostmask getInfoAsHostmask() {
        return new Hostmask(this.getNickname(), this.getIdent(), this.getHost());
    } 
    
    /**
     * Returns a special set of character flags associated with this user
     * which describe their status. Their significant (ie: away, operator
     * status, etc.) can be accessed in this class, but they are here for
     * you to retrieve anyway, especially since some servers may have 
     * special flags they insert.
     */
    public String getSpecialFlags() {
        return this.getNumericArg(5);
    }
    
    public boolean validate() {
        if(this.numericArgumentCount() == 7) {
            //pull hopcount/realname which is located in an odd place in the last argument
            String compactedInfo = this.getNumericArg(6);                
            int space = compactedInfo.indexOf(" ");
            if(space > -1) {
                 hopcount = Integer.parseInt(compactedInfo.substring(0, space));
                 realname = compactedInfo.substring(space + 1);
            } else {
                return false; // abort if the server is sending us malformed WHO reply
            }
            
            return true;
        } else {
            return false;
        }
    }
    
    public int getHandledNumeric() {
        return this.RPL_WHOREPLY;
    }
    
    public String toString() {
        return super.toString() + " CHAN:" + this.getChannel() + " HOSTMASK:" + this.getInfoAsHostmask() + " AWAY:" + this.isAway() + " OPER:" + this.isOperator() + " HOPCOUNT:" + this.getHopcount() + " SERVER:" + this.getServerName() + " REALNAME:" + this.getRealname();
    }
}
