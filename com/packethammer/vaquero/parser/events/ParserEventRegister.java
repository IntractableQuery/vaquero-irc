/*
 * Holds a registry of every event class -- this includes interfaces, abstract classes,
 * and regular classes. An internal hierarchy of the events is built based
 * on their relationships by extension or implementation. When instantiated,
 * it contains every possible event.
 *
 * Also allows lookup of numeric class information based on the numeric that
 * class handles.
 *
 * Since this class is largely for looking up events by some constant value
 * (class name, numeric number, etc.), the proper data structures are implemented
 * such that most or all of these operations are of O(1) time complexity.
 */

package com.packethammer.vaquero.parser.events;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import com.packethammer.vaquero.parser.events.basic.IRCActionEvent;
import com.packethammer.vaquero.parser.events.basic.IRCCTCPEvent;
import com.packethammer.vaquero.parser.events.basic.IRCDccRequestEvent;
import com.packethammer.vaquero.parser.events.basic.IRCMessageEvent;
import com.packethammer.vaquero.parser.events.basic.IRCModeChangeEvent;
import com.packethammer.vaquero.parser.events.basic.IRCNickChangeEvent;
import com.packethammer.vaquero.parser.events.basic.IRCNoticeEvent;
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
import com.packethammer.vaquero.parser.events.interfaces.IRCChannelTargetedEventI;
import com.packethammer.vaquero.parser.events.interfaces.IRCExtendedMessageEventI;
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
import com.packethammer.vaquero.parser.events.server.numeric.error.AlreadyRegisteredError;
import com.packethammer.vaquero.parser.events.server.numeric.error.ErroneousNicknameError;
import com.packethammer.vaquero.parser.events.server.numeric.error.NeedMoreParametersError;
import com.packethammer.vaquero.parser.events.server.numeric.error.NicknameCollisionError;
import com.packethammer.vaquero.parser.events.server.numeric.error.NicknameInUseError;
import com.packethammer.vaquero.parser.events.server.numeric.error.NoNicknameGivenError;
import com.packethammer.vaquero.parser.events.server.numeric.error.RestrictedNicknameError;
import com.packethammer.vaquero.parser.events.server.numeric.error.UnavailableResourceError;
import com.packethammer.vaquero.parser.events.server.numeric.reply.ChannelCreationReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.ChannelModeReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.ChannelsFormedReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.EndOfNamesReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.EndOfWhoReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.EndOfWhoisReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.NamesReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.OperatorsOnlineReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.ServerBounceReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.ServerCreatedReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.ServerISupportReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.ServerInfoReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.TopicReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.TopicWhoTime;
import com.packethammer.vaquero.parser.events.server.numeric.reply.UnknownConnectionsReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.UserHostReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.WelcomeReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.WhoReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.WhoXReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.WhoisAccountReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.WhoisChannelsReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.WhoisIdleReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.WhoisOperatorReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.WhoisServerReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.WhoisUserReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.YourHostReply;
import com.packethammer.vaquero.util.eventsystem.EventClassInformation;
import com.packethammer.vaquero.util.eventsystem.EventRegister;

public class ParserEventRegister extends EventRegister {  
    /**
     * A singleton instance of the event register for common usage.
     */
    public static final ParserEventRegister REGISTER = new ParserEventRegister();
    
    private HashMap<Integer,Vector<EventClassInformation>> numericEvents;
    
    public ParserEventRegister() {
        super();
    }
    
    public void buildRegistry() {   
        numericEvents = new HashMap();
        
        try {
            addEventClass(IRCEvent.class);

            // package basic
            addEventClass(IRCActionEvent.class);
            addEventClass(IRCCTCPEvent.class);
            addEventClass(IRCDccRequestEvent.class);
            addEventClass(IRCMessageEvent.class);
            addEventClass(IRCModeChangeEvent.class);
            addEventClass(IRCNickChangeEvent.class);
            addEventClass(IRCNoticeEvent.class);
            addEventClass(IRCQuitEvent.class);
            addEventClass(IRCUnknownEvent.class);

            // package channel
            addEventClass(IRCChannelActionEvent.class);
            addEventClass(IRCChannelCTCPEvent.class);
            addEventClass(IRCKickEvent.class);
            addEventClass(IRCChannelMessageEvent.class);
            addEventClass(IRCChannelModeChangeEvent.class);
            addEventClass(IRCChannelNoticeEvent.class);
            addEventClass(IRCJoinEvent.class);
            addEventClass(IRCPartEvent.class);
            addEventClass(IRCTopicChangeEvent.class);

            // package interfaces
            addEventClass(IRCChannelTargetedEventI.class);
            addEventClass(IRCExtendedMessageEventI.class);
            addEventClass(IRCHostmaskSourcedEventI.class);
            addEventClass(IRCNicknameTargetedEventI.class);
            addEventClass(IRCUserOrChannelTargetedEventI.class);

            // package personal
            addEventClass(PersonalActionEvent.class);
            addEventClass(PersonalCTCPEvent.class);
            addEventClass(PersonalChannelInvite.class);
            addEventClass(PersonalMessageEvent.class);
            addEventClass(PersonalNoticeEvent.class);

            // package server
            addEventClass(IRCAuthNoticeEvent.class);
            addEventClass(IRCErrorEvent.class);
            addEventClass(IRCKillEvent.class);
            addEventClass(IRCPingEvent.class);
            addEventClass(IRCPongEvent.class);
            addEventClass(IRCUserModeChangeEvent.class);
                // NOTE: thus far, classes are added in alphabetical order; however, some numeric handling classes share the same numeric, and as such, it is beneficial to have them follow a certain order in some cases
                // package numeric
                addEventClass(IRCNumericEvent.class);
                addEventClass(UnknownNumeric.class);
                    // package error
                    addEventClass(AlreadyRegisteredError.class);
                    addEventClass(ErroneousNicknameError.class);
                    addEventClass(NeedMoreParametersError.class);
                    addEventClass(NicknameCollisionError.class);
                    addEventClass(NicknameInUseError.class);
                    addEventClass(NoNicknameGivenError.class);
                    addEventClass(RestrictedNicknameError.class);
                    addEventClass(UnavailableResourceError.class);
                    // package reply
                    addEventClass(ChannelsFormedReply.class);
                    addEventClass(OperatorsOnlineReply.class);
                    addEventClass(ServerBounceReply.class);     // 005
                    addEventClass(ServerISupportReply.class);   // 005
                    addEventClass(ServerCreatedReply.class);
                    addEventClass(ServerInfoReply.class);
                    addEventClass(UnknownConnectionsReply.class);
                    addEventClass(WelcomeReply.class);
                    addEventClass(YourHostReply.class);
                    addEventClass(WhoReply.class);
                    addEventClass(WhoXReply.class);
                    addEventClass(EndOfWhoReply.class);
                    addEventClass(UserHostReply.class);
                    addEventClass(TopicReply.class);
                    addEventClass(TopicWhoTime.class);
                    addEventClass(NamesReply.class);
                    addEventClass(EndOfNamesReply.class);
                    addEventClass(ChannelModeReply.class);
                    addEventClass(ChannelCreationReply.class);
                    addEventClass(WhoisUserReply.class);
                    addEventClass(WhoisChannelsReply.class);
                    addEventClass(WhoisAccountReply.class);
                    addEventClass(WhoisIdleReply.class);
                    addEventClass(WhoisOperatorReply.class);
                    addEventClass(WhoisServerReply.class);
                    addEventClass(EndOfWhoisReply.class);

        } catch (Exception e) {
            // this should definitely not be happening, so make the result ugly
            System.err.println("--- Event Register exception in constructor while adding event classes to registry ---");
            e.printStackTrace(System.err);
        }
    }
    
    /**
     * Adds an event class to the internal registry.
     */
    public EventClassInformation addEventClass(Class event) throws IllegalAccessException,InstantiationException {
        EventClassInformation info = super.addEventClass(event);
        
        // catch all the subclasses of IRCNumericEvent, excluding UnknownNumeric
        if(!UnknownNumeric.class.equals(event) && !IRCNumericEvent.class.equals(event) && IRCNumericEvent.class.isAssignableFrom(event)) {
            IRCNumericEvent eventInstance = (IRCNumericEvent) event.newInstance();
            Integer numericHandled = eventInstance.getHandledNumeric();
            Vector<EventClassInformation> curEventsUnderNumeric = this.numericEvents.get(numericHandled);
            if(curEventsUnderNumeric == null)
                curEventsUnderNumeric = new Vector();
            
            curEventsUnderNumeric.add(info);
            this.numericEvents.put(numericHandled, curEventsUnderNumeric);
        }
        
        return info;
    }
    
    /**
     * Removes an event from the registry by its class name.
     *
     * @param eventClass The event class to remove.
     */
    public void removeEvent(Class eventClass) {
        super.removeEvent(eventClass);
        
        // remove from the numerics too
        Iterator<Vector<EventClassInformation>> i = this.numericEvents.values().iterator();
        while(i.hasNext()) {
            Vector<EventClassInformation> curClassList = i.next();
            Iterator<EventClassInformation> i2 = curClassList.iterator();
            while(i2.hasNext()) {
                EventClassInformation info = i2.next();
                if(info.getEventClass().equals(eventClass))
                    i2.remove();
            }
        }
    }

    
    /**
     * Returns a list of class information for a given IRC numeric, which 
     * represents those classes which can handle the numeric. May return
     * null if there are no classes.
     *
     * @param The numeric to pull classes with.
     * @return List or null if no information is known.
     */
    public List<EventClassInformation> getNumericHandlingClasses(int numeric) {
        return this.numericEvents.get(numeric);
    }
    
}
