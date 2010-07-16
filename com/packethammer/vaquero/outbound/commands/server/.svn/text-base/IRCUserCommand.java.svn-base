/**
 * The USER command allows you to provide the server with your ident ("user")
 * and realname (infotext). It has a "server" parameter, but most modern servers
 * ignore this. This command is used in the initial connection sequence and
 * should come after the NICK command.
 *
 * This is RFC2812-formed. Support for the on-connect usermode(s) bitmask is not included
 * since I belive it is not entirely useful. If you've a compelling reason to
 * have it added (and to have the client classes modified), let me know!
 */

package com.packethammer.vaquero.outbound.commands.server;

import com.packethammer.vaquero.outbound.commands.IRCCommand;
import com.packethammer.vaquero.util.protocol.IRCRawLine;

public class IRCUserCommand extends IRCCommand {
    private String realname;
    private String ident;
    
    /**
     * Initializes this command with the realname and ident to use.
     *
     * @param realname The realname (infotext).
     * @param ident The ident ('user login').
     */
    public IRCUserCommand(String realname, String ident) {
        this.realname = realname;
        this.ident = ident;
    }
    
    /**
     * Returns our real name (infotext).
     */
    public String getRealname() {
        return realname;
    }

    /**
     * Sets the real name (infotext).
     */
    public void setRealname(String realname) {
        this.realname = realname;
    }

    /** 
     * Returns the ident ('user login').
     */
    public String getIdent() {
        return ident;
    }

    /**
     * Sets the ident ('user login').
     */
    public void setIdent(String ident) {
        this.ident = ident;
    }
    
    public IRCRawLine renderForIRC() {
        return IRCRawLine.buildRawLine(true, "USER", this.getIdent(), "0", "0", this.getRealname());
    }
   
    public boolean isSendable() {
        return this.getIdent() != null && this.getRealname() != null;
    }


}
