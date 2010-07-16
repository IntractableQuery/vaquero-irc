/*
 * The usage of this command is officially documented in the ircu docs as 
 * 'readme.who'. I believe that its usage has changed over ircu versions at 
 * least once. At the time of writing this class, my copy of "readme.who" 
 * came with ircu2.10.12.10, so I'm basing support off that.
 *
 * Note that this class tries to contain information for using all the 
 * features possible in this command. Ultimately, implementing servers may
 * just drop or add support for new flags, etc. For this reason, you should
 * be prepared for some flags to not work correctly or at all on some 
 * servers. Hopefully the remote server will put dummy info in place of some
 * flags that it doesn't want to reply with data for, or we can end up never
 * getting reply events since they can't be parsed. If you run into problems
 * with the server replying, but Vaquero not picking it up, you will have
 * to identify exactly what it is the server does not support (typically 
 * a field flag or the querytype parameter) and stop using it.
 * 
 * This is not a new command unto itself, it just tacks on extra parameters and
 * flag information to a regular WHO query. However, it does result in its own
 * unique numeric reply! 
 *
 * Usage:
 * WHO <mask1> [<options> [<mask2>]]
 *
 * @see vaquero.parser.events.server.numerics.reply.WhoXReply
 */

package com.packethammer.vaquero.outbound.commands.extended.ircu;

import com.packethammer.vaquero.outbound.commands.IRCCommand;
import com.packethammer.vaquero.parser.StringOperations;
import com.packethammer.vaquero.util.protocol.IRCRawLine;

public class IRCWhoXCommand extends IRCCommand {
    private String searchMask;
    private WhoXSearchOptions options;
    
    /**
     * Initializes this WHOX command with the mask to search with and 
     * default options (that is, no options will be specified). Note that
     * if you leave the search options without any field flags that you
     * will get a normal WHO reply back rather than the special WHOX one.
     *
     * @param mask The search mask to use.
     */
    public IRCWhoXCommand(String mask) {
        this(mask, new WhoXSearchOptions());
    }
    
    /**
     * Initializes this WHOX command with the mask to search with and options
     * to use in the search.
     *
     * @param mask The search mask to use.
     * @param options The options to use.
     */
    public IRCWhoXCommand(String mask, WhoXSearchOptions options) {
        this.searchMask = mask;
        this.options = options;
    }
    
    /**
     * Sets the search mask to use. It can be affected by the flags in the
     * search options. You can normally use * and ? wildcards in your mask,
     * unless you are specifying some mask type such as an IP in the
     * options.
     *
     * You can also use commas in the mask to search for specific nicknames
     * or users in specific channels. Note that this will remove your
     * ability to use wildcards. Example: 'john,#somechannel,jill'
     *
     * @param mask The mask to use.
     */
    public void setSearchMask(String mask) {
        this.searchMask = mask;
    }
    
    /**
     * Returns the search mask being used for this query.
     */
    public String getSearchMask() {
        return searchMask;
    }

    /**
     * Returns the options being used for this query.
     */ 
    public WhoXSearchOptions getOptions() {
        return options;
    }

    /** 
     * Sets the options to be used for this query.
     */
    public void setOptions(WhoXSearchOptions options) {
        this.options = options;
    }
    
    public IRCRawLine renderForIRC() {        
        // first param is just 0 since it isn't even used. the last param is extended since WHOX is nice and lets us search for realnames using spaces.
        return IRCRawLine.buildRawLine(true, "WHO", "0", options.renderSearchOptions(), this.getSearchMask());
    }    
   
    public boolean isSendable() {
        return this.getOptions() != null && this.getSearchMask() != null;
    }
}
