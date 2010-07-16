/*
 * RPL_MYINFO
 * "<servername> <version> <available user modes> <available channel modes>"
 *
 * This is most ideal for getting well-formed and present server information.
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import java.util.Date;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;

public class ServerInfoReply extends IRCNumericEvent {
    public ServerInfoReply() {
    }
    
    /**
     * Determines the server name.
     *
     * @return Server name.
     */
    public String getServerName() {
        return this.getNumericArg(0);
    }
    
    /**
     * Determines server version name.
     *
     * @return Version name.
     */
    public String getServerVersion() {
        return this.getNumericArg(1);
    }
    
    /**
     * Determines all available user modes that the server provides.
     *
     * @return User modes.
     */
    public char[] getUserModes() {
        return this.getNumericArg(2).toCharArray();
    }
    
    /**
     * Determines all available channel modes that the server provides.
     *
     * @return Channel modes.
     */
    public char[] getChannelModes() {
         return this.getNumericArg(3).toCharArray();
    }
    
    public boolean validate() {
        return this.numericArgumentCount() >= 4;
    }
    
    public int getHandledNumeric() {
        return this.RPL_MYINFO;
    }
    
    public String toString() {
        return super.toString() + ", SERVERNAME:" + this.getServerName() + ", SERVERVERSION:" + this.getServerVersion() + ", USERMODES:" + this.getUserModes() + ", CHANNELMODES:" + this.getChannelModes();
    }
}
