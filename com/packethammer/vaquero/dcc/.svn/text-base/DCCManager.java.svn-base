/**
 * The DCC manager is the central location for performing DCC-specific 
 * operations. It handles port assignment for sending DCCs and allows you
 * to centrally receive/send DCC communication.
 *
 * It can be used for one or more IRC clients.
 */

package com.packethammer.vaquero.dcc;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import com.packethammer.vaquero.outbound.CommandManager;
import com.packethammer.vaquero.outbound.commands.basic.IRCDCCChatCommand;
import com.packethammer.vaquero.outbound.commands.basic.IRCDCCSendCommand;
import com.packethammer.vaquero.parser.events.basic.IRCDccRequestEvent;

public class DCCManager {
    private List<DCCSession> dccSessions;
    private List<PortRange> portRanges;
    private InetAddress bindAddress;
    private InetAddress externalAddress;
    
    private Set<Integer> portsInUse;
    
    /** 
     * Initializes the DCC manager.
     */
    public DCCManager() {
        dccSessions = new Vector();
        portRanges = new ArrayList();
        portsInUse = new HashSet();
    }
    
    /**
     * Auto-allocates a server socket with one of the available ports.
     *
     * @throws ConnectionListenerBindingException If there are no ports left to use, or there is a general problem binding.
     */
    private ServerSocket prepareServerSocket() throws ConnectionListenerBindingException {      
        // find an open port
        int port = 0; // default to system auto-assignment
       
        if(this.portRanges.size() > 0) {            
            // use a port in our port ranges
            IOException exception = null;
            ServerSocket server = null;
           
            // we need to use one of the ports in the ranges
            for(PortRange range : this.portRanges) {
                for(int p = range.getStart(); p <= range.getEnd(); p++) {
                    if(!this.portsInUse.contains(new Integer(p))) {
                        try {
                            // try to bind a server socket to the port
                            server = new ServerSocket(p, -1, this.getBindAddress());
                        } catch (IOException ex) {
                            // the port didn't bind -- it's in use, or the bind address we tried to use is illegal... so... let it try more ports
                            exception = ex;
                        }
                        
                        if(server != null) {
                            // the server socket bound okay
                            return server;
                        }
                    }
                }
            }
            
            // if we got here, we never bound a server socket to our given ports
            if(exception != null) {
                // it is remotelu possible the exception was actually a problem with address binding
                throw new ConnectionListenerBindingException("No ports left, or bind address is illegal. Last exception when test-binding a server socket: '" + exception.getMessage() + "'");
            } else {
                // we simply had no ports left for usage
                throw new ConnectionListenerBindingException("No ports left to use.");
            }
        } else {
            try {
                // use an automatically assigned port
                return new ServerSocket(0, -1, this.getBindAddress());
            } catch (IOException ex) {
                // the system was unable to auto-assign us a port
                throw new ConnectionListenerBindingException(ex);
            }
        }
    }
    
    /**
     * Trys get a local address to use for remote hosts to connect to us.
     */
    private InetAddress getMyAddress() throws IllegalStateException {
        if(this.getExternalAddress() != null) {
            return this.getExternalAddress();
        } else if(this.getBindAddress() != null) {
            return this.getBindAddress();
        } else {
            throw new IllegalStateException("Neither the bind address or external address is set.");
        }
    }
    
    /**
     * Given user's nickname and an outbound command manager, this will 
     * allocate a port listener for the dcc command and then send the DCC CHAT command
     * to the server. Note that the timeout begins as soon as you call this 
     * method. This method does not know if the command manager doesn't send
     * the DCC CHAT request, or when it sends it, so set your timeout accordingly.
     *
     * This method blocks until the remote host connects, or until the timeout
     * is reached.
     *
     * @param nickname The nickname of the user to send this chat request to.
     * @param outbound The command manager to send the request through.
     * @param timeout The timeout in milliseconds to wait for the remote host to connect. Does not include the time that the DCC command may sit inside the command queue before it is sent!
     * @return The DCC file transfer session.
     * @throws ConnectionListenerBindingException If we have no ports left to use, or there is a general address binding exception.
     * @throws IllegalStateException If we don't have a bind or external address set (required to tell the client where to connect to us at).
     * @throws ConnectionListeningTimedOutException If the timeout was reached (the remote host never connected in that time period).
     * @throws IOException If there was a general network problem.
     */
    public DCCChatSession sendDccChatRequest(String nickname, CommandManager outbound, int timeout) throws ConnectionListenerBindingException,ConnectionListeningTimedOutException,IOException {
        // grab a port to listen on
        ServerSocket server = this.prepareServerSocket();
        this.portsInUse.add(server.getLocalPort());
        
        try {            
            DCCChatSession session = doOutboundChatSession(server, nickname, outbound, timeout);
            return session;
        } catch (ConnectionListeningTimedOutException ex) {
            disposeOfServerSocket(server);
            throw(ex);
        } catch (IOException ex) {
            disposeOfServerSocket(server);
            throw(ex);
        }
    }
    
    /**
     * Starts a chat session given a server socket to listen with. Used
     * to make the task of intercepting exceptions before the user of this
     * class gets them easier.
     */
    private DCCChatSession doOutboundChatSession(ServerSocket server, String nickname, CommandManager outbound, int timeout) throws ConnectionListeningTimedOutException,IOException {
        // send the DCC chat request
        outbound.sendCommand(new IRCDCCChatCommand(nickname, this.getMyAddress(), server.getLocalPort()));
        
        try {            
            // start listening for the reply
            server.setSoTimeout(timeout);
        } catch (SocketException ex) {
            throw new RuntimeException(ex); // this really should not occur!
        }
        
        // wait for connection or timeout...
        Socket client = null;
        try {
            client = server.accept();
        } catch (SocketTimeoutException ex) {
            throw new ConnectionListeningTimedOutException("Timeout of " + timeout + " milliseconds expired without connection from remote host");
        } 
        
        // we got a client
        DCCChatSession session = new DCCChatSession(client, true);
        
        // hook finish event for cleanup
        session.addListener(new DCCSessionListener() {
            public void onFinish() {
                sessionFinished(getSession());
            }
        });
        
        this.dccSessions.add(session);
        
        return session;
    }
    
    
    /**
     * Given a DCC chat request event, this will extract its information and 
     * accept it.
     *
     * This blocks until we connect to the remote host, or the connection fails.
     *
     * @param dcc The DCC CHAT request sent to us.
     * @param timeout The maximum amount of time in milliseconds to wait for the connection to establish.
     * @return The DCC chat session once it's been accepted.
     * @throws UnknownHostException If the remoteHost is unknown.
     * @throws IOException If there was a network problem connecting to the remote host.
     * @throws IllegalArgumentException If you provide a DCC request that is not a DCC CHAT request.
     */
    public DCCChatSession acceptDccChat(IRCDccRequestEvent dcc, int timeout) throws UnknownHostException,IOException {
        if(dcc.isDccChatRequest()) {
            return this.acceptDccChat(dcc.getDccAddress(), dcc.getDccPort(), timeout);
        } else {
            throw new IllegalArgumentException("dcc is of type '" + dcc.getDccType() + "' -- it is supposed to be chat request!");
        }
    }
    
    /**
     * Accepts a DCC chat session with a remote host given the remote host's
     * information. Returns the DCC chat session so that you can begin
     * using it.
     *
     * This blocks until we connect to the remote host, or the connection fails.
     *
     * @param remoteHost The remote host to connect to.
     * @param remotePort The port on the remote host to connect to.
     * @param timeout The maximum amount of time in milliseconds to wait for the connection to establish.
     * @return The DCC chat session once it's been accepted.
     * @throws UnknownHostException If the remoteHost is unknown.
     * @throws IOException If there was a network problem connecting to the remote host.
     */
    public DCCChatSession acceptDccChat(InetAddress remoteHost, int remotePort, int timeout) throws UnknownHostException,IOException {
        Socket s = new Socket();
        if(this.getBindAddress() != null)
            s.bind(new InetSocketAddress(this.getBindAddress(), 0));
        s.connect(new InetSocketAddress(remoteHost, remotePort), timeout);
        
        DCCChatSession session = new DCCChatSession(s, false);
        
        // hook finish event for cleanup
        session.addListener(new DCCSessionListener() {
            public void onFinish() {
                sessionFinished(getSession());
            }
        });
        
        this.dccSessions.add(session);
        
        return session;
    }
    
    /**
     * Given a DCC SEND request, this will accept it (connect to the remote host)
     * and return the DCC file transfer session.
     *
     * This blocks until we connect to the remote host, or the connection fails. 
     *
     * Remember to begin the transfer for the session once you retrieve it!
     *
     * @param dcc The DCC SEND request sent to us.
     * @param saveAs The local file on disk to save incoming data to.
     * @param maxKilobytesPerSecond The maximum kilobytes per second to accept from the remote host. Set to 0 if you wish this to be unlimited.
     * @param timeout The maximum amount of time in milliseconds to wait for the connection to establish.
     * @return The DCC file send session once it's been accepted.
     * @throws UnknownHostException If the remoteHost is unknown.
     * @throws IOException If there was a network problem connecting to the remote host.
     * @throws IllegalArgumentException If you provide a DCC request that is not a DCC SEND request.
     */
    public DCCFileReceiveSession acceptDccFileSend(IRCDccRequestEvent dcc, File saveAs, int maxKilobytesPerSecond, int timeout) throws UnknownHostException,IOException {
        if(dcc.isDccSendRequest()) {
            return this.acceptDccFileSend(dcc.getDccAddress(), dcc.getDccPort(), dcc.getDccFileSize(), saveAs, maxKilobytesPerSecond, timeout);
        } else {
            throw new IllegalArgumentException("dcc is of type '" + dcc.getDccType() + "' -- it is supposed to be file send request!");
        }        
    }
    
    /**
     * Accepts a DCC SEND session with a remote host given the remote host's
     * information. Returns the DCC SEND session so you can begin using it.
     *
     * This blocks until we connect to the remote host, or the connection fails.
     *
     * Remember to begin the transfer for the session once you retrieve it!
     *
     * @param remoteHost The remote host to connect to.
     * @param remotePort The port on the remote host to connect to.
     * @param fileSize The expected file size (set to -1 if file size is unknown).
     * @param saveAs The local file on disk to save incoming data to.
     * @param maxKilobytesPerSecond The maximum kilobytes per second to accept from the remote host. Set to 0 if you wish this to be unlimited.
     * @param timeout The maximum amount of time in milliseconds to wait for the connection to establish.
     * @return The DCC chat session once it's been accepted.
     * @throws UnknownHostException If the remoteHost is unknown.
     * @throws IOException If there was a network problem connecting to the remote host.
     */
    public DCCFileReceiveSession acceptDccFileSend(InetAddress remoteHost, int remotePort, long fileSize, File saveAs, int maxKilobytesPerSecond, int timeout) throws UnknownHostException,IOException {
        Socket s = new Socket();
        if(this.getBindAddress() != null)
            s.bind(new InetSocketAddress(this.getBindAddress(), 0));
        s.connect(new InetSocketAddress(remoteHost, remotePort), timeout);
        
        DCCFileReceiveSession session = new DCCFileReceiveSession(s, saveAs, maxKilobytesPerSecond, fileSize);
        
        // hook finish event for cleanup
        session.addListener(new DCCFileTransferListener() {
            public void onFinish() {
                sessionFinished(getSession());
            }
        });
        
        this.dccSessions.add(session);
        
        return session;
    }
    
    /**
     * Given user's nickname and an outbound command manager, this will 
     * allocate a port listener for the dcc command and then send the DCC SEND command
     * to the server. Note that the timeout begins as soon as you call this 
     * method. This method does not know if the command manager doesn't send
     * the DCC CHAT request, or when it sends it, so set your timeout accordingly.
     *
     * This method blocks until the remote host connects, or until the timeout
     * is reached.
     *
     * @param nickname The nickname of the user to send this file to.
     * @param outbound The command manager to send the request through.
     * @param toSend The file to send to the remote host.
     * @param maxKilobytesPerSecond The maximum kilobytes per second to send to the remote host. Set to 0 if you wish this to be unlimited.
     * @param timeout The timeout in milliseconds to wait for the remote host to connect. Does not include the time that the DCC command may sit inside the command queue before it is sent!
     * @return The DCC file transfer session.
     * @throws ConnectionListenerBindingException If we have no ports left to use, or there is a general address binding exception.
     * @throws IllegalStateException If we don't have a bind or external address set (required to tell the client where to connect to us at).
     * @throws ConnectionListeningTimedOutException If the timeout was reached (the remote host never connected in that time period).
     * @throws IOException If there was a general network problem.
     */
    public DCCFileSendSession sendDccFile(String nickname, CommandManager outbound, File toSend, int maxKilobytesPerSecond, int timeout) throws ConnectionListenerBindingException,ConnectionListeningTimedOutException,IOException {
        // grab a port to listen on
        ServerSocket server = this.prepareServerSocket();
        this.portsInUse.add(server.getLocalPort());
        
        try {            
            DCCFileSendSession session = doOutboundFileSendSession(server, nickname, outbound, toSend, maxKilobytesPerSecond, timeout);
            return session;
        } catch (ConnectionListeningTimedOutException ex) {
            disposeOfServerSocket(server);
            throw(ex);
        } catch (IOException ex) {
            disposeOfServerSocket(server);
            throw(ex);
        }
    }
    
    /**
     * Starts a DCC SEND session given a server socket to listen with. Used
     * to make the task of intercepting exceptions before the user of this
     * class gets them easier.
     */
    private DCCFileSendSession doOutboundFileSendSession(ServerSocket server, String nickname, CommandManager outbound, File toSend, int maxKilobytesPerSecond, int timeout) throws ConnectionListenerBindingException,ConnectionListeningTimedOutException,IOException {
        // send the DCC send request
        outbound.sendCommand(new IRCDCCSendCommand(nickname, toSend.getName(), toSend.length(), this.getMyAddress(), server.getLocalPort()));
        
        try {            
            // start listening for the reply
            server.setSoTimeout(timeout);
        } catch (SocketException ex) {
            throw new RuntimeException(ex); // this really should not occur!
        }
        
        // wait for connection or timeout...
        Socket client = null;
        try {
            client = server.accept();
        } catch (SocketTimeoutException ex) {
            throw new ConnectionListeningTimedOutException("Timeout of " + timeout + " milliseconds expired without connection from remote host");
        } 
        
        // we got a client
        DCCFileSendSession session = new DCCFileSendSession(client, toSend, maxKilobytesPerSecond);
        
        // hook finish event for cleanup
        session.addListener(new DCCFileTransferListener() {
            public void onFinish() {
                sessionFinished(getSession());
            }
        });
        
        this.dccSessions.add(session);
        
        return session;
    }
    
    /**
     * Called by the session listeners for various DCC sessions running 
     * when they end.
     */
    private void sessionFinished(DCCSession session) {
        session.setFinished(true); // just to make sure
        if(session.isInitiatedByUs()) {
            // we were listening on a port, so make sure to free that port up
            portsInUse.remove(new Integer(session.getSocket().getLocalPort()));
        }
        this.dccSessions.remove(session);
    }

    /** 
     * This is called to get rid of a server socket that was used, but never
     * received a connection.
     */
    private void disposeOfServerSocket(ServerSocket sock) {
        this.portsInUse.remove(sock.getLocalPort());
        try {
            sock.close();
        } catch (IOException ex) {
        }
    }
    
    /**
     * Returns all active DCC sessions (that is, ones that have not finished
     * yet).
     *
     * @return A list of all DCC sessions that have not finished yet.
     */
    public List<DCCSession> getDccSessions() {
        return dccSessions;
    }

    /**
     * Returns a copy of the internal port ranges (that may overlap) usable for 
     * DCC sessions.
     *
     * @return A list of port ranges.
     */
    public List<PortRange> getPortRanges() {
        return new ArrayList(portRanges);
    }
    
    /**
     * Returns a set of all ports currently in use by the DCC manager.
     *
     * @return Ports.
     */
    public Set<Integer> getPortsInUse() {
        return this.portsInUse;
    }
    
    /**
     * Adds a port range to the internal port ranges usable for DCC sessions.
     *
     * If a port range is never added to the DCC manager, it will simply 
     * allow the operating system to choose open ports.
     */
    public void addPortRange(PortRange range) {
        this.portRanges.add(range);
    }

    /**
     * Returns the local address used for binding ports.
     *
     * @return Local address, or null if not in use.
     */
    public InetAddress getBindAddress() {
        return bindAddress;
    }

    /**
     * Sets the local address to bind to. This is most useful
     * on systems with multiple external IP addresses. Set to null if you wish
     * to accept default behavior (typically, DCC responses will be listened
     * for on all addresses on the system). This address is used for both
     * listening ports and outbound connection ports.
     *
     * @param bindAddress The address to bind to, or null if you wish not to use it.
     */
    public void setBindAddress(InetAddress bindAddress) {
        this.bindAddress = bindAddress;
    }

    /**
     * Returns our externally viewable address (typically, our address as we
     * can be contacted from the Internet).
     *
     * @return Our external address.
     */
    public InetAddress getExternalAddress() {
        return externalAddress;
    }

    /**
     * Sets our external address (typically, as seen from the Internet). If you 
     * set this to null, you must have the bind address set (the bind address
     * is the default address that will be used in that case). If no address
     * is set here or for the bind address, you can expect problems later when
     * you try to initiate DCC communcation.
     *
     * @param externalAddress Our external address (the one others can connect to us at).
     */
    public void setExternalAddress(InetAddress externalAddress) {
        this.externalAddress = externalAddress;
    }
}
