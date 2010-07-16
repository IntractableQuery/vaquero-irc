/**
 * This sends a DCC file send request to a single user's nickname.
 */

package com.packethammer.vaquero.outbound.commands.basic;

import java.net.InetAddress;

public class IRCDCCSendCommand extends IRCCTCPNickCommand {    
    /**
     * Instantiates the DCC SEND command with the nickname to send the request
     * to, the file info that will be sent to the user, and the address/port we want
     * the user to connect to for retrieving the file.
     *
     * @param nickname The person to DCC.
     * @param filename The name of the file being sent.
     * @param fileSize The size of the file being sent.
     * @param myAddress Our own ipv4 address for the remote host to connect to.
     * @param myPort The port we've opened on the given address for the remote host to connect to.
     */
    public IRCDCCSendCommand(String nickname, String filename, long fileSize, InetAddress myAddress, int myPort) {
        byte ip[] = myAddress.getAddress();
        long unsignedInt32 = (((long) ((ip[0] & 0xff) << 24
                | (ip[1] & 0xff) << 16
                | (ip[2] & 0xff) << 8
                | (ip[3] & 0xff)))
               & 0xFFFFFFFFL);
        this.addNick(nickname);
        this.setCTCPMessage("DCC SEND " + filename + " " + unsignedInt32 + " " + myPort + " " + fileSize);
    }
}
