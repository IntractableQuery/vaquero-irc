/**
 * Occurs when we were listening for a remote host to connect, but they never
 * did in the amount of time we allocated for them.
 */

package com.packethammer.vaquero.dcc;

public class ConnectionListeningTimedOutException extends Exception {
    public ConnectionListeningTimedOutException(String reason) {
        super(reason);
    }    
}
