/**
 * This filter uses the advanced tracker to adjust people's nicknames. It
 * is meant to be used in the pre-release filtering. For example, if we are
 * messaging a tracked user named jim, but the actual message sits in our
 * outbound queue for long enough that jim changes his name to john, it will
 * automatically be adjusted by this optimizer so that it will reach jim's
 * new name, john.
 *
 * Note that only commands implementing both AdjustableNicknameTargetCommandI
 * and NicknamesTargetedCommandI will be optimized.
 */

package com.packethammer.vaquero.advanced.dispatcher;

import com.packethammer.vaquero.advanced.tracker.NicknameHistory;
import com.packethammer.vaquero.advanced.tracker.TrackedUser;
import com.packethammer.vaquero.advanced.tracker.Tracker;
import com.packethammer.vaquero.outbound.commands.IRCCommand;
import com.packethammer.vaquero.outbound.commands.interfaces.AdjustableNicknameTargetCommandI;
import com.packethammer.vaquero.outbound.commands.interfaces.NicknamesTargetedCommandI;
import com.packethammer.vaquero.outbound.outboundprocessing.CommandFilterI;
import com.packethammer.vaquero.outbound.outboundprocessing.EncapsulatedIRCCommand;
import java.util.ArrayList;
import java.util.List;

// Yes, this is a hacky way to do this, but it's the only way to avoid writing
// complementing classes to the existing IRC commands (no thanks!)
public class NicknameTargetFilter implements CommandFilterI {
    private Tracker tracker;
    
    /**
     * Initializes the filter with the advanced tracker to use. Note that if
     * the tracker isn't configured to track nicknames far back enough, that
     * the nickname target filter will be ineffective. Make sure the tracker
     * is at least tracking nicknames for about as long as you think a command
     * could get stuck in our outbound queue.
     *
     * @param tracker The advanced tracker to use. 
     */
    public NicknameTargetFilter(Tracker tracker) {
        this.tracker = tracker;
    }
    
    public void filterCommand(EncapsulatedIRCCommand command) {
        IRCCommand cmd = command.getCommand();
        if(cmd instanceof AdjustableNicknameTargetCommandI && cmd instanceof NicknamesTargetedCommandI) {
            NicknamesTargetedCommandI nickGetter = (NicknamesTargetedCommandI) cmd;
            AdjustableNicknameTargetCommandI nickSetter = (AdjustableNicknameTargetCommandI) cmd;
            
            // build a new list of nicknames
            List<String> newNicks = new ArrayList();
            for(String curNick : nickGetter.getNicknames()) {
                // look it up in the tracker
                NicknameHistory history = tracker.getNicknameHistoryAtTime(curNick, command.getCreationTime());
                if(history != null) {
                    // we have history, so just get that user's latest nickname known
                    TrackedUser user = history.getUser();
                    newNicks.add(user.getHostmask().getNickname());
                } else {
                    // leave the original intact
                    newNicks.add(curNick);
                }
            }
            
            // set the new nickname(s)
            nickSetter.setNicknameTargets(newNicks);
        }
    } 
}
