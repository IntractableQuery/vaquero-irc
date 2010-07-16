/**
 * A data store is simply a collection of objects that are mapped to strings.
 * Essentially, it functions like a HashMap for speed and effeciency. However,
 * the keys (strings) only exist so long as they are "alive." To stay alive, 
 * they must have at least one "life qualifier." Once all life qualifiers
 * are gone, the key is removed from the data store. This allows you to do
 * things like add a timed life qualifier which will cause the key to be removed
 * after a certain amount of time has passed.
 *
 * Keys are case-insensitive.
 */

package com.packethammer.vaquero.util.datastore;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class DataStore {
    private HashMap<String,StorageElement> map;
    
    /** Creates a new instance of DataStore */
    public DataStore() {
        map = new HashMap();
    }
    
    /** 
     * Stores a value at a key with a given array of life qualifiers to
     * use. 
     *
     * @param key The key to store the value at.
     * @param value The object to store.
     * @param lifeQualifiers An array of life qualifiers to use.
     */
    public void store(String key, Object value, LifeQualifier[] lifeQualifiers) {
        key = key.toUpperCase();
        StorageElement e = new StorageElement();
        e.element = value;
        
        for(LifeQualifier q : lifeQualifiers)
            this.addLifeQualifier(key, q);
        
        map.put(key, e);
    }
    
    /** 
     * Stores a value at a key with a given life qualifier to use.
     *
     * @param key The key to store the value at.
     * @param value The object to store.
     * @param lifeQualifier The life qualifier to use.
     */
    public void store(String key, Object value, LifeQualifier lifeQualifier) {
        this.store(key, value, new LifeQualifier[] { lifeQualifier });
    }
    
    /** 
     * Stores a value at a key which will last for the life of this data store.
     * Adding additional life qualifiers to this key will not affect it.
     *
     * @param key The key to store the value at.
     * @param value The object to store.
     */
    public void store(String key, Object value) {
        this.store(key, value, new InfiniteLifeQualifier());
    }
    
    /**
     * Returns the object at a given key position, or null if the key does
     * not exist or the actual value was null.
     *
     * @param key The key to retrieve the value at. 
     * @return The value associated with the given key or null.
     */
    public Object get(String key) {
        StorageElement e = map.get(key.toUpperCase());
        if(e != null) {
            return e.element;
        } else {
            return null;
        }
    }
    
    /**
     * This convenience method returns the object associated with the given key 
     * as a string. If the key does not exist, the value is null, or the value
     * is not a String, null is returned.
     *
     * @param key The key to retrieve the value at.
     * @return The string associated with the given key or null.
     */
    public String getString(String key) {
        try {
            return (String) this.get(key);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Determines if we have a given key in the store.
     *
     * @param key The key to check for existence.
     * @return True if the key exists, false otherwise.
     */
    public boolean hasKey(String key) {
        return map.containsKey(key.toUpperCase());
    }
    
    /**
     * Removes a life qualifier (which is associated with a key inherently).
     * This typically occurs when one expires and is removing itself.
     *
     * Uses equals() for comparisons.
     *
     * @param q The life qualifier to remove.
     * @return True if one (or more) life qualifiers were removed.
     */
    public boolean removeLifeQualifier(LifeQualifier q) {
        boolean removed = false;
        
        StorageElement e = map.get(q.getKey());
        if(e != null) {
            Iterator<LifeQualifier> i = e.lifeQualifiers.iterator();
            while(i.hasNext()) {
                if(i.next().equals(q)) {
                    i.remove();
                    removed = true;
                }
            }
            
            if(e.lifeQualifiers.isEmpty()) {
                // no more life qualifiers = dead key
                map.remove(q.getKey());
            }
        }
        
         return removed;
    }
    
    /** 
     * Adds a life qualifier to a key.
     */
    public void addLifeQualifier(String key, LifeQualifier lifeQualifier) {
        key = key.toUpperCase();
        StorageElement e = map.get(key);
        if(e != null) {
            this.prepareLifeQualifier(key, lifeQualifier);
            e.lifeQualifiers.add(lifeQualifier);
        }
    }
    
    /**
     * Prepares a life qualifier for usage.
     */
    private void prepareLifeQualifier(String key, LifeQualifier lifeQualifier) {
        lifeQualifier.setKey(key.toUpperCase());
        lifeQualifier.setStore(this);
    }
    
    /**
     * Prepares this object for garbage collection by dumping as many references
     * as possible and performing general cleanup duties.
     */
    public void destroy() {
        map = null;
    }
    
    
    private class StorageElement {
        public Object element;
        public Vector<LifeQualifier> lifeQualifiers;
        
        public StorageElement() {
            lifeQualifiers = new Vector();
        }
    }
    
    public String toString() {
        String txt = "(";
        Iterator<String> i = this.map.keySet().iterator();
        while(i.hasNext()) {
            String s = i.next();
            txt += s + "=" + this.map.get(s).element;
            
            if(i.hasNext())
                txt += ", ";
        }
        
        txt += ")";
        
        return txt;
    }
}
