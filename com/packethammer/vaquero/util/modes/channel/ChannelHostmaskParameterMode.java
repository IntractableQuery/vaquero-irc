/*
 * Defines a channel mode which is ALWAYS accompanied by a hostmask. This
 * is most obviously going to be the +b mode according to the basic RFC, 
 * although other custom modes may exist.
 */

package com.packethammer.vaquero.util.modes.channel;

import com.packethammer.vaquero.util.Hostmask;

public class ChannelHostmaskParameterMode extends ChannelMode {
    private Hostmask hostmask;
    
    public ChannelHostmaskParameterMode(char mode, Hostmask hostmask, boolean beingAdded) {
        super(mode, null, beingAdded);
        this.setHostmask(hostmask);
    }

    public Hostmask getHostmask() {
        return hostmask;
    }

    public void setHostmask(Hostmask hostmask) {
        this.hostmask = hostmask;
        super.setParameter(hostmask.getFullHostmask());
    }
    
    /**
     * Sets this mode's parameter.
     *
     * @param parameter The mode's parameter.
     * @param casemappingConstant The server's casemapping constant, defined in CasemappedString
     */
    public void setParameter(String parameter) {
        this.setHostmask(Hostmask.parseHostmask(parameter));
    }
}
