/**
 * This defines a class that takes an IRC command and will allow/drop/modify it.
 */

package com.packethammer.vaquero.outbound.outboundprocessing;

public interface CommandFilterI {    
    /**
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
    public void filterCommand(EncapsulatedIRCCommand command);    
}
