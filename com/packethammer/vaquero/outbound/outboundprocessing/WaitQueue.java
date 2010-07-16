/**
 * This is where encapsulated IRC commands are sent as part of the entire 
 * command dispatch process. Here, they can be stored to prevent sending 
 * excessive data to the server in a short period of time. As a side benefit,
 * it makes it easier to perform general operations on a series of commands
 * to optimize/drop/modify them before they are sent out to the server. Because of this,
 * not only does a wait queue require a timed release scheme, but it may
 * also have "optimizers" which can listen to enqueue events and actively
 * perform operations on the internal wait queue (which is actually a list
 * that behaves as a queue, but with random-access list operations taking
 * place as the optimizers operate).
 */

package com.packethammer.vaquero.outbound.outboundprocessing;

import java.util.Iterator;
import java.util.Vector;
import com.packethammer.vaquero.outbound.CommandManager;

public class WaitQueue {
    private Vector<CommandOptimizer> commandOptimizers;
    private Vector<EncapsulatedIRCCommand> commandQueue;
    private CommandRelease release;
    
    /** Creates a new instance of WaitQueue */
    public WaitQueue() {
        commandOptimizers = new Vector();
        commandQueue = new Vector();
        release = new CommandRelease(this);
    }    
    
    /**
     * Returns the command optimizers operating on the internal queue.
     *
     * @return Command optimizers.
     */
    public Vector<CommandOptimizer> getCommandOptimizers() {
        return commandOptimizers;
    }
    
    /**
     * Adds a command optimizer. It is safe to assume that the order you add
     * optimizers in is preserved, although it is a bad design decision to rely
     * on order of the optimizers.
     *
     * @param o The command optimizer to add.
     */
    public void addCommandOptimizer(CommandOptimizer o) {
        o.setWaitQueue(this);
        commandOptimizers.add(o);
    }
    
    /**
     * Removes a command optimizer using its equals() method. Will remove multiple
     * instances if required. 
     *
     * @param o The command optimizer to remove.
     * @return True if one or more command optimizers were removed.
     */
    public boolean removeCommandOptimizer(CommandOptimizer o) {
        boolean removed = false;
        
        Iterator<CommandOptimizer> i = commandOptimizers.iterator();
        while(i.hasNext()) {
            if(i.next().equals(o)) {
                i.remove();
                removed = true;
            }
        }
        
        return removed;
    }
    
    /**
     * Returns a direct reference to the internal command queue. This is meant
     * for the command optimizers so that they can modify the command queue,
     * and it is not a sound idea to modify it yourself.
     *
     * The queue begins with the newest elements at index 0, and the oldest
     * elements at the last index (the last one presumably about to be
     * sent out soon).
     */
    public synchronized Vector<EncapsulatedIRCCommand> getCommandQueue() {
        return commandQueue;
    }
    
    /**
     * Puts a command into the queue system. Note that the command optimizers
     * will have a chance to mutate or drop it, so at this point, there
     * are no guarantees that it will even come back out of the command queue.
     * Note that the command is not cloned; it may actually be changed in
     * content since a direct reference is used with the optimizers. 
     *
     * @param command The encapsulated command to put into the processing system.
        
     */
    public void enqueue(EncapsulatedIRCCommand command) {
        if(CommandManager.runCommandThroughFilterChain(this.getCommandOptimizers(), command)) {
            return; // dropped
        }
        
        // we've sent the command through the optimizers, so now we add it to the queue
        commandQueue.add(0, command);
        
        release.newCommandInserted();
    }
    
    /**
     * Gives a timing scheme to the wait queue to configure for usage and start
     * up.
     */
    public void useTimingScheme(TimingScheme s) {
        s.setSource(release);
        s.start();
    }
    
    /**
     * This class allows the timing scheme to pull commands and block when
     * there are no commands available.
     */
    private class CommandRelease implements TimingSchemeCommandSourceI {
        private WaitQueue parent;
        
        public CommandRelease(WaitQueue parent) {
            this.parent = parent;
        }
        
        // called when a new command is added to the wait queue -- this means we may have a new command available to send
        public synchronized void newCommandInserted() {
            this.notifyAll();
        }
        
        public synchronized EncapsulatedIRCCommand getNextCommand() {
            while(parent.getCommandQueue().isEmpty()) {
                try {
                    this.wait();
                } catch (InterruptedException ex) {
                    
                }
            }
            
            // there's at least one command in the queue -- remove and return it
            return parent.getCommandQueue().remove(parent.getCommandQueue().size() - 1);            
        } 
    }
}
