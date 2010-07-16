/**
 * RPL_WHOISSERVER
 * "<nick> <server> :<server_info>"
 *
 * Part of WHOIS reply.
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;

public class WhoisServerReply extends IRCNumericEvent {
    
    public WhoisServerReply() {
    }
    
    /**
     * Returns the nickname of the user.
     */
    public String getNickname() {
        return this.getNumericArg(0);
    }
    
    /**
     * Returns the name of the server that the user is on.
     */
    public String getServer() {
        return this.getNumericArg(1);
    }
    
    /**
     * Returns the infotext for the server that the user is on.
     */    
    public String getServerInfotext() {
        return this.getNumericArg(2);
    }
    
    public boolean validate() {
        return this.numericArgumentCount() == 3;
    }
    
    public int getHandledNumeric() {
        return this.RPL_WHOISSERVER;
    }
    
    public String toString() {
        return super.toString() + ", NICK:" + this.getNickname() + ", SERVER:" + this.getServer() + ", SERVERINFO:" + this.getServerInfotext();
    }
}
