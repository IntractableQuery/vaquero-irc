/*
 * This queries the server's version. Please note that it is not a CTCP VERSION
 * request which is typically used on other IRC clients. 
 *
 * VERSION
 */

package com.packethammer.vaquero.outbound.commands.server;

import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.outbound.commands.IRCCommand;

public class IRCVersionCommand extends IRCCommand {   
    public IRCVersionCommand() {
    }
    
    public IRCRawLine renderForIRC() {
        return IRCRawLine.buildRawLine(false, "VERSION");
    }    
   
    public boolean isSendable() {
        return true;
    }
}
