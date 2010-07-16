/*
 * Represents a set of IRC modes being set/removed on a low level. May be 
 * channel or user modes.
 *
 * This class is best used for parsing and rendering modes; it's sort of 
 * a hacky way to store modes.
 */

package com.packethammer.vaquero.util.modes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;
import com.packethammer.vaquero.util.modes.channel.ChannelMode;
import com.packethammer.vaquero.util.modes.user.UserMode;
import com.packethammer.vaquero.parser.tracking.definitions.ChannelModeDefinition;
import com.packethammer.vaquero.parser.tracking.definitions.ModeDefinition;

public class Modes<E extends StandardMode> {
    private ArrayList<E> modes;
    private boolean strictModeSetEnabled;
    
    /**
     * Initializes this modes collection with additional options.
     *
     * @param strictModeSetEnabled Set to true if you want only one type of a mode to exist in this class (ie: cannot contain more than one 'o', one 'v', etc.)
     */
    public Modes(boolean strictModeSetEnabled) {
        this();
        this.strictModeSetEnabled = strictModeSetEnabled;
    }
    
    /**
     * Initializes the modes from a collection.
     *
     * @param collection The modes to initialize with. 
     */
    public Modes(Collection<E> modes) {
        this();
        this.modes.addAll(modes);
    }
    
    /** Creates a new instance of Modes */
    public Modes() {
        modes = new ArrayList();
    }
    
    /**
     * Adds a mode (in sequential order) to the mode list.
     *
     * @param m The mode to add.
     */
    public void addMode(E m) {
        if(strictModeSetEnabled) {
            for(E existingMode : this.modes) {
                if(m.getMode().equals(existingMode.getMode())) {
                    return; // don't allow duplicate entry
                }
            }
        }
        
        this.modes.add(m);
    }
    
    /**
     * Returns the modes being set/removed. This list is the internal one,
     * so modification to it effectively alters the state of this mode set.
     *
     * @return A list of modes.
     */
    public List<E> getModes() {
        return modes;
    }
    
    public Modes<UserMode> getAsUserModes() {
        return (Modes) this;
    }
    
    public Modes<ChannelMode> getAsChannelModes() {
        return (Modes) this;
    }
    
    /**
     * Parses a string and attempts to extract the modes using a set of mode
     * definitions. A runtime exception is thrown if an unexpected mode is 
     * encountered, as it is impossible to properly parse modes when we don't 
     * have definitions to match them.
     *
     * Note that this method DOES NOT determine a proper subclass of ChannelMode
     * or UserMode to use; this is because it can be somewhat problematic, and it is not 
     * particularly wise to assume a certain mode does a specific thing. As 
     * such, it is best if the user is left to do a little of their own mode
     * extrapolation -- the subclasses of ChannelMode/UserMode are best left
     * for sending modes rather than parsing!
     *
     * @param modesSet The modes being set/removed.
     * @param modeDefinitions The mode definitions required to parse the modesSet string.
     * @return A Modes collection if parsing was successful.
     * @throws MissingModeDefinitionException if we are missing a mode definition.
     * @throws ModeParsingException if a parsing problem is encountered.
     */
    public static Modes parseModes(String modesSet, Collection<ModeDefinition> modeDefinitions) throws MissingModeDefinitionException {
        Modes<StandardMode> newModes = new Modes();
        boolean adding = true;
        
        // try to figure out if these are channel modes
        boolean channelModes = false;
        if(!modeDefinitions.isEmpty())
            channelModes = modeDefinitions.iterator().next().isChannelMode();
        
        StringTokenizer t = new StringTokenizer(modesSet, " ");
        if(t.hasMoreTokens()) {
            String modesString = t.nextToken();
            for(char c : modesString.toCharArray()) {
                if(c == '+')
                    adding = true;
                else if(c == '-')
                    adding = false;
                else {
                    boolean foundDefinition = false;
                    // process mode
                    for(ModeDefinition modeDef : modeDefinitions) {
                        if(modeDef.getMode().equals(c)) {
                            foundDefinition = true;
                            StandardMode mode = null;
                            if(modeDef.isChannelMode()) {
                                mode = new ChannelMode((ChannelModeDefinition) modeDef);                                
                            } else if(modeDef.isUserMode()) {
                                mode = new UserMode(modeDef);
                            }
                            
                            if((adding && modeDef.isAdditionAccompaniedByParameter()) || 
                                    !adding && modeDef.isRemovalAccompaniedByParameter()) {
                                if(t.hasMoreTokens()) {
                                    mode.setParameter(t.nextToken());
                                } else {
                                    throw new ModeParsingException("Error while parsing for mode '" + mode.getMode() + "' -- expected parameter was missing");
                                }
                            }
                            
                            mode.setAdding(adding);
                            newModes.addMode(mode);
                        }
                    }
                    
                    if(!foundDefinition) {
                        // bad.
                        throw new MissingModeDefinitionException(c);
                    }
                }
            }
        } else {
            throw new ModeParsingException("Nothing to parse!");
        }
        
        return newModes;
    }
    
    /**
     * Determines if this modes set is only allowing one type of any given mode
     * to exist (no duplicate mode types).
     *
     * @return True if set-like behavrior for modes is on, false otherwise.
     */
    public boolean isStrictModeSetEnabled() {
        return strictModeSetEnabled;
    }
    
    /**
     * Renders this set of modes for IRC usage. Examples:
     * +xi
     * +mno jim
     *
     * @return IRC-suitable mode string.
     * @see vaquero.datastructures.modes.StandardMode.renderModes(String)
     */
    public String renderModes() {
        return StandardMode.renderModes((List) this.modes);
    }
    
    public String toString() {
        return renderModes();
    }

}
