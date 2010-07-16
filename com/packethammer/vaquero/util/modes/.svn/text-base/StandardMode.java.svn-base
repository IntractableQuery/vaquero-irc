/*
 * This is a generalized mode. A mode is is either being added or removed, it has
 * a mode character, and it may or may not have a parameter.
 *
 * This class also contains important utility methods to manipulate a Collection
 * of modes.
 *
 * Note that it is not the job of a mode to maintain its own integrity. That is,
 * it is not supposed to be aware if it can have or not have a parameter
 * (for example: user mode in a channel +o ALWAYS has a parameter, but here,
 * you could simply omit it). It is the job of the user building the mode to
 * build it correctly. This would be done with the assistance of a tracking 
 * device since knowlege of the network's properties is required in order to
 * validate a mode in most cases.
 *
 * However, there are subclasses which help with modification of parameters
 * and pre-made mode classes that represent the modes essential to IRC defined
 * by the RFC.
 */

package com.packethammer.vaquero.util.modes;

import java.util.*;
import com.packethammer.vaquero.parser.tracking.definitions.ModeDefinition;

public class StandardMode implements Comparable {
    private Character mode;
    private String parameter;
    private boolean adding;
    private ModeDefinition modeDefinition;
    
    public StandardMode() {      
    }
    
    /**
     * Instantiates this mode using a mode character and mode parameter.
     *
     * @param mode The mode to set.
     * @param parameter The mode's parameter.
     * @param beingAdded Set to true to add the mode, or false to remove it.
     */
    public StandardMode(Character mode, String parameter, boolean beingAdded) {      
       this.setMode(mode);
       this.setParameter(parameter);
       this.setAdding(beingAdded);
    }
    
    /**
     * Instantiates this mode using a mode character.
     *
     * @param mode The mode to set.
     * @param beingAdded Set to true to add the mode, or false to remove it.
     */
    public StandardMode(Character mode, boolean beingAdded) {      
       this(mode, null, beingAdded);
    }
    
    /**
     * Instantiates this mode using a mode definition. Mode definitions are
     * generally provided with a mode by the parser system so that it is 
     * easier to identify a mode and its unique properties.
     *
     * @param modeDefinition The mode definition for this usage of the mode.
     */
    public StandardMode(ModeDefinition modeDefinition) {      
        this.modeDefinition = modeDefinition;
        this.mode = modeDefinition.getMode();
    }
    
    /**
     * Returns this mode instance's mode definition, assuming it has one.
     *
     * @return The mode definition or null if there was none.
     */
    public ModeDefinition getModeDefinition() {
        return modeDefinition;
    }
    
    /**
     * Determines if this mode instance has a mode definition to provide.
     *
     * @return True if there is a mode definition, false otherwise.
     */
    public boolean hasModeDefinition() {
        return this.getModeDefinition() != null;
    }

    /**
     * Determines if this mode is being added to a channel, giving or taking some property.
     *
     * @return True if the mode is being added, false if it is being removed.
     */
    public boolean isAdding() {
        return this.isBeingAdded();
    }
    
    /**
     * Determines if this mode is being added to a channel, giving or taking some property.
     *
     * @return True if the mode is being added, false if it is being removed.
     */
    public boolean isBeingAdded() {
        return adding;
    }

    /**
     * Determines if this mode is being removed from a channel, giving or taking some property.
     *
     * @return True if the mode is being removed, false if it is being added.
     */
    public boolean isBeingRemoved() {
        return !this.isBeingAdded();
    }
    
    /**
     * Determines if this mode is being added or removed.
     *
     * @param adding Set to true if adding the mode, false if removing.
     */
    public void setAdding(boolean adding) {
        this.adding = adding;
    }

    /**
     * Returns this mode's character representation.
     *
     * @return The character representing this mode.
     */
    public Character getMode() {
        return mode;
    }
    
    /**
     * Returns this mode's character representation as a primitive char.
     *
     * @return The character representing this mode.
     */
    public char getModeChar() {
        return mode.charValue();
    }

    /**
     * Sets the character representing this mode.
     *
     * @param mode The character representing the mode.
     */
    public void setMode(char mode) {
        this.mode = new Character(mode);
    }
    
    /**
     * Determines if this mode has a parameter.
     *
     * @return True if this mode has a parameter, false otherwise.
     */
    public boolean hasParameter() {
        return this.getParameter() != null;
    }

    /**
     * Returns this mode's parameter. Will return null if no parameter.
     *
     * @return A parameter string, or null if no parameter.
     */
    public String getParameter() {
        return parameter;
    }

    /**
     * Sets this mode's parameter.
     *
     * @param parameter The mode's parameter.
     */
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
    
    /**
     * Returns a string formatted for IRC usage from a collection of modes.
     * You should never assume that the modes will be removed or added in a 
     * particular order, although things tend to go left-to-right with removed
     * modes coming first and added modes coming last. 
     * 
     * Example:
     * +ovmi john bill
     */
    public static String renderModes(Collection<StandardMode> modes) {
        // First, we split the modes into those to be removed and those to be added
        ArrayList<StandardMode> remove = new ArrayList();
        ArrayList<StandardMode> add = new ArrayList();
        
        for(StandardMode mode : modes) {
            if(mode.isBeingAdded())
                add.add(mode);
            else
                remove.add(mode);
        }
        
        StringBuilder modesText = new StringBuilder();
        StringBuilder parametersText = new StringBuilder();
        
        // I've observed that removal of modes normally takes place first, so we'll do that
        // add modes to the the string first...
        if(!remove.isEmpty()) {
            modesText.append('-');
            buildBuffersFromModes(remove, modesText, parametersText);
        }
        
        if(!add.isEmpty()) {
            modesText.append('+');
            buildBuffersFromModes(add, modesText, parametersText);
        }
        
        // now, concat the two strings and return them.
        return modesText.toString() + parametersText.toString();
    }
    
    /**
     * Builds a mode and parameters string from modes without regard to their signage.
     */
    private static void buildBuffersFromModes(ArrayList<StandardMode> modes, StringBuilder modesText, StringBuilder parametersText) {
        for(StandardMode mode : modes) {
            modesText.append(mode.getModeChar());

            if(mode.hasParameter()) {
                parametersText.append(' ');
                parametersText.append(mode.getParameter());
            } 
        }
    }
    
    /**
     * This will default to using the hash of the mode's character.
     * If you put this mode in a hash-sorted collection, it would be stored
     * according to only the character's hash.
     */
    public int hashCode() {
        return this.getMode().hashCode();
    }
    
    /**
     * Two modes are equal only if the mode characters and parameters are the 
     * exact same (including letter casing). Otherwise, standard lexigraphical
     * lookup methods are used on the modes, and then the parameters.
     *
     * This method obviously does not take into account the IRC network's
     * casemapping; keep this in mind for some special cases.
     */
    public int compareTo(Object o) {
        StandardMode m = (StandardMode) o;
        if(this.getMode() == m.getMode()) {
            // modes are the exact same, evaluate based on parameter
            if(this.getParameter() != null && m.getParameter() != null) {
                return this.getParameter().compareTo(m.getParameter());
            } else if (this.getParameter() == null && m.getParameter() != null) {
                // our param is null, the other's is not -- we are less than
                return -1;
            } else {
                // our param is set to something, the other's null -- we are greater
                return 1;
            }
        } else {
            // modes are different, evaluate
            return this.getMode().compareTo(m.getMode());
        }
    }
    
    /**
     * @see #compareTo()
     */
    public boolean equals(Object o) {
        return this.compareTo(o) == 0;
    }
    
    public String toString() {
        return "MODE: " + this.getMode() + ", PARAM: " + this.getParameter();
    }
}
