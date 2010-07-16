/**
 * If used, this should be sent as soon as we connect to the server. It allows
 * us to provide a password to login to the server with.
 *
 * PASS <PASSWORD>
 */

package com.packethammer.vaquero.outbound.commands.server;

import com.packethammer.vaquero.outbound.commands.IRCCommand;
import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;
import com.packethammer.vaquero.parser.events.server.numeric.error.AlreadyRegisteredError;
import com.packethammer.vaquero.parser.events.server.numeric.error.NeedMoreParametersError;
import com.packethammer.vaquero.util.protocol.IRCRawLine;

public class IRCPasswordCommand extends IRCCommand {
    private String password;
    
    /**
     * Initializes this PASS command with the password to use for the server.
     *
     * @param password The password to utilize. 
     */
    public IRCPasswordCommand(String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public IRCRawLine renderForIRC() {
        return IRCRawLine.buildRawLine(false, "PASS", this.getPassword());
    }
   
    public boolean isSendable() {
        return this.getPassword() != null;
    }

    public Class<IRCNumericEvent>[] getErrorReplies() {
        return new Class[] { 
            AlreadyRegisteredError.class, 
            NeedMoreParametersError.class 
        };
    }
}
