/*
 * Defines a method required for listening to IRC events.
 */

package com.packethammer.vaquero.util.eventsystem;

public abstract class EventListener {  
    private EventDistributor parentDistributor;
    private boolean active = true;
    
    /**
     * This method occurs when an event occurs based off some event object.
     * The subclass of this event listener should generate a specific event
     * based on this.
     */
    public abstract void onPureEvent(Object event);

    /**
     * Returns the event distributor that gives this event listener events.
     *
     * @return An event distributor.
     */
    protected EventDistributor getParentDistributor() {
        return parentDistributor;
    }

    /**
     * Sets the event distributor that gives this event listener events.
     *
     * @param parentDistributor An event distributor.
     */
    protected void setParentDistributor(EventDistributor parentDistributor) {
        this.parentDistributor = parentDistributor;
    }
    
    /**
     * This renders the listener unable to receive further events. Once you are
     * done with a listener, it is a very good idea to call this method to free
     * up resources.
     *
     * Note: this method does not try to unregister itself with the parent 
     * distributor directly since it can result in a ConcurrentModification 
     * exception. The parent distributor will clean up reference(s) when it
     * sees fit.
     */
    public void unregisterMe() {
        this.setParentDistributor(null);
        active = false;
    }

    /**
     * Determines if this event listener is active. If it is not, it is eventually
     * going to get discarded.
     */
    public boolean isActive() {
        return active;
    }
}
