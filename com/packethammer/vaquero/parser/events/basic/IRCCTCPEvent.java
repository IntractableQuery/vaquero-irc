/*
 * Represents a PRIVMSG CTCP (client-to-client protocol) message.
 */

package com.packethammer.vaquero.parser.events.basic;

import java.util.Date;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;

public abstract class IRCCTCPEvent extends IRCMessageEvent {
    /**
     * This is the CTCP character that begins and ends CTCP messages.
     */
    public static final char CTCP_CHAR = '\001';
    
    public IRCCTCPEvent() {
        this.setCTCP(true);
    }
    
    /**
     * Returns the CTCP message. Relies on the actual channel message and
     * simply removes the first and last characters, which are what distinquish
     * this message as a CTCP. By simply removing them and returning the result,
     * we save a little memory.
     *
     * @return A string representing the CTCP message.
     */
    public String getCTCPMessage() {
        String message = this.getMessage();
        // we can assume the message starts with the CTCP char, but does it end with it?
        if(message.charAt(message.length() - 1) == CTCP_CHAR) {
            return message.substring(1, message.length() - 1); // ends in the CTCP char
        } else {
            return message.substring(1); // lacks CTCP char on end -- malformed, but return it properly anyway
        }
    }
    
    /**
     * Returns the CTCP type (ACTION, DCC, etc.) without the text that follows it
     * (parameters for the CTCP command).
     *
     * @return The CTCP type.
     */
    public String getCTCPType() {
        String message = this.getCTCPMessage();
        
        int space = message.indexOf(" ");
        if(space > -1) {
            return message.substring(0, space);
        } else {
            return "";
        } 
    }
    
    /**
     * Returns the CTCP text, minus the prefix that indicates what kind of data
     * it is (DCC, ACTION, etc.).
     *
     * @return A string representing just the text of the CTCP message, and not the indicator of what the text is for. Returns null if no text exists.
     */
    public String getCTCPText() {
        String message = this.getCTCPMessage();
        
        int space = message.indexOf(" ");
        if(space > -1) {
            return message.substring(space + 1);
        } else {
            return null;
        }
    }
    
    /**
     * Determines if this is a version request.
     *
     * @return True if version request, false otherwise.
     */
    public boolean isVersionRequest() {
        return this.isTypeOfRequest("VERSION");
    }
    
    /**
     * Determines if this is a time request.
     *
     * @return True if time request, false otherwise.
     */
    public boolean isTimeRequest() {
        return this.isTypeOfRequest("TIME");
    }
    
    /**
     * Determines if this is a ping request.
     *
     * @return True if ping request, false otherwise.
     */
    public boolean isPingRequest() {
        return this.isTypeOfRequest("PING");
    }
    
    /**
     * This method simply checks to see if we are receiving a request
     * of some type you specify and returns true if so. An example would be
     * someone sending a CTCP "VERSION" request, which we can quickly
     * check using this method, although isVersionRequest() is availabe.
     * Will match regardless of case or trailing/preceeding whitespace.
     *
     * @return True if this CTCP is a request the same as specified, or false otherwise.
     */
    public boolean isTypeOfRequest(String request) {
        return this.getCTCPMessage().trim().equalsIgnoreCase(request);
    }
    
    public String toString() {
        return "[CTCP from " + this.getSource().getNickname() + "] " + this.getCTCPMessage();
    }  
}
