/*
 * Defines a method required for listening to IRC events.
 */

package com.packethammer.vaquero.parser;

import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.util.eventsystem.EventListener;

public class IRCEventListener extends EventListener {     
    /**
     * Called when IRC event takes place.
     *
     * @param e The IRC event. 
     */
    public void onEvent(IRCEvent e) {
        
    }
    
    public void onPureEvent(Object event) {
        this.onEvent((IRCEvent) event);
    }
}
