/**
 * SimpleBot just demonstrates the basic functionality of Vaquero, using the
 * BasicClient class. By taking a look at its source code, you can more or
 * less determine how you should properly handle IRC events and send commands.
 */

package com.packethammer.vaquero.examples;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import com.packethammer.vaquero.client.BasicClient;
import com.packethammer.vaquero.client.ClientInformation;
import com.packethammer.vaquero.dcc.DCCManager;
import com.packethammer.vaquero.net.IRCSocketConnector;
import com.packethammer.vaquero.outbound.commands.channel.IRCChannelMessageCommand;
import com.packethammer.vaquero.outbound.outboundprocessing.BasicThrottleTimingScheme;
import com.packethammer.vaquero.outbound.outboundprocessing.TimingScheme;
import com.packethammer.vaquero.parser.IRCEventListener;
import com.packethammer.vaquero.parser.SuccessfulLoginListener;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.channel.IRCChannelMessageEvent;
import com.packethammer.vaquero.parser.tracking.definitions.ModeDefinition;
import com.packethammer.vaquero.util.modes.Modes;
import com.packethammer.vaquero.util.modes.channel.ChanOperatorMode;
import com.packethammer.vaquero.util.modes.channel.ChannelMode;

public class SimpleBot {
    private BasicClient client;
    private String desiredChannel;
    
    /**
     * Initializes the bot with some basic information.
     */
    public SimpleBot(String remoteHost, int remotePort, String nickname, String joinChannel) throws Exception {       
        // This will be used later
        this.desiredChannel = joinChannel;

        // We're going to establish a connection to the server. We use
        // an IRC connector which is designed for use with sockets, so we
        // could easily modify this code to support SSL connections.
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(remoteHost, remotePort), 10000); // we'll wait 10 seconds for the connection to establish
        IRCSocketConnector connector = new IRCSocketConnector();
        connector.useSocket(socket);
        
        // Now, we initialize the basic client with the IRC connector and the
        // remote server's IP/hostname and port. Note that this is not used to 
        // establish an IRC connection -- it is just so that it can guess what the
        // server's name is later. The connector is what actually allows data
        // transmission.
        client = new BasicClient(connector, remoteHost, remotePort);
        
        // Let's configure the client to the maximum extent.
        
        // Enables the default version reply. The default for this is true.
        client.setAutoVersionResponseEnabled(true);
        
        // Enables a PING reply. Defaults to true.
        client.setAutoPongResponseEnabled(true);
        
        // Enables the mode optimization. The default is false. Read method
        // documentation for more info.
        client.setAutoModeOptimizationEnabled(true);
        
        // This step is unecessary, but simply points out that you can use one 
        // DCC manager across multiple clients.
        client.setDccManager(new DCCManager()); 
        
        // We're almost done. We're now going to call a seperate method to 
        // grab all the events we want to listen to before we actually send 
        // our connection details to the server.
        this.hookEvents();
        
        // Let's make sure we know when we're connected, so we can send the
        // channel we wish to join at that time.
        client.getIrcParser().addSuccessfulLoginListener(new SuccessfulLoginListener() {
            public void onSuccess() {
                client.irc().join(desiredChannel);
            }
        });
        
        // We will use a 'timing scheme' to slow down the rate that we send
        // commands at. This timing scheme will send one command every one second.
        // The typical use for this is to make sure we don't send so much data
        // to the server at once that it disconnects us for flooding.
        TimingScheme timing = new BasicThrottleTimingScheme(1000);
        
        // We will configure the information that we want to use to login
        // to the server with. This will use the nickname we desire, with
        // the ident (user login name) 'simple' and realname 'Simple bot!'
        ClientInformation myInfo = new ClientInformation(nickname, "simple", "Simple Bot!");

        
        // Okay, let's tell the client that it may begin logging in.
        client.initialize(myInfo, timing);
    }    
    
    /**
     * This 'hooks' all the events (listens for them) that we will want to use
     * for our bot. This method will be called before we try to login to the
     * server so that we make sure to get all information from the earliest
     * time possible. We can always add more listeners later.
     */
    private void hookEvents() {
        // Events in Vaquero are distributed in a manner similar to Java's
        // Swing GUI library. You attach listeners to certain events, and
        // those listeners get called when the event occurs. Quite simply, all
        // of the events you see in vaquero.parser.events.* are listable.
        // If you're new to Java, you'll want to make sure to read up on 
        // anonymous inner classes, which are one of the most useful event-listenting
        // constructs for Java!
        
        // First, let's add some simple channel commands to our bot that anyone can use.
        // We're going to use a 'hard' event listener, which will not listen
        // for subclasses of IRCChannelMessageEvent, just IRCChannelMessageEvent alone.
        // If you're confused, it works like this: The CTCP and DCC events, etc.
        // are subclasses of IRCChannelMessageEvent. We don't want those, just the
        // IRCChannelMessageEvent. As you might guess, a 'hard' event listener
        // is the type you'll use the most.
        client.getIrcParser().getEventDistributor().addHardEventListener(IRCChannelMessageEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                // We cast to the event type we expect
                IRCChannelMessageEvent message = (IRCChannelMessageEvent) e;
                
                // Here's our basic IRC channel commands.
                if(message.getMessage().equalsIgnoreCase("!time")) {
                    // We'll message the channel by creating a new IRC channel message command and sending it.
                    client.send(new IRCChannelMessageCommand(message.getChannel(), "Hey " + message.getSource().getNickname()
                        + "! The time is " + new Date().toString()));                  
                } else if(message.getMessage().equalsIgnoreCase("!info")) {
                    // Let's make some strings containing the user modes and channel modes the server supports, first
                    String usermodes = new String();
                    for(ModeDefinition modeDefinition : client.getServerContext().getKnownUserModes()) 
                        usermodes += modeDefinition.getMode();
                    
                    String channelmodes = new String();
                    for(ModeDefinition modeDefinition : client.getServerContext().getISupport().getChannelModes())
                        channelmodes += modeDefinition.getMode();
                    
                    // An easier way to reply is to just use the client's CommandHelper class -- it's up to you, though
                    client.irc().msgChan(message.getChannel(), "I'm on a server called " + client.getServerContext().getServerName()
                        + ", my hostmask currently looks to me like " + client.getServerContext().getMe()
                        + ", and this server supports usermodes '" + usermodes + "' and channel modes '" + channelmodes + "'");
                } else if(message.getMessage().equalsIgnoreCase("!help")) {
                    client.irc().msgChan(message.getChannel(), "My commands: !time !info");
                } 
            }
        });
        
        // Okay, so you know you can listen to specific events. What if you
        // want to listen to *all* messages (channel messages, private messages,
        // DCC messages, CTCP messages, etc.), or heck, just listen to every
        // event! To do so, add the event listener as a dynamic event listener.
        // Here, we're going to catch every possible event that comes from the
        // server and print it out.  
        // Note that IRCEvent is the top-level class that all events are derived
        // from, so adding it as a dynamic event to listen for means we want
        // both it and all classes derived from it.
        client.getIrcParser().getEventDistributor().addDynamicEventListener(IRCEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                // we'll format the output to look nice
                System.out.println("Event from source '" + e.getSource() + "' of type '" + e.getClass().getName() + "':");
                System.out.println(e.toString()); // Most, if not all of the events, have toString() methods that print out useful debugging information
            }
        });
    }
    
    public static void main(String[] args) throws Exception {
        if(args.length == 4) {
            new SimpleBot(args[0], Integer.parseInt(args[1]), args[2], args[3]);
        } else {
            System.out.println("Usage: " + SimpleBot.class.getSimpleName() + " <Server Address> <Port> <Nickname> <Channel to join>");
        }
    }
}
