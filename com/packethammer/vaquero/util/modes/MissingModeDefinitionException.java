/*
 * Typically thrown when parsing modes and we have encountered a mode that we
 * lack a definition for. 
 */

package com.packethammer.vaquero.util.modes;

public class MissingModeDefinitionException extends RuntimeException {
    /**
     * Accepts the mode that initiated the exception.
     *
     * @param offendingMode The mode we lack a definition for.
     */
    public MissingModeDefinitionException(char offendingMode) {
        super("Unable to parse; mode '" + offendingMode + "' is not defined.");
    }    
}
