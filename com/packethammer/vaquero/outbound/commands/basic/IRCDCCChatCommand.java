/**
 * This sends a DCC CHAT request to a user.
 */

package com.packethammer.vaquero.outbound.commands.basic;

import java.net.InetAddress;

public class IRCDCCChatCommand extends IRCCTCPNickCommand {
    /**
     * Instantiates the DCC CHAT command with the nickname to send the request
     * to and the address/port we want the user to connect to for chatting with
     * us.
     *
     * @param nickname The person to DCC.
     * @param myAddress Our own ipv4 address for the remote host to connect to.
     * @param myPort The port we've opened on the given address for the remote host to connect to.
     */
    public IRCDCCChatCommand(String nickname, InetAddress myAddress, int myPort) {
        byte ip[] = myAddress.getAddress();
        long unsignedInt32 = (((long) ((ip[0] & 0xff) << 24
                | (ip[1] & 0xff) << 16
                | (ip[2] & 0xff) << 8
                | (ip[3] & 0xff)))
               & 0xFFFFFFFFL);
        this.addNick(nickname);
        this.setCTCPMessage("DCC CHAT chat " + unsignedInt32 + " " + myPort);
    }
}
