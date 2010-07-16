/**
 * An outbound command listener allows you to receive events for a command
 * going through the outbound command manager.
 */

package com.packethammer.vaquero.outbound.outboundprocessing;

public abstract class OutboundCommandEventListener {
    private EncapsulatedIRCCommand encapsulatedCommand;
    
    /**
     * Occurs when the command is filtered and its class changes from what
     * it was before to something else (indicating a full command change).
     */
    public void onCommandTypeChange() {
        
    }
    
    /**
     * Occurs when the command reference changes during filtering, meaning that 
     * it may now be null or a command of a new type or of the same type.
     */
    public void onCommandReferenceChange() {
        
    }
    
    /**
     * Occurs when the command was dropped in filtering. This may result in the
     * command reference becoming null, so you can no longer rely on it.
     */
    public void onDrop() {
        
    }
    
    /**
     * Occurs when this command has successfully been sent out (it's been
     * sent to the remote server).
     */
    public void onSent() {
        
    }

    public EncapsulatedIRCCommand getEncapsulatedCommand() {
        return encapsulatedCommand;
    }

    public void setEncapsulatedCommand(EncapsulatedIRCCommand encapsulatedCommand) {
        this.encapsulatedCommand = encapsulatedCommand;
    }
}
