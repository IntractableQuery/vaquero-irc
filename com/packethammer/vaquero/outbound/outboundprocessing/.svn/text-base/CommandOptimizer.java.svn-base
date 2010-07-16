/**
 * A command optimizer works in a WaitQueue by listening for the enqueue event,
 * where it can allow/drop/modify the incoming command and operate on the 
 * WaitQueue's internal list of commands to be sent based upon that command.
 */

package com.packethammer.vaquero.outbound.outboundprocessing;

import com.packethammer.vaquero.outbound.commands.IRCCommand;

public abstract class CommandOptimizer implements CommandFilterI {
    private WaitQueue waitQueue;
    
    public CommandOptimizer() {
    }
    
    /**
     * Returns the wait queue this command optimizer is working with.
     */
    public WaitQueue getWaitQueue() {
        return waitQueue;
    }
    
    /**
     * Sets the wait queue the command optimizer is using.
     */
    public void setWaitQueue(WaitQueue waitQueue) {
        this.waitQueue = waitQueue;
    }
    
    /**
     * This occurs before the command is added to the wait-queue, giving you
     * time to modify the wait-queue if you wish. Be aware that if you
     * use this to "split up" a command into multiple commands that
     * you can really mess up their event listeners. Please don't perform
     * those sorts of operations on commands that typically have a listener
     * for them (such as the advanced dispatcher's listener for WHO commands
     * it sends).
     *
     * This accepts an encapsulated command and then performs one of the 
     * following to this contained command:
     *   1. It can simply leave it intact, no changes
     *   2. It can modify the data inside the command
     *   3. It can construct a new command and set that as the new reference
     *   4. It can set the command to null, indicating we wish to completely drop the command
     *
     * However, it SHOULD NOT modify any other information in the encapsulated
     * IRC command, unless you are certain it is safe to do so.
     *
     * @param command The encapsuled IRC command to process.
     * @return The filtered IRC command, or null if we are to completely drop it.
     */
    public abstract void filterCommand(EncapsulatedIRCCommand command);      
}
