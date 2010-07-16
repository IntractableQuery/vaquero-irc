/**
 * Defines a class that is a source for the timing scheme's commands.
 */

package com.packethammer.vaquero.outbound.outboundprocessing;

public interface TimingSchemeCommandSourceI {
    /**
     * Requests a command from the source. If the source has no command to give,
     * the source may block until a new command is available.
     */
    public EncapsulatedIRCCommand getNextCommand();
}
