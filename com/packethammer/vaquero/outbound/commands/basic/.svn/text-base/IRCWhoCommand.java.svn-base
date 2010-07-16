/*
 * This command allows you to query for user-specific information by a mask.
 * RFC1459 defines only one parameter, 'o', to search only for IRC operators,
 * but some servers may provide additional parameters.
 *
 * Note that the mask feature (that is, allowance of wildcards in WHO queries)
 * may not be supported on servers that are strictly RFC1459, which only allows
 * full nicknames to be used).
 *
 * TODO: (remove the following once we discover how WHO replies work)
 * TODO: Replicate the problem described below...
 * NOTE: The response numerics for this behave in an interesting way on some
 * servers, sometimes responding with multiple nicknames that include previous
 * connections. Odd.
 */

package com.packethammer.vaquero.outbound.commands.basic;

import java.util.HashSet;
import java.util.Set;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.outbound.commands.IRCCommand;

public class IRCWhoCommand extends IRCCommand {  
    /** This flag specifies that only server operators should be returned in the WHO reply. */
    public static final Character IRC1459_PARAMETER_OPERSONLY = new Character('o');
            
    private String userMask;
    private Set<Character> parameters;
    
    /**
     * Instantiates this WHO command with a mask to search with and a single
     * parameter to use in addition. Please read how the mask works under
     * the setUserMask() method if you don't already know.
     *
     * @param mask The mask to search with.
     * @param parameter The single parameter to use. Can be null to use none.
     * @see #setUserMask()
     */
    public IRCWhoCommand(String mask, Character parameter) {
        this();
        this.setUserMask(mask);
        
        if(parameter != null)
            this.addParameter(parameter);
    }   
    
    /**
     * Instantiates this WHO command with a mask to search with. Please read how 
     * the mask works under the setUserMask() method if you don't already know.
     *
     * @param mask The mask to search with.
     * @see #setUserMask()
     */
    public IRCWhoCommand(String mask) {
        this(mask, null);
    }     
    
    public IRCWhoCommand() {
        parameters = new HashSet();
    }    
    
    /**
     * This adds a parameter to include with our request.
     */
    public void addParameter(Character parameter) {
        this.parameters.add(parameter);
    }
    
    /**
     * Returns the parameter list we are sending with this command.
     */
    public Set<Character> getParameters() {
        return parameters;
    }
    
    /**
     * Sets the search mask to use when attempting to find the given user. This
     * is defined in RFC1459 as a nickname, but RFC2812 allows masks. However,
     * it appears this mask is not in traditional hostmask form -- instead, it
     * searches both nicknames and hosts at the same time, so searches for 
     * "*m" would return people who have nicknames ending in m (ie: 'johnm') and 
     * people with hosts ending in m (ie: 'myisp.com').
     *
     * This can also be a channel name if we wanted users in a channel.
     */
    public void setUserMask(String mask) {
        this.userMask = mask;
    }
    
    /**
     * @see #setUserMask()
     */
    public String getUserMask() {
        return userMask;
    }
    
    public IRCRawLine renderForIRC() {
        String parameterString = new String();
        for(Character c : parameters)
            parameterString += c;
        
        if(parameterString.length() == 0)
            parameterString = null;
        
        return IRCRawLine.buildRawLine(false, "WHO", this.getUserMask(), parameterString);
    }    
   
    public boolean isSendable() {
        return this.getUserMask() != null;
    }
}
