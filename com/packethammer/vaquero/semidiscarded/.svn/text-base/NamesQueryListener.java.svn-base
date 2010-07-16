/**
 * Defines a class that listens for NAME query replies.
 */

package com.packethammer.vaquero.semidiscarded;

import com.packethammer.vaquero.advanced.dispatcher.querying.AbstractCommandQueryListener;
import java.util.LinkedHashMap;
import java.util.Map;
import com.packethammer.vaquero.parser.events.server.numeric.reply.EndOfNamesReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.NamesReply;
import com.packethammer.vaquero.parser.tracking.IRCServerISupport;
import com.packethammer.vaquero.parser.tracking.definitions.ChannelNickPrefixModeDefinition;

public class NamesQueryListener extends AbstractCommandQueryListener {
    Map<String,ChannelNickPrefixModeDefinition> allNames;
    
    public NamesQueryListener() {
        allNames = new LinkedHashMap();
    }
    
    /**
     * This method adds a names reply to this listener and calls the
     * onReply() method. Uses ISUPPORT information to extract names in
     * useful format.
     *
     * @param processNicknames Only set to false if this is a dummy listener. Otherwise, keep it true.
     */
    public void addNamesReply(NamesReply names, IRCServerISupport iSupport, boolean processNicknames) {
        Map<String,ChannelNickPrefixModeDefinition> map = names.getNicknames(iSupport);
        allNames.putAll(map);
        onReply(names, map);
    }
            
    /**
     * Occurs when we receive a relevant names reply from the server for our
     * query. Includes the processed names map for convenience
     *
     * @param names The names reply.
     * @param namesMap The nicknames with their corresponding prefixing modes.
     */
    public void onReply(NamesReply names, Map<String,ChannelNickPrefixModeDefinition> namesMap) {
        
    }
    
    /**
     * Occurs when this NAMES reply ends.
     */
    public void onFinished(EndOfNamesReply end) {
        
    }
}
