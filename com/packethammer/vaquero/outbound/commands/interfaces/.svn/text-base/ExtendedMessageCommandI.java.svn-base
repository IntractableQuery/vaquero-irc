/*
 * Defines a command which contains a message that is somehow displayed to other
 * clients via the server. Examples of this sort of command include TOPIC, 
 * NOTICE, PRIVMSG, PART (with a parting message), QUIT, etc.
 */

package com.packethammer.vaquero.outbound.commands.interfaces;

public interface ExtendedMessageCommandI {
    /**
     * Returns the message which has the potential to reach other clients.
     * This can return null if no extended message is present.
     */
    public String getMessage();
    
    /**
     * Sets the message which other clients may see.
     */
    public void setMessage(String message);
}
