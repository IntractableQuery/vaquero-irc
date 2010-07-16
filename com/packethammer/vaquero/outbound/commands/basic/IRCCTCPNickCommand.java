/**
 * This sends a CTCP message to a user. 
 */

package com.packethammer.vaquero.outbound.commands.basic;

public class IRCCTCPNickCommand extends IRCMessageNickCommand {    
    /**
     * Instantiates the CTCP message command with a target nickname and CTCP message.
     *
     * @param nickname The person to message.
     * @param message The CTCP message to send.
     */
    public IRCCTCPNickCommand(String nickname, String message) {
        this(message);
        this.addNick(nickname);
    }
    
    /**
     * Instantiates the CTCP message command with a CTCP message, but no target(s).
     *
     * @param message The CTCP message to send.
     */
    public IRCCTCPNickCommand(String message) {
        this.setCTCPMessage(message);
    }
    
    public IRCCTCPNickCommand() {
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
