/*
 * Sends a PRIVMSG directly to one or more users with a given message.
 *
 * PRIVMSG <TARGETS> <MESSAGE>
 */

package com.packethammer.vaquero.outbound.commands.basic;

import com.packethammer.vaquero.outbound.commands.interfaces.AdjustableNicknameTargetCommandI;
import java.util.Collection;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.StringOperations;
import com.packethammer.vaquero.outbound.commands.interfaces.NicknamesTargetedCommandI;
import java.util.List;

public class IRCMessageNickCommand extends MultitargetMessage implements NicknamesTargetedCommandI,AdjustableNicknameTargetCommandI {
    /**
     * Instantiates a message command with a target nickname and message.
     *
     * @param nickname The person to message.
     * @param message The message to send.
     */
    public IRCMessageNickCommand(String nickname, String message) {
        this(message);
        this.addNick(nickname);
    }
    
    /**
     * Instantiates a message command with a message, but no target(s).
     *
     * @param message The message to send.
     */
    public IRCMessageNickCommand(String message) {
        this();
        this.setMessage(message);
    }
    
    public IRCMessageNickCommand() {
        super();
    }
    
    /**
     * Returns the nicknames we are messaging.
     *
     * @return Nicknames being targeted.
     */
    public Collection<String> getNicknames() {
        return super.getTargets();
    }
    
    /**
     * Adds a nickname to the target list. 
     */
    public void addNick(String nickname) {
        super.addTarget(nickname);
    }
    
    /**
     * Adds a nickname to the target list.
     *
     * @see #addNick()
     */
    public void addTarget(String target) {
        this.addNick(target);
    }
    
    public void setNicknameTargets(List<String> targets) {
        this.getNicknames().clear();
        this.getNicknames().addAll(targets);
    }
    
    public IRCRawLine renderForIRC() {
        String targets = StringOperations.commaDelimit(this.getNicknames());
        
        return IRCRawLine.buildRawLine(true, "PRIVMSG", targets, this.getMessage());
    }    
   
    public boolean isSendable() {
        return !this.getNicknames().isEmpty() && this.getMessage() != null;
    }
}
