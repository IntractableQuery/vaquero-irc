/**
 * This query processor handles WHO and WHOX queries.
 */

// note: this class is a bit...uh...ghetto. I can't think of a clean way to
// handle WHO and WHOX at the same time in a manner that requires less type-casting.

package com.packethammer.vaquero.advanced.dispatcher.querying;

import java.util.Vector;
import com.packethammer.vaquero.outbound.commands.basic.IRCWhoCommand;
import com.packethammer.vaquero.outbound.commands.extended.ircu.IRCWhoXCommand;
import com.packethammer.vaquero.outbound.outboundprocessing.EncapsulatedIRCCommand;
import com.packethammer.vaquero.outbound.outboundprocessing.OutboundCommandEventListener;
import com.packethammer.vaquero.parser.IRCEventListener;
import com.packethammer.vaquero.parser.IRCParser;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.server.numeric.reply.EndOfWhoReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.WhoReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.WhoXReply;

public class WhoReplyQueryProcessor extends AbstractQueryProcessor {
    private IRCParser parser;
    private Vector<WhoSession> liveQueries; // index 0 = query awaiting reply, end = newest query sent live
    private Vector<WhoSession> waitingQueries; // queries not sent yet
    
    /** 
     * Initializes this query processor with the parser it is to use.
     */
    public WhoReplyQueryProcessor(IRCParser parser) {
        this.liveQueries = new Vector();
        this.waitingQueries = new Vector();
        
        this.parser = parser;  
        
        hookEvents();
    }
    
    /**
     * Hooks events vital to processing WHO replies.
     */
    private void hookEvents() {
        parser.getEventDistributor().addHardEventListener(WhoReply.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                WhoReply r = (WhoReply) e;
                
                if(getCurrentListener() instanceof WhoQueryListener) {
                    WhoQueryListener q = (WhoQueryListener) getCurrentListener();
                    q.addReply(r);
                }
            }
        });
        
        parser.getEventDistributor().addHardEventListener(WhoXReply.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                WhoXReply r = (WhoXReply) e;
                
                if(getCurrentListener() instanceof WhoXQueryListener) {
                    WhoXQueryListener q = (WhoXQueryListener) getCurrentListener();
                    q.addReply(r);
                }
            }
        });
        
        parser.getEventDistributor().addHardEventListener(EndOfWhoReply.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                EndOfWhoReply end = (EndOfWhoReply) e;
                getCurrentListener().onFinished(end);
                
                // remove listener
                liveQueries.remove(0);
            }
        });
    }
    
    private AbstractWhoListener getCurrentListener() {
        if(this.liveQueries.isEmpty()) {
            throw new RuntimeException("Tried to get current listener, but queue is empty; possible WHO desync -- very bad");
        }
        
        return liveQueries.get(0).whoListener;
    }
    
    /**
     * Adds a WHO or WHOX query to this query processor with its listener.
     *
     * The command must be an instance of WhoCommand or WhoXCommand, or an
     * exception will be thrown.
     *
     * @param command The WHO command to process.
     * @param listener The listener to use.
     * @throws IllegalArgumentException If the command is not a WhoCommand or WhoXCommand, or if the listener does not match the command type.
     */
    public void addQueryingCommand(EncapsulatedIRCCommand command, AbstractCommandQueryListener listener) {
        // check class types
        if((command.getCommand() instanceof IRCWhoCommand && !(listener instanceof WhoQueryListener)) ||
           (command .getCommand() instanceof IRCWhoXCommand && !(listener instanceof WhoXQueryListener)) ||
           !(command.getCommand() instanceof IRCWhoCommand || command.getCommand() instanceof IRCWhoXCommand)) {
            throw new IllegalArgumentException("Command type is wrong, or listener does not match command type.");
        }
        
        // make new session
        WhoSession session = new WhoSession();
        // make our local listener for the who command to track its progress through the outbound system
        WhoCommandListener commandListener = new WhoCommandListener(session);
        // assign the who-reply listener to the session
        session.whoListener = (AbstractWhoListener) listener;
        // assign our local command watcher to the session
        session.commandListener = commandListener;
        // store the encapsulated command
        session.command = command;
        
        if(listener instanceof WhoXQueryListener) {
            // make sure to pass options to the listener
            ((WhoXQueryListener) listener).setSearchOptions(((IRCWhoXCommand) command.getCommand()).getOptions());
        }
        
        // add our local command watcher to the command
        command.addListener(commandListener);
        
        // add our session to the wait queue until it pops out from the outbound system
        waitingQueries.add(session);
    }
    
    private class WhoSession {
        public AbstractWhoListener whoListener;
        public WhoCommandListener commandListener;
        public EncapsulatedIRCCommand command;
    }
    
    private class WhoCommandListener extends OutboundCommandEventListener {
        public WhoSession session;
        
        public WhoCommandListener(WhoSession session) {
            this.session = session;
        }
        
        public void onCommandTypeChange() {
            onDrop();
        }
        
        public void onCommandReferenceChange() {
            // let command type change handle it
        }    
        
        public void onDrop() {
            // remove from wait list and mark as canceled (premature removal)
            session.whoListener.onCancel();
            waitingQueries.remove(session);
        }
        
        public void onSent() {
            // remove from wait list
            waitingQueries.remove(session);
            
            // add to the end of the expecting queue
            liveQueries.add(session);
        }
    }
}
