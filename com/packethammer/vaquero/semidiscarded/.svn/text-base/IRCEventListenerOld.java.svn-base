/**
 * Classes wishing to listen to incoming IRC events should extend this
 * class.
 *
 * All IRC events are delegated to their own methods, which are called when
 * the event takes place.
 *
 * Since this is a class meant for extension (subclassing), you can select
 * which events you wish to repond to by overriding them.
 *
 * It is guaranteed that only one event will be called for every line of raw
 * IRC data. However, this is not true of the onData() event, which will ALWAYS
 * be called for every line of IRC data.
 */

package com.packethammer.vaquero.semidiscarded;

import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.basic.IRCActionEvent;
import com.packethammer.vaquero.parser.events.basic.IRCCTCPEvent;
import com.packethammer.vaquero.parser.events.basic.IRCMessageEvent;
import com.packethammer.vaquero.parser.events.basic.IRCNickChangeEvent;
import com.packethammer.vaquero.parser.events.basic.IRCNoticeEvent;
import com.packethammer.vaquero.parser.events.basic.IRCQuitEvent;
import com.packethammer.vaquero.parser.events.channel.IRCChannelActionEvent;
import com.packethammer.vaquero.parser.events.channel.IRCChannelCTCPEvent;
import com.packethammer.vaquero.parser.events.channel.IRCKickEvent;
import com.packethammer.vaquero.parser.events.channel.IRCChannelMessageEvent;
import com.packethammer.vaquero.parser.events.channel.IRCChannelModeChangeEvent;
import com.packethammer.vaquero.parser.events.channel.IRCChannelNoticeEvent;
import com.packethammer.vaquero.parser.events.channel.IRCJoinEvent;
import com.packethammer.vaquero.parser.events.channel.IRCPartEvent;
import com.packethammer.vaquero.parser.events.channel.IRCTopicChangeEvent;
import com.packethammer.vaquero.parser.events.server.IRCAuthNoticeEvent;
import com.packethammer.vaquero.parser.events.server.IRCUserModeChangeEvent;
import com.packethammer.vaquero.parser.events.server.IRCPingEvent;
import com.packethammer.vaquero.parser.events.server.IRCPongEvent;
import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;

public abstract class IRCEventListenerOld {
// --- GENERAL SERVER-WIDE EVENTS INVOLVING USERS ---
    /**
     * Occurs when a user (possibly us) changes their nickname.
     */
    public void onNickChanged(IRCNickChangeEvent e) {        
        
    }
    
    /**
     * Occurs when a user (possibly us) quits the IRC server.
     */
    public void onQuit(IRCQuitEvent e) {
        
    }
    
// --- CHANNEL-SPECIFIC EVENTS ---
    /**
     * Occurs when a server/user sends a regular message to a channel.
     */
    public void onChannelMessage(IRCChannelMessageEvent e) {
        
    }
    
    /**
     * Occurs when a server/user sends a CTCP message to a channel. As hinted by 
     * this class' description, this would exclude the special ACTION 
     * CTCP (/me), which is an event unto itself.
     */
    public void onChannelCTCP(IRCChannelCTCPEvent e) {
        
    }
    
    /**
     * Occurs when a server/user sends an ACTION to a channel (/me).
     */
    public void onChannelAction(IRCChannelActionEvent e) {
        
    }
    
    /**
     * Occurs when a server/user sends a notice to a channel. Note that notices may
     * target a certain user group, such as all ops (@) or all voices (+).
     * There currently is no way to predict if a server will do this to you
     * (at least so far as the IRC RFCs), so it is up to you to decide if you
     * should expect to deal with such special notices or not. However, nothing
     * stops you from checking if you received such a notice here.
     */
    public void onChannelNotice(IRCChannelNoticeEvent e) {
        
    }
    
    /**
     * Occurs when a server/user changes the modes on a channel.
     */
    public void onChannelModeChange(IRCChannelModeChangeEvent e) {
        
    }
    
    /**
     * Occurs when a user (possibly us) joins a channel.
     */
    public void onJoin(IRCJoinEvent e) {
        
    }
    
    /**
     * Occurs when a user (possibly us) leaves a channel.
     */
    public void onPart(IRCPartEvent e) {
        
    }
    
    /**
     * Occurs when a server/user (possibly us) changes the topic of a channel.
     */
    public void onTopicChange(IRCTopicChangeEvent e) {
        
    }
    
    /**
     * Occurs when a (server?)/user (possibly us) kicks a user from a channel.
     */
    public void onChannelKick(IRCKickEvent e) {
        
    }
    
// --- PRIVATE (PERSONAL) EVENTS TARGETING US NOT COVERED BY CHANNEL EVENTS ---
    /**
     * Occurs when we receive an event from something messaging us directly.
     */
    public void onMessageMe(IRCMessageEvent e) {
        
    }
    
    /**
     * Occurs when we receive an event from something CTCPing us directly. Excludes
     * CTCP ACTION, of course.
     */
    public void onCTCPMe(IRCCTCPEvent e) {
        
    }
    
    /**
     * Occurs when we receive an action (/me) from something messaging us directly.
     */
    public void onActionMe(IRCActionEvent e) {
        
    }
    
    /**
     * Occurs when we receive a notice from something noticing us directly.
     */
    public void onNoticeMe(IRCNoticeEvent e) {
        
    }

// --- SERVER-SPECIFIC EVENTS (THOSE ORIGINATING FROM THE SERVER OR PERSONAL) ---
    /**
     * Occurs when the server sends us a PING. We should typically reply to it 
     * if nothing else is going to. Otherwise, the server may think we are 
     * dead.
     */
    public void onServerPing(IRCPingEvent e) {
        
    }
    
    /**
     * Occurs when the server sends us a PONG (reply to a PING). May be accompanied
     * by a payload reply.
     */
    public void onServerPong(IRCPongEvent e) {
        
    }
    
    /**
     * This occurs when we receive a numeric from the server. There is a very 
     * large number of numeric responses, so it is not economical to provide
     * event methods for all of them. Instead, take note of the fact that
     * "...vaquero.parser.events.server.numeric" contains common numeric events
     * that the server may send to us, which makes it easier to extract their
     * information without knowing the numeric code and such. There are a few
     * different ways to handle specific events this way, but here is the 
     * cleanest and most logical example for catching a specific IRC numeric
     * event, assuming you know the class name of the one you want:
     *
     * if(e instanceof WelcomeReply) {
     *      WelcomeReply welcome = (WelcomeReply) e;
     *      System.out.println("The welcome message was: " + welcome.getWelcomeMessage());
     * }
     *
     * @param The base numeric event, although it may be a subclass of IRCNumericEvent that makes data more accessible.
     * 
     */
    public void onNumericEvent(IRCNumericEvent e) {
        
    }
    
    /**
     * This is the type of notice you receive before even being established as
     * a client on the IRC server. It is generally a message notifying you of your
     * status as the server checks your hostname, ident, etc.
     */
    public void onAuthNotice(IRCAuthNoticeEvent e) {
        
    }
    
    /**
     * Occurs when our user modes change.
     */
    public void onUserModeChangeMe(IRCUserModeChangeEvent e) {
        
    }
    
// --- SPECIAL EVENT ---
    /**
     * Called for every line of IRC data we receive. Unlike the other events 
     * in this class, this one will be called before the others. After it,
     * a specific event may follow. You normally do not need to utilize this
     * event, but it is there to give you a central location to watch every
     * possible event that might come in, so you could do something
     * such as monitor the raw server data without using all methods in this
     * class.
     *
     * @param line The event that corresponds with a line of IRC data.
     */
    public void onData(IRCEvent line) {
        
    }
}
