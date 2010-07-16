/**
 * A terrible class that will be eventually be removed -- I'm just a bit lazy,
 * so I need it.
 */

package com.packethammer.vaquero.testing;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import com.packethammer.vaquero.advanced.dispatcher.Dispatcher;
import com.packethammer.vaquero.advanced.dispatcher.querying.WhoXQueryListener;
import com.packethammer.vaquero.advanced.tracker.Tracker;
import com.packethammer.vaquero.advanced.tracker.TrackerSettings;
import com.packethammer.vaquero.dcc.DCCChatSession;
import com.packethammer.vaquero.dcc.DCCFileReceiveSession;
import com.packethammer.vaquero.dcc.DCCFileSendSession;
import com.packethammer.vaquero.dcc.DCCFileTransferListener;
import com.packethammer.vaquero.dcc.DCCManager;
import com.packethammer.vaquero.dcc.DCCSession;
import com.packethammer.vaquero.dcc.DCCSessionListener;
import com.packethammer.vaquero.dcc.PortRange;
import com.packethammer.vaquero.outbound.OutboundRawIRCLineSenderI;
import com.packethammer.vaquero.outbound.commands.basic.IRCWhoCommand;
import com.packethammer.vaquero.outbound.commands.channel.IRCChannelMessageCommand;
import com.packethammer.vaquero.outbound.commands.extended.ircu.IRCWhoXCommand;
import com.packethammer.vaquero.outbound.commands.extended.ircu.WhoXSearchOptions;
import com.packethammer.vaquero.outbound.outboundprocessing.BasicThrottleTimingScheme;
import com.packethammer.vaquero.parser.IRCEventListener;
import com.packethammer.vaquero.parser.IRCParser;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.basic.IRCDccRequestEvent;
import com.packethammer.vaquero.parser.events.channel.IRCChannelMessageEvent;
import com.packethammer.vaquero.parser.events.server.IRCPingEvent;
import com.packethammer.vaquero.parser.events.server.numeric.reply.EndOfWhoReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.WelcomeReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.WhoXReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.WhoisChannelsReply;

/**
 *
 * @author iron
 */
public class MegaTesting {
   PrintStream out = null;
   Dispatcher dispatcher;
   Tracker tracker;
   IRCParser p;
   DCCManager dcc;
            
    public static void main(String[] args) throws Exception {
        new MegaTesting();
    }
    
    public MegaTesting() throws Exception {
        dcc = new DCCManager();
        dcc.setExternalAddress(InetAddress.getByName("192.168.1.3"));
        dcc.addPortRange(new PortRange(1338, 1339));
        p = new IRCParser("irc.neon-net.com", 6668);
                
        Socket s = new Socket("irc.neon-net.com", 6668);
        Scanner in = new Scanner(s.getInputStream());
        out = new PrintStream(s.getOutputStream());
        
        dispatcher = new Dispatcher(new BasicThrottleTimingScheme(1), new OutboundRawIRCLineSenderI() {
            public void sendRawLine(String line) {
                System.out.println("-> -> RAW OUT: " + line);
                out.println(line);
            }
        }, p);
        
        
        tracker = new Tracker(p, dispatcher, new TrackerSettings());
        
        
        p.getEventDistributor().addHardEventListener(IRCPingEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                IRCPingEvent ping = (IRCPingEvent) e;
                out.println("PONG :" + ping.getMessage());
            }
        });        
        
        p.getEventDistributor().addHardEventListener(WhoisChannelsReply.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                WhoisChannelsReply w = (WhoisChannelsReply) e;
                System.out.println(" >>> WHOIS CHANS: " + w.getChannels(p.getServerContext().getISupport()));
            }
        });
        
        // dcc chat initiate
        p.getEventDistributor().addHardEventListener(IRCChannelMessageEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                IRCChannelMessageEvent m = (IRCChannelMessageEvent) e;
                if(m.getMessage().startsWith("!chat")) {
                    // send the chat request
                    try {
                        int timeout = Integer.parseInt(m.getMessage().substring(6));
                        dispatcher.sendCommand(new IRCChannelMessageCommand("#w4r", "Sending using external address " + dcc.getExternalAddress() + " timeout is " + timeout + "ms"));
                        DCCChatSession dccChat = dcc.sendDccChatRequest(m.getSource().getNickname(), dispatcher, timeout);
                        dccChat.writeLine("Okay, it worked. Bye.");
                        dccChat.close();
                    } catch (Exception ex) {
                        dispatcher.sendCommand(new IRCChannelMessageCommand("#w4r", "Chat failed:" + ex.getMessage()));
                    }
                }
            }
        });
        
        // dcc chat auto-accept
        p.getEventDistributor().addHardEventListener(IRCDccRequestEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                IRCDccRequestEvent d = (IRCDccRequestEvent) e;
                if(d.isDccChatRequest()) {
                    DCCChatSession dccChat = null;
                    try {
                        dccChat = dcc.acceptDccChat(d, 5000);
                        TestDCCSession ses = new TestDCCSession();
                        ses.session = dccChat;
                        ses.setDaemon(true);
                        ses.start();
                    } catch (Exception ex) {
                        dispatcher.sendCommand(new IRCChannelMessageCommand("#w4r", "Problem accepting DCC chat: " + ex.getMessage()));
                    }                    
                } else if(d.isDccSendRequest()) {
                        try {
                            dispatcher.sendCommand(new IRCChannelMessageCommand("#w4r", "Accepting DCC file send from " + e.getSource().getNickname() + ", file " + d.getDccSpecialArgument() + ", size " + d.getDccFileSize() + " (connect " + d.getDccAddress() + " " + d.getDccPort() + ")"));
                        } catch (UnknownHostException ex) {
                            ex.printStackTrace();
                        }
                        try {
                            DCCFileReceiveSession dccFile = dcc.acceptDccFileSend(d, new File("/home/iron/java/VaqueroSummer/test-dcc/" + d.getDccSpecialArgument()), 30, 5000);
                            dccFile.addListener(new DCCFileTransferListener() {
                                public void onTransferSuccess() {
                                    dispatcher.sendCommand(new IRCChannelMessageCommand("#w4r", "Transfer successfully finished."));
                                }
                                
                                public void onFail(IOException ex) {
                                    dispatcher.sendCommand(new IRCChannelMessageCommand("#w4r", "Transfer failed: " + ex.getMessage()));
                                }
                            });
                            dccFile.beginTransfer();
                            dispatcher.sendCommand(new IRCChannelMessageCommand("#w4r", "Connection established, file transfer begins..."));
                        } catch (UnknownHostException ex) {
                            ex.printStackTrace();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                }
            }
        });
        
        p.getEventDistributor().addHardEventListener(WelcomeReply.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                //dispatcher.sendCommand(new IRCJoinCommand(""));
                out.println("join #w4r,#testmegasuperduper");
                out.println("WHOIS iron");
            }
        });
        
        p.getEventDistributor().addDynamicEventListener(IRCEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
               System.out.println(String.format("%-30s%s", e.getClass().getSimpleName(), e));
            }
        });
        
        p.getEventDistributor().addHardEventListener(IRCChannelMessageEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                IRCChannelMessageEvent m = (IRCChannelMessageEvent) e;
                if(m.getMessage().equals("!testwho")) {
                    // try to monkeywrench that sucker
                    dispatcher.sendCommand(new IRCWhoCommand("#neon"));
                    dispatcher.performWhoXQuery(new IRCWhoXCommand("#w4r", new WhoXSearchOptions(new char[] { }, new char[] { 
                        WhoXSearchOptions.SEARCHFIELD_CHANNEL,WhoXSearchOptions.SEARCHFIELD_REALNAME,WhoXSearchOptions.SEARCHFIELD_IP
                    } )), new WhoXQueryListener() {
                        public void onReply(WhoXReply reply) {
                            System.out.println("WhoX query reply: " + reply);
                        }
                        
                        public void onFinished(EndOfWhoReply end) {
                            System.out.println("WhoX query end: " + end);
                        }
                        
                        public void onCancel() {
                            System.out.println("WhoX query cancelled prematurely.");
                        }

                    });
                } else if(m.getMessage().equals("!die")) {
                    System.exit(-1);
                } else if(m.getMessage().equals("!tracker")) {
                    tracker.debugDump(System.out);
                } else if(m.getMessage().equals("!dccs")) {
                    for(DCCSession session : dcc.getDccSessions()) {
                        dispatcher.sendCommand(new IRCChannelMessageCommand("#w4r", session.toString()));
                        dispatcher.sendCommand(new IRCChannelMessageCommand("#w4r", "ports in use: " + dcc.getPortsInUse()));
                    }
                } else if(m.getMessage().startsWith("!sendfile")) {
                    String[] split = m.getMessage().split(" ");
                    dispatcher.sendCommand(new IRCChannelMessageCommand("#w4r", "Initiating send..."));
                    try {
                        DCCFileSendSession dccFile = dcc.sendDccFile(m.getSource().getNickname(), dispatcher, new File("/home/iron/java/VaqueroSummer/test-dcc/" + split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));

                        dispatcher.sendCommand(new IRCChannelMessageCommand("#w4r", "Connection established, outbound file transfer begins..."));
                        TestDCCFileSendSession session = new TestDCCFileSendSession();
                        session.session = dccFile;
                        session.start();
                    } catch(Exception ex) {
                        dispatcher.sendCommand(new IRCChannelMessageCommand("#w4r", "Problem sending file: " + ex.getClass().toString() + " -> " + ex.getMessage()));
                    }
                }
            }
        });
        
        
        out.println("NICK testclient");
        out.println("USER x x x :x");
        
        while(in.hasNextLine()) {
            String line = in.nextLine();
            //System.out.println("(RAW) " + line);
            p.parseLine(line);
        }
        
        System.out.println(" >>>>>>>>>> DISCONNECTED");
    }  
    
    private class TestDCCSession extends Thread {
        public DCCChatSession session;
        
        public void run() {
            session.addListener(new DCCSessionListener() {
                public void onFinish() {
                    dispatcher.sendCommand(new IRCChannelMessageCommand("#w4r", "DCC chat finished."));
                }
            });
            String line = null;
            while((line = session.readLine()) != null && !line.equals("exit")) {
                dispatcher.sendCommand(new IRCChannelMessageCommand("#w4r", "Read: " + line));
            }
            session.writeLine("Bye.");
            session.close();
            this.stop();
        }
    }
    
    // oops: this isn't even needed, beginTransfer does not block forever...oh well
    private class TestDCCFileSendSession extends Thread {
        public DCCFileSendSession session;
        public void run() {
            dispatcher.sendCommand(new IRCChannelMessageCommand("#w4r", "File transfer capped rate is " + session.getMaxKilobytesPerSecond() + "kb/sec"));

            session.addListener(new DCCFileTransferListener() {
                public void onTransferSuccess() {
                    dispatcher.sendCommand(new IRCChannelMessageCommand("#w4r", "Transfer of " + session.getFile() + " successfully finished."));
                }

                public void onFail(IOException ex) {
                    dispatcher.sendCommand(new IRCChannelMessageCommand("#w4r", "Transfer failed: " + ex.getClass().toString() + " -> " + ex.getMessage()));
                }
            });
            
            try {                
                session.beginTransfer();
            } catch(Exception ex) {
                dispatcher.sendCommand(new IRCChannelMessageCommand("#w4r", "Problem sending file inside spawned thread: " + ex.getClass().toString() + " -> " + ex.getMessage()));
            }

            this.stop();
        }
    }
}
