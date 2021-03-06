/*
 * Distributes IRC events to listeners. 
 *
 * Event generation is very effecient -- finding listeners that are listening
 * to a specific event type (whether they be dynamic or hard listeners) is an
 * O(1) operation. Adding/removing listeners is fairly expensive, but this is
 * acceptable as these operations don't occur very often. However, it is best
 * to do as much as possible with your listener(s) and avoid unnecessary 
 * adding/removal.
 */

package com.packethammer.vaquero.parser;

import com.packethammer.vaquero.util.eventsystem.EventDistributor;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.util.eventsystem.EventRegister;

public class IRCEventDistributor extends EventDistributor {
    public IRCEventDistributor(EventRegister register) {
        super(register);
    }
    
    /**
     * Adds an event listener for an event type (class). Can contain more than 
     * one instance of a single event listener. You may assume that event
     * listeners added in a given order will receive events in that order.
     *
     * This adds the event listener as a "dynamic" event listener, which means
     * that the listener will receive events that match the provided class name
     * and those events that are subclasses of it. As an example, if you provided
     * the IRCMessageEvent class as an event to listen for, you would also 
     * receive IRCCTCPEvent, IRCActionEvent, etc. (these are subclasses of 
     * IRCMessageEvent). As a side note, you could catch every event possible
     * event by simply catching the top-level IRCEvent class.
     */
    public void addDynamicEventListener(Class eventClass, IRCEventListener listener) {
        super.addDynamicEventListener(eventClass, listener);
    }
    
    /**
     * Adds an event listener using an instance of the event type we wish to
     * listen for. See overloaded counterpart for more details.
     *
     * @param eventInstance An IRC event instance from which we can derive a class type to listen to.
     * @param listener The listener that catches these kinds of events.
     * @see #addHardEventListener(Class, IRCEventListener)
     */
    public void addHardEventListener(IRCEvent eventType, IRCEventListener listener) {
        Class eventClass = eventType.getClass();
        super.addHardEventListener(eventClass, listener);
    }
    
    /**
     * Adds an event listener for an event type (class). Can contain more than 
     * one instance of a single event listener. You may assume that event
     * listeners added in a given order will receive events in that order.
     *
     * This adds the event listener as a "hard" event listener, which means that
     * the event listener will only receive events that match the provided class
     * name EXACTLY. A hard event listener ignores class hierarchy. It is ideal
     * for events that have no subclasses (such as JOIN/PART/QUIT) and events
     * that are more useful to catch at different levels in the hierarchy like
     * IRCMessageEvent (as opposed to its subclass, IRCCTCPEvent, which for most
     * uses is not even considered a message). 
     *
     * A hard listener is the most effecient type of event listener since it does
     * not need to catch events that are below it in the class hierarchy. As an
     * obvious result, it guarantees the event class type that is received.
     *
     * @param eventClass A class definition for IRCEvent or similarly, a subclass of it.
     * @param listener The listener that catches these kinds of events.
     */
    public void addHardEventListener(Class eventClass, IRCEventListener listener) {
        super.addHardEventListener(eventClass, listener);
    }
    
}
