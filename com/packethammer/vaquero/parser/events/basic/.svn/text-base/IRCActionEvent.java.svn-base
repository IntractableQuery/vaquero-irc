/*
 * Represents a CTCP ACTION event, which is typically used in most IRC clients
 * with /me or /action, resulting in something like the following:
 *
 * * Nickname speaks in the third person
 */

package com.packethammer.vaquero.parser.events.basic;

import java.util.Date;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;

public abstract class IRCActionEvent extends IRCCTCPEvent {
    /**
     * This is the text in the CTCP that prefixes the action.
     */
    public static final String ACTION_PREFIX = "ACTION";
    
    public IRCActionEvent() {
        super();
    }
    
    /**
     * Returns the action text that was used.
     *
     * @return The action text.
     */
    public String getAction() {
        return(this.getCTCPText());
    }
    
    public String toString() {
        return "* " + this.getSource().getNickname() + " " + this.getAction();
    }
}
