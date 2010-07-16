/*
 * This class consists of some useful string operations. Some may not even
 * be used by the parser, although they are left intact.
 */

package com.packethammer.vaquero.parser;

import java.util.Collection;
import java.util.Iterator;

public class StringOperations {
    /**
     * Given a starting and ending string, and a starting position, this method
     * will attempt to extract the string between the starting and ending 
     * string and return it as a substring.
     * 
     * @param source The string to search.
     * @param start The left side of the string to pull out.
     * @param end The right side of the string to pull out.
     * @param startIndex The index to begin at.
     * @return The string between start and end or null if the string is non-existent.
     */
    public static String getBetween(String source, String start, String end, int startIndex) {
        int startLoc = source.indexOf(start, startIndex);
        if(startLoc > -1) {
            int endLoc = source.indexOf(end, startLoc + start.length());
            if(endLoc > -1) {
                return source.substring(startLoc + start.length(), endLoc);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    
    /**
     * Determines if a string is a valid integer.
     *
     * @param num The possible integer in string form.
     * @return True if integer, false otherwise.
     */
    public static boolean isInteger(String num) {
        try {
            Integer.parseInt(num);            
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
    
    /**
     * Given a starting and ending string, this method
     * will attempt to extract the string between the starting and ending 
     * string and return it as a substring.
     *
     * @param source The string to search.
     * @param start The left side of the string to pull out.
     * @param end The right side of the string to pull out.
     * @return The string between start and end or null if the string is non-existent.
     */
    public static String getBetween(String source, String start, String end) {
        return getBetween(source, start, end, 0);
    }
    
    /**
     * Given a list of strings, this will return the contents in a comma-delimited 
     * back-to-back string with no whitespaces.
     */
    public static String commaDelimit(Collection list) {
        StringBuilder b = new StringBuilder();
        
        Iterator i = list.iterator();
        while(i.hasNext()) {
            String str = i.next().toString();
            b.append(str);
            
            if(i.hasNext())
               b.append(','); 
        }
        
        return b.toString();
    }
}
