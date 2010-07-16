/*
 * This class is a central interface for sending IRC commands to a server.
 *
 * The most important part is the outbound command system. This system has
 * been configured in a specific order to prevent some confusing problems
 * down the road. It looks something like the following:
 * 1. The pre-wait filter chain, which allows you to filter commands before they go
 *    into the wait queue.
 * 2. The wait queue, which holds IRC commands waiting to go to the server
 * 3. The pre-release filter chain, which is a last chance to deal with a 
 *    command before it gets sent to the server
 * 4. Actual release -- command is converted to a String and dispatched to the
 *    server.
 */

package com.packethammer.vaquero.outbound;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import com.packethammer.vaquero.outbound.commands.IRCCommand;
import com.packethammer.vaquero.outbound.outboundprocessing.CommandFilterI;
import com.packethammer.vaquero.outbound.outboundprocessing.CommandReleaseI;
import com.packethammer.vaquero.outbound.outboundprocessing.EncapsulatedIRCCommand;
import com.packethammer.vaquero.outbound.outboundprocessing.OutboundCommandEventListener;
import com.packethammer.vaquero.outbound.outboundprocessing.TimingScheme;
import com.packethammer.vaquero.outbound.outboundprocessing.WaitQueue;

public class CommandManager {
    private Vector<CommandFilterI> preWaitFilterChain;
    private Vector<CommandFilterI> preReleaseFilterChain;
    private WaitQueue waitQueue;
    private TimingScheme timingScheme;
    private OutboundRawIRCLineSenderI rawLineSender;
    
    /** 
     * Initializes the command manager with a timing scheme to use for command
     * dispatch from the wait queue and where to send raw lines.
     *
     * @param scheme The timing scheme to use.
     * @param rawLineSender The place to send our outbound raw lines.
     */
    public CommandManager(TimingScheme scheme, OutboundRawIRCLineSenderI rawLineSender) {
        // first, configure pre-wait filter chain
        preWaitFilterChain = new Vector();
        
        // now, configure wait queue
        this.timingScheme = scheme;
        timingScheme.setDestination(new CommandReleaseI() {
            public void commandReleased(EncapsulatedIRCCommand command) {
                commandLeftWaitQueue(command);
            }
        });
        waitQueue = new WaitQueue();
        waitQueue.useTimingScheme(timingScheme);
        
        // now, configure pre-release filter chain
        preReleaseFilterChain = new Vector();
        
        // do the rest
        this.rawLineSender = rawLineSender;
    }

    /**
     * Returns the chain used for filtering commands just before they
     * go into the wait queue.
     */
    public Vector<CommandFilterI> getPreWaitFilterChain() {
        return preWaitFilterChain;
    }
    
    /**
     * Adds a filter to the pre-wait-queue filter chain. You may assume that
     * filters maintain the order that they are added in, although it is a bad
     * design decision to rely on this.
     */
    public void addPreWaitFilter(CommandFilterI filter) {
       this.preWaitFilterChain.add(filter);
    }
    
    /**
     * Removes a filter form the pre-wait-queue filter chain using equals() 
     * comparison. Will remove multiple instances if required.
     *
     * @return True if one or more filters were removed.
     */
    public boolean removePreWaitFilter(CommandFilterI filter) {
       boolean removed = false;
       Iterator<CommandFilterI> i = this.preWaitFilterChain.iterator();
       while(i.hasNext()) {
           if(i.next().equals(filter)) {
               i.remove();
               removed = true;
           }
       }
       return removed;
    }
    
    /**
     * Returns the chain used for filtering commands just before they go 
     * to the server.
     */
    public Vector<CommandFilterI> getPreReleaseFilterChain() {
        return preReleaseFilterChain;
    }
    
    /**
     * Adds a filter to the pre-release filter chain. You may assume that
     * filters maintain the order that they are added in, although it is a bad
     * design decision to rely on this.
     */
    public void addPreReleaseFilter(CommandFilterI filter) {
       this.preReleaseFilterChain.add(filter);
    }
    
    /**
     * Removes a filter form the pre-release filter chain using equals() 
     * comparison. Will remove multiple instances if required.
     *
     * @return True if one or more filters were removed.
     */
    public boolean removePreReleaseFilter(CommandFilterI filter) {
       boolean removed = false;
       Iterator<CommandFilterI> i = this.preReleaseFilterChain.iterator();
       while(i.hasNext()) {
           if(i.next().equals(filter)) {
               i.remove();
               removed = true;
           }
       }
       return removed;
    }
    
    /**
     * Returns the wait queue being used to store commands.
     */
    public WaitQueue getWaitQueue() {
        return waitQueue;
    }

    /**
     * Returns the timing scheme being used for the wait queue.
     */
    public TimingScheme getTimingScheme() {
        return timingScheme;
    }

    /**
     * Returns the raw line sender being used.
     */
    public OutboundRawIRCLineSenderI getRawLineSender() {
        return rawLineSender;
    }
    
    /**
     * Occurs when a command leaves the wait queue.
     */
    private void commandLeftWaitQueue(EncapsulatedIRCCommand command) {
        // run the command through the pre-release filter
        if(runCommandThroughFilterChain(this.getPreReleaseFilterChain(), command)) {
            return; // command dropped
        }
        
        // now, attempt sending the command
        IRCCommand ircCommand = command.getCommand();
        if(ircCommand.isSendable()) {
            this.rawLineSender.sendRawLine(ircCommand.renderForIRC().toRawLine());
            
            // sent command with success
            for(OutboundCommandEventListener listener : command.getListeners()) {
                listener.onSent();
            }
        } else {
            // dropped command
            for(OutboundCommandEventListener listener : command.getListeners()) {
                listener.onDrop();
            }
        }
    }
    
    /**
     * Sends an IRC command using the outbound system.
     *
     * @param command The IRCCommand to send.
     */
    public void sendCommand(IRCCommand command) {
        this.sendEncapsulatedCommand(new EncapsulatedIRCCommand(command));
    }
    
    /**
     * Sends an encapsulated IRC command.
     *
     * @param command The encapsulated IRC command to send.
     */
    protected void sendEncapsulatedCommand(EncapsulatedIRCCommand command) {
        // first, run it through pre-wait filter
        if(runCommandThroughFilterChain(this.getPreWaitFilterChain(), command)) {
            return; // command dropped
        }
        
        // now, put the command into the wait queue
        waitQueue.enqueue(command);
    }
    
    /**
     * Filters a command using a filter chain, spawning any necessary events
     * for the command's listeners. Returns true if command was dropped.
     */
    public static boolean runCommandThroughFilterChain(List<? extends CommandFilterI> filterChain, EncapsulatedIRCCommand command) {
        for(CommandFilterI filter : filterChain) {
            IRCCommand oldCommand = command.getCommand();
            
            // let the filter work on it
            filter.filterCommand(command);
            
            if(oldCommand != command.getCommand()) {
                // reference changed
                for(OutboundCommandEventListener listener : command.getListeners()) {
                    listener.onCommandReferenceChange();
                }
            }
            
            if(!oldCommand.getClass().equals(command.getCommand().getClass())) {
                // class type changed
                for(OutboundCommandEventListener listener : command.getListeners()) {
                    listener.onCommandTypeChange();
                }
            }
                
            if(command.getCommand() == null) {
                // command dropped, do not continue
                for(OutboundCommandEventListener listener : command.getListeners()) {
                    listener.onDrop();
                }
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Cleans up resources used by the command manager.
     */
    public void die() {
        // drop all existing commands sitting in queue
        Iterator<EncapsulatedIRCCommand> i = waitQueue.getCommandQueue().iterator();        
        while(i.hasNext()) {
            EncapsulatedIRCCommand command = i.next();
            for(OutboundCommandEventListener listener : command.getListeners()) {
                listener.onDrop();
            }
            i.remove();
        }
        
        this.preReleaseFilterChain = this.preWaitFilterChain = null;
        this.waitQueue = null;
        this.timingScheme.stopTiming();
        this.timingScheme = null;
        this.rawLineSender = null;
    }
}
