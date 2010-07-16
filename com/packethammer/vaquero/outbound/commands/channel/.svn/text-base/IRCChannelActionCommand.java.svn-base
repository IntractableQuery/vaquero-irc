/**
 * This is a CTCP ACTION that is performed on one or more channels. It makes
 * you appear to "do something."
 */

package com.packethammer.vaquero.outbound.commands.channel;

public class IRCChannelActionCommand extends IRCChannelCTCPCommand {    
    /**
     * Instantiates this CTCP ACTION message command with a target channel and ACTION message.
     *
     * @param channel The channel to message.
     * @param message The ACTION message to send.
     */
    public IRCChannelActionCommand(String channel, String message) {
        this(message);
        this.addChannel(channel);
    }
    
    /**
     * Instantiates a CTCP ACTION message command with a ACTION message, but no target(s).
     *
     * @param message The ACTION message to send.
     */
    public IRCChannelActionCommand(String message) {
        this();
        this.setActionMessage (message);
    }
    
    public IRCChannelActionCommand() {
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
