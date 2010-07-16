/*
 * This is simply an IRC event we received that we don't know how to parse.
 * It is not generated from an actual IRC command.
 */

package com.packethammer.vaquero.parser.events.basic;

import java.util.Date;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;

public class IRCUnknownEvent extends IRCEvent{
    public IRCUnknownEvent() {
    } 
}
