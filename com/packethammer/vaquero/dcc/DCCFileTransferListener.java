/**
 * This defines a class that listens for events related to a DCC file send
 * or retrieval.
 */

package com.packethammer.vaquero.dcc;

import java.io.IOException;

public class DCCFileTransferListener extends DCCSessionListener {    
    /**
     * Occurs when this DCC file transfer session has finished, and the file
     * has successfully been transferred.
     */
    public void onTransferSuccess() {
        
    }
    
    /**
     * Occurs when this DCC file transfer has failed.
     *
     * @param e The IO exception that caused the failure.
     */
    public void onFail(IOException e) {
        
    }
    
    /**
     * Occurs every time a packet of data containing part of the file is 
     * transferred (that is, every time we sent a packet, or every time 
     * we get a packet
     */
    public void onChunkTransfer(byte[] data) {
        
    }
}
