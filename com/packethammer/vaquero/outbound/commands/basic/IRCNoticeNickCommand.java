/*
 * This command allows you to send a NOTICE to one or more nicknames, which
 * is accompanied by a message.
 *
 * NOTICE <TARGETS> <MESSAGE>
 */

package com.packethammer.vaquero.outbound.commands.basic;

import com.packethammer.vaquero.outbound.commands.interfaces.AdjustableNicknameTargetCommandI;
import java.util.Collection;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.StringOperations;
import com.packethammer.vaquero.outbound.commands.interfaces.NicknamesTargetedCommandI;
import java.util.List;

public class IRCNoticeNickCommand extends MultitargetMessage implements NicknamesTargetedCommandI,AdjustableNicknameTargetCommandI {
    /**
     * Instantiates a notice command with a target nickname and message.
     *
     * @param nickname The person to message.
     * @param message The message to send.
     */
    public IRCNoticeNickCommand(String nickname, String message) {
        this(message);
        this.addNick(nickname);
    }
    
    /**
     * Instantiates a notice command with a message, but no target(s).
     *
     * @param message The message to send.
     */
    public IRCNoticeNickCommand(String message) {
        this();
        this.setMessage(message);
    }
    
    public IRCNoticeNickCommand() {
        super();
    }
    
    /**
     * Returns the nicknames we are noticing.
     *
     * @return Nicknames being targeted.
     */
    public Collection<String> getNicknames() {
        return super.getTargets();
    }
    
    /**
     * Adds a nickname to the target list. Use this instead of addTarget().
     */
    public void addNick(String nickname) {
        super.addTarget(nickname);
    }
    
    public void setNicknameTargets(List<String> targets) {
        this.getNicknames().clear();
        this.getNicknames().addAll(targets);
    }
    
    /**
     * DO NOT USE THIS METHOD. THROWS EXCEPTION BY DEFAULT.
     */
    public void addTarget(String target) {
        throw new IllegalStateException("Do not use this method in this subclass; use addNick");
    }
    
    public IRCRawLine renderForIRC() {
        String targets = StringOperations.commaDelimit(this.getNicknames());
        
        return IRCRawLine.buildRawLine(true, "NOTICE", targets, this.getMessage());
    }    
   
    public boolean isSendable() {
        return !this.getNicknames().isEmpty() && this.getMessage() != null;
    }
}
