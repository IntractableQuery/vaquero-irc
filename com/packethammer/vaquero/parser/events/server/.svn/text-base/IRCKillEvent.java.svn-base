/*
 * Occurs when a user (typically just us) is killed. Normally, we don't get this
 * event when other users are killed -- they just quit the network with a regular
 * QUIT that contains information related to them being killed.
 *
 * ARGS: KILL <NICKNAME> <MESSAGE>
 */

package com.packethammer.vaquero.parser.events.server;

import java.util.Date;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.interfaces.IRCNicknameTargetedEventI;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;

public class IRCKillEvent extends IRCEvent implements IRCNicknameTargetedEventI {    
    public IRCKillEvent() {
    }

    /**
     * Returns the nickname of the person being killed. Typically, this is going
     * to be us.
     */
    public String getTarget() {
        return this.getSecondArgument();
    }
    
    /**
     * Returns the reason for the kill.
     */
    public String getMessage() {
        return this.getArg(2);
    }
 
    public String toString() {
        return "[KILL] " + this.getTarget() + " was killed (" + this.getMessage() + ")";
    }
}
