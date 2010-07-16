/*
 * Contains a string that conforms to a particular IRC-related casemapping.
 *
 * The casemapping definition backing this string may change internally
 * at any time, affecting the equals() and compareTo() method behavior. As 
 * such, you should never use a casemapped string in a collection such
 * as a set or as a key in a map.
 *
 * Provides methods similar to String which should be used instead of String's
 * own methods (which you could access using toString()) unless no others are
 * provided. As the name suggests, this is different from a usual String in that
 * lowercase/uppercase versions of the string exist, but they are not considered 
 * distinct.
 *
 * This class will not be used in places where it might seem logical to use it, 
 * such as Hostmask. This is because the CasemappingDefinition contained within
 * is not concrete; it is possible it will be RFC1459 upon initial server 
 * connection, but then it is discovered the server actually supports 
 * only ASCII casemapping, so the definition changes. As such, this is not a 
 * very trustworthy class, but is still highly usable for checking ban lists
 * and such since the casemapping is more or less definitely known at that point.
 * Do not attempt to use CasemappedStrings in collections where equals() must
 * maintain immutable integrity (such as a Set or as a Map key).
 */

package com.packethammer.vaquero.util;

import java.util.TreeSet;

public class CasemappedString implements Comparable {   
    // TODO: this class is a bit hackish -- needs to be tidied up so there's not so much redundancy
    private CasemappingDefinition casemapping;
    private String string;
    
    /**
     * Intantiates a new casemapped string using a regular string representation
     * and a casemapping constant type to define how this string behaves.
     *
     * @param string The string being casemapped.
     * @param casemapping The casemapping definition used for this string.
     */
    public CasemappedString(String string, CasemappingDefinition casemapping) {
        this.string = string;
        this.casemapping = casemapping;
    }    
    
    /**
     * Returns the casemapping constant being used by this string instance.
     */
    public CasemappingDefinition getCasemapping() {
        return casemapping;
    }

    /**
     * Returns the string encapsulated by this casemapped string instance.
     */
    public String getString() {
        return string;
    }

    /**
     * Sets the string encapsulated by this casemapped string instance.
     */
    public void setString(String string) {
        this.string = string;
    }
    
    /**
     * Returns a new casemapped string that represents this casemapped string
     * in its lowercase form.
     */
    public CasemappedString toLowerCase() {
        StringBuilder newString = new StringBuilder();
        
        for(int x = 0; x < this.getString().length(); x++) {
            char c = this.getString().charAt(x);
            
            // 97-122 -> 65-90
            if(this.getCasemapping().getCasemappingConstant() == CasemappingDefinition.CASEMAPPING_ASCII 
                    || this.getCasemapping().getCasemappingConstant() == CasemappingDefinition.CASEMAPPING_RFC1459 
                    || this.getCasemapping().getCasemappingConstant() == CasemappingDefinition.CASEMAPPING_STRICT_RFC1459) {
                if(c >= 65 && c <= 90)
                    c += 32;
            }

            // 97-125 -> 65-93 (adds 123-125 -> 91-93)
            if(this.getCasemapping().getCasemappingConstant() == CasemappingDefinition.CASEMAPPING_STRICT_RFC1459 
                    || this.getCasemapping().getCasemappingConstant() == CasemappingDefinition.CASEMAPPING_RFC1459) {
                if(c >= 91 && c <= 93)
                    c += 32;
            }

            // 97-126 -> 65-94 (adds 126 -> 94)
            if(this.getCasemapping().getCasemappingConstant() == CasemappingDefinition.CASEMAPPING_RFC1459) {
                if(c == 94)
                    c = 126;
            }
            
            newString.append(c);
        }
        
        return new CasemappedString(newString.toString(), this.getCasemapping());
    }
    
    /**
     * Returns a new casemapped string that represents this casemapped string
     * in its uppercase form.
     */
    public CasemappedString toUpperCase() {
        StringBuilder newString = new StringBuilder();
        
        for(int x = 0; x < this.getString().length(); x++) {
            char c = this.getString().charAt(x);
            
            // 97-122 -> 65-90
            if(this.getCasemapping().getCasemappingConstant() == CasemappingDefinition.CASEMAPPING_ASCII 
                    || this.getCasemapping().getCasemappingConstant() == CasemappingDefinition.CASEMAPPING_RFC1459 
                    || this.getCasemapping().getCasemappingConstant() == CasemappingDefinition.CASEMAPPING_STRICT_RFC1459) {
                if(c >= 97 && c <= 122)
                    c -= 32;
            }

            // 97-125 -> 65-93 (adds 123-125 -> 91-93)
            if(this.getCasemapping().getCasemappingConstant() == CasemappingDefinition.CASEMAPPING_STRICT_RFC1459 
                    || this.getCasemapping().getCasemappingConstant() == CasemappingDefinition.CASEMAPPING_RFC1459) {
                if(c >= 97 && c <= 125)
                    c -= 32;
            }

            // 97-126 -> 65-94 (adds 126 -> 94)
            if(this.getCasemapping().getCasemappingConstant() == CasemappingDefinition.CASEMAPPING_RFC1459) {
                if(c == 126)
                    c = 94;
            }
            
            newString.append(c);
        }
        
        return new CasemappedString(newString.toString(), this.getCasemapping());
    }

    /**
     * Returns the string encapsulated by this casemapped string instance.
     */
    public String toString() {
        return this.getString();
    }
    
    /**
     * Determines if this casemapped string is equal to another casemapped
     * string based on internal casemapping. As expected, it disregards
     * character casing.
     */
    public boolean equals(Object o) {
        return this.compareTo((CasemappedString) o) == 0;
    }
    
    /**
     * Determines if this casemapped string is equal to another non-casemapped
     * string based on internal casemapping. As expected, it disregards
     * character casing. 
     */
    public boolean equals(String s) {
        return this.compareTo(s) == 0;
    }
    
    /**
     * Returns the hashCode() of the internal string.
     */
    public int hashCode() {
        return this.getString().hashCode();
    }
    
    /**
     * Compares a given casemapped string to this casemapped string using this
     * casemapping. As expected, it disregards character casing.
     */
    public int compareTo(Object o) {
        return this.compareTo(((CasemappedString) o).getString());
    }
    
    /**
     * Compares a given non-casemapped string to this casemapped string using this
     * casemapping. As expected, it disregards character casing.
     */
    public int compareTo(String s) {
        if(s == this.getString())
            return 0;
        
        int lengthComparison = (new Integer(this.getString().length())).compareTo(new Integer(s.length()));
        
        if(lengthComparison == 0) {
            // strings are of same length
            for(int x = 0; x < s.length(); x++) {
                int result = this.compareCharsBasedOnCasemapping(this.getString().charAt(x), s.charAt(x));
                
                if(result != 0) { // chars were not equal
                    return result;
                }
                
                String g = "";
                g.equals("");
            }
           
            
             return 0; // if we exit the loop, it is definitely equal
        } else {
            return lengthComparison;
        }
    }
    
    /**
     * Compares two characters based on internal casemapping. Returns the following,
     * the same as compareTo's general definition:
     *
     *  1   c1 > c2
     *  -1  c1 < c2
     *  0   c1 = c2
     *
     * @return An int that matches compareTo's general definition.
     */
    public int compareCharsBasedOnCasemapping(char c1, char c2) {
        if(c1 == c2) {
            return 0;
        }
        
        // first, decide if they are equal based on casemapping
        // 97-122 -> 65-90
        if(this.getCasemapping().getCasemappingConstant() == CasemappingDefinition.CASEMAPPING_ASCII 
                || this.getCasemapping().getCasemappingConstant() == CasemappingDefinition.CASEMAPPING_RFC1459 
                || this.getCasemapping().getCasemappingConstant() == CasemappingDefinition.CASEMAPPING_STRICT_RFC1459) {
            if((c1 >= 97 && c1 <= 122) && (c2 >= 65 && c2 <= 90)) { 
                if(c1 - c2 == 32) 
                    return 0;
            } else if((c2 >= 97 && c2 <= 122) && (c1 >= 65 && c1 <= 90)) {
                if(c2 - c1 == 32)
                    return 0;
            }
        }
        
        // 97-125 -> 65-93 (adds 123-125 -> 91-93)
        if(this.getCasemapping().getCasemappingConstant() == CasemappingDefinition.CASEMAPPING_STRICT_RFC1459 
                || this.getCasemapping().getCasemappingConstant() == CasemappingDefinition.CASEMAPPING_RFC1459) {
            if((c1 >= 123 && c1 <= 125) && (c2 >= 91 && c2 <= 93)) { 
                if(c1 - c2 == 32) 
                    return 0;
            } else if((c2 >= 123 && c2 <= 125) && (c1 >= 91 && c1 <= 93)) {
                if(c2 - c1 == 32)
                    return 0;
            }
        }
        
        // 97-126 -> 65-94 (adds 126 -> 94)
        if(this.getCasemapping().getCasemappingConstant() == CasemappingDefinition.CASEMAPPING_RFC1459) {
            if(((c1 == 126) && (c2 == 94)) || ((c2 == 126) && (c1 == 94))) { 
                return 0;
            }
        }
        
        // just check like usual
        if(c1 > c2) {
            return 1;
        } else {
            return -1;
        }
    }
}
