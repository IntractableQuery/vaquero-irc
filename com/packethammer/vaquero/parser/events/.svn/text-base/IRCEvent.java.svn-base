/**
 * Represents a base IRC event. Events should be guaranteed to originate from
 * a single IRC line. As such, that raw line's data is included with each IRC event. 
 * It should be left to intermediate event systems to generate more specific 
 * events off of this all-encompassing event (example: a subclass of this might 
 * represent channel modes being changed, while an intermediate event 
 * monitoring system could spawn its own events for each +o, +v, etc.) -- that
 * is not the job of the general parser.
 *
 * Children of this class should use the parameters contained herein to return 
 * "parsed" data when resonably possible to save memory. However, CPU-expensive
 * operations should take advantage of the pre-initialization phase to build 
 * internal data.
 *
 * This class is designed around rfc1459's description of an incoming message;
 * a message may or may not contain the origin server of the command (indicated by a 
 * ":server.name.here <arguments...>"), but has a set of arguments, which by
 * the RFC specification may not not exceed 15 in number.
 */

package com.packethammer.vaquero.parser.events;

import java.util.Date;
import com.packethammer.vaquero.parser.IRCParser;
import com.packethammer.vaquero.util.protocol.IRCRawLine;

public abstract class IRCEvent extends IRCRawLine {
    private Date arrival;
    private boolean targetingMe;
    private IRCParser parser;

    public IRCEvent() {
    }

    /**
     * Gets the arrival date/time (which is accurate to the millisecond 
     * inherently) of this event (this should generally be the time it was 
     * received from the server, although it likely isn't wise to use it
     * for much other than general logging features).
     */
    public Date getArrival() {
        return arrival;
    }

    /**
     * Sets the arrival time of this IRC event.
     * 
     * @param val The date (time) this IRC event arrived.
     */
    public void setArrival(Date val) {
        this.arrival = val;
    }
    
    /**
     * Determines if this event targets you directly. The most common example
     * of this is private notices, private messages, etc.
     *
     * @return True if we are targeted, false otherwise.
     */
    public boolean isTargetingMe() {
        return targetingMe;
    }

    /**
     * Determines if we are the target of this event.
     *
     * @param targetingMe True if we are the target, false otherwise.
     */
    public void setTargetingMe(boolean targetingMe) {
        this.targetingMe = targetingMe;
    }
    
    /**
     * Returns the parser that has generated this event.
     *
     * @return The irc parser.
     */
    public IRCParser getParser() {
        return parser;
    }

    /**
     * Sets the parser generating this event.
     *
     * @param parser The irc parser.
     */
    public void setParser(IRCParser parser) {
        this.parser = parser;
    }
    
    /**
     * This method is called on a method right after it is assigned its raw
     * line, but before any of its accessor methods are called. It should 
     * mainly be used by the parser to prepare the event before it can be
     * accessed.
     *
     * Inside this method, you should perform and initialization and initial
     * parsing you need to perform.
     */
    public void preinitialize() {
        
    }
    
    

    /**
     * Returns a string representing this object.
     */
    public String toString() {
        return "[" + this.getSource().getShortHostmask() + "] " + this.toRawLine();
    }

}
