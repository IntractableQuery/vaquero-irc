/*
 * This class is meant for use in maintaining information on a server that the
 * parser is receiving information from.
 */

package com.packethammer.vaquero.parser.tracking;

import com.packethammer.vaquero.parser.tracking.definitions.ChannelTypeDefinition;
import java.util.HashSet;
import java.util.Set;
import com.packethammer.vaquero.util.CasemappedString;
import com.packethammer.vaquero.util.Hostmask;
import com.packethammer.vaquero.parser.tracking.definitions.ModeDefinition;
import com.packethammer.vaquero.util.MaskMatcher;

public class IRCServerContext {
    private String serverPhysicalAddress;
    private int serverPhysicalPort;
    private IRCServerISupport iSupport;
    private Hostmask me;
    private String serverName;
    private boolean fullyConnected;
    private HashSet<ModeDefinition> userModeDefinitions;
    private HashSet<String> channels;

    public IRCServerContext() {
         this.iSupport = new IRCServerISupport();
         this.me = new Hostmask(null, null, null);
         this.userModeDefinitions = new HashSet();
         this.channels = new HashSet();
    }
    
    /**
     * Returns the IP/hostname used for connecting to the server.
     */
    public String getServerPhysicalAddress() {
        return serverPhysicalAddress;
    }

    /**
     * Sets the IP/hostname used for connecting to the server.
     */
    public void setServerPhysicalAddress(String serverPhysicalAddress) {
        this.serverPhysicalAddress = serverPhysicalAddress;
    }

    /**
     * Returns the port used for connecting to the server.
     */
    public int getServerPhysicalPort() {
        return serverPhysicalPort;
    }

    /**
     * Sets the port used for connecting to the server.
     */
    public void setServerPhysicalPort(int serverPhysicalPort) {
        this.serverPhysicalPort = serverPhysicalPort;
    }
    
    /**
     * Returns our known hostmask, which obviously contains our nickname, ident,
     * and hostname. Of course, some or all of these properties may be null 
     * if they are unknown.
     *
     * @return Your own hostmask, which at the very least should contain your known nickname, assuming you are connected to the server.
     */
    public Hostmask getMe() {
        return me;
    }
    
    /**
     * Sets our current internally tracked nickname.
     */
    public void setMyTrackedNickname(String nick) {
        this.getMe().setNickname(nick);
    }
    
    /**
     * Sets our current internally tracked hostmask.
     */
    public void setMyTrackedHostmask(Hostmask host) {
        this.me = host;
    }
    
    /**
     * Returns an encapsulated casemapped string based on the current casemapping
     * known or guessed that the server supports.
     *
     * @param string The string to return encapsulated in a casemapped string instance.
     * @return The casemapped string.
     */
    public CasemappedString casemapString(String string) {
        return new CasemappedString(string, this.getISupport().getCasemappingDefinition());
    }
    
    /**
     * Returns a compiled mask matcher using a match mask string. This will
     * automatically assign the current known IRC casemapping to the
     * mask matcher, making it ideal for comparing bans, etc. just as the
     * server might compare them.
     *
     * @param mask The mask to use.
     * @return The compiled mask matcher that can now be used against strings.
     */
    public MaskMatcher createMaskMatcher(String mask) {
        return new MaskMatcher(mask, this.getISupport().getCasemappingDefinition());
    }
    
    /**
     * Returns the server support expressed by numeric 005 ISUPPORT. Note that
     * since this support is so vital to the client's parsing abilities, it
     * contains methods which may return defaulted information that is not
     * truly reflective of what ISUPPORT says. It is entirely possible to never
     * receive the ISUPPORT numeric and still be able to get basic information 
     * like modes and channel types from here. Please read the documentation
     * closely to be sure in all cases.
     *
     * @return The ISUPPORT information provided by the server or a default implementation of it.
     */
    public IRCServerISupport getISupport() {
        return iSupport;
    }

    /**
     * Returns the name that the server appears to have. This is typically
     * a fully qualified domain name, but it can also be more vanity-oriented.
     * Returns null if the server name is unknown.
     *
     * @return The server name, or null if it is unknown.
     */
    public String getServerName() {
        return serverName;
    }
    
    /**
     * Sets the server name that this server gives itself.
     *
     * @param name The server's name.
     */
    public void setServerName(String name) {
        this.serverName = name;
    }

    /**
     * Determines if we know the server's name yet.
     *
     * @return True if the server name is known, false otherwise.
     */
    public boolean isServerNameKnown() {
        return this.getServerName() != null;
    }
    
    /**
     * Determines if a given nickname is us, using the server's casemapping.
     *
     * @param nickname The nickname to test.
     * @return True if a nickname is us, false otherwise.
     */
    public boolean isMe(String nickname) {
        if(nickname != null) {
            return this.getISupport().getCasemappingDefinition().areStringsEqual(this.getMe().getNonNullNickname(), nickname);
        } else {
            return false;
        }
    }
    
    /**
     * Determines if a given string is a valid channel name, using the server's
     * expressed (or implied) channel types.
     *
     * @param channel The string to test.
     * @return True if the provided string is a valid channel name, false otherwise.
     */
    public boolean isChannel(String channel) {
        if(channel != null && channel.length() > 0) {
            ChannelTypeDefinition type = this.getISupport().getChannelTypeByPrefix(new Character(channel.charAt(0)));
            
            return type != null;
        } else {
            return false;
        }
    }
    
    /**
     * Adds a known usermode expressed by the server.
     */
    public void addKnownUserMode(char mode) {
        ModeDefinition def = new ModeDefinition(mode, false, true, false, false); // we assume all usermodes have no params
        userModeDefinitions.add(def);
    }
    
    /**
     * Returns a set of all known usermodes expressed by the server. The set
     * may be empty if none are known.
     */
    public Set<ModeDefinition> getKnownUserModes() {
        return this.userModeDefinitions;
    }

    /**
     * This determines if we are "fully connected" to the network. That is, if
     * we have received our nickname from the server and such (typically occurs
     * when we get 001-005 numerics).
     *
     * @return True if fully connected, false otherwise.
     */
    public boolean isFullyConnected() {
        return fullyConnected;
    }

    /**
     * Sets our connection status.
     *
     * @param fullyConnected True if fully connected, false otherwise.
     * @see #isFullyConnected()
     */
    public void setFullyConnected(boolean fullyConnected) {
        this.fullyConnected = fullyConnected;
    }
    
    public String toString() {
        String ret = "";
        ret += "Available user modes: " + this.getKnownUserModes() + "\n";
        ret += "My hostmask: " + this.getMe() + "\n";
        ret += "My channels: " + this.getChannels() + "\n";
        ret += "Server name: " + this.getServerName() + "\n";
        ret += "Initial physical information: " + this.getServerPhysicalAddress() + " (port " + this.getServerPhysicalPort() + ")" + "\n";
        ret += "* ISUPPORT:\n" + this.getISupport();
        return ret;
    }

    /**
     * Returns the internal listing of channels we are currently in. These 
     * channels have been set to lowercase to minimize retrieval/access 
     * difficulties.
     *
     * @return Channels we are currently in.
     */
    public HashSet<String> getChannels() {
        return channels;
    }
    
    /**
     * Determines if we are in a given channel.
     *
     * @param channel The full channel name to determine if we are in.
     * @return True if we are in the channel, false otherwise.
     */
    public boolean inChannel(String channel) {
        return channels.contains(channel.toLowerCase());
    }
}
