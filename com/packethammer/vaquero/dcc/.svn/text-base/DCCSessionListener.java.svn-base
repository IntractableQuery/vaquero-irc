/**
 * Defines a class that listens to a DCC session.
 */

package com.packethammer.vaquero.dcc;

public abstract class DCCSessionListener {
    private DCCSession session;
    
    /** 
     * Occurs when this DCC session is finished. It may or may not have completed
     * its initial function successfully, but it is definitely no longer in use.
     */
    public void onFinish() {
        
    }
    
    /**
     * This occurs only when we sent a DCC request of some sort, but it timed
     * out (we didn't receive the connection from the remote host in the timed 
     * we wanted it in).
     *
     * This does not construe failure in a file transfer, since the file 
     * transfer never began.
     */
    public void onServerListeningTimeout() {
        
    }

    /**
     * Returns the DCC session this listener is listening to.
     */
    public DCCSession getSession() {
        return session;
    }

    /**
     * @see #getSession();
     */
    public void setSession(DCCSession session) {
        this.session = session;
    }
}
