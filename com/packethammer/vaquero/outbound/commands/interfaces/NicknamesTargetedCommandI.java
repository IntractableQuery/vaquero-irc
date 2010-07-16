/*
 * Defines a command that targets one or more nicknames directly. 
 */

package com.packethammer.vaquero.outbound.commands.interfaces;

import java.util.Collection;

public interface NicknamesTargetedCommandI {
    /**
     * Returns the nicknames being targeted by this command. Guaranteed to not
     * be null, but may be empty (indicating none were targeted).
     *
     * @return Nicknames being targeted.
     */
    public Collection<String> getNicknames();
}