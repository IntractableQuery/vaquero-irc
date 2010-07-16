/**
 * RPL_USERHOST 
 * "nickname [ "*" ] "=" ( "+" / "-" ) ident@host"
 *
 * example:
 * tim=+~timothy@myisp.net
 *
 *
 * The '*' indicates that this user is a server operator. A '+' prefix indicates
 * this user is away (with an away message). A '-' prefix indicates this
 * user is not away.
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;
import com.packethammer.vaquero.util.Hostmask;

public class UserHostReply extends IRCNumericEvent {
    public static final Pattern matchPattern = Pattern.compile("(.*)=(\\*?)([\\-,\\+])(.*)@(.*)$");
    private Matcher match;
    
    public UserHostReply() {
    }
    
    /**
     * Returns the raw data that accompanied this userhost reply.
     */
    public String getUserhostInformation() {
        return this.getNumericArg(0);
    }
    
    /**
     * Returns the user's nickname.
     */
    public String getNickname() {
        return match.group(1);
    }
    
    /**
     * Determines if this user is an server operator.
     */
    public boolean isServerOperator() {
        return !(match.group(2) != null && match.group(2).length() == 0);
    }
    
    /**
     * Determines if this user is away or here.
     */
    public boolean isAway() {
        return match.group(3).equals("-");
    }
    
    /**
     * Returns this user's ident.
     */
    public String getIdent() {
        return match.group(4);
    }
    
    /**
     * Returns this user's hostname.
     */
    public String getHostname() {
        return match.group(5);
    }
    
    /**
     * This convenience method allows you to retrieve a hostmask from the
     * contained information.
     */
    public Hostmask getAsHostmask() {
        return new Hostmask(this.getNickname(), this.getIdent(), this.getHostname());
    }
    
    public boolean validate() {
        if(this.numericArgumentCount() == 1) {
            match = matchPattern.matcher(this.getUserhostInformation()); 
            return match.find();
        } else {
            return false;
        }
    }
    
    public int getHandledNumeric() {
        return this.RPL_USERHOST;
    }
    
    public String toString() {
        return super.toString() + ", OPER:" + this.isServerOperator() + ", AWAY:" + this.isAway() + ", HOSTMASK:" + this.getAsHostmask();
    }
}
