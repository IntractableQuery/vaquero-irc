/**
 * This is a DCC SEND session for when we're the one sending the file.
 */

package com.packethammer.vaquero.dcc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class DCCFileSendSession extends DCCFileTransferSession {
    public static final int CHUNKSIZE = 1024;
    
    private long amountTransferred;
    private long amountTransferredAccordingToHost;
    private Thread fileTransfer;
    
    private DataInputStream fileIn;
    private DataInputStream socketIn;
    private DataOutputStream socketOut;
            
    
    /**
     * Initializes this DCC file transfer session with the file to send,
     * the socket to use, and the maximum amount of bandwidth to use in
     * kilobytes-per-second.
     *
     * @param socket The socket to use.
     * @param file The file being sent or received.
     * @param maxKilobytesPerSecond The maximum number of kilobytes/sec to send to the remote host. Set to 0 for no limit.
     */
    public DCCFileSendSession(Socket socket, File file, int maxKilobytesPerSecond) {
        super(socket, file, true, maxKilobytesPerSecond, file.length());
    }
    
    /** 
     * This method initializes any required resources and loads the file for
     * transfer. It will then execute the file transfer in a new thread,
     * meaning that this method does not block.
     *
     * @throw IOException if there was a problem initializing network resources or preparing the file for reading.
     */
    public void beginTransfer() throws IOException {
        this.getSocket().setSendBufferSize(CHUNKSIZE);
        fileIn = new DataInputStream(new FileInputStream(this.getFile()));
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
        long fileLen = this.getFile().length();
        try { 
            while(amountTransferred < fileLen) { 
                // decide how much data is left to read, and use the max chunk size, or the amount left
                long numBytes = fileLen - amountTransferred;
                if(numBytes > CHUNKSIZE)
                    numBytes = CHUNKSIZE;

                int nBytes = (int) numBytes;

                // get permission to send that much data
                this.blockForData(nBytes);

                // read the data from the file
                byte[] data = new byte[nBytes];
                fileIn.read(data);

                // send the data to the remote host, then wait for the packet acknowlegement response
                socketOut.write(data);
                socketOut.flush();

                // increase the amount sent
                amountTransferred += numBytes;

                // notify listeners
                for(DCCFileTransferListener listener : this.getTransferListeners()) {
                    listener.onChunkTransfer(data);
                }

                // we will now keep reading the responses that indicate how much data the remote host has received so far until it matches the total amount we've sent (this enforces consistency with the outbound data throttling)
                while(amountTransferredAccordingToHost < amountTransferred) {
                    amountTransferredAccordingToHost = this.readUnsignedInt32(this.socketIn);
                }

                // once we're here, the data we just sent has all been received by the remote host -- we can continue the loop now
            }

            // if we're here, the file transfer is finished -- we just wait for the remote host to acknowlege the rest of the data if it hasn't yet (I think this is redundant...)
            while(amountTransferredAccordingToHost < amountTransferred) {
                amountTransferredAccordingToHost = this.readUnsignedInt32(this.socketIn);
            }
            
            // now, the transfer is finished -- notify listeners
            for(DCCFileTransferListener listener : this.getTransferListeners()) {
                listener.onTransferSuccess();
            }
        } catch (IOException ex) {
            // this is bad -- the transfer fails.
            for(DCCFileTransferListener listener : this.getTransferListeners()) {
                listener.onFail(ex);
            }
        }
        
        // clean up
        try { fileIn.close();       } catch (Exception e) {};
        try { socketIn.close();     } catch (Exception e) {};
        try { socketOut.close();    } catch (Exception e) {};
        
        for(DCCFileTransferListener listener : this.getTransferListeners()) {
            listener.onFinish();
        }
    }
    
    private long readUnsignedInt32(DataInputStream in) throws IOException {
        return (((long) ((in.readByte() & 0xff) << 24
                | (in.readByte() & 0xff) << 16
                | (in.readByte() & 0xff) << 8
                | (in.readByte() & 0xff)))
               & 0xFFFFFFFFL);
    }

    public long getAmountTransferred() {
        return amountTransferred;
    }

    /**
     * This returns the current amount of data (in bytes) that the remote host 
     * currently has. This number is usually slightly lower than the amount
     * we've transferred, assuming we are still sending the file.
     *
     * @return Amount received thus far in bytes.
     */
    public long getAmountTransferredAccordingToHost() {
        return amountTransferredAccordingToHost;
    }
}
