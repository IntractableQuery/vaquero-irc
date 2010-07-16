/**
 * The dispatcher is an advanced outbound command
 * manager. It allows normal command sending, but can also perform client-side
 * operations to correct for outbound queue latency and can allow you to perform
 * true queries on a per-command basis (such as for WHO).
 */

package com.packethammer.vaquero.advanced.dispatcher;

import com.packethammer.vaquero.advanced.dispatcher.querying.WhoQueryListener;
import com.packethammer.vaquero.advanced.dispatcher.querying.WhoReplyQueryProcessor;
import com.packethammer.vaquero.advanced.dispatcher.querying.WhoXQueryListener;
import com.packethammer.vaquero.outbound.CommandManager;
import com.packethammer.vaquero.outbound.OutboundRawIRCLineSenderI;
import com.packethammer.vaquero.outbound.commands.IRCCommand;
import com.packethammer.vaquero.outbound.commands.basic.IRCRawCommand;
import com.packethammer.vaquero.outbound.commands.basic.IRCWhoCommand;
import com.packethammer.vaquero.outbound.commands.extended.ircu.IRCWhoXCommand;
import com.packethammer.vaquero.outbound.outboundprocessing.EncapsulatedIRCCommand;
import com.packethammer.vaquero.outbound.outboundprocessing.TimingScheme;
import com.packethammer.vaquero.parser.IRCParser;

public class Dispatcher extends CommandManager {
    private IRCParser parser;
    private boolean queryingEnabled;
    private boolean rawCommandSendingEnabled;
    
    private WhoReplyQueryProcessor whoProcessor;

    /** 
     * Initializes the command manager with a timing scheme to use for command
     * dispatch from the wait queue and where to send raw lines.
     *
     * @param scheme The timing scheme to use.
     * @param rawLineSender The place to send our outbound raw lines.
     * @param parser The IRC parser to use.
     */
    public Dispatcher(TimingScheme scheme, OutboundRawIRCLineSenderI rawLineSender, IRCParser parser) {
        super(scheme, rawLineSender);
        this.parser = parser;
        this.whoProcessor = new WhoReplyQueryProcessor(parser);
        this.setQueryingEnabled(true);
        this.setRawCommandSendingEnabled(true);
    }

    public IRCParser getParser() {
        return parser;
    }
    
    /**
     * Sends an IRC command using the outbound system.
     *
     * @param command The IRCCommand to send.
     */
    public void sendCommand(IRCCommand command) {
        if(!this.isRawCommandSendingEnabled()) {
            if(command instanceof IRCRawCommand) {
                throw new IllegalStateException("You cannot send an IRCRawCommand when raw command sending is disabled!");
            }
        }
        
        // force it through as a query so that the applicable query system won't get desynchronized if we need to
        if(command instanceof IRCWhoCommand) {
            this.performWhoQuery((IRCWhoCommand) command, new WhoQueryListener() {});
        } else if(command instanceof IRCWhoXCommand) {
            this.performWhoXQuery((IRCWhoXCommand) command, new WhoXQueryListener() {});
        } else {
            // send regularly
            this.sendEncapsulatedCommand(new EncapsulatedIRCCommand(command));
        }
    }
    
    /**
     * Performs a regular WHO query and notifies the given listener of the
     * results.
     *
     * @param command The WHO command to use.
     * @param listener The query listener to use.
     * @throws IllegalStateException If querying is disabled.
     */
    public void performWhoQuery(IRCWhoCommand command, WhoQueryListener listener) {
        checkQuerying();
        EncapsulatedIRCCommand ec = new EncapsulatedIRCCommand(command);
        this.whoProcessor.addQueryingCommand(ec, listener);
        this.sendEncapsulatedCommand(ec);
    }
    
    /**
     * Performs an (ircu-specific) WHOX query and notifies the given listener 
     * of the results.
     *
     * DO NOT USE THIS ON NETWORKS THAT DO NOT SUPPORT WHOX. IT MAY DESYNCHRONIZE 
     * THE WHO QUERY SYSTEM SO THAT YOU CAN NO LONGER USE IT FOR THIS IRC
     * SESSION.
     *
     * @param command The WHOX command to use.
     * @param listener The query listener to use.
     * @throws IllegalStateException If querying is disabled.
     */
    public void performWhoXQuery(IRCWhoXCommand command, WhoXQueryListener listener) {
        checkQuerying();
        EncapsulatedIRCCommand ec = new EncapsulatedIRCCommand(command);
        this.whoProcessor.addQueryingCommand(ec, listener);
        this.sendEncapsulatedCommand(ec);
    }
    
    /*
     * Throws an exception if we aren't allowed to perform queries.
     */
    private void checkQuerying() {
        if(!this.isQueryingEnabled())
            throw new IllegalStateException("You cannot use querying methods when querying is disabled on the dispatcher!");
    }

    /**
     * Determines if querying is enabled.
     */
    public boolean isQueryingEnabled() {
        return queryingEnabled;
    }

    /**
     * Allows you to turn querying on or off. Setting this to false will cause
     * all query methods to throw IllegalStateExceptions if they are called.
     *
     * The primary use of this is for ensuring that queries are never sent when
     * there may also be instances of IRCRawCommand being sent (these have
     * the potential to bypass the internal checking mechanisms and ruin
     * the querying system for the entire IRC session).
     *
     * If you're concerned with the safety of the querying system (that is,
     * preventing it from becoming desynchronized), you might also wish
     * to see setRawCommandSendingEnabled() as an alternative to setting
     * this.
     *
     * Be aware that the Tracker may rely on querying being enabled for certain
     * types of options. Be prepared to disable these options if needed.
     *
     * @param queryingEnabled Set to true to allow querying, false otherise.
     * @see #setRawCommandSendingEnabled(boolean)
     */
    public void setQueryingEnabled(boolean queryingEnabled) {
        this.queryingEnabled = queryingEnabled;
    }

    /**
     * Determines if this dispatcher will allow you to send an IRCRawCommand.
     */
    public boolean isRawCommandSendingEnabled() {
        return rawCommandSendingEnabled;
    }

    /**
     * Used to allow or disallow the ability to send an IRCRawCommand through 
     * the dispatcher. 
     *
     * This is typically used to prevent raw commands from going out so that
     * it is impossible to accidently desynchronize the querying systems. 
     *
     * If you're concerned with the safety of the querying system (that is,
     * preventing it from becoming desynchronized), you might also wish
     * to see setQueryingEnabled() as an alternative to setting
     * this.
     *
     * @param rawCommandSendingEnabled True to allow raw commands to be sent, or false to force an IllegalStateException to be thrown when it is tried.
     */
    public void setRawCommandSendingEnabled(boolean rawCommandSendingEnabled) {
        this.rawCommandSendingEnabled = rawCommandSendingEnabled;
    }
}
