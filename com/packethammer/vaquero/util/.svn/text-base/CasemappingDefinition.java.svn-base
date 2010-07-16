/*
 * This class does little more than hold a constant for a casemapping type.
 * It is designed to be instantiated for use within the irc parser and modified
 * as new server information is discovered. Since it can change, it has the
 * unfortunate side effect of modifying the behavior of CasemappedStrings
 * after their creation. Sadly, this cannot be avoided. Luckily, this
 * change should either never occur, or only occur once as we get numeric
 * 005 ISUPPORT information from the server.
 */

package com.packethammer.vaquero.util;

public class CasemappingDefinition {
    /**
     * The ASCII characters 97 to 122 (decimal) are defined as
     * the lower-case characters of ASCII 65 to 90 (decimal).  No other
     * character equivalency is defined.
     */
    public static final int CASEMAPPING_ASCII = 0;    
    
    /**
     * The ASCII characters 97 to 126 (decimal) are defined as
     * the lower-case characters of ASCII 65 to 94 (decimal).  No other
     * character equivalency is defined.
     */
    public static final int CASEMAPPING_RFC1459 = 1;
    
    /**
     * The ASCII characters 97 to 125 (decimal) are
     * defined as the lower-case characters of ASCII 65 to 93 (decimal).
     * No other character equivalency is defined.
     */
    public static final int CASEMAPPING_STRICT_RFC1459 = 2;
    
    private int casemappingConstant;
    
    public CasemappingDefinition() {
    }
    
    /**
     * Initializes this casemapping definition with the casemapping mode to use.
     *
     * @param mode The mode corresponding to the CASEMAPPING_* constants.
     */
    public CasemappingDefinition(int mode) {
        this.casemappingConstant = mode;        
    }

    /**
     * Returns the casemapping constant represented.
     */
    public int getCasemappingConstant() {
        return casemappingConstant;
    }

    /**
     * Sets the casemapping constant represented.
     */
    public void setCasemappingConstant(int casemappingConstant) {
        this.casemappingConstant = casemappingConstant;
    }
    
    /**
     * Determines if two strings are equal using this casemapping.
     */
    public boolean areStringsEqual(String string1, String string2) {
        CasemappedString str = new CasemappedString(string1, this);
        return str.equals(string2);
    }
    
    
    /**
     * Returns a casemapping constant by its name, or -1 if none was found.
     * These names are derived from numeric 005 ISUPPORT's CASEMAPPING.
     */
    public static int getCasemappingConstantByName(String name) {
        if(name.equalsIgnoreCase("RFC1459"))
            return CASEMAPPING_RFC1459;
        else if(name.equalsIgnoreCase("ASCII"))
            return CASEMAPPING_ASCII;
        else if(name.equalsIgnoreCase("STRICT-RFC1459"))
            return CASEMAPPING_STRICT_RFC1459;
        else
            return -1;
    }
    
    public String toString() {
        return "CASEMAPPING CONSTANT=" + this.getCasemappingConstant();
    }
}
