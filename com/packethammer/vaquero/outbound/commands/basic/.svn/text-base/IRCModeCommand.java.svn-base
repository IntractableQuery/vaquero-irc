/*
 * Changes the modes on a user or channel. These modes do not require definitions
 * to send.
 *
 * MODE <channel/user> [[+/-modes] [modeparams...]]
 */

package com.packethammer.vaquero.outbound.commands.basic;

import com.packethammer.vaquero.util.modes.Modes;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.outbound.commands.IRCCommand;

public abstract class IRCModeCommand extends IRCCommand {
    private Modes modes;
    private String target;
    
    /**
     * Instantiates this class with base target and modes information.
     *
     * @param target The channel/user to set the modes on.
     * @param modes The modes to set on the channel/user.
     */
    public IRCModeCommand(String target, Modes modes) {
        this.target = target;
        this.modes = modes;
    }
    
    
    /**
     * Instantiates this class with base target and modes information.
     *
     * @param target The channel/user to set the modes on.
     */
    public IRCModeCommand(String target) {
        this(target, null);
    }
    
    public IRCModeCommand() {        
    }

    /**
     * Returns the modes being set.
     *
     * @return User or channel modes being set.
     */
    public Modes getModes() {
        return modes;
    }

    /**
     * Sets the modes being set.
     *
     * @param modes The modes being set.
     */
    public void setModes(Modes modes) {
        this.modes = modes;
    }
    
    /**
     * Returns the target of these modes -- a user or channel.
     *
     * @return Target channel or user.
     */
    public String getTarget() {
        return target;
    }

    /**
     * Sets the target of these modes.
     *
     * @param target The target channel/users.
     */
    public void setTarget(String target) {
        this.target = target;
    }
    
    public IRCRawLine renderForIRC() {
        if(modes != null) {
            String[] modesSetParams = modes.renderModes().split(" ");

            String[] args = new String[modesSetParams.length + 2]; // +2 for MODE and <TARGET>
            args[0] = "MODE";
            args[1] = this.getTarget();
            System.arraycopy(modesSetParams, 0, args, 2, modesSetParams.length);

            return IRCRawLine.buildRawLine(false, args);
        } else {
            return IRCRawLine.buildRawLine(false, "MODE", this.getTarget());
        }
    }    
   
    public boolean isSendable() {
        return this.getTarget() != null;
    }

}
