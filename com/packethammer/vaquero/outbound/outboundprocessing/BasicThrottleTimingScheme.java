/**
 * This class simply mandates that x milliseconds seperate each command sent
 * to the server.
 */

package com.packethammer.vaquero.outbound.outboundprocessing;

public class BasicThrottleTimingScheme extends TimingScheme {
    private long throttleRate;
    
    /**
     * Initializes this timing scheme with the number of milliseconds to 
     * stagger each command release by.
     *
     * @param throttleRate The rate in milliseconds to put between each command release. 
     */
    public BasicThrottleTimingScheme(long throttleRate) {
        this.throttleRate = throttleRate;
    }
    
    public synchronized void run() {
        while(!this.isStopped()) {
            this.releaseCommand();
            
            try {
                this.wait(throttleRate);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
