/**
 * This class is designed to hook the IRC channel and user mode change events from
 * the parser and process the mode change events to extract common modes
 * (like +b, +v, +e, etc.) and send them out to listener classes. 
 *
 * Note that this class is not actually certain if a mode does a certain thing.
 * As an example, mode +e may not be ban exception on some networks, but it's
 * considered so here, since that's common usage. This class also supports some
 * modes that aren't even in RFC1459 and 2812, but they are popular enough
 * to create their own events in the hope someone may wish to use them.
 *
 * This makes it easy to handle things like a user becoming opped, etc.
 */

package com.packethammer.vaquero.util.parserlisteners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.packethammer.vaquero.parser.IRCEventListener;
import com.packethammer.vaquero.parser.IRCParser;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.channel.IRCChannelModeChangeEvent;
import com.packethammer.vaquero.parser.events.server.IRCUserModeChangeEvent;
import com.packethammer.vaquero.util.Hostmask;
import com.packethammer.vaquero.util.modes.channel.ChannelMode;
import com.packethammer.vaquero.util.modes.user.UserMode;

public class ModeChangeHandler {
    private IRCParser parser;
    private ArrayList<ModeChangeListener> listeners;
    
    /** 
     * Initializes this channel mode handler with the IRC parser to listen
     * to for mode change events to act on.
     *
     * @param parser The IRC parser to use.
     */
    public ModeChangeHandler(IRCParser parser) {
        this.parser = parser;
        
        // hook channel mode change event
        this.parser.getEventDistributor().addHardEventListener(IRCChannelModeChangeEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                IRCChannelModeChangeEvent c = (IRCChannelModeChangeEvent) e;
                List<ChannelMode> modes = c.getChannelModes().getModes();
                String channel = c.getChannel();
                
                for(ChannelMode mode : modes) {
                    handleChannelMode(c.getSource(), mode, channel);
                }
            }
        });
        
        // hook user mode change event
        this.parser.getEventDistributor().addHardEventListener(IRCUserModeChangeEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                IRCUserModeChangeEvent u = (IRCUserModeChangeEvent) e;
                List<UserMode> modes = u.getUserModes().getModes();
                
                for(UserMode mode : modes) {
                    handleUserMode(mode);
                }
            }
        });
    }
    
    /**
     * Handle a single user mode.
     */
    private void handleUserMode(UserMode mode) {
        if(mode.isBeingAdded()) {
            for(ModeChangeListener listener : this.getListeners())
                listener.onUserModeAdded(mode);
        } else {
            for(ModeChangeListener listener : this.getListeners())
                listener.onUserModeRemoved(mode);
        }
        
        if(mode.isBeingAdded() && mode.equals(UserMode.MODE_INVISIBLE)) {
            for(ModeChangeListener listener : this.getListeners())
                listener.onSetInvisible();
        } else if(mode.isBeingAdded() && mode.equals(UserMode.MODE_SERVERNOTICES)) {
            for(ModeChangeListener listener : this.getListeners())
                listener.onSetServerNotices();
        } else if(mode.isBeingAdded() && mode.equals(UserMode.MODE_SERVEROPERATOR)) {
            for(ModeChangeListener listener : this.getListeners())
                listener.onSetServerOperator();
        } else if(mode.isBeingAdded() && mode.equals(UserMode.MODE_WALLOPS)) {
            for(ModeChangeListener listener : this.getListeners())
                listener.onSetWallops();
        } else if(mode.isBeingAdded() && mode.equals(new Character('x'))) { // cloaking usermode
            for(ModeChangeListener listener : this.getListeners())
                listener.onSetHostnameCloaked();
        }
    }
    
    /**
     * Handle a single channel mode.
     */
    private void handleChannelMode(Hostmask source, ChannelMode mode, String channel) {
        if(mode.isBeingAdded()) {
            for(ModeChangeListener listener : this.getListeners())
                listener.onChannelModeAdded(channel, source, mode);
        } else {
            for(ModeChangeListener listener : this.getListeners())
                listener.onChannelModeRemoved(channel, source, mode);
        }
        
        if(mode.getMode().equals(ChannelMode.MODE_BAN)) {
            if(mode.isBeingAdded()) {
                for(ModeChangeListener listener : this.getListeners()) 
                    listener.onBan(channel, source, mode.getParameter());
            } else {
                for(ModeChangeListener listener : this.getListeners()) 
                    listener.onUnban(channel, source, mode.getParameter());
            }                
        } else if(mode.getMode().equals(ChannelMode.MODE_OP)) {
            if(mode.isBeingAdded()) {
                for(ModeChangeListener listener : this.getListeners()) 
                    listener.onOp(channel, source, mode.getParameter());
            } else {
                for(ModeChangeListener listener : this.getListeners()) 
                    listener.onDeop(channel, source, mode.getParameter());
            }                
        } else if(mode.getMode().equals(ChannelMode.MODE_VOICE)) {
            if(mode.isBeingAdded()) {
                for(ModeChangeListener listener : this.getListeners()) 
                    listener.onVoice(channel, source, mode.getParameter());
            } else {
                for(ModeChangeListener listener : this.getListeners()) 
                    listener.onDevoice(channel, source, mode.getParameter());
            }                
        } else if(mode.getMode().equals(new Character('h'))) { //halfop
            if(mode.isBeingAdded()) {
                for(ModeChangeListener listener : this.getListeners()) 
                    listener.onHalfop(channel, source, mode.getParameter());
            } else {
                for(ModeChangeListener listener : this.getListeners()) 
                    listener.onDehalfop(channel, source, mode.getParameter());
            }                
        } else if(mode.getMode().equals(ChannelMode.MODE_INVITEONLY)) {
            if(mode.isBeingAdded()) {
                for(ModeChangeListener listener : this.getListeners()) 
                    listener.onChannelSetInviteOnly(channel, source);
            } else {
                for(ModeChangeListener listener : this.getListeners()) 
                    listener.onChannelUnsetInviteOnly(channel, source);
            }
                
        } else if(mode.getMode().equals(ChannelMode.MODE_KEY)) {
            if(mode.isBeingAdded()) {
                for(ModeChangeListener listener : this.getListeners()) 
                    listener.onChannelSetKey(channel, source, mode.getParameter());
            } else {
                for(ModeChangeListener listener : this.getListeners()) 
                    listener.onChannelUnsetKey(channel, source, mode.getParameter());
            }                
        } else if(mode.getMode().equals(ChannelMode.MODE_LIMIT)) {
            if(mode.isBeingAdded()) {
                for(ModeChangeListener listener : this.getListeners()) 
                    listener.onChannelSetLimit(channel, source, Integer.parseInt(mode.getParameter()));
            } else {
                for(ModeChangeListener listener : this.getListeners()) 
                    listener.onChannelUnsetLimit(channel, source);
            }                
        } else if(mode.getMode().equals(ChannelMode.MODE_MODERATED)) {
            if(mode.isBeingAdded()) {
                for(ModeChangeListener listener : this.getListeners()) 
                    listener.onChannelSetModerated(channel, source);
            } else {
                for(ModeChangeListener listener : this.getListeners()) 
                    listener.onChannelUnsetModerated(channel, source);
            }                
        } else if(mode.getMode().equals(ChannelMode.MODE_NOEXTERNALMESSAGES)) {
            if(mode.isBeingAdded()) {
                for(ModeChangeListener listener : this.getListeners()) 
                    listener.onChannelSetNoOutsideMessages(channel, source);
            } else {
                for(ModeChangeListener listener : this.getListeners()) 
                    listener.onChannelSetNoOutsideMessages(channel, source);
            }                
        } else if(mode.getMode().equals(ChannelMode.MODE_PRIVATE)) {
            if(mode.isBeingAdded()) {
                for(ModeChangeListener listener : this.getListeners()) 
                    listener.onChannelSetPrivate(channel, source);
            } else {
                for(ModeChangeListener listener : this.getListeners()) 
                    listener.onChannelUnsetPrivate(channel, source);
            }                
        } else if(mode.getMode().equals(ChannelMode.MODE_SECRET)) {
            if(mode.isBeingAdded()) {
                for(ModeChangeListener listener : this.getListeners()) 
                    listener.onChannelSetSecret(channel, source);
            } else {
                for(ModeChangeListener listener : this.getListeners()) 
                    listener.onChannelUnsetSecret(channel, source);
            }                
        } else if(mode.getMode().equals(ChannelMode.MODE_TOPICLOCK)) {
            if(mode.isBeingAdded()) {
                for(ModeChangeListener listener : this.getListeners()) 
                    listener.onChannelSetTopicOps(channel, source);
            } else {
                for(ModeChangeListener listener : this.getListeners()) 
                    listener.onChannelUnsetTopicOps(channel, source);
            }                
        } 
    }
    
    /**
     * Adds a mode change listener to this handler.
     */
    public void addListener(ModeChangeListener listener) {
        this.listeners.add(listener);
    }
    
    /**
     * Removes a mode change listener from this handler. Removes multiple 
     * instances if they exist. Uses equals() method.
     */
    public void removeListener(ModeChangeListener listener) {
        Iterator<ModeChangeListener> i = this.listeners.iterator();
        while(i.hasNext()) {
            if(i.next().equals(listener)) {
                i.remove();
            }
        }
    }
    
    /**
     * Returns the mode listener list.
     *
     * @return The listeners.
     */
    public List<ModeChangeListener> getListeners() {
        return this.listeners;
    }
}
