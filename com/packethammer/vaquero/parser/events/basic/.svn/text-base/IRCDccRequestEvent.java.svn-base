/**
 * This represents an inbound DCC CHAT or SEND event. 
 */

package com.packethammer.vaquero.parser.events.basic;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IRCDccRequestEvent extends IRCCTCPEvent {
    public static final String DCC_TYPE_FILESEND = "SEND";
    public static final String DCC_TYPE_CHAT = "CHAT";
    
    public static final String DCC_PREFIX = "DCC";
    
    /** Creates a new instance of IRCDccRequestEvent */
    public IRCDccRequestEvent() {
        super();
    }
    
    /**
     * Returns the type of this DCC. Typically, this is CHAT or SEND.
     */
    public String getDccType() {
        return this.getCTCPText().split(" ")[0];
    }    
    
    /**
     * Determines if this is a DCC CHAT request.
     */
    public boolean isDccChatRequest() {
        return this.getDccType().equalsIgnoreCase(DCC_TYPE_CHAT);
    }
    
    /**
     * Determines if this is a DCC SEND request.
     */
    public boolean isDccSendRequest() {
        return this.getDccType().equalsIgnoreCase(DCC_TYPE_FILESEND);
    }
    
    /**
     * Returns the special argument associated with this DCC type. This is
     * only used for DCC_TYPE_FILESEND, where it is the name of the file being sent.
     */
    public String getDccSpecialArgument() {
        return this.getCTCPText().split(" ")[1];
    }
    
    /**
     * Returns the address that the remote host wants us to connect to.
     *
     * @throws UnknownHostException If there was a problem retrieving the address.
     */
    public InetAddress getDccAddress() throws UnknownHostException {
        // gosh, I love java's signed datatypes...
        long num = Long.parseLong(this.getCTCPText().split(" ")[2]); // IP is unsigned int32
        byte[] ip = new byte[4];
        ip[0] = (byte) ((num & 0xFF000000) >> 24);
        ip[1] = (byte) ((num & 0x00FF0000) >> 16);
        ip[2] = (byte) ((num & 0x0000FF00) >> 8);
        ip[3] = (byte) ((num & 0x000000FF));
        
        return InetAddress.getByAddress(ip);
    }   
    
    /**
     * Returns the port on the remote host that the remote host wants us to
     * connect to.
     */
    public int getDccPort() {
        return Integer.parseInt(this.getCTCPText().split(" ")[3]);
    }   
    
    /**
     * Returns the size of the file being sent, assuming this DCC is of type
     * DCC_TYPE_FILESEND. Note that some clients, even when sending us a file,
     * may omit this information. 
     *
     * @return The size of the file, or -1 if it was not present or if this request is not a file send request.
     */
    public long getDccFileSize() {
        if(this.getDccType().equals(DCC_TYPE_FILESEND)) {
            String[] s = this.getCTCPText().split(" ");
            if(s.length >= 5) {
                return Long.parseLong(s[4]);
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }
    
    
}
