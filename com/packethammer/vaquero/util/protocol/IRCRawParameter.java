/*
 * Represents a raw parameter in an IRC line. There are two types: 
 * regular and extended. A regular parameter contains a string with no
 * whitespaces (after all, whitespace delimits the parameters). An extended
 * parameter can contain whitespace, and there is only one of them in a 
 * single IRC line, if any.
 */

package com.packethammer.vaquero.util.protocol;

public class IRCRawParameter {
    private boolean extended;
    private String parameterString;
            
    /**
     * Initiates this parameter with essential data.
     *
     * @param isExtended Determines if this parameter is extended or not.
     * @param parameterString Determines the parameter text.
     */
    public IRCRawParameter(boolean extended, String parameterString) {
        this.extended = extended;
        this.parameterString = parameterString;
    }
    
    /**
     * Determines if this is an extended (multi-whitespace or capacity for multiple
     * whitespaces) parameter.
     *
     * @return True if extended, false otherwise.
     */
    public boolean isExtended() {
        return extended;
    }
    
    /**
     * Returns the parameter as a string.
     *
     * @return Parameter as string.
     */
    public String getParameterString() {
        return parameterString;
    }
    
    /**
     * Returns this parameter formatted for IRC. This mostly entails prefixing
     * an extended parameter with a colon (':').
     *
     * @return The parameter rendered for raw IRC usage.
     */
    public String renderParameterForIRC() {
        if(this.isExtended())
            return ':' + this.getParameterString();
        else
            return this.getParameterString();
    }

    /**
     * Returns the same thing as getParameterString() for ease.
     *
     * @see #getParameterString()
     */
    public String toString() {
        return this.getParameterString();
    }
}
