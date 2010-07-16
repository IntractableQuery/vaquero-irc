/**
 * The advanced client is based off the basic client, with the most important
 * difference being that this client uses the advanced user tracker. In 
 * addition, it uses the Dispatcher, an advanced command manager that makes
 * it possible to perform certain types of "true queries," where you 
 * make a request for some sort of information and receive the reply just
 * for that request by some means (usually, a listener class).
 * 
 * It goes without saying that the advanced client will eat up more resources,
 * but it gives you maximal control over what you can do with Vaquero. It's 
 * ideal for IRC bots, but may not be so ideal for end-user graphical
 * IRC clients, depending on the situation.
 * 
 * A word of warning to those of you who may send commands of type IRCRawCommand:
 * Consider this situation -- you use the dispatcher to ask for the reply to a
 * basic IRCWhoCommand. The dispatcher works behind the scenes to keep track
 * of all the current WHO queries being sent out so that it can match the replies
 * with the original command. You can break this process quite easily by sending
 * an IRCRawCommand with a WHO in it, since the dispatcher can only inspect the
 * command type, not the actual contents. So, in short, either don't use
 * the dispatcher's query methods, or don't use IRC raw commands when you
 * think it is possible they could interrupt the system. Note that the dispatcher
 * has a method to make it impossible to send raw commands.
 * 
 * A client session is a one-use-only class. If you need to reconnect to the
 * server, disconnect this client and create a new one.
 */

package com.packethammer.vaquero.client;

import com.packethammer.vaquero.advanced.dispatcher.Dispatcher;
import com.packethammer.vaquero.advanced.dispatcher.NicknameTargetFilter;
import com.packethammer.vaquero.advanced.tracker.Tracker;
import com.packethammer.vaquero.advanced.tracker.TrackerSettings;
import com.packethammer.vaquero.net.IRCConnector;
import com.packethammer.vaquero.outbound.CommandManager;
import com.packethammer.vaquero.outbound.OutboundRawIRCLineSenderI;
import com.packethammer.vaquero.outbound.outboundprocessing.TimingScheme;

public class AdvancedClient extends BasicClient {    
    private Tracker tracker;
    private TrackerSettings trackerSettings;
    private boolean adjustNicknameTargetsEnabled;
    
    /**
     * This initializes the client with some basic information and initializes
     * the parser so you can begin hooking IRC events before we begin conversing
     * with the remote IRC server.
     *
     * @param connector The IRC connector to use. Please initialize it before passing it here.
     * @param perceivedServerHost The remote IP or hostname of the server we're connected to. This is used by the parser to make educated guesses for events it generates later on. It is not used for an actual TCP connection.
     * @param perceivedServerPort The remote port of the server we're connected to. This is used by the parser. It is not part of a TCP connection.
     * @param trackerSettings The tracker settings to use. Set to null to use defaults.
     */
    public AdvancedClient(IRCConnector connector, String perceivedServerHost, int perceivedServerPort, TrackerSettings trackerSettings) {
        super(connector, perceivedServerHost, perceivedServerPort);
        this.trackerSettings = trackerSettings;
        if(this.trackerSettings == null)
            this.trackerSettings = new TrackerSettings();
    }
    
    public void initialize(ClientInformation clientInfo, TimingScheme outboundTimingScheme) {
        super.initialize(clientInfo, outboundTimingScheme);
    }
    
    protected void preinitializeBeforeLogin() {
        tracker = new Tracker(this.getIrcParser(), this.getDispatcher(), this.trackerSettings);
        if(this.isAdjustNicknameTargetsEnabled()) {
            this.getDispatcher().addPreReleaseFilter(new NicknameTargetFilter(tracker));
        }
    }
    
    protected CommandManager getNewFunctionalCommandManager(TimingScheme outboundTimingScheme, OutboundRawIRCLineSenderI lineSender) {
        return new Dispatcher(outboundTimingScheme, lineSender, this.getIrcParser());
    }
    
    /**
     * Returns the dispatcher in use (it is the same as the command manager).
     *
     * @return The advanced command manager (the dispatcher).
     */
    public Dispatcher getDispatcher() {
        // yeah... I'm not sure how else this can be handled anyway
        return (Dispatcher) this.getOutboundCommandManager();
    }
    
    /**
     * Returns the tracker in use.
     *
     * @return The advanced tracker.
     */
    public Tracker getTracker() {
        return this.tracker;
    }

    /**
     * Determines if we are auto-adjusting command nicknames automatically.
     */
    public boolean isAdjustNicknameTargetsEnabled() {
        return adjustNicknameTargetsEnabled;
    }

    /**
     * Determines if we should set up a NicknameTargetFilter in the pre-release
     * filter chain. This will automatically adjust the nicknames in commands 
     * that typically target one or more users, just as PRIVMSG, NOTICE, KICK, etc.
     *
     * It is desirable to turn this on if you find your outbound command queue
     * filling up with commands that don't reach people due to their nickname
     * changing after you sent the command.
     *
     * See NicknameTargetFilter to find out more information.
     *
     * @param adjustNicknameTargetsEnabled Set to true to adjust nicknames, false otherwise.
     */
    public void setAdjustNicknameTargetsEnabled(boolean adjustNicknameTargetsEnabled) {
        if(this.isInitialized())
            throw new IllegalStateException("Cannot set this property after the client is initialized.");
        this.adjustNicknameTargetsEnabled = adjustNicknameTargetsEnabled;
    }
}
