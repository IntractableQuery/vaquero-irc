/**
 * Temporary class.
 */

package com.packethammer.vaquero.testing;

import java.io.BufferedReader;
import java.io.FileReader;
import com.packethammer.vaquero.parser.IRCEventListener;
import com.packethammer.vaquero.semidiscarded.IRCEventListenerOld;
import com.packethammer.vaquero.parser.IRCParser;
import com.packethammer.vaquero.parser.events.IRCEvent;
import com.packethammer.vaquero.parser.events.server.numeric.reply.NamesReply;

/**
 *
 * @author iron
 */
public class ParserTest extends IRCEventListenerOld {
    IRCParser p;
    
    /** Creates a new instance of ParserTest */
    public ParserTest() throws Exception {
        p = new IRCParser("the.server.i.connected.to.com", 6667);
  
        p.getEventDistributor().addDynamicEventListener(IRCEvent.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                System.out.println(e.getClass().getSimpleName() + " -> " + e);
            }
        });
        
        p.getEventDistributor().addDynamicEventListener(NamesReply.class, new IRCEventListener() {
            public void onEvent(IRCEvent e) {
                NamesReply n = (NamesReply) e;
                System.out.println(">>>> " + n.getNicknames(p.getServerContext().getISupport()));
            }
        });
        
        // read in irc data now
        BufferedReader in = new BufferedReader(new FileReader("irctest.txt"));
        //BufferedReader in = new BufferedReader(new FileReader("who-test.txt"));
        String line = null;
        while((line = in.readLine()) != null) {
            if(line.startsWith("<- ")) {
                p.parseLine(line.substring(3));
            }
        }
        
        System.out.println("********** TRACKER *************");
        System.out.println(p.getServerContext());
    }
    
    public static void main(String[] args) throws Exception {
        new ParserTest();
    }
    
}
