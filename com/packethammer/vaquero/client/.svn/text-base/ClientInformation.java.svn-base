/**
 * This is basic, essential information used with a client session to 
 * provide a remote server with information to let us log in.
 */

package com.packethammer.vaquero.client;

public class ClientInformation {
    private String desiredNickname;
    private String realname;
    private String ident;
    private String password;
    
    /**
     * Initializes this client information.
     *
     * @param desiredNickname Your desired nickname.
     * @param ident Your desired ident (note that identd server replies will override this).
     * @param realname Your realname (infotext).
     * @param password The password to connect with (set to null to not log in using a password). 
     */
    public ClientInformation(String desiredNickname, String ident, String realname, String password) {
        this.desiredNickname = desiredNickname;
        this.ident = ident;
        this.realname = realname;
        this.password = password;
    }
    
    /**
     * Initializes this client information without a server login password.
     *
     * @param desiredNickname Your desired nickname.
     * @param ident Your desired ident (note that identd server replies will override this).
     * @param realname Your realname (infotext).
     */
    public ClientInformation(String desiredNickname, String ident, String realname) {
        this(desiredNickname, ident, realname, null);
    }

    /**
     * Returns the desired nickname.
     */
    public String getDesiredNickname() {
        return desiredNickname;
    }

    /**
     * Sets the desired nickname.
     */
    public void setDesiredNickname(String desiredNickname) {
        this.desiredNickname = desiredNickname;
    }

    /**
     * Returns the realname/infotext.
     */
    public String getRealname() {
        return realname;
    }

    /**
     * sets the realname/infotext.
     */
    public void setRealname(String realname) {
        this.realname = realname;
    }

    /**
     * Returns the ident ('login').
     */
    public String getIdent() {
        return ident;
    }


    /**
     * Sets the ident ('login'). Note that if you are running an identd server,
     * that the identd reply will override this.
     */
    public void setIdent(String ident) {
        this.ident = ident;
    }

    /**
     * Returns the password to use for logging into the server, or null if none.
     */
    public String getPassword() {
        return password;
    }

    /*
     * Sets the password to use for logging into the server. Set to null if you
     * wish to not provide one.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
}
