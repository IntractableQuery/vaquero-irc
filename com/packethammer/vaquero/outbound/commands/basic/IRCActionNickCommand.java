/**
 * This sends a CTCP ACTION message to a user. This is typically used to
 * show yourself as "doing something."
 */

package com.packethammer.vaquero.outbound.commands.basic;

public class IRCActionNickCommand extends IRCCTCPNickCommand {
    /**
     * Instantiates the CTCP ACTION message command with a target nickname and ACTION message.
     *
     * @param nickname The person to message.
     * @param message The ACTION message to send.
     */
    public IRCActionNickCommand(String nickname, String message) {
        this(message);
        this.addNick(nickname);
    }
    
    /**
     * Instantiates the CTCP ACTION message command with an ACTION message, but no target(s).
     *
     * @param message The ACTION message to send.
     */
    public IRCActionNickCommand(String message) {
        this.setCTCPMessage(message);
    }
    
    public IRCActionNickCommand() {
        super();
    }
    
    /**
     * This sets the action message to use when messaging.
     *
     * @param message The action message.
     */
    public void setActionMessage(String message) {
        this.setCTCPMessage("ACTION " + message);
    }
}
