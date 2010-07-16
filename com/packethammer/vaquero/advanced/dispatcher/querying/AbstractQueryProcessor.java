/**
 * Defines a class that processes replies for a command for other query
 * listeners.
 */

package com.packethammer.vaquero.advanced.dispatcher.querying;

import com.packethammer.vaquero.outbound.outboundprocessing.EncapsulatedIRCCommand;

public abstract class AbstractQueryProcessor {
    /**
     * Adds a command with its listener to this command processor.
     *
     * @param command The appropriate encapsulated command to use.
     * @param listener The listener to use.
     */
    public abstract void addQueryingCommand(EncapsulatedIRCCommand command, AbstractCommandQueryListener listener);
}
