/*
 * This represents the message you receive before you have successfuly 
 * "connected" to the server. At this time, you know virtually nothing about
 * yourself and have no nickname as you are not yet on the IRC server, but 
 * rather, awaiting entrance.
 *
 * NOTICE AUTH <MESSAGE>
 */

package com.packethammer.vaquero.parser.events.server;

import java.util.Date;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.basic.IRCNoticeEvent;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;

public class IRCAuthNoticeEvent extends IRCNoticeEvent {
    public IRCAuthNoticeEvent() {
        this.setTargetingMe(false); // logically, it may seem this should be true, but consider the fact that outside systems may rely on this to set our current nickname, but this is definitely not our assigned nick on the server!
    }    

    public String toString() {
        return "[CONNECT AUTHNOTICE] " + this.getMessage();
    }
}
