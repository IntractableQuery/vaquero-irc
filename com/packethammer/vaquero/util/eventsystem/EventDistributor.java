/*
 * Distributes events to listeners. 
 *
 * Event generation is very effecient -- finding listeners that are listening
 * to a specific event type (whether they be dynamic or hard listeners) is an
 * O(1) operation. Adding/removing listeners is fairly expensive, but this is
 * acceptable as these operations don't occur very often. However, it is best
 * to do as much as possible with your listener(s) and avoid unnecessary 
 * adding/removal.
 */

package com.packethammer.vaquero.util.eventsystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class EventDistributor {
    private EventRegister register;
    
    // Facilitates speedy lookup of listeners by event type
    private HashMap<Class, Vector<EventListener>> listeners;
    
    /**
     * Initializes this event distributor with an event register to use.
     */
    public EventDistributor(EventRegister register) {
        listeners = new HashMap();
        this.register = register;
    }
    
    /**
     * Adds an event listener for an event type (class). Can contain more than 
     * one instance of a single event listener. You may assume that event
     * listeners added in a given order will receive events in that order.
     *
     * Events that are subclasses of the given class will be provided to the
     * listener in addition to the actual class given.
     */
    public void addDynamicEventListener(Class eventClass, EventListener listener) {
        // note: this method just adds tons of hard event listeners for the given class and all children
        addHardEventListener(eventClass, listener); // add for immediate class first
        
        // now add hard listeners for all children of this given class
        for(EventClassInformation classInfo : register.getAllChildrenFor(eventClass)) {
            addHardEventListenerDirectly(classInfo.getEventClass(), listener);
        }
    }
    
    /**
     * Adds an event listener using an instance of the event type we wish to
     * listen for. See overloaded counterpart for more details.
     *
     * @param eventInstance An IRC event instance from which we can derive a class type to listen to.
     * @param listener The listener that catches these kinds of events.
     * @see #addHardEventListener(Class, IRCEventListener)
     */
    public void addHardEventListener(Object eventType, EventListener listener) {
        Class eventClass = eventType.getClass();
        this.addHardEventListener(eventClass, listener);
    }
    
    /**
     * Adds an event listener for an event type (class). Can contain more than 
     * one instance of a single event listener. You may assume that event
     * listeners added in a given order will receive events in that order.
     *
     * Only events with class names matching exactly that given will be given
     * to the listener; subclasses of the given event do not get passed to
     * the listener.
     *
     * A hard listener is the most effecient type of event listener since it does
     * not need to catch events that are below it in the class hierarchy. As an
     * obvious result, it guarantees the event class type that is received.
     *
     * @param eventClass A class definition for IRCEvent or similarly, a subclass of it.
     * @param listener The listener that catches these kinds of events.
     */
    public void addHardEventListener(Class eventClass, EventListener listener) {
        addHardEventListenerDirectly(eventClass, listener);
    }
    
    /**
     * Behaves like its public counterpart, but does not perform important 
     * pre- and post-operations essential to the integrity of data inside this
     * class.
     */
    private void addHardEventListenerDirectly(Class eventClass, EventListener listener) {
        listener.setParentDistributor(this);
        Vector<EventListener> currentEventListeners; // holds the current listeners for this type of event
        currentEventListeners = listeners.get(eventClass);
        if(currentEventListeners == null) {
            // first, make sure to create a new event listener list
            currentEventListeners = new Vector();
        }
            
        
        // add listener
        currentEventListeners.add(listener);
        
        listeners.put(eventClass, currentEventListeners);
    }
    
    /**
     * Removes an event listener. Uses default equals() method to determine if
     * the provided listener is the same as one stored internally. Will remove
     * multiple equal listeners (it does not exit after first removal).
     * 
     * @param listener The listener to remove.
     * @return True if successful in removing one or more listeners, false otherwise.
     */
    public boolean removeEventListener(EventListener listener) {
        boolean success = false;
        
        synchronized(listeners) {
            for(Class eventClass : listeners.keySet()) {
                Vector<EventListener> listenersList = listeners.get(eventClass);
                Iterator<EventListener> i = listenersList.iterator(); // for each listener in the event's listener list
                while(i.hasNext()) {
                    EventListener curListener = i.next();
                    if(curListener.equals(listener)) {
                        listener.unregisterMe();
                        i.remove();
                        success = true;
                    }
                }
            }
        }
        
        return success;        
    }
    
    /**
     * Takes an abitrary event and propagates it amongst the proper
     * event listeners. Note that it is entirely possible that there are no
     * listeners for the event that is provided here.
     * 
     * @param event The event to attempt propagation of.
     */
    public void propagateEvent(Object event) {
        Class eventClass = (Class) event.getClass();
        
        synchronized(listeners) {
            Vector<EventListener> eventListeners = listeners.get(eventClass);
            if(eventListeners != null) {
                Iterator<EventListener> i = eventListeners.iterator();
                while(i.hasNext()) {
                    EventListener listener = i.next();
                    if(!listener.isActive()) {
                        // remove it
                        i.remove();
                    } else {
                        listener.onPureEvent(event);

                        if(!listener.isActive()) {
                            // remove it
                            i.remove();
                        }
                    }
                }
            }
        }
    }
   
    /**
     * Gets a collection of every event type being listened for, including
     * abstract classes and interfaces.
     */
    public Collection<Class> getAllListenedEventClasses() {
        return this.listeners.keySet();
    }
    
    /**
     * Cleans up resources and renders this event distributor unusable.
     */
    public void die() {
        // copy the listeners to unregister
        List<EventListener> list = new ArrayList();
        for(List<EventListener> listenerList : this.listeners.values()) {
            for(EventListener listener : listenerList) {
                list.add(listener);
            }
        }
        
        // start unregistering the listeners
        for(EventListener listener : list) {
            listener.unregisterMe();
        }
        
        this.listeners = null;
    }
}
