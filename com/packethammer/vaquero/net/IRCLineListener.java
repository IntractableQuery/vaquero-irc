/**
 * This defines a class that listens for lines of data from an IRC connector.
 */

package com.packethammer.vaquero.net;

public interface IRCLineListener {
    /**
     * Occurs every time we get a raw line of data from the remote server.
     *
     * @param line The raw line of data from the server.
     */
    public void onLine(String line);
}
