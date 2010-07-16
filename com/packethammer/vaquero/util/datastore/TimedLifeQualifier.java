/**
 * This life qualifier only lasts for x milliseconds from its time of creation,
 * unless it is configured to not start until later.
 */

package com.packethammer.vaquero.util.datastore;

public class TimedLifeQualifier extends LifeQualifier {
    private long time;
    private boolean started;
    private Thread dequalifier;
    
    /**
     * Starts this timed life qualifier right away with the specified number of
     * milliseconds to wait until dequalifying.
     *
     * @param time Milliseconds to wait until dequalifying. 
     */
    public TimedLifeQualifier(long time) {
        this(time, true);
    }
    
    /** 
     * Initializes this timed life qualifier with the number of milliseconds
     * to wait before dequalifying.
     *
     * @param time Milliseconds to wait until dequalifying.
     * @param autostart True if timing should begin right away, or false if this must manually be started.
     */
    public TimedLifeQualifier(long time, boolean autostart) {
        this.time = time;
    }
    
    /**
     * Starts this timed life qualifier.
     */
    public void start() {
        if(!started) {
            started = true;
            dequalifier = new Thread() {
                public synchronized void run() {
                    try {
                        wait(time);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    dequalify();
                }
            };
            
            dequalifier.start();
        }
    }
}
