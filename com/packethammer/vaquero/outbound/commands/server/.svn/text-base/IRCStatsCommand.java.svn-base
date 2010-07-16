/*
 * This command queries for server statistics based on a flag provided. It is 
 * also possible to query a specific server by its name. This class defines the
 * flags as constants derived from RFC1459, although their behavior may differ
 * from what RFC1459 suggests on some servers. Also, some flags require that
 * you have server administrator status. 
 *
 * RFC1459 claims that omitting the target server should result in actual
 * usage of that command with the given flag being reported, but I've seen
 * no server exhibiting this behavior. Omitting the server parameter should
 * simply result in the STATS reply giving back the statistics for the 
 * server you are currently connected to.
 *
 * RFC2812 states that you may use wildcards in the target server mask.
 *
 * According to RFC2812, there are some base flags that an RFC2812-compliant
 * server should always support. These flags are located in this class as
 * constants. The RFC1459 flags are not guaranteed to exist on the server.
 *
 * By omitting a flag, you typically get back a response with all possible 
 * flags for usage.
 *
 * Some documentation in this class is derived directly from RFC1459 and RFC2812.
 *
 * STATS [<FLAG> [SERVER]]
 */

package com.packethammer.vaquero.outbound.commands.server;

import com.packethammer.vaquero.util.protocol.IRCRawLine;

public class IRCStatsCommand {
    // RFC 1459 flag definitions
    /** returns a list of servers which the server may connect to or allow connections from */
    public static final Character RFC1459_STATS_s = new Character('s');
    /** returns a list of servers which are either forced to be treated as leaves or allowed to act as hubs */
    public static final Character RFC1459_STATS_h = new Character('h');
    /** returns a list of hosts which the server allows a client to connect from */
    public static final Character RFC1459_STATS_i = new Character('i');
    /** returns a list of banned username/hostname combinations for that server */
    public static final Character RFC1459_STATS_k = new Character('k');
    /** returns a list of the server's connections, showing how long each connection has been established and the traffic over that connection in bytes and messages for each direction */
    public static final Character RFC1459_STATS_l = new Character('l');
    /** returns a list of commands supported by the server and the usage count for each if the usage count is non zero */
    public static final Character RFC1459_STATS_m = new Character('m');
    /** returns a list of hosts from which normal clients may become operators */
    public static final Character RFC1459_STATS_o = new Character('o');
    /** show Y (Class) lines from server's configuration file */
    public static final Character RFC1459_STATS_y = new Character('y');
    /** returns a string showing how long the server has been up */
    public static final Character RFC1459_STATS_u = new Character('u');
    
    // RFC2812 flag definitions
    /** returns a list of the server's connections, showing how long each connection has been established and the traffic over that connection in Kbytes and messages for each direction */
    public static final Character RFC2812_STATS_l = new Character('l');
    /** returns the usage count for each of commands supported by the server; commands for which the usage count is zero MAY be omitted */
    public static final Character RFC2812_STATS_m = new Character('m');
    /** returns a list of configured privileged users, operators */
    public static final Character RFC2812_STATS_o = new Character('o');
    /** returns a string showing how long the server has been up */
    public static final Character RFC2812_STATS_u = new Character('u');
    
    private Character flag;
    private String targetServer;
    
    /**
     * Initializes this STATS command with the flag that indicates what sort of
     * statistics we want and the server name to query for the statistics.
     *
     * @param flag The single-character flag that indicates the stats we want.
     * @param targetServer The server to target for the request, or null to omit it.
     */
    public IRCStatsCommand(Character flag, String targetServer) {
        this();
        this.setFlag(flag);
        this.setTargetServer(targetServer);
    }
    
    /**
     * Initializes this STATS command with the flag that indicates what sort of
     * statistics we want.
     *
     * @param flag The single-character flag that indicates the stats we want.
     */
    public IRCStatsCommand(Character flag) {
        this(flag, null);
    }
    
    /**
     * Initializes this STATS command as a simple parameterless request which
     * typically will result in the server responding with the available 
     * STATS flags.
     */
    public IRCStatsCommand() {
    }
    
    /**
     * Returns the flag we are using with the STATS command, which indicates 
     * what sort of statistics we want. May return null if we are simply
     * trying to get the supported flags list.
     */
    public Character getFlag() {
        return flag;
    }
    
    /**
     * Sets the STATS flag, which indicates what sort of statistics we want.
     * Set to null if we are just trying to get the supported flags list.
     */
    public void setFlag(Character flag) {
        this.flag = flag;
    }
    
    /** 
     * Returns the server we are trying to target this request at. Returns null 
     * if we are not targeting a specific server.
     *
     * @return A server name.
     */
    public String getTargetServer() {
        return targetServer;
    }
    
    /** 
     * Sets the name of the server to query. This is optional and may be set to
     * null.
     */
    public void setTargetServer(String server) {
        this.targetServer = server;
    }
    
    public IRCRawLine renderForIRC() {
        return IRCRawLine.buildRawLine(false, "STATS", (this.getFlag() != null ? this.getFlag().toString() : null), this.getTargetServer());
    }    
   
    public boolean isSendable() {
        if(this.getTargetServer() != null)
            return this.getFlag() != null;
        else
            return true;
    }
}
