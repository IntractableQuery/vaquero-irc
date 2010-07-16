/*
 * RPL_ISUPPORT
 * "<PARAMETER=VALUE ...>"
 *
 * Contains multiple "key=value" or "key" arguments. We should ignore the 
 * extended argument that comes with it. Note that a duplicate numeric, 
 * RPL_BOUNCE exists. It is the job of the parser to determine which of the 
 * two this is.
 *
 * @see ServerBounceReply
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.util.protocol.IRCRawParameter;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;

public class ServerISupportReply extends IRCNumericEvent {
    private Map<String, String> iSupport;
    
    public ServerISupportReply() {
    }
    
    /**
     * Returns a map of key-value pairs. Some keys may not have parameters, as
     * indicated by a null value associated with that key.
     *
     * @return A Map of key-value pairs.
     */
    public Map<String, String> getSupport() {        
        return iSupport;
    }
    
    public boolean validate() {
        if(this.numericArgumentCount() == 0)
                return false;
        
        try {
            LinkedHashMap<String, String> map = new LinkedHashMap(); // will retain order of insertion
            for(IRCRawParameter param : this.getNumericArguments()) {
                // we must ignore the extended argument since it is normally just a text string such as "are supported by this server"
                if(!param.isExtended()) {
                    // each parameter is either "key" or "key=val" -- handle accordingly
                    String key = null; 
                    String val = null;
                    String paramText = param.getParameterString();
                    int equalsLoc = paramText.indexOf("=");
                    if(equalsLoc > -1) {
                        key = paramText.substring(0, equalsLoc);
                        val = paramText.substring(equalsLoc + 1);
                    } else {
                        key = paramText;
                        val = null;
                    }

                    // now, add it to the map
                    map.put(key, val);
                }
            }

            iSupport = map;
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public int getHandledNumeric() {
        return this.RPL_ISUPPORT;
    }
    
    public String toString() {
        return super.toString() + ", ISUPPORT:" + this.getSupport();
    }
}
