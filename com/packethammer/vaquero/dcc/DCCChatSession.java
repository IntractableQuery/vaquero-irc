/**
 * This represents a DCC chat session that may have been initiated by us
 * or the remote host.
 */

package com.packethammer.vaquero.dcc;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public class DCCChatSession extends DCCSession {    
    private PrintStream out;
    private Scanner in;
    private Vector<DCCSessionListener> chatListeners;
    
    /**
     * Initializes this DCC chat session with the socket to use for communications. 
     *
     * @param socket The socket (presumably connected to the remote host) to use for communication.
     * @param initiatedByUs Set to true if we sent the request, false otherwise.
     * @throws IOException If there was a problem preparing the network streams.
     */
    public DCCChatSession(Socket socket, boolean initiatedByUs) throws IOException {
        super(socket, initiatedByUs ? TYPE_CHATSEND : TYPE_CHATRECEIVE);
        this.chatListeners = new Vector();
        this.out = new PrintStream(socket.getOutputStream());
        this.in = new Scanner(socket.getInputStream());
    }
    
    /**
     * Sends a line of text to the remote host. Does not guarantee it will be
     * received by the remote host, although the order you send lines in
     * is obviously maintained due to the transport protocol being TCP.
     *
     * @param line The text to send.
     */
    public void writeLine(String line) {
        try {
            out.println(line);
        } catch (Exception e) {
            // done
            cleanup();
        }
    }
    
    /**
     * Reads a line of text from the remote host, blocking until there is a 
     * line to read. Returns null if the remote host closed the connection.
     *
     * @return A line of text, or null if no more text is there to be read.
     */
    public String readLine() {
        if(in.hasNextLine()) {
            return in.nextLine();
        } else {
            // remote host closed connection!
            cleanup();
            return null;
        }
    }
    
    /**
     * This closes the connection with the remote host.
     */
    public void close() {
        try { out.flush();     } catch (Exception e) {}; // just to be sure
        
        cleanup();
    }
    
    private void cleanup() {
        if(!this.isFinished()) { // don't do this more than once!
            try { in.close();     } catch (Exception e) {};
            try { out.close();    } catch (Exception e) {};

            this.setFinished(true);
            
            for(DCCSessionListener listener : this.getChatListeners()) {
                listener.onFinish();
            }
        }
    }
    
    /**
     * Returns the current chat listeners for this chat session.
     */
    public Vector<DCCSessionListener> getChatListeners() {
        return chatListeners;
    }
    
    /**
     * Adds a chat listener.
     *
     * @param listener The chat listener to add.
     */
    public void addListener(DCCSessionListener listener) {
        listener.setSession(this);
        this.chatListeners.add(listener);
    }
    
    /**
     * Removes a chat listener. Removes multiple copies if they exist.
     *
     * @param listener The chat listener to remove.
     */
    public void removeListener(DCCSessionListener listener) {
        while(this.chatListeners.remove(listener)) {
            // just keep removing the listener until we no longer have any more of that type left
        }
    }
}
