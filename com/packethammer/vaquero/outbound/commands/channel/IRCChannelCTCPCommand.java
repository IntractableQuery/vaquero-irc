/**
 * Represents a CTCP message sent to one or more channels.
 */

package com.packethammer.vaquero.outbound.commands.channel;

public class IRCChannelCTCPCommand extends IRCChannelMessageCommand {
 
    /**
     * Instantiates this CTCP message command with a target channel and CTCP message.
     *
     * @param channel The channel to message.
     * @param message The CTCP message to send.
     */
    public IRCChannelCTCPCommand(String channel, String message) {
        this(message);
        this.addChannel(channel);
    }
    
    /**
     * Instantiates a CTCP message command with a CTCP message, but no target(s).
     *
     * @param message The CTCP message to send.
     */
    public IRCChannelCTCPCommand(String message) {
        this();
        this.setCTCPMessage(message);
    }
    
    public IRCChannelCTCPCommand() {
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
