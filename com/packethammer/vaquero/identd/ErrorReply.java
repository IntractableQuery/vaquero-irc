/**
 * This represents an error reply to an ident query.
 */

package com.packethammer.vaquero.identd;

public class ErrorReply extends AbstractIdentReply {
    /**
     * Either the local or foreign port was improperly
     * specified.  This should be returned if either or
     * both of the port ids were out of range (TCP port
     * numbers are from 1-65535), negative integers, reals or
     * in any fashion not recognized as a non-negative
     * integer.
     */
    public static final String ERR_INVALIDPORT = "INVALID-PORT";
    
    /**
     * The connection specified by the port pair is not
     * currently in use or currently not owned by an
     * identifiable entity.
     */
    public static final String ERR_NOUSER = "NO-USER";

    /**
     * The server was able to identify the user of this
     * port, but the information was not returned at the
     * request of the user.
     */
    public static final String ERR_HIDDENUSER = "HIDDEN-USER";

    /**
     *  Can't determine connection owner; reason unknown.
     *  Any error not covered above should return this
     *  error code value.  Optionally, this code MAY be
     *  returned in lieu of any other specific error code
     *  if, for example, the server desires to hide
     *  information implied by the return of that error
     *  code, or for any other reason.  If a server
     *  implements such a feature, it MUST be configurable
     *  and it MUST default to returning the proper error message.
     */
    public static final String ERR_UNKNOWNERR = "UNKNOWN-ERROR";
    
    
    private String error;

    
    /**
     * Initializes the ident reply with the port on us, the port on them that
     * is being checked, and the error reply to use.
     *
     * @param ourPort The port on us being checked.
     * @param theirPort The port on them being checked.
     * @param errorReply The error reply to use (see ERR_* constants for valid errors).
     */
    public ErrorReply(int ourPort, int theirPort, String errorReply) {
        super(ourPort, theirPort);
        this.error = errorReply;
    }
    
    public String getReponseType() {
        return "ERROR";
    }
    
    public String getAdditionalInfo() {
        return error;
    }
}
