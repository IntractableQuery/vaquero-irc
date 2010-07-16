/**
 * A timing scheme is used for a wait queue to determine when events should
 * be released from the queue. It is typically used to throttle down outbound
 * data rates to avoid flooding the server with data. A timing scheme requests
 * the next encapsulated IRC command from the wait queue when it wants to send
 * a new command. 
 *
 * Due to the nature of the run() method (it is threaded), timing calculations
 * can be made in an infinite loop if we desire.
 */

package com.packethammer.vaquero.outbound.outboundprocessing;

public abstract class TimingScheme extends Thread {
    private TimingSchemeCommandSourceI source;
    private CommandReleaseI destination;
    private boolean stopped = false;
    
    public TimingScheme() {
    }    

    /**
     * Returns the source of IRC commands.
     */
    public TimingSchemeCommandSourceI getSource() {
        return source;
    }

    /**
     * Sets the source of IRC commands. 
     */
    public void setSource(TimingSchemeCommandSourceI source) {
        this.source = source;
    }
    
    /**
     * Returns the destinaton of IRC commands.
     */
    public CommandReleaseI getDestination() {
        return destination;
    }
    
    /**
     * Sets the destination of IRC commands.
     */
    public void setDestination(CommandReleaseI dest) {
        this.destination = dest;
    }
    
    /**
     * Forces the release of a command. This command may block if the source
     * of commands has no command to give.
     */
    public void releaseCommand() {
        EncapsulatedIRCCommand command = source.getNextCommand();
        destination.commandReleased(command);
    }
    
    /**
     * Starts the command-pulling process for command release. This method is
     * spawned as part of a new thread, so we can perform blocking operations
     * without locking up the class starting us up.
     *
     * This cannot restart a stopped timing scheme.
     */
    public abstract void run();
    
    /**
     * This will flag this timing scheme internally as "stopped". run() should
     * monitor our internal stopped status and cease operations on the command
     * source (otherwise, a null pointer exception will follow).
     */
    public void stopTiming() {
        stopped = true;
        source = null;
    }
    
    /**
     * Determines if this timing scheme is stopped or not.
     */
    public boolean isStopped() {
        return stopped;
    }
    
}
