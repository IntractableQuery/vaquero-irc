/**
 * Temporary class.
 */

package com.packethammer.vaquero.testing;

import com.packethammer.vaquero.outbound.CommandManager;
import com.packethammer.vaquero.outbound.OutboundRawIRCLineSenderI;
import com.packethammer.vaquero.outbound.commands.basic.IRCNoticeNickCommand;
import com.packethammer.vaquero.outbound.outboundprocessing.BasicThrottleTimingScheme;
import com.packethammer.vaquero.outbound.outboundprocessing.CommandFilterI;
import com.packethammer.vaquero.outbound.outboundprocessing.EncapsulatedIRCCommand;

/**
 *
 * @author iron
 */
public class CommandWaitQueueTester implements OutboundRawIRCLineSenderI {
    CommandManager m;
     
    public static void main(String[] args) {
        new CommandWaitQueueTester();
    }
    
    public CommandWaitQueueTester() {
        m = new CommandManager(new BasicThrottleTimingScheme(1000), this);
        
        /*
        m.addPreReleaseFilter(new CommandFilterI() {
            public EncapsulatedIRCCommand filterCommand(EncapsulatedIRCCommand command) {
                if(command.getCommand() instanceof IRCNoticeNickCommand) {
                    IRCNoticeNickCommand notice = (IRCNoticeNickCommand) command.getCommand();
                    if(notice.getNicknames().contains("Jim3")) {
                        System.out.println("killing teh jim");
                        return null;
                    }
                }
                
                return command;
            }
        });
         */

        
        m.sendCommand(new IRCNoticeNickCommand("Jim", "Hello there"));
        m.sendCommand(new IRCNoticeNickCommand("Jim2", "two"));
        m.sendCommand(new IRCNoticeNickCommand("Jim3", "..three"));
        m.sendCommand(new IRCNoticeNickCommand("Jim4", "yeh four"));
         
    }
    
    public void sendRawLine(String line) {
        System.out.println("SEND RAW:" + line);
    }
}
