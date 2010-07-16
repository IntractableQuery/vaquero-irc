/**
 * This sends your desired nickname to the server upon initial connection, or
 * allows you to change it from your current one.
 *
 * If you are using this for the initial connection, it should be sent after the 
 * PASS command (assuming you even send one), but before the USER command.
 *
 * NICK <NICKNAME>
 */

package com.packethammer.vaquero.outbound.commands.server;

import com.packethammer.vaquero.outbound.commands.IRCCommand;
import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;
import com.packethammer.vaquero.parser.events.server.numeric.error.ErroneousNicknameError;
import com.packethammer.vaquero.parser.events.server.numeric.error.NicknameCollisionError;
import com.packethammer.vaquero.parser.events.server.numeric.error.NicknameInUseError;
import com.packethammer.vaquero.parser.events.server.numeric.error.NoNicknameGivenError;
import com.packethammer.vaquero.parser.events.server.numeric.error.RestrictedNicknameError;
import com.packethammer.vaquero.parser.events.server.numeric.error.UnavailableResourceError;
import com.packethammer.vaquero.util.protocol.IRCRawLine;

public class IRCNickCommand extends IRCCommand {
    private String nickname;
    
    /** 
     * Initializes this command with the nickname you wish to use.
     *
     * @param nick The nickname to use.
     */
    public IRCNickCommand(String nick) {
        this.nickname = nick;
    }
    
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public IRCRawLine renderForIRC() {
        return IRCRawLine.buildRawLine(false, "NICK", this.getNickname());
    }
   
    public boolean isSendable() {
        return this.getNickname() != null;
    }

    public Class<IRCNumericEvent>[] getErrorReplies() {
        return new Class[] { 
            NoNicknameGivenError.class,
            NicknameInUseError.class,
            UnavailableResourceError.class,
            ErroneousNicknameError.class,
            NicknameCollisionError.class,
            RestrictedNicknameError.class
        };
    }
}
