/**
 * RPL_ENDOFWHOIS
 * "<nick> :<info text>"
 *
 * Part of WHOIS reply, sent to terminate the reply numerics.
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;

public class EndOfWhoisReply extends IRCNumericEvent {
    
    public EndOfWhoisReply() {
    }
    
    /**
     * Returns the nickname of the user.
     */
    public String getNickname() {
        return this.getNumericArg(0);
    }
    
    /**
     * Returns a (generally useless) line of human-readable informational
     * text.
     */    
    public String getInfoText() {
        return this.getNumericArg(1);
    }
    
    public boolean validate() {
        return this.numericArgumentCount() == 2;
    }
    
    public int getHandledNumeric() {
        return this.RPL_ENDOFWHOIS;
    }
    
    public String toString() {
        return super.toString() + ", NICK:" + this.getNickname() + ", INFO:" + this.getInfoText();
    }
}
