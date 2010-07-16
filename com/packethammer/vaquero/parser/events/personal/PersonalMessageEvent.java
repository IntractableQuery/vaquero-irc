/*
 * Occurs when a message comes directly to us from a user/server.
 */

package com.packethammer.vaquero.parser.events.personal;

import com.packethammer.vaquero.parser.events.basic.IRCMessageEvent;

public class PersonalMessageEvent extends IRCMessageEvent {
    public PersonalMessageEvent() {
        super();
    }    
    
    public String toString() {
        return "[PRIVATE] " + super.toString();
    }
}
