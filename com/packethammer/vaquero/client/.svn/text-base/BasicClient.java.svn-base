/**
 * This is a basic client that's designed to keep resource usage low, while
 * still offering important features for use. For most simple IRC bots and 
 * even IRC clients, this class provides the simplicity and speed you'll need.
 *
 * A client session is a one-use-only class. If you need to reconnect to the
 * server, disconnect this client and create a new one.
 */

package com.packethammer.vaquero.client;

import com.packethammer.vaquero.advanced.dispatcher.Dispatcher;
import com.packethammer.vaquero.dcc.DCCManager;
import com.packethammer.vaquero.net.IRCConnector;
import com.packethammer.vaquero.net.IRCLineListener;
import com.packethammer.vaquero.outbound.CommandManager;
import com.packethammer.vaquero.outbound.ModeOptimizer;
import com.packethammer.vaquero.outbound.OutboundRawIRCLineSenderI;
import com.packethammer.vaquero.outbound.commands.IRCCommand;
import com.packethammer.vaquero.outbound.commands.basic.IRCCTCPReplyNickCommand;
import com.packethammer.vaquero.outbound.commands.server.IRCNickCommand;
import com.packethammer.vaquero.outbound.commands.server.IRCPasswordCommand;
import com.packethammer.vaquero.outbound.commands.server.IRCPongCommand;
import com.packethammer.vaquero.outbound.commands.server.IRCUserCommand;
import com.packethammer.vaquero.outbound.outboundprocessing.CommandOptimizer;
import com.packethammer.vaquero.outbound.outboundprocessing.TimingScheme;
import com.packethammer.vaquero.parser.IRCEventListener;
import com.packethammer.vaquero.parser.IRCParser;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.basic.IRCCTCPEvent;
import com.packethammer.vaquero.parser.events.server.IRCPingEvent;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;
import com.packethammer.vaquero.util.CommandHelper;

public class BasicClient {
    private DCCManager dccManager;
    private IRCConnector ircConnector;
    private IRCParser ircParser;
    private CommandManager outboundCommandManager;
    private CommandHelper irc;
    private boolean initialized;
    
    private IRCLineListener lineListener;
    private OutboundRawIRCLineSenderI lineSender;
    
    private boolean autoPongResponseEnabled;
    private boolean autoVersionResponseEnabled;
    private boolean autoModeOptimizationEnabled;
    
    /**
     * This initializes the client with some basic information and initializes
     * the parser so you can begin hooking IRC events before we begin conversing
     * with the remote IRC server.
     *
     * @param connector The IRC connector to use. Please initialize it before passing it here.
     * @param perceivedServerHost The remote IP or hostname of the server we're connected to. This is used by the parser to make educated guesses for events it generates later on. It is not used for an actual TCP connection.
     * @param perceivedServerPort The remote port of the server we're connected to. This is used by the parser. It is not part of a TCP connection.
     */
    public BasicClient(IRCConnector connector, String perceivedServerHost, int perceivedServerPort) {    
        // set defaults
        this.setAutoPongResponseEnabled(true);
        this.setAutoVersionResponseEnabled(true);
        
        // set up the parser
        this.ircParser = new IRCParser(perceivedServerHost, perceivedServerPort);
        
        // deal with connector
        this.ircConnector = connector;
        
        // set up a listener to send inbound data to the parser
        lineListener = new IRCLineListener() {
            public void onLine(String line) {
                ircParser.parseLine(line);
            }
        };
        ircConnector.setLineListener(lineListener);
        
        // set up the line sender
        lineSender = new OutboundRawIRCLineSenderI() {
            public void sendRawLine(String line) {
                ircConnector.sendLine(line);
            }
        };
    }
    
    /**
     * Once you've set the client up how you want it, this is called to start
     * the IRC session. Please ensure that the IRC connector is actually
     * prepared before doing this, or bad things may result.
     *
     * If you didn't initialize the DCC manager, a default (new) DCC manager
     * will be created.
     *
     * You should hook parser events before calling this method to ensure you
     * receive some of the very first initial events.
     *
     * @param clientInfo The information to send to the server during the login process.
     * @param outboundTimingScheme The timing scheme to use for sending commands to the remote server.
     */
    public void initialize(ClientInformation clientInfo, TimingScheme outboundTimingScheme) {        
        // provide a default DCC manager if required
        if(this.getDccManager() == null)
            this.setDccManager(new DCCManager()); 
        
        // set up the outbound command manager
        this.outboundCommandManager = this.getNewFunctionalCommandManager(outboundTimingScheme, lineSender);
        
        // set up the command helper
        this.irc = new CommandHelper(this.outboundCommandManager);
        
        // add any optimizers we are automatically going to use
        if(this.isAutoModeOptimizationEnabled())
            this.outboundCommandManager.getWaitQueue().addCommandOptimizer(new ModeOptimizer(this.ircParser.getServerContext()));
        
        // hook the events we want
        hookEvents();
        
        // allow access to all methods that require initialization
        initialized = true;
        
        // let subclass do whatever it needs
        preinitializeBeforeLogin();
        
        // tell the IRC connector that we're ready
        ircConnector.begin();
        
        // send our client information
        if(clientInfo.getPassword() != null)
            this.send(new IRCPasswordCommand(clientInfo.getPassword()));
        this.send(new IRCNickCommand(clientInfo.getDesiredNickname()));
        this.send(new IRCUserCommand(clientInfo.getRealname(), clientInfo.getIdent()));
    }
    
    protected void preinitializeBeforeLogin() {
        
    }
    
    /**
     * Returns a new command manager to use.
     */
    protected CommandManager getNewFunctionalCommandManager(TimingScheme outboundTimingScheme, OutboundRawIRCLineSenderI lineSender) {
        return new CommandManager(outboundTimingScheme, lineSender);
    }
    
    private void hookEvents() {
        // pong reply
        if(this.isAutoPongResponseEnabled()) {
            this.getIrcParser().getEventDistributor().addHardEventListener(IRCPingEvent.class, new IRCEventListener() {
               public void onEvent(IRCEvent e) {
                   IRCPingEvent ping = (IRCPingEvent) e;
                   send(new IRCPongCommand(ping.getMessage()));
               } 
            });
        }
        
        // version response
        if(this.isAutoVersionResponseEnabled()) {
            this.getIrcParser().getEventDistributor().addDynamicEventListener(IRCCTCPEvent.class, new IRCEventListener() {
               public void onEvent(IRCEvent e) {
                   IRCCTCPEvent ctcp = (IRCCTCPEvent) e;
                   if(ctcp.isVersionRequest()) {
                       send(new IRCCTCPReplyNickCommand(ctcp.getSource().getNickname(), "VERSION " + IRCParser.VAQUERO_VERSIONTEXT));
                   }
               } 
            });
        }
    }

    /**
     * Returns the DCC manager currently in use.
     *
     * @return The DCC mananger.
     */
    public DCCManager getDccManager() {
        return dccManager;
    }

    /**
     * Sets the DCC manager to use for this IRC client. Note that you can
     * use one DCC manager across multiple clients if you wish. If you
     * don't set this before you initialize the client, the client will 
     * create a new default DCC manager during initialization.
     *
     * @param dccManager The DCC manager to use.
     */
    public void setDccManager(DCCManager dccManager) {
        this.dccManager = dccManager;
    }

    /**
     * Returns the IRC connector being used for this IRC session.
     */
    public IRCConnector getIrcConnector() {
        return ircConnector;
    }

    /**
     * Returns the IRC parser, which contains stateful information on what the
     * IRC server supports, what our status is, etc.
     *
     * As soon as this client is instantiated, you may begin accessing the
     * parser to hook events you want.
     *
     * @return The IRC parser in use.
     */
    public IRCParser getIrcParser() throws IllegalStateException {
        return ircParser;
    }

    /**
     * Returns the outbound command manager. That is, the thing you should be
     * using to send IRC commands to the server.
     *
     * @return The outbound command manager.
     * @throws IllegalStateException If the client has not been intialized yet.
     */
    public CommandManager getOutboundCommandManager() throws IllegalStateException {
        if(!this.isInitialized())
            throw new IllegalStateException("You cannot access the command manager until the client has been intialized!");
        
        return outboundCommandManager;
    }

    /**
     * This is a helpful utility method that returns a sort of easy-to-use
     * swiss-army knife instance of the CommandHelper class, which makes performing
     * common IRC commands less of a verbose operation!
     *
     * @return A command helper that uses the outbound command manager.
     * @throws IllegalStateException If the client has not been intialized yet.
     */
    public CommandHelper irc() throws IllegalStateException {
        if(!this.isInitialized())
            throw new IllegalStateException("You cannot access the command manager until the client has been intialized!");
        
        return irc;
    }
    
    /**
     * This utility method calls sendCommand() on the outbound command manager
     * to send an IRC command.
     *
     * @param command The IRC command to send/
     */
    public void send(IRCCommand command) {
        this.getOutboundCommandManager().sendCommand(command);
    }
    
    /**
     * This utility method returns the IRCParser's IRCServerContext for
     * use.
     *
     * @return The IRC server context maintained by the parser.
     */
    public IRCServerContext getServerContext() {
        return this.getIrcParser().getServerContext();
    }
    
    /**
     * It's an extremely good idea to call this method when you're done with
     * this IRC client, as it will work to free as many resources as possible
     * right away.
     *
     * It's also an easy way to terminate your connection with the remote 
     * server without sending a QUIT message, although it's perfectly fine
     * to call it after sending a QUIT.
     */
    public void die() {
        this.ircParser.die();
        this.ircParser = null;
        
        if(this.ircConnector != null)
            this.ircConnector.close();
        this.ircConnector = null;
        
        this.dccManager = null;
                
        this.lineListener = null;
        this.lineSender = null;
        
        this.outboundCommandManager.die();
        this.outboundCommandManager = null;
        
        this.irc = null;
    }

    /**
     * Determines if we respond to the server's PING requests or not.
     */    
    public boolean isAutoPongResponseEnabled() {
        return autoPongResponseEnabled;
    }

    /**
     * Set this to true to automatically reply to the server's PING requests,
     * otherwise, the server may disconnect us. The default is true.
     *
     * @param autoPongResponseEnabled True to enable, false otherwise.
     */
    public void setAutoPongResponseEnabled(boolean autoPongResponseEnabled) {
        this.autoPongResponseEnabled = autoPongResponseEnabled;
    }

    /**
     * Determines if we can reply with the default Vaquero version reply.
     * If you want to use your own reply alone (please keep Vaquero in it if you'd
     * be so kind!), disable this.
     *
     * Note that disabling this may be a good security practice anyway -- it
     * doesn't try to check for malicious flooders. However, you may be using
     * the outbound command manager in a configuration that protects you.
     */
    public boolean isAutoVersionResponseEnabled() {
        return autoVersionResponseEnabled;
    }

    /**
     * Determines if the automatic version response is enabled.
     */
    public void setAutoVersionResponseEnabled(boolean autoVersionResponseEnable) {
        this.autoVersionResponseEnabled = autoVersionResponseEnable;
    }
    
    /**
     * This convenience method adds an optimizer to the command manager.
     *
     * @param optimizer The optimizer to add.
     */
    public void addCommandOptimizer(CommandOptimizer optimizer) {
        this.getOutboundCommandManager().getWaitQueue().addCommandOptimizer(optimizer);
    }

    /**
     * Determines if the initialized() method has been called yet. 
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Determines if we enabled mode optimization.
     */
    public boolean isAutoModeOptimizationEnabled() {
        return autoModeOptimizationEnabled;
    }

    /**
     * Determines if we should automatically attach a ModeOptimizer to the
     * command manager. See the documentation for ModeOptimizer to find out
     * more about what it does.
     *
     * This method can only be called before intialization.
     *
     * @param autoModeOptimizationEnabled Set to true to use it, false otherwise.
     * @throws IllegalStateException If the client has already been initialized.
     * @see com.packethammer.vaquero.outbound.ModeOptimizer
     */
    public void setAutoModeOptimizationEnabled(boolean autoModeOptimizationEnabled) {
        if(this.isInitialized())
            throw new IllegalStateException("This can only be set before initialization!");
        this.autoModeOptimizationEnabled = autoModeOptimizationEnabled;
    }
}
