/**
 * The CTCP specification indicates that all CTCP replies should be sent
 * via notice.
 */

package com.packethammer.vaquero.outbound.commands.basic;

public class IRCCTCPReplyNickCommand extends IRCNoticeNickCommand {
    /**
     * Instantiates the CTCP reply command with a target nickname and CTCP message.
     *
     * @param nickname The person to message.
     * @param message The CTCP message to send.
     */
    public IRCCTCPReplyNickCommand(String nickname, String message) {
        this(message);
        this.addNick(nickname);
    }
    
    /**
     * Instantiates the CTCP reply command with a CTCP message, but no target(s).
     *
     * @param message The CTCP message to send.
     */
    public IRCCTCPReplyNickCommand(String message) {
        this.setCTCPMessage(message);
    }
    
    public IRCCTCPReplyNickCommand() {
        super();
    }
    
    /**
     * Sets the CTCP message to use.
     *
     * @param message The CTCP message.
     */
    public void setCTCPMessage(String message) {
        this.setMessage("\001" + message + "\001");
    }
}
