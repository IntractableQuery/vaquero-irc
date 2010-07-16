/*
 * RPL_YOURHOST
 * "Your host is <servername>, running version <ver>"
 *
 * Note that ServerInfoReply is best for extracting server info.
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.packethammer.vaquero.parser.StringOperations;
import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;

public class YourHostReply extends IRCNumericEvent {  
    public static final Pattern extendedPattern = Pattern.compile("(?i)Your host is (.*), running version (.*)$");
    private Matcher match;
    
    public YourHostReply() {
    }
    
    /**
     * Returns the server information string accompanying this numeric.
     */
    public String getServerInformation() {
        return this.getNumericFirstArg();
    }
    
    /**
     * Returns the server name located within the server information.
     *
     * @return Server name.
     */
    public String getServerName() {
        return match.toMatchResult().group(1);
    }
    
    /**
     * Returns the server version located within the server information.
     *
     * @return Server version.
     */
    public String getServerVersion() {
        return match.toMatchResult().group(2);
    }
    
    public boolean validate() {
        if(this.numericArgumentCount() == 1) {
            match = extendedPattern.matcher(this.getServerInformation()); 
            return match.find();
        } else {
            return false;
        }
    }
    
    public int getHandledNumeric() {
        return this.RPL_YOURHOST;
    }
    
    public String toString() {
        return super.toString() + ", SERVERNAME: " + this.getServerName() + ", SERVERVERSION: " + this.getServerVersion() + ", SERVERINFO: " + this.getServerInformation();
    }
}
