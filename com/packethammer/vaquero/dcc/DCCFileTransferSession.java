/**
 * This abstract class represents either an outbound or inbound file
 * transfer operation.
 */

package com.packethammer.vaquero.dcc;

import java.io.File;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public abstract class DCCFileTransferSession extends DCCSession {
    private File file;
    private int maxKilobytesPerSecond;
    private Timer dataTimer;
    private long fileSize;
    private Vector<DCCFileTransferListener> transferListeners;
    
    private int bytesSentInPreviousSecond;
    private int bytesSentInLastSecond;
    
    /**
     * Initializes this DCC file transfer session with the file to transfer,
     * the socket to use, and the maximum amount of bandwidth to use in
     * kilobytes-per-second.
     *
     * @param socket The socket to use.
     * @param file The file being sent or received.
     * @param initiatedByUs Set to true if we are sending the file, or false if we're receiving it.
     * @param maxKilobytesPerSecond The maximum number of kilobytes/sec to send or receive from the remote host. Set to 0 for no limit.
     * @param fileSize The size of the file being transferred. Set to -1 if it is unknown.
     */
    public DCCFileTransferSession(Socket socket, File file, boolean initiatedByUs, int maxKilobytesPerSecond, long fileSize) {
        super(socket, initiatedByUs ? TYPE_FILESEND : TYPE_FILERECEIVE);
        this.transferListeners = new Vector();
        this.file = file;
        this.fileSize = fileSize;
        this.setMaxKilobytesPerSecond(maxKilobytesPerSecond);
        
        // start a timer to handle the max kilobytes per second, assuming we need it
        if(this.getMaxKilobytesPerSecond() > 0) {
            dataTimer = new Timer();
            dataTimer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    fired();
                }
            }, 0, 1000);
        }
    }

    /**
     * Returns the file being transferred (its location on disk).
     */
    public File getFile() {
        return file;
    }

    /**
     * Returns the maximum number of kilobytes to transfer per second. This
     * applies to a file we are receiving or a file we are sending.
     */
    public int getMaxKilobytesPerSecond() {
        return maxKilobytesPerSecond;
    }

    /**
     * Sets the maximum number of kilobytes to transfer per second. This
     * applies to a file we are receiving or a file we are sending. Changing this
     * while the file transfer is in progress will immediately take effect.
     *
     * It is notable that this works most accurately for files we are sending out. 
     * It still works for files that are being received, but due to the nature
     * of DCC file transfers, it won't work with as great accurancy.
     */
    public void setMaxKilobytesPerSecond(int maxKilobytesPerSecond) {
        this.maxKilobytesPerSecond = maxKilobytesPerSecond;
    }
    
    /**
     * Returns the size of the file being transferred. This will be -1 if it
     * is unknown (the only time it is possibly unknown is when we are receiving
     * a file, assuming the remote host neglected to tell us the size).
     *
     * @return Transfer file size in bytes or -1 if it is unknown.
     */
    public long getFileSize() {
        return this.fileSize;
    }
    
    /**
     * Returns the current transfer rate in bytes-per-second.
     *
     * @return Tranfer rate in bytes per second.
     */
    public int getTransferRate() {
        // if this transfer was less than a second, we need to use the other rate
        if(this.bytesSentInPreviousSecond > 0)
            return this.bytesSentInPreviousSecond;
        else
            return this.bytesSentInLastSecond;
    }
    
    /**
     * Returns percentage finished, or -1.0 if the file size is unknown
     * (and thus, we cannot gauge the percent finished).
     *
     * @return Percentage of transfer that has finished, or -1.0 if unknown
     */
    public double getPercentageFinished() {
        if(this.getFileSize() > -1) {
            return (((double) this.getAmountTransferred()) / ((double) this.getFileSize())) * 100.0D;
        } else {
            return -1.0D;
        }
    }
    
    /**
     * This is a method meant only for internal use. It will block (prevent
     * return) so that the maximum kilobytes per second is never exceeded. It
     * is called for every chunk of file data we send or receive
     * and it will not return until we are allowed to send/receive more data.
     */
    public synchronized void blockForData(int numBytes) {
        bytesSentInLastSecond += numBytes;
        
        if(this.getMaxKilobytesPerSecond() > 0) {
            // we're throttling data
            while((bytesSentInLastSecond / 1024) >= this.getMaxKilobytesPerSecond()) {
                try {
                    this.wait(); // the fired() method will wake us up when this second expires, allowing more data to be sent.
                } catch (InterruptedException ex) {
                }
            }
        } else {
            // we're not throttling data, so return right away
            return;
        }
    }
    
    /** 
     * Returns the number of bytes transferred thus far.
     */
    public abstract long getAmountTransferred();
    
    /** 
     * Occurs when the 1-second timer goes off.
     */
    private synchronized void fired() {
        bytesSentInPreviousSecond = bytesSentInLastSecond;
        
        // reset the counter for this new second
        bytesSentInLastSecond = 0;
        
        // wake up any blocking that may be going on in handleKilobyteBlock()
        this.notifyAll();
    }

    /**
     * Returns the current file transfer listeners for this file transfer 
     * session.
     */
    public Vector<DCCFileTransferListener> getTransferListeners() {
        return transferListeners;
    }
    
    /**
     * Adds a file transfer listener.
     *
     * @param listener The file transfer listener to add.
     */
    public void addListener(DCCFileTransferListener listener) {
        listener.setSession(this);
        this.transferListeners.add(listener);
    }
    
    /**
     * Removes a file transfer listener. Removes multiple copies if they exist.
     *
     * @param listener The file transfer listener to remove.
     */
    public void removeListener(DCCFileTransferListener listener) {
        while(this.transferListeners.remove(listener)) {
            // just keep removing the listener until we no longer have any more of that type left
        }
    }
    
    public String toString() {
        double percentage = this.getPercentageFinished();
        String finished = String.format("%.2f", percentage) + "%";
        return super.toString() + ", FINISHED:" + finished + ", TRANSFERRED:" + this.getAmountTransferred() + ", SIZE:" + this.getFileSize() + ", RATE:" + (this.getTransferRate() / 1024) + "kb/sec, CAPPED:" + this.getMaxKilobytesPerSecond() + "kb/sec, FILE:" + this.getFile();
    }
}
