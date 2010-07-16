/*
 * This defines a mode that is related directly to a channel mode which
 * is performed on an IRC user which subsequently changes their symbol
 * prefix in that channel.
 *
 * Be aware that down the road when it comes time to parse a channel's NAMES
 * reply, that those prefixes are not wholly telling of the user's actual
 * modes in a channel. It only lists the "greatest" one (that is, if a user
 * has +vo in a channel, only the traditional @ would be shown -- you wouldn't
 * know they have +v without some further investigation or observation).
 */


package com.packethammer.vaquero.parser.tracking.definitions;

import com.packethammer.vaquero.util.modes.channel.ChannelMode;

public class ChannelNickPrefixModeDefinition extends ChannelModeDefinition {  
    private Character prefix;
    //private boolean statusPrefixMsgMode; // see below
    
    /** 
     * Initializes the immutable information for this mode.
     *
     * @param mode The literal case-sensitive IRC mode's character of representation.
     * @param modePrefix The prefix (@, +, etc.) that is given to users who receive this mode.
     */
    public ChannelNickPrefixModeDefinition(Character mode, Character modePrefix) {
        super(mode, MODETYPE_B_ALWAYSPARAM); // modes of this type are implictly of type B
        this.prefix = modePrefix;
    }
    
    /**
     * Returns the mode prefix character that users are given when they receive
     * this mode. Remember, some modes have precendence over others when 
     * being displayed in something like NAMES reply, so be prepared
     * for that, especially when you wish to render your own user list.
     * 
     * @return The symbol the user is adorned with when they are given this mode.
     */
    public Character getPrefix() {
         return prefix;
    }
    

    // see where "setStatusPrefixMsgMode" is commented out
    /*
     * Determines if this is a STATUSMSG mode as defined within numeric 005 
     * ISUPPORT. Such a mode can be used as a prefix to a fully-qualified
     * channel name in a NOTICE, which will send the message to all users with
     * equal or higher status as the mode indicates. More information can be 
     * found in the draft document for ISUPPORT, "draft-brocklesby-irc-isupport-03.txt"
     *
     * Note that this *may* work with a PRIVMSG. It is really very server-dependant.
     * Consult documentation for the server you are on to determine exact 
     * behavior.
     *
     * @return True if this is a STATUSMSG mode, false otherwise.
     *
    public boolean isStatusPrefixMsgMode() {
        return statusPrefixMsgMode;
    }
     */

    // the following is currently disabled since WALLCHOPS/WALLVOICES/STATUSMSG is damn wacky on ircu
    // TODO: figure out a definitive way to deal with this
    /*
     * Determines if this is a STATUSMSG mode.
     *
     * @param statusPrefixMsgMode True if STATUSMSG mode, false otherwise.
     
    public void setStatusPrefixMsgMode(boolean statusPrefixMsgMode) {
        this.statusPrefixMsgMode = statusPrefixMsgMode;
    }
     */
    
    /**
     * From this mode definition, this creates a channel mode instance, setting
     * the definition for that mode as this definition.
     */
    public ChannelMode createChannelMode() {
        ChannelMode mode = new ChannelMode(this);
        mode.setMode(this.getMode());
        mode.setAdding(true);
        
        return mode;
    }
    
    public String toString() {
        return super.toString() + " PREFIX:" + this.getPrefix();
    }
}
