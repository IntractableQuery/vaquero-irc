/**
 * This listener is used for WHOX queries.
 */

package com.packethammer.vaquero.advanced.dispatcher.querying;

import java.util.ArrayList;
import com.packethammer.vaquero.outbound.commands.extended.ircu.WhoXSearchOptions;
import com.packethammer.vaquero.parser.events.server.numeric.reply.WhoXReply;

public class WhoXQueryListener extends AbstractWhoListener {
    private WhoXSearchOptions searchOptions;
    private ArrayList<WhoXReply> replies;
    
    public WhoXQueryListener() {
        replies = new ArrayList();
    }

    /**
     * Returns the search options used for this query.
     */
    public WhoXSearchOptions getSearchOptions() {
        return searchOptions;
    }

    /**
     * @see #getSearchOptions()
     */
    public void setSearchOptions(WhoXSearchOptions searchOptions) {
        this.searchOptions = searchOptions;
    }
    
    /**
     * Used by the query processor to add a reply to this query listener,
     * which adds it to the internal list and calls the onReply event.
     */
    protected void addReply(WhoXReply reply) {
        reply.setSearchOptions(this.getSearchOptions());
        replies.add(reply);
        onReply(reply);
    }
    
    /**
     * Occurs for each reply from the server related to our query. Note that
     * the search options for the reply will already be set, so you do
     * not need to set them before accessing the data in the reply.
     */
    public void onReply(WhoXReply reply) {
        
    }
    
    /**
     * Returns a list of all replies received thus far to our original WHO
     * query. If you use this in the onFinished event, you will receive all
     * query replies.
     */
    public ArrayList<WhoXReply> getReplies() {
        return replies;
    }
}
