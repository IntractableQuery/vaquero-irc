/*
 * This class houses important information for PRIVMSG/NOTICE.
 */

package com.packethammer.vaquero.outbound.commands.basic;

import java.util.List;
import java.util.Vector;
import com.packethammer.vaquero.outbound.commands.IRCCommand;
import com.packethammer.vaquero.outbound.commands.interfaces.ExtendedMessageCommandI;

public abstract class MultitargetMessage extends IRCCommand implements ExtendedMessageCommandI {
    private Vector<String> targets;
    private String message;
    
    public MultitargetMessage() {
        targets = new Vector();
    }
    
    /**
     * Returns the targets of this message.
     */
    public List<String> getTargets() {
        return targets;
    }
    
    /**
     * Adds a target for this message. Avoid using this method in subclasses
     * of this class (example: do not use it in a channel message command).
     */
    public void addTarget(String target) {
        targets.add(target);
    }
    
    /**
     * Returns the message being sent.
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Sets the message being sent.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
