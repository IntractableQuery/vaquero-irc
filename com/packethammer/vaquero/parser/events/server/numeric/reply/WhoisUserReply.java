/**
 * RPL_WHOISUSER
 * "<nick> <ident> <host> * :<real_name>"
 *
 * Part of WHOIS reply.
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;
import com.packethammer.vaquero.util.Hostmask;

public class WhoisUserReply extends IRCNumericEvent {
    
    public WhoisUserReply() {
    }
    
    /**
     * Returns the user's nickname.
     */
    public String getNickname() {
        return this.getNumericArg(0);
    }
    
    /**
     * Returns the user's ident.
     */
    public String getIdent() {
        return this.getNumericArg(1);
    }
    
    /**
     * Returns the user's hostname.
     */
    public String getHostname() {
        return this.getNumericArg(2);
    }
    
    /**
     * This convenience method returns the user information as a hostmask.
     */
    public Hostmask getAsHostmask() {
        return new Hostmask(this.getNickname(), this.getIdent(), this.getHostname());
    }
    
    /**
     * Returns this user's realname (infotext).
     */
    public String getRealname() {
        return this.getNumericArg(4);
    }

    public boolean validate() {
        return this.numericArgumentCount() == 5;
    }
    
    public int getHandledNumeric() {
        return this.RPL_WHOISUSER;
    }
    
    public String toString() {
        return super.toString() + ", HOSTMASK:" + this.getAsHostmask() + " REALNAME:" + this.getRealname();
    }
}
