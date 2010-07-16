/**
 * This sends a raw command to the server. You are highly discouraged from
 * using this command since it robs Vaquero of a lot of its automated 
 * faculties available to outbound commands. Using this to sent certain
 * commands when you're using the advanced Dispatcher can desynchronize 
 * the dispatcher and make it stop working entirely.
 *
 * So, please, only use this in stripped-down, basic clients!
 */

package com.packethammer.vaquero.outbound.commands.basic;

import com.packethammer.vaquero.outbound.commands.IRCCommand;
import com.packethammer.vaquero.util.protocol.IRCRawLine;

public class IRCRawCommand extends IRCCommand {
    private IRCRawLine raw;
    
    /**
     * Initializes this command with a raw IRC line. 
     *
     * @param line The raw line.
     */
    public IRCRawCommand(String line) {
        raw = IRCRawLine.parse(line);
    }
    
    public IRCRawLine renderForIRC() {
        return raw;
    }    
   
    public boolean isSendable() {
        return raw != null;
    }
}
