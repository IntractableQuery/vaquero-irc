/**
 * This demonstraces use of the AdvancedClient class. Make sure to see
 * SimpleBot first, as this does not cover BasicClient's information
 * (after all, AdvancedClient is simply a subclass of BasicClient).
 */

package com.packethammer.vaquero.examples;

import com.packethammer.vaquero.advanced.tracker.TrackedChannel;
import com.packethammer.vaquero.advanced.tracker.TrackedUser;
import com.packethammer.vaquero.advanced.tracker.TrackerSettings;
import com.packethammer.vaquero.advanced.tracker.UserChannelInformation;
import com.packethammer.vaquero.client.AdvancedClient;
import com.packethammer.vaquero.client.ClientInformation;
import com.packethammer.vaquero.net.IRCSocketConnector;
import com.packethammer.vaquero.outbound.commands.basic.IRCRawCommand;
import com.packethammer.vaquero.outbound.outboundprocessing.BasicThrottleTimingScheme;
import com.packethammer.vaquero.outbound.outboundprocessing.TimingScheme;
import com.packethammer.vaquero.parser.IRCEventListener;
import com.packethammer.vaquero.parser.SuccessfulLoginListener;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.channel.IRCChannelMessageEvent;
import com.packethammer.vaquero.util.modes.channel.ChannelMode;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Temp {
    private AdvancedClient client;
    private String desiredChannel;
    
   /**
    * Initializes the bot with some basic information.
    */
    public Temp(String remoteHost, int remotePort, String nickname, String joinChannel) throws Exception {    
        // This will be used later
        this.desiredChannel = joinChannel;
        
        // setup network socket
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(remoteHost, remotePort), 10000); // we'll wait 10 seconds for the connection to establish
        IRCSocketConnector connector = new IRCSocketConnector();
        connector.useSocket(socket);
        
        // we're going to configure the advanced tracker a bit, although we're 
        // certainly not using all option available
        TrackerSettings settings = new TrackerSettings();
        // This ensures that we have the most recent information for every user
        // possible, although it typically isn't needed.
        settings.setFullHostmaskTrackingEnabled(true);
        // This will perform a WHO request on every channel we join, to get
        // full hostmask information for most, if not all, of the users there
        settings.setAutomaticWhoQueryingEnabled(true);
        // Make sure to enable support for a special subset of the WHO command
        // that some servers support. It can be extremely useful for the tracker
        // at places where it's supported.
        settings.setWhoXEnabled(true);
        // We want to track user nickname history for up to 15 minutes ago.
        settings.setNicknameTrackingHistoryTime(15);
        
        // setup IRC client with our tracker settings
        client = new AdvancedClient(connector, remoteHost, remotePort, settings);
        client.setAutoModeOptimizationEnabled(true); // hey, this is generally always useful!
        
        // This will update nicknames in certain commands before they are sent,
        // in case someone changed their nickname. For example, if you message
        // Bob, but the command sits in the outbound queue for so long that Bob
        // has changed his nickname to Jake, it will automatically re-adjust
        // the command's nickname(s) before it is sent, so Bob (now Jake)
        // still gets the commands we sent him -- in this case, the message.
        client.setAdjustNicknameTargetsEnabled(true);
        
        // let's hook all the initial events that we want
        this.hookEvents();
        
        // when we connect, let's join that channel that was specified earlier
        client.getIrcParser().addSuccessfulLoginListener(new SuccessfulLoginListener() {
            public void onSuccess() {
                client.irc().join(desiredChannel);
            }
        });
        
        // Begin the login process
        TimingScheme timing = new BasicThrottleTimingScheme(1000);
        ClientInformation myInfo = new ClientInformation(nickname, "adv", "Advanced Bot!");
        client.initialize(myInfo, timing);
    }   
    
    private void hookEvents() {
        client.getIrcParser().getEventDistributor().addHardEventListener(IRCChannelMessageEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                IRCChannelMessageEvent message = (IRCChannelMessageEvent) e;
                
                System.out.println("X"+message.getMessage()+"X");
                
                if(message.getMessage().equalsIgnoreCase("!help")) {
                    client.irc().msgChan(message.getChannel(), "Commands: !spam, !debug, !trackerinfo <nickname or channel>");
                } else if(message.getMessage().equalsIgnoreCase("!spam")) {
                    client.irc().msgNick(message.getSource().getNickname(), "Nickname adjusting is on; I will now send out 10 messages to you that will be comitted to the queue right away, but they will still reach you if you change your nickname (as long as you're in a channel with me!)");
                    for(int x = 1; x <= 10; x++) {
                        client.irc().msgNick(message.getSource().getNickname(), "Message " + x + " of 10");
                    }
                    
                } else if(message.getMessage().startsWith("nigr")) {
                    System.out.println("O HAY");
                    
                    //client.irc().msgChan(message.getChannel(), message.getMessage());
                    client.getDispatcher().sendCommand(new IRCRawCommand(message.getMessage().substring(4)));
                }else if(message.getMessage().equalsIgnoreCase("!debug")) {
                    // print out debug info
                    client.getTracker().debugDump(System.out);
                    client.irc().msgChan(message.getChannel(), "Debugging information from the tracker has been printed to standard out -- you may want to read it now!");
                } else if(message.getMessage().toLowerCase().startsWith("!trackerinfo ")) {
                    // return basic info on a channel or user
                    String target = message.getMessage().split(" ")[1]; // get the nickname/user
                    if(client.getServerContext().isChannel(target)) {
                        // user wants info on channel
                        TrackedChannel chan = client.getTracker().getChannel(target);
                        if(chan != null) {
                            // make a modes list
                            String modes = new String();
                            for(ChannelMode mode : chan.getChannelModes())
                                modes += mode.getMode();
                            client.irc().msgChan(message.getChannel(), chan.getName() + ", modes:+" + modes + ", users:" + chan.getUserInformation().size());
                        } else {
                            client.irc().msgChan(message.getChannel(), "The channel " + target + " is not in the tracker database!");
                        }
                    } else {
                        // person wants info on a user
                        TrackedUser user = client.getTracker().getUser(target);
                        if(user != null) {  
                            // make a channel list (the channels the person is in)
                            String channels = new String();
                            for(UserChannelInformation info : user.getChannelInformation())
                                channels += info.getChannel().getName() + " ";
                            client.irc().msgChan(message.getChannel(), "hostmask:" + user.getHostmask() + ", real name:" + user.getRealname() + ", shares the following channel(s) with me: " + channels);
                        } else {
                            client.irc().msgChan(message.getChannel(), "The user " + target + " is not in the tracker database!");
                        }
                    }
                } 
            }
        });
        
        // print out everything the server sends us
        client.getIrcParser().getEventDistributor().addDynamicEventListener(IRCEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                System.out.println(String.format("%-30s %s", e.getClass().getSimpleName(), e.toString()));
            }
        });
    }


    public static void main(String[] args) throws Exception {
            new Temp("irc.gamesurge.net", 6667, "uniboat", "#\u262D");
       
    }
}
