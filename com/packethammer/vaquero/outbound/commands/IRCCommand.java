/*
 * Represents a base IRC command that can be rendered for sending to an IRC
 * server.
 */

package com.packethammer.vaquero.outbound.commands;

import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;
import com.packethammer.vaquero.util.protocol.IRCRawLine;

public abstract class IRCCommand {    
    public IRCCommand() {
    } 
    
    /**
     * Renders this IRC command so that it can be sent to an IRC server.
     */
    public abstract IRCRawLine renderForIRC();
    
    /**
     * Determines if this IRC command is sendable with the data it currently
     * contains. Otherwise, the command manager can just discard it instead
     * of actually sending it.
     */
    public abstract boolean isSendable();
    
    // TODO: implement these for all commands in future... they're nice to have,
    // but currently serve no purpose.
    /*
     * Returns all the numeric replies that may be generated in response
     * to this command.
     *
     * These replies are derived from RFC2812, although RFC1459 still has influence on them.
     *
    public Class<IRCNumericEvent>[] getReplies() {
        return new Class[0];
    }
    
    /*
     * Returns all the numeric error replies that may be generated in response
     * to this command.
     *
     * These replies are derived from RFC2812, although RFC1459 still has influence on them.
     *
    public Class<IRCNumericEvent>[] getErrorReplies() {
        return new Class[0];
    }
     */
}
