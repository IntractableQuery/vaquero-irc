/**
 * RPL_CHANNELMODEIS
 * "<channel> <mode(s) params...>"
 *
 * This is a reply we typically receive when we perform "MODE #chan" with no
 * arguments (it is a mode request).
 *
 * This is one of the few events that requires the parser to perform an
 * operation on it before one of its fields can be accessed.
 */

package com.packethammer.vaquero.parser.events.server.numeric.reply;

import com.packethammer.vaquero.parser.events.server.numeric.IRCNumericEvent;
import com.packethammer.vaquero.util.modes.Modes;
import com.packethammer.vaquero.util.modes.channel.ChannelMode;

public class ChannelModeReply extends IRCNumericEvent {
    private Modes<ChannelMode> modes;
    
    /** Creates a new instance of ChannelModeReply */
    public ChannelModeReply() {
    }
    
    /**
     * This returns a raw string containing the modes set on the channel.
     */
    public String getRawModesString() {
        StringBuilder args = new StringBuilder();
        
        for(int x = 1; x < this.numericArgumentCount(); x++) {
            args.append(this.getNumericArg(x));
            
            if( x < this.numericArgumentCount() - 1)
                args.append(' ');
        }
        
        return args.toString();
    }
    
    /**
     * Returns the channel name these modes are for.
     */
    public String getChannel() {
        return this.getNumericArg(0);
    }
    
    /**
     * Returns the modes set for the given channel.
     */
    public Modes<ChannelMode> getModes() {
        return modes;
    }

    public void setModes(Modes<ChannelMode> modes) {
        this.modes = modes;
    }
    
    public boolean validate() {
        if(this.numericArgumentCount() >= 1) {
            // pre-parse modes
            modes = this.getParser().parseChannelModes(this.getRawModesString());
            
            return true;
        } else {
            return false;
        }
    }
    
    public int getHandledNumeric() {
        return this.RPL_CHANNELMODEIS;
    }
    
    public String toString() {
        return super.toString() + ", CHAN:" + this.getChannel() + ", MODES:" + this.getModes();
    }

}
