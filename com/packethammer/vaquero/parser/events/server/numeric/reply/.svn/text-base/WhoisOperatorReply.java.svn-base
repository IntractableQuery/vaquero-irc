/**
 * RPL_WHOISOPERATOR
 * "<nick> :<info text concerning oper privileges>"
 *
 * Part of WHOIS reply to indicate user is server operator.
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;

public class WhoisOperatorReply extends IRCNumericEvent {
    
    public WhoisOperatorReply() {
    }
    
    /**
     * Returns the nickname of the user who is a server operator.
     */
    public String getNickname() {
        return this.getNumericArg(0);
    }
    
    /**
     * Returns a (generally useless) line of human-readable informational
     * text, which generally pertains to this user's operator privileges.
     */    
    public String getInfoText() {
        return this.getNumericArg(1);
    }
    
    public boolean validate() {
        return this.numericArgumentCount() == 2;
    }
    
    public int getHandledNumeric() {
        return this.RPL_WHOISOPERATOR;
    }
    
    public String toString() {
        return super.toString() + ", NICK:" + this.getNickname() + ", OPERINFO:" + this.getInfoText();
    }
}
