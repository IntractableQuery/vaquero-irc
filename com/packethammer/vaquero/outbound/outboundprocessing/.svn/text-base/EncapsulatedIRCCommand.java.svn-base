/**
 * It is somewhat desirable to contain the command to send in a class that
 * can possibly be subclassed for other filtering purposes.
 */

package com.packethammer.vaquero.outbound.outboundprocessing;

import java.util.Vector;
import com.packethammer.vaquero.outbound.commands.IRCCommand;
import java.util.Date;

public class EncapsulatedIRCCommand {
    private IRCCommand command;
    private Date creationTime;
    private Vector<OutboundCommandEventListener> listeners;
    
    /**
     * Initializes with the IRC command to be sent.
     *
     * @param command The IRC command. 
     */
    public EncapsulatedIRCCommand(IRCCommand command) {
        this.setCommand(command);
        this.creationTime = new Date();
        listeners = new Vector();
    }

    /**
     * Returns the command being transported.
     */
    public IRCCommand getCommand() {
        return command;
    }

    /**
     * @see #getCommand()
     */
    public void setCommand(IRCCommand command) {
        this.command = command;
    }
    
    /**
     * Adds a listener to this command.
     */
    public void addListener(OutboundCommandEventListener listener) {
        listener.setEncapsulatedCommand(this);
        this.listeners.add(listener);
    }
    
    /**
     * Removes a listener from this command using equals() method.
     */
    public void removeListener(OutboundCommandEventListener listener) {
        this.listeners.remove(listener);
    }
    
    /**
     * Returns all listeners for this command.
     */
    public Vector<OutboundCommandEventListener> getListeners() {
        return this.listeners;
    }

    /**
     * Returns the time that this command was initialized.
     *
     * @return Time of initialization.
     */
    public Date getCreationTime() {
        return creationTime;
    }
    
}
