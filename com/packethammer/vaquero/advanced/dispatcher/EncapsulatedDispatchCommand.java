/**
 * This is a special encapsulation class for the dispatcher to use.
 */

package com.packethammer.vaquero.advanced.dispatcher;

import com.packethammer.vaquero.advanced.dispatcher.querying.AbstractCommandQueryListener;
import com.packethammer.vaquero.advanced.dispatcher.querying.AbstractQueryProcessor;
import com.packethammer.vaquero.outbound.commands.IRCCommand;
import com.packethammer.vaquero.outbound.outboundprocessing.EncapsulatedIRCCommand;

public class EncapsulatedDispatchCommand extends EncapsulatedIRCCommand {
    private AbstractCommandQueryListener commandQueryListener;
    private AbstractQueryProcessor commandQueryProcessor;
    
    /**
     * Initializes with the IRC command to be sent.
     *
     * @param command The IRC command. 
     */
    public EncapsulatedDispatchCommand(IRCCommand command) {
        super(command);
    }
    
    public AbstractCommandQueryListener getCommandQueryListener() {
        return commandQueryListener;
    }

    public void setCommandQueryListener(AbstractCommandQueryListener commandQueryListener) {
        this.commandQueryListener = commandQueryListener;
    }

    public AbstractQueryProcessor getCommandQueryProcessor() {
        return commandQueryProcessor;
    }

    public void setCommandQueryProcessor(AbstractQueryProcessor commandQueryProcessor) {
        this.commandQueryProcessor = commandQueryProcessor;
    }    
}
