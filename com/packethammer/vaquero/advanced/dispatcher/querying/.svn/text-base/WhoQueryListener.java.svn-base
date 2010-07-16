/**
 * This listener is used for watching for regular WHO replies.
 */

package com.packethammer.vaquero.advanced.dispatcher.querying;

import java.util.ArrayList;
import com.packethammer.vaquero.parser.events.server.numeric.reply.WhoReply;

public class WhoQueryListener extends AbstractWhoListener {
    private ArrayList<WhoReply> replies = new ArrayList();
    
    /**
     * Used by the query processor to add a reply to this query listener,
     * which adds it to the internal list and calls the onReply event.
     */
    protected void addReply(WhoReply reply) {
        replies.add(reply);
        onReply(reply);
    }
    
    /**
     * Occurs for each reply from the server related to our query.
     */
    public void onReply(WhoReply reply) {
        
    }
    
    /**
     * Returns a list of all replies received thus far to our original WHO
     * query. If you use this in the onFinished event, you will receive all
     * query replies.
     */
    public ArrayList<WhoReply> getReplies() {
        return replies;
    }
}
