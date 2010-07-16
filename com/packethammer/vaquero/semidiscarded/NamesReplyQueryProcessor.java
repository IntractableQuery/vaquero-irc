/**
 * This processes NAMES replies.
 */

package com.packethammer.vaquero.semidiscarded;

import com.packethammer.vaquero.advanced.dispatcher.querying.AbstractCommandQueryListener;
import com.packethammer.vaquero.advanced.dispatcher.querying.AbstractQueryProcessor;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import com.packethammer.vaquero.outbound.commands.channel.IRCNamesCommand;
import com.packethammer.vaquero.outbound.outboundprocessing.EncapsulatedIRCCommand;
import com.packethammer.vaquero.outbound.outboundprocessing.OutboundCommandEventListener;
import com.packethammer.vaquero.parser.IRCEventListener;
import com.packethammer.vaquero.parser.IRCParser;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.server.numeric.reply.EndOfNamesReply;
import com.packethammer.vaquero.parser.events.server.numeric.reply.NamesReply;

public class NamesReplyQueryProcessor extends AbstractQueryProcessor {
    private IRCParser parser;
    private LinkedBlockingQueue<NamesSession> liveQueries;
    private ArrayList<NamesSession> waitingQueries;
    
    /**
     * Initializes this query processor with essential information. 
     */
    public NamesReplyQueryProcessor(IRCParser parser) {
        this.parser = parser;
        this.liveQueries = new LinkedBlockingQueue();
        this.waitingQueries = new ArrayList();
        
        hookEvents();
    }
    
    private void hookEvents() {
        parser.getEventDistributor().addHardEventListener(NamesReply.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                NamesReply n = (NamesReply) e;
                
                liveQueries.peek().namesListener.addNamesReply(n, parser.getServerContext().getISupport(), !liveQueries.peek().dummy);
            }
        });
        
        parser.getEventDistributor().addHardEventListener(EndOfNamesReply.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                EndOfNamesReply end = (EndOfNamesReply) e;
                liveQueries.peek().namesListener.onFinished(end);
                try {
                    
                    // remove listener
                    liveQueries.take();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
        
    /**
     * Adds a NAMES query command and listener.
     *
     * @param command The NAMES command to process.
     * @param listener The listener to use.
     * @throws IllegalArgumentException If the command is not a NAMES query.
     */
    public void addQueryingCommand(EncapsulatedIRCCommand command, AbstractCommandQueryListener listener) {
        // check class types
        if(!(command.getCommand() instanceof IRCNamesCommand) || !(listener instanceof NamesQueryListener))
            throw new IllegalArgumentException("Command or listener is not of NAMES type.");
        
        // make new session
        NamesSession session = new NamesSession();
        // make our local listener for the NAMES command to track its progress through the outbound system
        NamesCommandListener commandListener = new NamesCommandListener(session);
        // assign the reply listener to the session
        session.namesListener = (NamesQueryListener) listener;
        // assign our local command watcher to the session
        session.commandListener = commandListener;

        // add our local command watcher to the command
        command.addListener(commandListener);
        
        // add our session to the wait queue until it pops out from the outbound system
        waitingQueries.add(session);
    }    
    
    private class NamesSession {
        public NamesQueryListener namesListener;
        public NamesCommandListener commandListener;
        public boolean dummy; // set to true to lower resource usage for dummy listeners
    }
    
    private class NamesCommandListener extends OutboundCommandEventListener {
        public NamesSession session;
        
        public NamesCommandListener(NamesSession session) {
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
            session.namesListener.onCancel();
            waitingQueries.remove(session);
        }
        
        public void onSent() {
            // remove from wait list
            waitingQueries.remove(session);
            try {
                
                // add to the end of the expecting queue
                liveQueries.put(session);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
