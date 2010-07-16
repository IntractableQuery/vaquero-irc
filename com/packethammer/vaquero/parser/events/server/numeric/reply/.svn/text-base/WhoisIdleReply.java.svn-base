/**
 * RPL_WHOISIDLE
 * "<nick> <seconds> :<info text>"
 *
 * Part of WHOIS reply.
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;

public class WhoisIdleReply extends IRCNumericEvent {
    
    public WhoisIdleReply() {
    }
    
    /**
     * Returns the nickname of the user.
     */
    public String getNickname() {
        return this.getNumericArg(0);
    }
    
    /**
     * Returns the user's idle time in seconds.
     */
    public int getIdleTime() {
        return Integer.parseInt(this.getNumericArg(1));
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
        return this.RPL_WHOISIDLE;
    }
    
    public String toString() {
        return super.toString() + ", NICK:" + this.getNickname() + ", IDLETIME:" + this.getIdleTime() + " INFO:" + this.getInfoText();
    }
}
