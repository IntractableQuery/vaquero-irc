/**
 * This defines a class that will allow Vaquero's internal tracking system to
 * modify the list of nicknames that are targeted by this command. Be aware that
 * the tracking system cannot arbitrarily add or remove any nicknames from the
 * original command, only modify them -- this can result in undefined/undesirable
 * behavior.
 */

package com.packethammer.vaquero.outbound.commands.interfaces;

import java.util.List;

public interface AdjustableNicknameTargetCommandI {
    /**
     * This method is only for adjusting nicknames by some of Vaquero's
     * internal systems. You should never need to use it -- use the command's
     * regular nickname setting method.
     *
     * @param targets A new list of nickname targets, no larger than those currently already in the command.
     */
    public void setNicknameTargets(List<String> targets);
}
