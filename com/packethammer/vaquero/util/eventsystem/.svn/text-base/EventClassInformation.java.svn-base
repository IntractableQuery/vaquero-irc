/*
 * Contains information related to an event's class which allows reverse lookups
 * to determine which classes extend or implement this class/interface.
 */

package com.packethammer.vaquero.util.eventsystem;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Vector;

public class EventClassInformation {
    private Class eventClass;
    private List<EventClassInformation> classesDerivedFromThis;     
            
    /**
     * Initializes this entry with its event class.
     *
     * @param eventClass The event class.
     */
    public EventClassInformation(Class eventClass) {
        this.eventClass = eventClass;
        this.classesDerivedFromThis = new Vector();
    }
    
    /**
     * Returns the event class represented.
     *
     * @return The event class.
     */
    public Class getEventClass() {
        return this.eventClass;
    }
    
    /**
     * Adds a class derived from this class.
     *
     * @param classInfo The class information for the class derived from this.
     */
    public void addDerivedClass(EventClassInformation classInfo) {
        this.classesDerivedFromThis.add(classInfo);
    }
    
    /**
     * Returns the instance of the derived class list.
     *
     * @return List of all classes derived from this class.
     */
    public List<EventClassInformation> getClassesDerivedFromThis() {
        return this.classesDerivedFromThis;
    }
    
    /**
     * Clears the relationships this class has to others.
     */
    public void clearRelationships() {
        this.classesDerivedFromThis.clear();
    }
    
    public String toString() {
        String ret = "CLASS:[" + this.getEventClass().getSimpleName() + "] IMPLEMENTS:[ ";
        for(Class c : this.getEventClass().getInterfaces()) {
            ret += c.getSimpleName() + " ";
        }
        
        Class s = (Class) this.getEventClass().getGenericSuperclass();
        
        String superClass = "x";
        if(s != null)
            superClass = s.getSimpleName();
        
        ret += "] SUPER:" + superClass;
        
        String derv = "";
        for(EventClassInformation i : this.getClassesDerivedFromThis()) {
            derv += i.getEventClass().getSimpleName() + " ";
        }
        
        ret += " DERIVATIONS:" + derv;
        
        return ret;
    }
}
