/**
 * RPL_NAMREPLY
 * "['=' / '*' / '@'] <channel> :<names...>"
 *
 * This is the NAMES reply for a channel. The NAMES list is given to you when
 * you join a channel, although the NAMES command can be sent to retrieve it.
 *
 * There is a difference between RFC1459 and RFC2812's RPL_NAMREPLY, so I've
 * sort of created a mix between the two. This class ignores the character
 * located between the channel name and the names list, since it is
 * fairly useless. If you really want it for usage, let me know.
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;
import com.packethammer.vaquero.parser.tracking.IRCServerISupport;
import com.packethammer.vaquero.parser.tracking.definitions.ChannelNickPrefixModeDefinition;

public class NamesReply extends IRCNumericEvent {
    private ArrayList<String> nicknames;
    
    public NamesReply() {
        nicknames = new ArrayList();
    }
    
    /**
     * Returns the name of the channel that this reply is for.
     */
    public String getChannel() {
        if(this.isRFC2812())
            return this.getNumericArg(1);
        else
            return this.getNumericArg(0);
    }
    
    /**
     * Determines if this NAMES reply was in RFC2812 format (it has additional
     * information as the first parameter).
     */
    public boolean isRFC2812() {
        if(this.getNumericArg(0).equals("=") || this.getNumericArg(0).equals("*") || this.getNumericArg(0).equals("@"))
            return true;
        else
            return false;
    }
    
    /**
     * Returns the nicknames list with the mode prefix intact (that is, these
     * nicknames are not quite 'clean' yet).
     */
    public String[] getUncleanNicknames() {
        if(this.isRFC2812())
            return this.getNumericArg(2).split(" ");
        else
            return this.getNumericArg(1).split(" ");
    }
    
    /**
     * Given the ISUPPORT information tracker, this will return a map with
     * nicknames as the key, and nickname mode prefix definitions.
     *
     * @param iSupport The parser's ISUPPORT information.
     * @return A non-empty map of nicknames mapped to their respective nickname prefix modes (this may be null if the user has no prefix)
     */
    public Map<String,ChannelNickPrefixModeDefinition> getNicknames(IRCServerISupport iSupport) {
        Map<String,ChannelNickPrefixModeDefinition> map = new LinkedHashMap();
        for(String rawNick : this.getUncleanNicknames()) {
            for(ChannelNickPrefixModeDefinition def : iSupport.getNickPrefixModes()) {
                if(new Character(rawNick.charAt(0)).equals(def.getPrefix())) {
                    map.put(rawNick.substring(1), def);
                    break;
                }
                map.put(rawNick, null);
            }
        }
        return map;
    }
    
    
    public boolean validate() {
        if(this.numericArgumentCount() >= 2) {
            // pre-parse the list...
            int x = 1; // should be first nickname's index
            if(this.isRFC2812())
                x++; // we were at the channel name, so hop ahead
            
            // pull names
            for( ; x < this.numericArgumentCount(); x++) {
                nicknames.add(this.getNumericArg(x));
            }
            
            return true;
        } else {
            return false;
        }
    }
    
    public int getHandledNumeric() {
        return this.RPL_NAMREPLY;
    }
    
    public String toString() {
        String names = "";
        for(String nick : this.getUncleanNicknames()) 
            names += nick + " ";
        return super.toString() + ", CHAN:" + this.getChannel() + ", NAMES:" + names;
    }
}
