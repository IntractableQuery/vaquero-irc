/**
 * Represents a raw line of IRC data.
 *
 * Most important for use with the event system, although it has been stripped 
 * down so that it is appropriate for use with sending IRC data also.
 *
 * This class is designed around rfc1459's description of an incoming message;
 * a message may or may not contain the origin server of the command (indicated by a 
 * ":server.name.here <arguments...>"), but has a set of arguments, which by
 * the RFC specification may not not exceed 15 in number.
 */

package com.packethammer.vaquero.util.protocol;

import com.packethammer.vaquero.util.Hostmask;
import java.util.StringTokenizer;

public class IRCRawLine {
    private IRCRawParameter[] parameters;
    private Hostmask source;
    private boolean sourceDefinite;

    public IRCRawLine() {
    }

    /**
     * Constructs a raw line from the internal parsed data.
     */
    public String toRawLine() {
        return this.toRawLine(0, true, true);
    }
    
    /**
     * Constructs a raw line from the internal parsed data.
     *
     * @param start The starting parameter to begin output with. Set to 0 to get them all.
     * @param includeSourcePrefix Determines if we should prefix with the :source, assuming it appears to be known.
     * @param renderExtendedPrefix Determines if we should prefix the final extended prefix argument (if it exists) with its normal colon.
     */
    public String toRawLine(int start, boolean includeSourcePrefix, boolean renderExtendedPrefix) {
        if(parameters != null) {
            StringBuilder raw = new StringBuilder();

            if(includeSourcePrefix && this.isSourceDefinite()) {
                raw.append(":" + this.getSource().getShortHostmask() + " ");
            }
            
            for(int x = start; x < parameters.length; x++) {
                IRCRawParameter param = parameters[x];
                if(renderExtendedPrefix)
                    raw.append(param.renderParameterForIRC());
                else
                    raw.append(param.getParameterString());

                // so long as we are not on last param, provide a space after it.
                if(x < parameters.length - 1)
                    raw.append(' ');
            }

            return raw.toString();
        } else {
            return null;
        }
    }
    
    /**
     * Returns the parameters that accompanied this raw line.
     *
     * @return The parameters contained in this IRC event.
     */
    public IRCRawParameter[] getParameters() {
        return parameters;
    }

    /**
     * Sets the parameters received with the IRC line.
     *
     * @param parameters The parameters.
     */
    public void setParameters(IRCRawParameter[] parameters) {
        this.parameters = parameters;
    }

    /**
     * Returns the server or user hostmask that initiated this event.
     * If a source (origin) was not provided by the IRC server, this should
     * return a best-guess (somewhat accurate or exactly precise) of the
     * IRC server's hostname we are connected to, since rfc1459 states that
     * events lacking an origin are guaranteed to be coming directly from
     * the IRC server we are connected to.
     *
     * @return The initiator of this event.
     */
    public Hostmask getSource() {
        return source;
    }

    /**
     * Sets the server or user hostmask that initiated this event.
     *
     * Note: rfc1459 states that if no source of the IRC event is specified,
     * that the source should be considered to be the server we are connected to.
     * A best guess should be made by the parsing system that instantiates this
     * event as to the source of this IRC event. It should ensure that it flags
     * setSourceDefinite() properly.
     *
     * @param source The server or user that initiated this event.
     */
    public void setSource(Hostmask source) {
        this.source = source;
    }

    /**
     * Determines if we know the definite origin of this IRC event. If we don't,
     * we are using inference to deduct it. It is likely very accurate, but
     * this method will allow one to determine the accuracy of the source
     * regardless.
     *
     * The only time when the source is indefinite is when the IRC server chooses
     * to omit it. rfc1459 says that the source is the server we are connected
     * to in this case. Since that server has its own self-appointed name that we may
     * or may not know yet, the source is not quite as accurate as possible under
     * all circumstances, as the source may be assumed to be the only server 
     * name we know (its internet address) or the name the server gives itself
     * (which should be discovered some time after the initial connect sequence).
     *
     * @return True if this source is accurate, or false if we are using inference to deduct it.
     */
    public boolean isSourceDefinite() {
        return sourceDefinite;
    }

    /**
     * Determines if we know for sure the origin of this IRC event. The only case
     * when we don't know for sure is when an origin is not given to us by the IRC
     * server. In that case, rfc1459 says we should assume the origin is from
     * the IRC server we are connected to. Although the server normally identifies
     * itself for us (such as "ourserver.domain.com" instead of the server 
     * address we actually connected to), we can never be sure if the source
     * of the server is actually what the server might want it to be (that is,
     * the name the IRC server gives itself as opposed to what we might name it).
     * In most cases, after initial connection, the default source origin
     * (the server's self-appointed name) should be discovered, allowing
     * a completely valid identification of the server. 
     *
     * In any case, this must be set to false if we received this IRC event from
     * the server but it lacked the actual origin.
     *
     * @param sourceDefinite True if the event source is known, false if it is inferred.
     */
    public void setSourceDefinite(boolean sourceDefinite) {
        this.sourceDefinite = sourceDefinite;
    }
    
    /**
     * Determines if this IRC event contained an extended argument. That is,
     * one that is designed to hold a message that may contain whitespace.
     * The extended argument is always the last argument known, assuming
     * there is one.
     *
     * @return True if the last argument is extended, false otherwise.
     */
    public boolean containsExtendedArgument() {
        if(parameters.length > 0) 
            return parameters[parameters.length - 1].isExtended();
        else 
            return false;
    }
    
    /**
     * Returns the first argument, which is typically the "command" of the IRC
     * event. 
     *
     * @return The first argument, or null if there is none.
     */
    public String getCommandArgument() {
        return getArg(0);
    }
    
    /**
     * Returns the second argument, which is typically an important parameter
     * in a majority of the IRC events, which warrants its own accessor method.
     *
     * @return The second argument, or null if there is none.
     */
    public String getSecondArgument() {
        return getArg(1);
    }
    
    /**
     * Returns the argument at the given index or null if none was there.
     * This is the ideal way to access parameters if an event class subclasses 
     * this event.
     *
     * @return The parameter's value or null if it did not exist.
     */
    public String getArg(int index) {
        if(index < parameters.length && index >= 0)
            return parameters[index].getParameterString();
        else
            return null;
    }
    
    /**
     * Determines if the length of this raw line is compliant with RFC1459.
     * A valid length is one not exceeding 510 ASCII characters.
     */
    public boolean lengthIsRFCCompliant() {
        return this.toRawLine().length() <= 510;
    }
    
    /**
     * Determines if the number of parameters in this line is RFC1459 compliant.
     * No more than 15 parameters are allowed if this is to be compliant.
     */
    public boolean parametersAreRFCCompliant() {
        return this.getParameters().length <= 15;
    }
    
    /**
     * Returns the number of parameters in this irc line.
     */
    public int parametersCount() {
        return this.parameters.length;
    }
    
    /**
     * Takes an IRCRawLine and clones its information and stores it in this
     * raw IRC line.
     *
     * @param rawLine The IRCRawLine to clone from.
     */
    public void cloneFrom(IRCRawLine rawLine) {
        this.parameters = new IRCRawParameter[rawLine.getParameters().length];
        System.arraycopy(rawLine.getParameters(), 0, this.parameters, 0, rawLine.getParameters().length);
        
        this.setSource(rawLine.getSource());
        this.setSourceDefinite(rawLine.isSourceDefinite());
    }

    /**
     * Builds a raw IRC line from a string of IRC arguments. Null args do not
     * get added.
     *
     * @param lastArgIsExtended Set to true if the last argument is to be an extended argument (prefixed with ':' as a raw)
     */
    public static IRCRawLine buildRawLine(boolean lastArgIsExtended, String... args) {
        int notNull = 0;
        for(String str : args) 
            if(str != null)
                notNull++;
        
        IRCRawParameter[] params = new IRCRawParameter[notNull];
        
        for(int x = 0; x < args.length; x++) {
            if(args[x] != null) {
                IRCRawParameter param = null;

                if(x == args.length - 1 && lastArgIsExtended) {
                    param = new IRCRawParameter(true, args[x]);
                } else {
                    param = new IRCRawParameter(false, args[x]);
                }

                params[x] = param;
            }
        }
        
        IRCRawLine rawLine = new IRCRawLine();
        rawLine.setParameters(params);
        return rawLine;
    }
    
    /**
     * Parses a raw IRC line and returns it. The one thing you might need to do
     * is set the source if it isn't known.
     *
     * @param line The line to parse.
     */
    public static IRCRawLine parse(String line) {
        IRCRawLine rawLine = new IRCRawLine();
                
        // pull off the extended argument as the first task, assuming it exists
        int beginExtendedLoc = line.indexOf(" :");
        IRCRawParameter extendedArgument = null;
        if(beginExtendedLoc > -1) {
            extendedArgument = new IRCRawParameter(true, line.substring(beginExtendedLoc + 2));
        }
        
        // parse the line and encapsulate it
        StringTokenizer singleParams = null;
        // ensure that the extended argument isn't here
        if(extendedArgument != null) {
             singleParams = new StringTokenizer(line.substring(0, beginExtendedLoc), " ");
        } else {
             singleParams = new StringTokenizer(line, " ");
        }
        
        // Determine origin
        String origin = null;
        
        if(line.startsWith(":")) {
            // first arg is source of irc line
            rawLine.setSource(Hostmask.parseHostmask(singleParams.nextToken().substring(1)));
            rawLine.setSourceDefinite(true);
        } else {            
            rawLine.setSourceDefinite(false);
        }
        
        // parse out single parameters and prepare storage array
        IRCRawParameter[] params;
        if(extendedArgument != null) 
            params = new IRCRawParameter[singleParams.countTokens() + 1];
        else
            params = new IRCRawParameter[singleParams.countTokens()];
        
        int index = 0;
        while(singleParams.hasMoreTokens()) {
            IRCRawParameter param = new IRCRawParameter(false, singleParams.nextToken());
            params[index++] = param;
        }
        
        if(extendedArgument != null)
            params[params.length - 1] = extendedArgument;
        
        rawLine.setParameters(params);
        return rawLine;
    }
    
    /**
     * Returns a string representing this object.
     */
    public String toString() {
        return this.toRawLine();
    }
}
