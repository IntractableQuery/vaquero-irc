/*
 * A general exception that occurs when parsing modes.
 */

package com.packethammer.vaquero.util.modes;

public class ModeParsingException extends RuntimeException {    
    /** 
     * Instantiates exception with reason.
     *
     * @param problem The problem encountered.
     */
    public ModeParsingException(String problem) {
        super(problem);
    }
    
}
