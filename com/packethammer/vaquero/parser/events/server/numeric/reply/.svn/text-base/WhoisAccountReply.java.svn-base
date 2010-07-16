/**
 * RPL_WHOISACCOUNT
 * "<nick> <authname> :<info>"
 *
 * This numeric originated with ircu (it is nonstandard). Part of WHOIS reply.
 * If the user is not logged in, you shouldn't receive this numeric as part
 * of the WHOIS reply.
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;

public class WhoisAccountReply extends IRCNumericEvent {
    
    public WhoisAccountReply() {
    }
    
    /**
     * Returns the nickname of the user.
     */
    public String getNickname() {
        return this.getNumericArg(0);
    }
    
    /**
     * Returns the account name this user is using.
     */
    public String getAccount() {
        return this.getNumericArg(1);
    }
    
    /**
     * Returns a (generally useless) line of human-readable informational
     * text.
     */    
    public String getInfoText() {
        return this.getNumericArg(2);
    }
    
    public boolean validate() {
        return this.numericArgumentCount() == 3;
    }
    
    public int getHandledNumeric() {
        return this.RPL_WHOISACCOUNT;
    }
    
    public String toString() {
        return super.toString() + ", NICK:" + this.getNickname() + ", ACCOUNT:" + this.getAccount() + " INFO:" + this.getInfoText();
    }
}
