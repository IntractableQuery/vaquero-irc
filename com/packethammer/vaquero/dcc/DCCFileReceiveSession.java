/**
 * This is a DCC SEND session for when we're the one receiving a file.
 */

package com.packethammer.vaquero.dcc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DCCFileReceiveSession extends DCCFileTransferSession { 
    public static final int CHUNKSIZE = 2048;
    
    private long amountTransferred;
    private Thread fileTransfer;
    
    private DataOutputStream fileOut;
    private DataInputStream socketIn;
    private DataOutputStream socketOut;
                
    /**
     * Initializes this DCC file transfer session with the file to write to,
     * the socket to use, and the maximum amount of bandwidth to use in
     * kilobytes-per-second.
     *
     * This assumes the socket is already connected.
     *
     * @param socket The socket to use.
     * @param file The file being sent or received.
     * @param maxKilobytesPerSecond The maximum number of kilobytes/sec to receive from the remote host. Set to 0 for no limit.
     * @param fileSize The size of the file we're receiving, or -1 if it is unknown
     */
    public DCCFileReceiveSession(Socket socket, File file, int maxKilobytesPerSecond, long fileSize) {
        super(socket, file, false, maxKilobytesPerSecond, fileSize);
    }
    
    /** 
     * This method initializes any required resources and loads the file for
     * writing. It will then execute the file receival in a new thread so
     * that this method does not block.
     *
     * Calling this method assumes that we are connected to the remote host.
     *
     * @throw IOException if there was a problem initializing network resources or preparing the file for writing.
     */
    public void beginTransfer() throws IOException {
        fileOut = new DataOutputStream(new FileOutputStream(this.getFile()));
        socketIn = new DataInputStream(this.getSocket().getInputStream());
        socketOut = new DataOutputStream(this.getSocket().getOutputStream());        
        
        fileTransfer = new Thread() {
            public void run() {
                transferFile();
            }
        };
        
        fileTransfer.start();
    }
    
    private synchronized void transferFile() {    
        try {
            while(true) {
                if(amountTransferred == this.getFileSize() && this.getFileSize() > -1) {
                    // we're finished!
                    for(DCCFileTransferListener listener : this.getTransferListeners()) {
                        listener.onTransferSuccess();
                    }
                    
                    break;
                }

                // read in any available bytes
                byte[] data = new byte[CHUNKSIZE];  
                int numBytes = socketIn.read(data);
                
                // update the total transferred bytes
                amountTransferred += numBytes;

                if(numBytes == -1) { // no data to read, meaning remote host closed connection
                    // did we get all the data we needed?
                    if(this.getFileSize() > -1) {
                        // we know how much we need
                        if(this.getFileSize() == amountTransferred) {
                            // we're done!
                            break;
                        } else {
                            // we're not done, but the connection closed -- bad
                            throw new IOException("Remote DCC host closed connection as if file transfer was finished, but it had not sent us enough data!");
                        }
                    } else {
                        // we didn't know how much data to receive, so assume the file transfer finished
                        for(DCCFileTransferListener listener : this.getTransferListeners()) {
                            listener.onTransferSuccess();
                        }
                        
                        break;
                    }
                }
                
                // we got data, so see if we need to pause before digesting it
                this.blockForData(numBytes);
                
                // now, write to disk
                fileOut.write(data, 0, numBytes);
                
                // tell the remote host how much data we have received so far (this should include how much data was in the packet it sent...hopefully we read a packet?)
                writeUnsignedInt32(socketOut, amountTransferred);                
            }
        } catch (IOException ex) {
            // this is bad -- the transfer fails.
            for(DCCFileTransferListener listener : this.getTransferListeners()) {
                listener.onFail(ex);
            }
        }
        
        // clean up
        try { fileOut.close();      } catch (Exception e) {};
        try { socketIn.close();     } catch (Exception e) {};
        try { socketOut.close();    } catch (Exception e) {};
        
        this.setFinished(true);
        
        for(DCCFileTransferListener listener : this.getTransferListeners()) {
            listener.onFinish();
        }
    }
    
    private void writeUnsignedInt32(DataOutputStream out, long int32) throws IOException {
        byte[] data = new byte[4];
	data[0] = (byte) ((int32 & 0xFF000000L) >> 24);
	data[1] = (byte) ((int32 & 0x00FF0000L) >> 16);
	data[2] = (byte) ((int32 & 0x0000FF00L) >> 8);
	data[3] = (byte) ((int32 & 0x000000FFL));
        out.write(data);
        out.flush();
    }

    public long getAmountTransferred() {
        return amountTransferred;
    }
}
