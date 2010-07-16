/**
 * Represents a WHO-reply listener.
 */

package com.packethammer.vaquero.advanced.dispatcher.querying;

import com.packethammer.vaquero.parser.events.server.numeric.reply.EndOfWhoReply;

public abstract class AbstractWhoListener extends AbstractCommandQueryListener {
    /**
     * Occurs when the replies to this WHO query have finished.
     */
    public void onFinished(EndOfWhoReply end) {
        
    }
}