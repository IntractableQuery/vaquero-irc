/*
 * RPL_CREATED
 * "This server was created <date>"
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.packethammer.vaquero.parser.StringOperations;
import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;

public class ServerCreatedReply extends IRCNumericEvent {
    public static final Pattern extendedPattern = Pattern.compile("(?i)This server was created (.*)$");
    private Matcher match;
    
    public ServerCreatedReply() {
    }
    
    /**
     * Returns the entire server creation date informational string.
     */
    public String getCreatedInformation() {
        return this.getNumericFirstArg();
    }
    
    /**
     * Returns the date as a string. There is no definition for the formatting
     * of the date, and it normally contains a time too. Unless you have very
     * good date parsing faculties, this is best left to human usage.
     *
     * @return A date string or null if none was given by the server.
     */
    public String getDateString() {
        return match.toMatchResult().group(1);
    }
    
    public boolean validate() {
        if(this.numericArgumentCount() >= 1) {
            match = extendedPattern.matcher(this.getCreatedInformation()); 
            return match.find();
        } else {
            return false;
        }
    }
    
    public int getHandledNumeric() {
        return this.RPL_CREATED;
    }
    
    public String toString() {
        return super.toString() + ", DATE: " + this.getDateString() + ", CREATEDINFO: " + this.getCreatedInformation();
    }
}
