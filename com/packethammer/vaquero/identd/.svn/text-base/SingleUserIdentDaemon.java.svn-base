/**
 * This is a simplistic single-user ident daemon. It always replies with the
 * username you specify.
 */

package com.packethammer.vaquero.identd;

import java.io.IOException;
import java.net.Socket;

public class SingleUserIdentDaemon extends AbstractIdentDaemon {
    private String user;
    
    public SingleUserIdentDaemon() throws IOException {
        super();
    }

    /**
     * Returns the username that this ident server always replies with.
     *
     * @return A username.
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the username that this ident server always replies with.
     *
     * @param user The new username.
     */
    public void setUser(String user) {
        this.user = user;
    }
    
    public synchronized AbstractIdentReply replyToQuery(Socket client, int ourPort, int theirPort) {
        return new UserReply(ourPort, theirPort, this.getUser());
    }
}
