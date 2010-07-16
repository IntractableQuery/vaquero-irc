/**
 * This is a "mode optimizer". It does the following things:
 *  1. Splits mode commands up into more than one command if the mode command
 *     is trying to set too many modes at once (some servers have a limit on
 *     how many of a certain type of mode you can set at a time).
 *  2. Looks for modes that are back-to-back in the queue and merges them
 *     together into one command if possible. This only occurs in very safe
 *     situations where it won't ruin the importance of the order you are
 *     setting modes in.
 *
 * Note that this won't work if the server never gave us any numeric 005 
 * MODES information.
 *
 * TODO: Review this and consider special cases carefully to possibly expand
 * the merging abilities.
 */

package com.packethammer.vaquero.outbound;

import com.packethammer.vaquero.outbound.commands.channel.IRCChannelModeCommand;
import com.packethammer.vaquero.outbound.outboundprocessing.CommandOptimizer;
import com.packethammer.vaquero.outbound.outboundprocessing.EncapsulatedIRCCommand;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;
import com.packethammer.vaquero.util.modes.Modes;
import com.packethammer.vaquero.util.modes.StandardMode;
import java.util.Iterator;

public class ModeOptimizer extends CommandOptimizer {
    private IRCServerContext serverContext;
    
    /**
     * Initializes this mode optimizer with the server context to use for
     * optimizing modes.
     */
    public ModeOptimizer(IRCServerContext serverContext) {
        this.serverContext = serverContext;
    }
            
    public void filterCommand(EncapsulatedIRCCommand command) {
        if(command.getCommand() instanceof IRCChannelModeCommand) {
            IRCChannelModeCommand modeCmd = (IRCChannelModeCommand) command.getCommand();
            
            // make sure it's an actual mode set!
            if(modeCmd.getModes() != null) {            
                if(this.serverContext.getISupport().isMaxChanModesWithParamKnown()) {
                    // is there an excessive number of modes being set?
                    int maxModesWithParam = this.serverContext.getISupport().getMaxChanModesWithParam();
                    command.setCommand(trimExcessiveModesToQueue(modeCmd, maxModesWithParam)); // trims off excessive modes and puts them in queue, then returns "ok" command

                    // can we merge this (entire) mode set with the modes being set directly at the top of the queue?
                    command.setCommand(tryMerge(modeCmd, maxModesWithParam));
                }
            }
        }
    }
    
    /**
     * Given a channel mode set command, this tries to merge *all* of it into
     * the mode set at the top of the queue right now, assuming that it's
     * even possible.
     *
     * Returns the original command or the merged version of it.
     */
    private IRCChannelModeCommand tryMerge(IRCChannelModeCommand command, int maxParamModes) {
        // is topmost element actually a channel mode command?
        if(!this.getWaitQueue().getCommandQueue().isEmpty() && this.getWaitQueue().getCommandQueue().get(0).getCommand() instanceof IRCChannelModeCommand) {
            IRCChannelModeCommand otherCommand = (IRCChannelModeCommand) this.getWaitQueue().getCommandQueue().get(0).getCommand();
            
            // is the other command a modes list request, or an actual mode setting?
            if(otherCommand.getModes() != null) {
                // are the modes all being added or all being removed on each one? (this prevents some very tricky cases which could result in very undesirable mode-setting behavior)
                if(modesAreDifferentEnough(command.getModes(), otherCommand.getModes())) {
                    // do channels match?
                    if(this.serverContext.casemapString(command.getChannels().iterator().next()).equals(otherCommand.getChannels().iterator().next())) {
                        // okay, now, if we merged the two mode sets, would the parameterized modes be too high?
                        int totalParameterizedModesIfMerged = this.countNumberParameterizedModes(otherCommand.getModes()) + this.countNumberParameterizedModes(command.getModes());
                        if(totalParameterizedModesIfMerged <= maxParamModes) {
                            // yes, we can merge them!
                            // remove the otherCommand from the queue
                            this.getWaitQueue().getCommandQueue().remove(0);

                            // now, merge the commands that were going to be sent 
                            IRCChannelModeCommand newCommand = new IRCChannelModeCommand(command.getChannels().iterator().next(), new Modes());
                            newCommand.getModes().getModes().addAll(otherCommand.getModes().getModes());
                            newCommand.getModes().getModes().addAll(command.getModes().getModes());
                            return newCommand;
                        } else {
                            return command; // we can't do anything
                        }
                    }
                }
            }
        }
        
        return command;
    }
    
    /**
     * This method recursively continues to trim off excessive modes and puts
     * them directly into the queue until the mode is okay, then it returns the
     * leftover mode.
     */
    private IRCChannelModeCommand trimExcessiveModesToQueue(IRCChannelModeCommand command, int maxParamModes) {
        // count the number of parameterized modes in the command
        int parameterizedModes = countNumberParameterizedModes(command.getModes());
        
        int numberOfModesPastLimit = parameterizedModes - maxParamModes;
        
        //System.out.println("Modes past limit: " + numberOfModesPastLimit);
        
        if(numberOfModesPastLimit > 0) {
            // trim out the max param modes and put them into queue as a new command
            Modes newModes = new Modes();
            
            Iterator<StandardMode> i = command.getModes().getModes().iterator();
            int curModeNum = 1;
            while(i.hasNext() && curModeNum <= maxParamModes) {
                StandardMode mode = i.next();
                System.out.println("cur mode num for new mode command:" + curModeNum + ", nickname=" + mode.getParameter());
                if(mode.getParameter() != null) {
                    i.remove(); // remove this mode from the current command...
                    newModes.addMode(mode); // ... and add it to the new modes set.
                }
                
                curModeNum++;
            }
            
            // we're done stripping the number of modes we want -- make new command and put in queue
            String channel = command.getChannels().iterator().next();
            this.getWaitQueue().getCommandQueue().insertElementAt(new EncapsulatedIRCCommand(new IRCChannelModeCommand(channel, newModes)), 0);
            
            //System.out.println("new modes length:" + newModes.getModes().size());
            //System.out.println("Original modes length now:" + command.getModes().getModes().size());
            
            // try another round
            return this.trimExcessiveModesToQueue(command, maxParamModes);
        } else {
            // the command is fine now, return it since we're done
            return command;
        }
    }
    
    private int countNumberParameterizedModes(Modes modes) {
        int parameterizedModes = 0;
        
        Iterator<StandardMode> i = modes.getModes().iterator();
        while(i.hasNext()) {
            if(i.next().getParameter() != null)
                parameterizedModes++;
        }
        
        return parameterizedModes;
    }
    
    /*
     * This determines if the modes in a given set are all being added or all
     * being removed.
     */
    private boolean modesAreAllSame(Modes modes) {
        boolean lastVal;
        
        if(!modes.getModes().isEmpty()) {
            Iterator<StandardMode> i = modes.getModes().iterator();
            lastVal = i.next().isBeingAdded();
            while(i.hasNext()) {
                if(i.next().isBeingAdded() != lastVal) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /*
     * Determines if it is okay to merge two sets of modes.
     */
    private boolean modesAreDifferentEnough(Modes modes1, Modes modes2) {
        if(this.modesAreAllSame(modes1) && this.modesAreAllSame(modes2)) {
            if(!modes1.getModes().isEmpty() && !modes2.getModes().isEmpty()) {
                Iterator<StandardMode> i = modes1.getModes().iterator();
                Iterator<StandardMode> i2 = modes2.getModes().iterator();
                
                return i.next().isBeingAdded() == i2.next().isBeingAdded();
            }
        }
        
        return false;
    }
    
}
 