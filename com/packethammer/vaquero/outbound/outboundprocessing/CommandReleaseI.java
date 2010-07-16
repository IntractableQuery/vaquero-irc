/**
 * Defines a class that waits for a command to be released to it.
 */

package com.packethammer.vaquero.outbound.outboundprocessing;

public interface CommandReleaseI {
    /**
     * Occurs when we are being given a released command.
     */
    public void commandReleased(EncapsulatedIRCCommand command);
}
