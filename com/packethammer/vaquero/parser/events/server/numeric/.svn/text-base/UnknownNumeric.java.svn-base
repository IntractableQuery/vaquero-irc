/*
 * Occurs when we receive a numeric that we cannot create a specific subclassed
 * child event of IRCNumericEvent with. This is not a regular event; it is created
 * by the parser when we truly have no idea what an IRC numeric represents
 * (although, you can still attempt to determine uncommon numerics manually
 * through the use of IRCNumericEvent's methods).
 */

package com.packethammer.vaquero.parser.events.server.numeric;

public class UnknownNumeric extends IRCNumericEvent {
    /**
     * This is an "unknown" numeric and is not a normal numeric. As such, this
     * method never needs to be called (the parser should not even call it).
     * Returns true in its default implementation here.
     * 
     * @returns True.
     */
    public boolean validate() {
        return true; 
    }
    
    /**
     * This is an "unknown" numeric and is not a normal numeric. This method 
     * returns -1 since this class does not actually represent a specific
     * numeric.
     *
     * @returns -1.
     */
    public int getHandledNumeric() {
        return -1;
    }
    
    public String toString() {
        return "UNKNOWN " + super.toString() + " [" + this.toRawLine(2, false, true) + "]";
    }
}
