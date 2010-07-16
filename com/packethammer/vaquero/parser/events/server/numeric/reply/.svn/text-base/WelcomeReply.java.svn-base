/*
 * RPL_WELCOME
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import java.util.Date;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;

public class WelcomeReply extends IRCNumericEvent {
    public WelcomeReply() {
    }
    
    /**
     * Returns the welcome message.
     */
    public String getWelcomeMessage() {
        return this.getNumericFirstArg();
    }
    
    public boolean validate() {
        return this.numericArgumentCount() == 1;
    }
    
    public int getHandledNumeric() {
        return this.RPL_WELCOME;
    }
    
    public String toString() {
        return super.toString() + ", WELCOME: " + this.getWelcomeMessage();
    }
}
