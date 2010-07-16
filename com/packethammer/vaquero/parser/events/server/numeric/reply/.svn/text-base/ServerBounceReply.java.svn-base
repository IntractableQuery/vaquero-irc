/*
 * RPL_BOUNCE
 * "Try server <server name>, port <port number>"
 *
 * Note: this is an ambigious reply, as it is shared with the now more common
 * RPL_ISUPPORT. As a result, it is the job of the parser to determine what
 * the event is exactly.
 *
 * @see ServerISupportReply
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.packethammer.vaquero.parser.StringOperations;
import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;

public class ServerBounceReply extends IRCNumericEvent {
    public static final Pattern extendedPattern = Pattern.compile("(?i)Try server (.+), port ([0-9]+)$");
    private Matcher match;
    
    public ServerBounceReply() {
    }
    
    /**
     * Returns the server bounce message.
     *
     * @return Server's bounce message.
     */
    public String getBounceMessage() {
        return this.getNumericFirstArg();
    }
    
    /**
     * Returns the server name (supposedly an actual domain/IP) that is suggested for the connection bounce.
     *
     * @return Server name.
     */
    public String getServerName() {
        return match.toMatchResult().group(1);
    }
    
    /**
     * Returns the server port that is suggested for the connection bounce.
     *
     * @return Server port.
     */
    public int getServerPort() {
        return Integer.parseInt(match.toMatchResult().group(2));
    }
    
    public boolean validate() {
        if(this.numericArgumentCount() >= 1) {
            match = extendedPattern.matcher(this.getBounceMessage()); 
            return match.find();
        } else {
            return false;
        }
    }
    
    public int getHandledNumeric() {
        return this.RPL_BOUNCE;
    }
    
    public String toString() {
        return super.toString() + ", SERVERNAME: " + this.getServerName() + ", PORT: " + this.getServerPort() + ", BOUNCEMESSAGE:" + this.getBounceMessage();
    }
}
