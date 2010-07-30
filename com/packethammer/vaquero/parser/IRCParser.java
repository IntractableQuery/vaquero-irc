/**
 * This class is the glue that ties event classes together with the raw IRC lines
 * that arrive. Although the fact that event classes are backed by a raw IRC line
 * cuts down on a huge amount of parsing, this class still performs the menial
 * tasks of parsing raw IRC lines into their object-oriented form and some
 * additional parsing that is impossible to leave to the event classes (like
 * mode parsing).
 *
 * All event classes for events are resolved using HashMaps (this goes for
 * regular IRC commands and numerics), so lookup is a constant-time operation.
 *
 * This parser is designed to support every RFC1459 non-optional feature. However,
 * it supports some additional features that are dominant enough to be useful or
 * are essential to keep the parser from generating events that could break 
 * things (a good example is ircu's use of a @-prefix on channel names in a notice
 * when someone sends WALLOPS/WALLVOICES/'NOTICE @#chan' -- we must strip these
 * since it isn't nice to leave them there as part of the channel name).
 *
 * Rant in documentation:
 * As a side note, IRC is endlessly evil if you want to create an all-encompassing
 * parser that is instantly aware of what a server supports and what it does not.
 * IRC is not particularly standardized beyond the basic original RFC commands,
 * so instead of engaging in nazi-like identification of server types to find
 * out their quirks, it will mostly be left up to the user of this IRC client
 * framework to discover or determine that.
 */

package com.packethammer.vaquero.parser;

import java.util.*;
import com.packethammer.vaquero.util.Hostmask;
import com.packethammer.vaquero.util.modes.Modes;
import com.packethammer.vaquero.util.protocol.IRCRawLine;
import com.packethammer.vaquero.util.protocol.IRCRawParameter;
import com.packethammer.vaquero.util.eventsystem.EventClassInformation;
import com.packethammer.vaquero.parser.events.ParserEventRegister;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.basic.IRCActionEvent;
import com.packethammer.vaquero.parser.events.basic.IRCCTCPEvent;
import com.packethammer.vaquero.parser.events.basic.IRCDccRequestEvent;
import com.packethammer.vaquero.parser.events.basic.IRCNickChangeEvent;
import com.packethammer.vaquero.parser.events.basic.IRCQuitEvent;
import com.packethammer.vaquero.parser.events.basic.IRCUnknownEvent;
import com.packethammer.vaquero.parser.events.channel.IRCChannelActionEvent;
import com.packethammer.vaquero.parser.events.channel.IRCChannelCTCPEvent;
import com.packethammer.vaquero.parser.events.channel.IRCKickEvent;
import com.packethammer.vaquero.parser.events.channel.IRCChannelMessageEvent;
import com.packethammer.vaquero.parser.events.channel.IRCChannelModeChangeEvent;
import com.packethammer.vaquero.parser.events.channel.IRCChannelNoticeEvent;
import com.packethammer.vaquero.parser.events.channel.IRCJoinEvent;
import com.packethammer.vaquero.parser.events.channel.IRCPartEvent;
import com.packethammer.vaquero.parser.events.channel.IRCTopicChangeEvent;
import com.packethammer.vaquero.parser.events.interfaces.IRCHostmaskSourcedEventI;
import com.packethammer.vaquero.parser.events.interfaces.IRCNicknameTargetedEventI;
import com.packethammer.vaquero.parser.events.interfaces.IRCUserOrChannelTargetedEventI;
import com.packethammer.vaquero.parser.events.personal.PersonalActionEvent;
import com.packethammer.vaquero.parser.events.personal.PersonalCTCPEvent;
import com.packethammer.vaquero.parser.events.personal.PersonalChannelInvite;
import com.packethammer.vaquero.parser.events.personal.PersonalMessageEvent;
import com.packethammer.vaquero.parser.events.personal.PersonalNoticeEvent;
import com.packethammer.vaquero.parser.events.server.IRCAuthNoticeEvent;
import com.packethammer.vaquero.parser.events.server.IRCErrorEvent;
import com.packethammer.vaquero.parser.events.server.IRCKillEvent;
import com.packethammer.vaquero.parser.events.server.IRCPingEvent;
import com.packethammer.vaquero.parser.events.server.IRCPongEvent;
import com.packethammer.vaquero.parser.events.server.IRCUserModeChangeEvent;
import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;
import com.packethammer.vaquero.parser.events.server.numeric.UnknownNumeric;
import com.packethammer.vaquero.parser.events.server.numeric.reply.ServerISupportReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.ServerInfoReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.UserHostReply;
import com.packethammer.vaquero.parser.tracking.IRCServerContext;
import com.packethammer.vaquero.parser.tracking.definitions.ChannelNickPrefixModeDefinition;

public class IRCParser {
    /** This string is vaquero's numeric version */
    public static final String VAQUERO_VERSION = "0.2";
    /** This is a human-friendly string indicating Vaquero's version */
    public static final String VAQUERO_VERSIONTEXT = "Vaquero " + VAQUERO_VERSION + " (beta)";
            
    private IRCEventDistributor eventDistributor;
    private IRCServerContext serverContext;
    private HashMap<String,ParserHandler> commandHandlers;
    private List<SuccessfulLoginListener> loginListeners;

    /**
     * Initiates the IRC parser with essential information. Although the parser
     * may be initiated with connection-oriented information, this is actually 
     * for the sake of the internal tracking systems when generating events.
     * 
     * @param serverPhysicalAddress The server address, ideally in IP or hostname form.
     * @param serverPhysicalPort The server port.
     */
    public IRCParser(String serverPhysicalAddress, int serverPhysicalPort) {
        this.eventDistributor = new IRCEventDistributor(ParserEventRegister.REGISTER);
        this.serverContext = new IRCServerContext();
        this.commandHandlers = new HashMap();
        this.loginListeners = new Vector();
        this.serverContext.setServerPhysicalAddress(serverPhysicalAddress);
        this.serverContext.setServerPhysicalPort(serverPhysicalPort);
        
        // set up essential parser command handlers
        setupCommandHandlers();
        
        // hook some events which are important for proper functionality and tracking
        hookEvents();
    }
    
    /**
     * Adds the command handlers needed for parsing.
     */
    private void setupCommandHandlers() {
        commandHandlers.put("PRIVMSG", new ParserHandler() {
            public IRCEvent pullEvent(IRCRawLine line) {
                // PRIVMSG <TARGET> <MESSAGE>
                if(line.parametersCount() == 3) {
                    String msg = line.getArg(2);
                    // decide if this is a message to us directly
                    if(getServerContext().isMe(line.getSecondArgument())) {
                        // assume it is personal (private) message
                        if(msg.startsWith(IRCCTCPEvent.CTCP_CHAR + "")) {
                            if(msg.toUpperCase().startsWith(IRCCTCPEvent.CTCP_CHAR + IRCActionEvent.ACTION_PREFIX)) {
                                return new PersonalActionEvent();
                            } else if(msg.toUpperCase().startsWith(IRCCTCPEvent.CTCP_CHAR + IRCDccRequestEvent.DCC_PREFIX)) {
                                if(msg.split(" ").length >= 5)
                                    return new IRCDccRequestEvent();
                            } else {
                                return new PersonalCTCPEvent();
                            }
                        } else {
                            return new PersonalMessageEvent();
                        }
                    } else {
                        // assume it is channel notice
                        if(msg.startsWith(IRCCTCPEvent.CTCP_CHAR + "")) {
                            if(msg.startsWith(IRCCTCPEvent.CTCP_CHAR + IRCActionEvent.ACTION_PREFIX)) {
                                return new IRCChannelActionEvent();
                            } else {
                                return new IRCChannelCTCPEvent();
                            }
                        } else {
                            return new IRCChannelMessageEvent();
                        }
                    }
                }
                
                return null;
            }     
        });
        
        commandHandlers.put("NOTICE", new ParserHandler() {
            public IRCEvent pullEvent(IRCRawLine line) {
                // NOTICE <TARGET> <MESSAGE>
                if(line.parametersCount() == 3) {
                    // decide if this is a message to us directly
                    if(!getServerContext().isFullyConnected()) {
                        // assume it is AUTH on-connect notice
                        return new IRCAuthNoticeEvent();
                    } else if(getServerContext().isMe(line.getSecondArgument())) {
                        // assume it is personal (private) notice
                        return new PersonalNoticeEvent();
                    } else {
                        String target = line.getSecondArgument();
                        char firstChar = target.charAt(0);
                        // assume it is channel notice
                        // now, the ircu ircd (at least in some configurations, like on the GameSurge network) doesn't give a proper STATUSMSG in ISUPPORT, so we have no way of knowing if the channel name has a prefix on it... the solution? try to find it ourself. oh, yeah -- the prefix is useless for notices to voices on gamesurge (it appears as @#chan anyway). sigh.
                        IRCChannelNoticeEvent event = new IRCChannelNoticeEvent();
                        
                        for(ChannelNickPrefixModeDefinition modeDef : getServerContext().getISupport().getNickPrefixModes()) {
                            if(modeDef.getPrefix().equals(firstChar)) {
                                event.setTargetedUsers(modeDef); // at the moment, NoticeEvent will return a proper target
                                break;
                            }
                        }
                        
                        return event;
                    }
                }
                return null;
            }
        });
        
        commandHandlers.put("NICK", new ParserHandler() {
            public IRCEvent pullEvent(IRCRawLine line) {
                // NICK <NEW NICKNAME>
                if(line.parametersCount() == 2) {
                    return new IRCNickChangeEvent();
                }
                return null;
            }
        });
        
        commandHandlers.put("QUIT", new ParserHandler() {
            public IRCEvent pullEvent(IRCRawLine line) {
                // QUIT [QUIT MESSAGE]
                if(line.parametersCount() >= 1) {
                    return new IRCQuitEvent();
                }
                return null;
            }
        });
        
        commandHandlers.put("MODE", new ParserHandler() {
            // TODO: evaluate this and determine problems for this client changing another user's usermodes and seeing that
            public IRCEvent pullEvent(IRCRawLine line) {
                // MODE <TARGET> <MODE CHANGE PARAMETERS>
                if(line.parametersCount() >= 3) {
                    String modesSet = line.toRawLine(2, false, false); // do not render source, do not render colon prefix if it is has an extended argument
                    if(getServerContext().isMe(line.getSecondArgument())) {
                        // assume it is our usermodes being changed
                        IRCUserModeChangeEvent modesChange = new IRCUserModeChangeEvent();
                        modesChange.setModes(parseUserModes(modesSet));
                        return modesChange;
                    } else {
                        // assume it is channel modes
                        IRCChannelModeChangeEvent modesChange = new IRCChannelModeChangeEvent();
                        modesChange.setModes(parseChannelModes(modesSet));
                        return modesChange;
                    }
                }
                return null;
            }
        });
        
        commandHandlers.put("JOIN", new ParserHandler() {
            public IRCEvent pullEvent(IRCRawLine line) {
                // JOIN <CHANNEL>
                if(line.parametersCount() == 2) {
                    return new IRCJoinEvent();
                }
                return null;
            }
        });
        
        commandHandlers.put("PART", new ParserHandler() {
            public IRCEvent pullEvent(IRCRawLine line) {
                // PART <CHANNEL> [MESSAGE]
                if(line.parametersCount() >= 2) {
                    return new IRCPartEvent();
                }
                return null;
            }
        });
        
        commandHandlers.put("KICK", new ParserHandler() {
            public IRCEvent pullEvent(IRCRawLine line) {
                // KICK <CHANNEL> <TARGET NICKNAME> [REASON]
                if(line.parametersCount() >= 3) {
                    return new IRCKickEvent();
                }
                return null;
            }
        });
        
        commandHandlers.put("TOPIC", new ParserHandler() {
            public IRCEvent pullEvent(IRCRawLine line) {
                // TOPIC <CHANNEL> <NEW TOPIC>
                if(line.parametersCount() == 3) {
                    return new IRCTopicChangeEvent();
                }
                return null;
            }
        });
        
        commandHandlers.put("INVITE", new ParserHandler() {
            public IRCEvent pullEvent(IRCRawLine line) {
                // INVITE <NICKNAME (US)> <CHANNEL>
                if(line.parametersCount() == 3) {
                    return new PersonalChannelInvite();
                }
                return null;
            }
        });
        
        commandHandlers.put("PING", new ParserHandler() {
            public IRCEvent pullEvent(IRCRawLine line) {
                // PING [PAYLOAD]
                if(line.parametersCount() >= 1) {
                    return new IRCPingEvent();
                }
                return null;
            }
        });
        
        commandHandlers.put("PONG", new ParserHandler() {
            public IRCEvent pullEvent(IRCRawLine line) {
                // PONG [PAYLOAD]
                if(line.parametersCount() >= 1) {
                    return new IRCPongEvent();
                }
                return null;
            }
        });
        
        commandHandlers.put("ERROR", new ParserHandler() {
            public IRCEvent pullEvent(IRCRawLine line) {
                // ERROR <MESSAGE>
                if(line.parametersCount() == 2) {
                    return new IRCErrorEvent();
                }
                return null;
            }
        });
        
        commandHandlers.put("KILL", new ParserHandler() {
            public IRCEvent pullEvent(IRCRawLine line) {
                // KILL <NICKNAME> <MESSAGE>
                if(line.parametersCount() == 3) {
                    return new IRCKillEvent();
                }
                return null;
            }
        });
    }
    
    /**
     * Hooks events that the parser needs to handle.
     */ 
    private void hookEvents() {
        // maintain the channels listing
        this.getEventDistributor().addDynamicEventListener(IRCJoinEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                IRCJoinEvent event = (IRCJoinEvent) e;                
                if(getServerContext().isMe(e.getSource().getNickname())) {
                    getServerContext().getChannels().add(event.getChannel().toLowerCase());
                }
            }
        });
        this.getEventDistributor().addDynamicEventListener(IRCPartEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                IRCPartEvent event = (IRCPartEvent) e;                
                if(getServerContext().isMe(e.getSource().getNickname())) {
                    getServerContext().getChannels().remove(event.getChannel().toLowerCase());
                }
            }
        });
        this.getEventDistributor().addDynamicEventListener(IRCKickEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                IRCKickEvent event = (IRCKickEvent) e;                
                if(getServerContext().isMe(event.getTarget())) {
                    getServerContext().getChannels().remove(event.getChannel().toLowerCase());
                }
            }
        });
        
        // listen for proper connection and also get nickname using numerics
        this.getEventDistributor().addDynamicEventListener(IRCNumericEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                IRCNumericEvent event = (IRCNumericEvent) e;                
                if((event.getNumeric() >= 0 && event.getNumeric() <= 4) || event.getNumeric() == IRCNumericEvent.RPL_MOTDSTART) {
                    // we can now assume we are connected to the network and we can set our nickname
                    getServerContext().setFullyConnected(true);
                    getServerContext().setMyTrackedNickname(event.getTarget());
                    
                    // Tell the successful login listeners that we're ready for business
                    for(SuccessfulLoginListener listener : loginListeners) {
                        listener.onSuccess();
                    }
                    
                    // okay, from here on out, the NICK change tracker should handle things, so stop using this
                    unregisterMe();
                }
            }
        });
        
        // this listens for USERHOST information from the server to establish our own hostmask. I believe we must ask the server for it.
        this.getEventDistributor().addDynamicEventListener(UserHostReply.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                UserHostReply event = (UserHostReply) e;                
                if(getServerContext().isMe(event.getNickname())) {
                    getServerContext().setMyTrackedHostmask(event.getAsHostmask());
                }
            }
        });
        
        // the only purpose of this handler is to figure out what the server calls itself
        this.getEventDistributor().addDynamicEventListener(IRCNumericEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
            // TODO: IS THE FIRST NUMERIC ALWAYS THE SERVER? MAY BE FOREIGN... JUST DO 001-004?
                IRCNumericEvent event = (IRCNumericEvent) e;                
                if(!getServerContext().isServerNameKnown()) {
                    // we don't know the server name... if this numeric's source is definite, we will know it, though
                    if(event.isSourceDefinite()) {
                        getServerContext().setServerName(event.getSource().getHost()); 
                        unregisterMe(); // we are done, we set the server name.
                    } 
                } else {
                    // some way, we know the server name already, so we're done
                    unregisterMe();
                }
            }
        });
        
        // listen for numeric 005 ISUPPORT
        this.getEventDistributor().addHardEventListener(ServerISupportReply.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                ServerISupportReply event = (ServerISupportReply) e;                
                // feed support into the support tracker
                Map<String,String> support = event.getSupport();
                for(String key : support.keySet()) {
                    getServerContext().getISupport().addISupport(key, support.get(key));
                }
            }
        });
        
        // Get the usermodes available on the server.
        this.getEventDistributor().addHardEventListener(ServerInfoReply.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                ServerInfoReply event = (ServerInfoReply) e;
                for(char userMode : event.getUserModes())
                    getServerContext().addKnownUserMode(userMode);
                unregisterMe(); // once we've gotten this numeric, we're done
            }
        });
        
        // watch for our own nickname changes        
        this.getEventDistributor().addHardEventListener(IRCNickChangeEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                IRCNickChangeEvent event = (IRCNickChangeEvent) e;
                if(getServerContext().isMe(event.getSource().getNickname())) {
                    getServerContext().setMyTrackedNickname(event.getNewNickname());
                }
            }
        });   
        
        // try to catch our own full hostmask -- once we know it, we assume it never changes, so we exit after that
        this.getEventDistributor().addDynamicEventListener(IRCHostmaskSourcedEventI.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                IRCHostmaskSourcedEventI event = (IRCHostmaskSourcedEventI) e;
                if(getServerContext().isMe(event.getSource().getNickname())) {
                    getServerContext().setMyTrackedHostmask(event.getSource());
                    unregisterMe();
                }
            }
        });  
    }

    /**
     * Parses a single line of data from the server.
     * 
     * @param line A line of raw IRC data.
     */
    public void parseLine(String line) {
        IRCRawLine rawLine = IRCRawLine.parse(line);
          
        if(!rawLine.isSourceDefinite()) {
            // we have to guess the origin -- first try to use server name, then use physical server address, which is definitely known
            if(this.getServerContext().isServerNameKnown())
                rawLine.setSource(Hostmask.parseHostmask(this.getServerContext().getServerName()));
            else
                rawLine.setSource(Hostmask.parseHostmask(this.getServerContext().getServerPhysicalAddress()));
        }

        
        generateEventFrom(rawLine);
    }
    
    /**
     * Given an IRCRawLine, this method will inspect it and generate an event based off it.
     *
     * @param rawLine The line to analyze for event generation.
     */    
    private void generateEventFrom(IRCRawLine rawLine) {
        Date arrival = new Date();
        IRCEvent event = null;
        
        // begin actual parsing
        String command = rawLine.getCommandArgument();
        if(command != null) {
            // First, consult the parser handlers that deal with basic "commands"
            ParserHandler commandHandler = this.commandHandlers.get(command.toUpperCase());
            if(commandHandler != null) {
                event = commandHandler.pullEvent(rawLine);
                if(event != null) {
                    // go ahead and set the raw line and parser 
                    event.cloneFrom(rawLine);
                    event.setParser(this);
                }
            } else {
                // may be a numeric
                try {
                    Integer numeric = Integer.parseInt(command);

                    // it is definitely a numeric; pull the event
                    event = this.generateNumericEvent(numeric.intValue(), rawLine);
                } catch (NumberFormatException e) {
                    // it was a command of length 3, but not a number... no idea what that is
                }
            }
        }
        
        // now, did we actually instantiate an event of some sort?
        if(event == null) {
            // no, we did not -- we have no idea what this line of IRC data is then.
            event = new IRCUnknownEvent();
        }
        
        // set the event's essential information and send it out
        event.setArrival(arrival);
        event.preinitialize(); // give the event a chance to initialize its internal data
        
        // one last thing to do: determine if we need to flag the event as "targeting me"
        if(event instanceof IRCNicknameTargetedEventI) {
            if(this.getServerContext().isMe(((IRCNicknameTargetedEventI) event).getTarget()))
                event.setTargetingMe(true);
        } else if(event instanceof IRCUserOrChannelTargetedEventI) {
            if(this.getServerContext().isMe(((IRCUserOrChannelTargetedEventI) event).getTarget()))
                event.setTargetingMe(true);
        }
        
        this.getEventDistributor().propagateEvent(event);
    }
    
    /**
     * Returns a suitable numeric event class for a given numeric code. 
     * Guarantees an event will be returned.
     */
    private IRCNumericEvent generateNumericEvent(int numeric, IRCRawLine line) {
        // we'll utilize the event register, which has a nice lookup method to automatically pull correct classes.
        // the event register won't return the base abstract IRC numeric class or the unknown numeric class.
        List<EventClassInformation> numericClasses = ParserEventRegister.REGISTER.getNumericHandlingClasses(numeric);
        IRCNumericEvent event = null;
        
        if(numericClasses != null) {
            for(EventClassInformation info : numericClasses) {
                try {
                    // we start off by assuming that this event class is okay to use, so we instantiate it...
                    IRCNumericEvent eventInstance = (IRCNumericEvent) info.getEventClass().newInstance();
                    // now, give the event its raw line
                    eventInstance.cloneFrom(line);
                    // give it us (the parser)
                    eventInstance.setParser(this);
                    // is the event actually valid now?
                    if(eventInstance.validate()) {
                        // yes, the event was valid!
                        return eventInstance;
                    } else {
                        // event was not valid -- just let the loop continue on in search of a valid event
                    }
                } catch (Exception e) {
                    // ...bad, but we can do little
                }
            }
        }
        
        // we never found a suitable class if we got this far
        event = new UnknownNumeric();
        event.cloneFrom(line);
        return event;
    }
    
    /**
     * Given a string representing modes being set on a user, this will parse
     * them and return a Modes list.
     *
     * Example input: '+ix'
     */
    public Modes parseUserModes(String userModes) {
        return Modes.parseModes(userModes, this.serverContext.getKnownUserModes());       
    }
    
    /**
     * Given a string representing modes being set on a channel, this will parse
     * them and return a Modes list.
     *
     * Example input: '+mno jim'
     */
    public Modes parseChannelModes(String channelModes) {
        return Modes.parseModes(channelModes, (Collection) this.serverContext.getISupport().getChannelModes());       
    }   

    /**
     * Returns the server context for this parser. This contains stateful information
     * like our nickname, and information about the server's support. 
     */
    public IRCServerContext getServerContext() {
        return serverContext;
    }
    
    /**
     * Returns the event distributor used by this parser.
     */
    public IRCEventDistributor getEventDistributor() {
        return eventDistributor;
    }
    
    /**
     * Frees up resources and renders this parser unusable.
     */
    public void die() {
        this.getEventDistributor().die();
        this.eventDistributor = null;
        this.serverContext = null;
        this.commandHandlers = null;
    }

    /**
     * Returns the successful-login listeners.
     */
    public List<SuccessfulLoginListener> getSuccessfulLoginListeners() {
        return loginListeners;
    }

    /**
     * Adds a successful-login listener.
     *
     * @param listener The listener to add.
     */
    public void addSuccessfulLoginListener(SuccessfulLoginListener listener) {
        this.loginListeners.add(listener);
    }
    
    /**
     * Removes a successful-login listener, removing multiple equal instances
     * if they exit.
     *
     * @param listener The listener to remove.
     */
    public void removeSuccessfulLoginListener(SuccessfulLoginListener listener) {
        while(this.loginListeners.remove(listener));
    }
}

