/**
 * This is an implementation of an RFC1413 ident server.  It is worth note 
 * that a true  system-level ident server is basically impossible to design in 
 * java without using non-portable solutions. For out purposes, it is just fine 
 * for IRC, especially if we don't need strict system-user-to-TCP-connection 
 * correlation.
 *
 * To use this, just extend it and override the query method.
 */

package com.packethammer.vaquero.identd;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public abstract class AbstractIdentDaemon {  
    /** This is the standard ident port, 113. Note that it is a protected port on some UNIX-based systems. */
    public static final int IDENT_PORT = 113;
    
    private int port;
    private ServerSocket server;
    private Thread listenThread;
    private Vector<ClientHandler> clients;
    
    /** 
     * Initializes this ident daemon on the default ident port.
     *
     * @throws IOException If there was a problem binding the server socket.
     */
    public AbstractIdentDaemon() throws IOException {
        this(IDENT_PORT);
    } 
    
    /** 
     * Initializes this ident daemon on the default ident port using a specific
     * bind address.
     *
     * @param bindAddress The local address to bind to. Set to null to not try to bind to a specific address.
     * @throws IOException If there was a problem binding the server socket.
     */
    public AbstractIdentDaemon(InetAddress bindAddress) throws IOException {
        this(bindAddress, IDENT_PORT);
    } 
    
    /** 
     * Initializes this ident daemon with a port number.
     *
     * @param port The local port to bind to.
     * @throws IOException If there was a problem binding the server socket.
     */
    public AbstractIdentDaemon(int port) throws IOException {
        this(null, port);
    } 
    
    /** 
     * Initializes this ident daemon with a port number and bind address
     *
     * @param bindAddress The local address to bind to. Set to null to not try to bind to a specific address.
     * @param port The local port to bind to.
     * @throws IOException If there was a problem binding the server socket.
     */
    public AbstractIdentDaemon(InetAddress bindAddress, int port) throws IOException {
        clients = new Vector();
        server = new ServerSocket();
        if(bindAddress != null)
            server.bind(new InetSocketAddress(bindAddress, port));
        else
            server.bind(new InetSocketAddress(port));
        
        listenThread = new Thread() {
            public void run() {
                while(true) {
                    try {                        
                        Socket client = server.accept();
                        acceptClient(client);
                    } catch (IOException ex) {
                        // just ignore it
                    }
                }
            }
        };
        
        listenThread.start();
    }  
    
    private synchronized void acceptClient(Socket client) {
        try {
            clients.add(new ClientHandler(client));
        } catch (Exception e) {
            // problem.
            try { client.close(); } catch (Exception ex) {};
        }
    }
    
    /** 
     * Responds to a query based on our local port and the port on the remote
     * querying system.
     *
     * Make sure to reply with the same ourPort/theirPort pair given.
     *
     * @param client The socket this request came from.
     * @param ourPort The port on our system.
     * @param theirPort The port on the remote system.
     */
    public abstract AbstractIdentReply replyToQuery(Socket client, int ourPort, int theirPort);
    
    private class ClientHandler extends Thread {
        private Scanner in;
        private PrintStream out;
        private Socket client;
        
        public ClientHandler(Socket client) throws IOException {
            this.client = client;
            this.in = new Scanner(client.getInputStream());
            this.out = new PrintStream(client.getOutputStream());
        }
        
        public void run() {
            while(in.hasNextLine()) {
                String line = in.nextLine();
                String[] parts = line.split(",");
                boolean success = false;
                if(parts.length == 2) {
                    try {
                        int ourPort = Integer.parseInt(parts[0].trim());
                        int theirPort = Integer.parseInt(parts[1].trim());
                        
                        out.print(replyToQuery(client, ourPort, theirPort).getReply());
                        out.flush();
                        success = true;
                    } catch (Exception e) {}; // code below handles this
                    
                    if(success == false) {
                        // if we got this far, the client sent bad ports -- tell them using a manual error since they may not be ints
                        out.print(line + " : ERROR : " + ErrorReply.ERR_INVALIDPORT);
                        out.flush();
                    }
                } else {
                    // close the connection, client is sending malformed request that we can't even reply to.
                    try { client.close(); } catch (Exception e) {};
                    return;
                }
            }
            
            clients.remove(this); // we're done
        }
    }
}
