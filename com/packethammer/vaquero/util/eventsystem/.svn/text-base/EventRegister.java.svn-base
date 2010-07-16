/*
 * Holds a registry of every event class -- this includes interfaces, abstract classes,
 * and regular classes. An internal hierarchy of the events is built based
 * on their relationships by extension or implementation. When instantiated,
 * it contains every possible event.
 */

package com.packethammer.vaquero.util.eventsystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public abstract class EventRegister {  
    private HashMap<Class, EventClassInformation> events;
    
    public EventRegister() {
        events = new HashMap();
        
        buildRegistry();
        
        // Now, build the relationships
        establishAllRelationships();
    }
    
    /**
     * This is where the registry should be initialized with data.
     */
    public abstract void buildRegistry();
    
    /**
     * Adds an event class to the internal registry.
     *
     * @return The event class information generated for the event.
     */
    public EventClassInformation addEventClass(Class event) throws IllegalAccessException,InstantiationException {
        EventClassInformation info = new EventClassInformation(event);
        events.put(event, info);
        return info;
    }
    
    /**
     * Establishes every class' relationship to the other event classes.
     */
    private void establishAllRelationships() {
        // First, clear existing relationships
        for(EventClassInformation info : events.values()) {
            info.clearRelationships();
        }
        
        // Now, make new ones
        for(EventClassInformation info : events.values()) {
            establishClassRelationships(info);
        }
    }
    
    /**
     * Takes a single class and establishes its relationship with all other event
     * classes, such that those classes know this class is subclassing/implementing
     * them.
     */
    private void establishClassRelationships(EventClassInformation info) {
        if(!info.getEventClass().isInterface()) {
            Class[] interfaces = info.getEventClass().getInterfaces();
            Class superClass = info.getEventClass().getSuperclass();
            
            // go try to find the interface(s)/superclass in our registry to establish relationships
            for(Class iface : interfaces) {
                EventClassInformation eventClassInfo = events.get(iface);
                if(eventClassInfo != null)
                    eventClassInfo.addDerivedClass(info);
            }
            
            EventClassInformation eventClassInfo = events.get(superClass);
            if(eventClassInfo != null)
                eventClassInfo.addDerivedClass(info);
        }
    }
    
    /**
     * Returns all of the internal event class information collected.
     *
     * @return Event class information.
     */
    public Collection<EventClassInformation> getEventClassInformation() {
        return this.events.values();
    }
    
    /**
     * Returns a list of every child class of a given class known inside this
     * registry. This includes both immediate child classes and those that are
     * further down in the hierarchy.
     *
     * @return List of all child classes.
     */
    public List<EventClassInformation> getAllChildrenFor(Class eventClass) {
        EventClassInformation eventInfo = this.getInformationFor(eventClass);
        if(eventInfo != null) {
            return getChildrenRecursively(eventInfo);
        }
        
        return null;
    }
    
    /**
     * Recursively visits every subclass of a given EventClassInformation
     * and returns a list of its subclasses.
     */
    private List<EventClassInformation> getChildrenRecursively(EventClassInformation eventInfo) {
        ArrayList<EventClassInformation> children = new ArrayList();
        for(EventClassInformation subEvent : eventInfo.getClassesDerivedFromThis()) {
            children.add(subEvent);
        }
        
        for(EventClassInformation subEvent : eventInfo.getClassesDerivedFromThis()) {
            children.addAll(getChildrenRecursively(subEvent));
        }
        
        return children;
    }
    
    /**
     * Removes an event from the registry by its class name.
     *
     * @param eventClass The event class to remove.
     */
    public void removeEvent(Class eventClass) {
        this.events.remove(eventClass);
    }
    
    /**
     * Returns class information based upon a class.
     *
     * @param eventClass The class of the event.
     * @return Class information for the given class or null if the class is not in the registry.
     */
    public EventClassInformation getInformationFor(Class eventClass) {
        return this.events.get(eventClass);
    }
}
