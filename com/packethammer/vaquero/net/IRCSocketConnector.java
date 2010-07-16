/**
 * This IRC connector uses a Socket to connect to an IRC server. This allows
 * for IRC's typical TCP communication via ipv4, and hopefully, ipv6.
 *
 * Note that the ability to use your own socket is the key part of this class;
 * you can use sockets that transport data over a proxy, or sockets that use
 * SSL.
 */

package com.packethammer.vaquero.net;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class IRCSocketConnector extends IRCConnector {
    private Socket socket = null;
    private SocketLineListener lineListener;
    private PrintStream out;
    private Scanner in;
    
    public IRCSocketConnector() {
    }

    /**
     * Returns the socket being used for this connector.
     */
    public Socket getSocket() {
        return socket;
    }
    
    /**
     * Once the connected socket is set, call this method to begin receiving 
     * and sending data from the server. Note that vaquero's default IRC client
     * implementations handle this automatically.
     */
    public void begin() {
        // spawn connection events
        for(IRCConnectorListener listener : getListeners()) {
            listener.onConnected();
        }

        // start listening for data
        lineListener = new SocketLineListener(in);
        lineListener.start();
    }
    
    public void sendLine(String line) {
        try { this.out.println(line); } catch (Exception e) {};
    }
    
    /**
     * This takes a socket that is presumably already connected to an IRC
     * server and notifies all connector listeners of the connected status, then
     * begins reading/writing lines of data to the socket.
     *
     * @param socket The connected socket to use.
     * @throws IOException If there was a problem initializing the input/output streams of the socket for use.
     * @throws IllegalStateException If this method is called when we already have a live socket to use.
     */
    public void useSocket(Socket socket) throws IOException {
        if(this.socket != null) {
            throw new IllegalStateException("There is already a socket in use here, operation fails.");
        } else {
            this.socket = socket;
            
            // get output stream for later use
            this.out = new PrintStream(socket.getOutputStream());
            
            // get the input steam
            this.in = new Scanner(socket.getInputStream());
        }
    }
    
    /**
     * This forcefully terminates the connection with the remote server.
     */
    public void close() {
        try { this.socket.getOutputStream().flush() ; } catch (Exception e) {}; // hope this gets out our final QUIT real fast if we're going to follow up with a connection close...
        try { this.socket.close(); } catch (Exception e) {};
    }
    
    private class SocketLineListener extends Thread {
        private Scanner in;
        
        public SocketLineListener(Scanner in) {
            this.in = in;
        }
        
        public void run() {
            while(in.hasNextLine()) {
                getLineListener().onLine(in.nextLine());
            }
            
            // connection closed
            for(IRCConnectorListener listener : getListeners()) {
                listener.onConnectionClosed();
            }
        }
    }
}
