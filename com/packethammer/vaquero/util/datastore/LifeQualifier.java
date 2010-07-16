/**
 * A life qualifier keeps a key in a DataStore alive. Once a key has no more 
 * life qualifiers, it is automatically removed from the data store.
 */

package com.packethammer.vaquero.util.datastore;

public abstract class LifeQualifier {
    private String key;
    private DataStore store;
    
    public LifeQualifier() {
    }
    
    /**
     * Calling this causes this life qualifier to be removed from the key
     * it is currently assigned to. 
     */
    public synchronized void dequalify() {
        getStore().removeLifeQualifier(this);
    }        

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public DataStore getStore() {
        return store;
    }

    public void setStore(DataStore store) {
        this.store = store;
    }
}
