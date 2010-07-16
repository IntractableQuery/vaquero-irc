/**
 * This class handles IRC-oriented mask matching. A mask is simply a search
 * string constructed of ASCII characters, which can contain the wildcards
 * '?' and '*' as special search parameters. The question-mark matches a 
 * single character of any type, while the asterisk matches any number of
 * characters. You cannot escape the wildcard characters.
 *
 * Examples:
 * The mask '?oy' matches 'Toy' and 'Roy', but not 'Buoy', 'oy', etc.
 * The mask 'H*x' matches 'Hax', 'Hx', and 'Haaaaax', but not 'ax' or 'hallo'
 *
 * For more information on mask matching, see RFC1459. Mask matching is most
 * popularly used for channel bans.
 * 
 * Note that on IRC, the server's supported casemapping must be taken into
 * account when comparing two strings, especially a mask string versus a
 * string to test it on. This class is meant to use a casemapping definition
 * to compare a string with a mask, although for other generalized use, you
 * can forgo it for traditional exact-match or case-insensitive matching.
 *
 * This class uses an internal recursive method to perform matching, so beware
 * of feeding this class massive search masks that could exceed the call
 * stack size if fed certain strings (quite rare, and basically impossible for IRC).
 *
 * Note: this class does not use regex internally since IRC casemapping must be taken 
 * into account, so we can't simply formulate a regex matching string!
 */

package com.packethammer.vaquero.util;

import java.util.ArrayList;

public class MaskMatcher {
    /** In this mode, all characters must match the mask perfectly. */
    public static final int MATCHMODE_EXACT = 0;
    /** In this mode, the matching is case-insensitive (so 'bob' matches 'BOB') */
    public static final int MATCHMODE_CASE_INSENSITIVE = 1;
    /** In this mode, the mask matcher requires a casemapping definition to use in comparing a mask to a string */
    public static final int MATCHMODE_IRC_CASEMAPPED = 2;
    
    private int mode;
    private String mask;
    private CasemappingDefinition casemappingDefinition;
    private ArrayList<MatchSection> compiledMask;
    
    /** 
     * Initializes this mask matcher with a search mask string and the casemapping
     * definition to use. This initializes this mask matcher in MATCHMODE_IRC_CASEMAPPED
     * mode.
     *
     * @param mask The mask to use for comparisons (this is internally compiled for optimization)
     * @param definition The IRC casemapping definition to use when comparing this mask to a string. 
     */
    public MaskMatcher(String mask, CasemappingDefinition definition) {
        this(mask, MATCHMODE_IRC_CASEMAPPED);
        this.setCasemappingDefinition(definition);
    }
    
    /** 
     * Initializes this mask matcher with a search mask string and the mode to
     * operate in.
     *
     * @param mask The mask to use for comparisons (this is internally compiled for optimization)
     * @param mode See the MATCHMODE_* constants for a mode to use.
     */
    public MaskMatcher(String mask, int mode) {
        this.mask = mask;
        this.mode = mode;
        
        // compile mask
        compiledMask = new ArrayList();
        
        int curPos = 0;
        while(curPos < mask.length()) {
            if(mask.charAt(curPos) == '?') {
                compiledMask.add(new MatchSection(MatchSection.MATCHMODE_SINGLECHAR));
                curPos++;
            } else if(mask.charAt(curPos) == '*') {
                compiledMask.add(new MatchSection(MatchSection.MATCHMODE_MULTICHAR));
                curPos++;
            } else {
                // start of text string -- find the end of it and add it
                // find a wildcard, which would terminate it
                int end = findEndOfString(curPos, mask);
                
                compiledMask.add(new MatchSection(mask.substring(curPos, end)));
                
                curPos = end;
            }
        }
    }
    
    /**
     * Given a string of text, this will determine if it matches this search
     * mask using the current matching mode. 
     *
     * @return True if the text matches this mask, false otherwise.
     * @return IllegalStateException If we are using MATCHMODE_IRC_CASEMAPPED but the casemapping definition is not yet set.
     */
    public boolean matches(String text) {
        if(this.getMode() == MATCHMODE_IRC_CASEMAPPED && this.getCasemappingDefinition() == null)
            throw new IllegalStateException("Using MATCHMODE_IRC_CASEMAPPED, but no casemapping definition has been set.");
        
        // special case: mask string is empty 
        if(this.getMask().length() == 0) {
            return text.length() == 0; // they only match if they are both empty
        }
        
        return doMatch(text, this.compiledMask, 0);
    }
    
    private boolean doMatch(String text, ArrayList<MatchSection> maskSearch, int sectionIndex) {
        MatchSection currentSection = maskSearch.get(sectionIndex);
        
        boolean isLastMatchSection = false;
        if(sectionIndex == maskSearch.size() - 1) {
            isLastMatchSection = true;
        }
        
        if(currentSection.matchMode == MatchSection.MATCHMODE_SINGLECHAR) {
            if(isLastMatchSection) {
                // we're on the last mask section
                
                // is there *any* characters after the first one in the text? If so, it is impossible for an ending ? match to take place
                if(text.length() == 1) {
                    return true; // only 1 char
                } else {
                    return false; // 0 or more chars
                }
            } else {
                // we're in the middle of the mask somewhere
                if(text.length() >= 1) {
                    // there is one or more characters, indicating the one-character requirement is fulfilled
                    return doMatch(text.substring(1), maskSearch, sectionIndex + 1);
                } else {
                    // not even a first character, fail
                    return false;
                }
            }
        } else if(currentSection.matchMode == MatchSection.MATCHMODE_TEXT) {
            if(isLastMatchSection) {
                // we're on the last mask section
                
                // is there *any* characters after the length of the text we want? if so, it can't possibly match at the end
                if(text.length() == currentSection.text.length()) {
                    // no trailing text, perform match
                    return this.stringsEqual(text, currentSection.text);
                } else {
                    // trailing text = fail
                    return false;
                }
            } else {
                // we're in the middle of the mask somewhere
                if(text.length() >= currentSection.text.length()) {
                    // see if the text matches
                    boolean matches = stringsEqual(text.substring(0, currentSection.text.length()), currentSection.text);
                    if(matches) {
                        if(isLastMatchSection) {
                            // we can end now, this is the last section
                            return true;
                        } else {
                            // proceed to next check       
                            return doMatch(text.substring(currentSection.text.length()), maskSearch, sectionIndex + 1);
                        }
                    } else {
                        // no match
                        return false;
                    }
                } else {
                    // not possible that text can match
                    return false;
                }
            }
        } else if(currentSection.matchMode == MatchSection.MATCHMODE_MULTICHAR) {
            if(isLastMatchSection) {
                // we're on the last mask section
                return true; // no matter what characters remain, they match
            } else {
                // we're in the middle of the mask somewhere
                
                // begin from index 0 and move forward to the end of the string in search of a full match
                for(int index = 0; index < text.length(); index++) {
                    boolean matches = doMatch(text.substring(index), maskSearch, sectionIndex + 1);
                    
                    if(matches) {
                        // all is well, we're done
                        return true;
                    } 
                }
                
                // if we got here, there was no match 
                return false;
            }
        } else {
            throw new IllegalStateException("Illegal mode:" + this.getMode());
        }
    }
    
    private boolean stringsEqual(String str1, String str2) {
        if(this.getMode() == this.MATCHMODE_EXACT) {
            return str1.equals(str2);
        } else if(this.getMode() == this.MATCHMODE_CASE_INSENSITIVE) {
            return str1.equalsIgnoreCase(str2);
        } else {
            return this.getCasemappingDefinition().areStringsEqual(str1, str2);
        }
    }   
    
    private int findEndOfString(int startPosition, String txt) {
        int wildcard1 = txt.indexOf("?", startPosition);
        int wildcard2 = txt.indexOf("*", startPosition);
        
        if(wildcard1 == -1)
            wildcard1 = Integer.MAX_VALUE;
        if(wildcard2 == -1)
            wildcard2 = Integer.MAX_VALUE;
        
        if(wildcard1 < wildcard2 && wildcard1 != Integer.MAX_VALUE)
            return wildcard1;
        else if(wildcard2 < wildcard1 && wildcard2 != Integer.MAX_VALUE)
            return wildcard2;
        else if(wildcard1 == wildcard2 && wildcard1 != Integer.MAX_VALUE)
            return wildcard1;
        else
            return txt.length();
    }

    /**
     * Returns the mode that this mask matcher is in (see MATCHMODE_* constants
     * that correspond to this mode integer).
     */
    public int getMode() {
        return mode;
    }

    /**
     * @see #getMode()
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * Returns the search mask that this mask matcher was initialized with.
     */
    public String getMask() {
        return mask;
    }

    /**
     * Returns the casemapping definition being used by this mask matcher. 
     * The mask matcher only has a casemapping definition (and it is only used)
     * if the matcher is using mode MATCHMODE_IRC_CASEMAPPED.
     *
     * @return Casemapping definition in use.
     */
    public CasemappingDefinition getCasemappingDefinition() {
        return casemappingDefinition;
    }

    /**
     * @see #getCasemappingDefinition()
     */
    public void setCasemappingDefinition(CasemappingDefinition casemappingDefinition) {
        this.casemappingDefinition = casemappingDefinition;
    }
    
    private class MatchSection {
        public String text;
        public int matchMode;
        
        public static final int MATCHMODE_SINGLECHAR = 0;
        public static final int MATCHMODE_MULTICHAR = 1;
        public static final int MATCHMODE_TEXT = 2;
        
        public MatchSection(int mode) {
            this.matchMode = mode;
        }
        
        public MatchSection(String text) {
            this.matchMode = MATCHMODE_TEXT;
            this.text = text;
        }
        
        public String toString() {
            String txt = "";
            if(this.matchMode == MATCHMODE_SINGLECHAR)
                txt += "{SINGLE}";
            else if(this.matchMode == MATCHMODE_MULTICHAR)
                txt += "{MULTI}";
            else
                txt += text;
            
            return txt;
        }
    }
    
    /**
     * For testing the mask matcher.
     */
    public static void main(String[] args) {
        MaskMatcher m = new MaskMatcher("I*ro***n?ist", new CasemappingDefinition(CasemappingDefinition.CASEMAPPING_ASCII));
        System.out.println(m.matches("IronFist"));
    }
}
