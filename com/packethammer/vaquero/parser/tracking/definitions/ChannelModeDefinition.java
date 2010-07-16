/*
 * Represents a mode that can take place in a channel. This includes modes that
 * directly affect the channel (such as +m, +t, +n, etc.) and modes that 
 * perform similar duties such as +b, +o, +v, etc. Note that channel modes
 * that are applied on users may have prefixes, and that is handled by a subclass
 * rather than being stored here.
 *
 * This class is mostly immutable, except for certain properties which must be
 * changeable to avoid some reference problems, such as the listing limit.
 */

package com.packethammer.vaquero.parser.tracking.definitions;

public class ChannelModeDefinition extends ModeDefinition {    
    /** 
    * A = Mode that adds or removes a nick or address to a list. Always has a parameter.
    * Note: Modes of type A return the list when there is no parameter present in
    * setting the mode on a channel. 
    */
    public static final char MODETYPE_A_NICKADDRESS = 'A';

    /** 
    * B = Mode that changes a setting and always has a parameter.
    */
    public static final char MODETYPE_B_ALWAYSPARAM = 'B';

    /**
    * C = Mode that changes a setting and only has a parameter when set.
    */
    public static final char MODETYPE_C_SETPARAM = 'C';

    /**
    * D = Mode that changes a setting and never has a parameter.
    */
    public static final char MODETYPE_D_NOPARAM = 'D';
    
    private char modeType;
    private int listLimit;
    private boolean banExceptionMode;
    private boolean inviteExceptionMode;
    
    
    /** 
     * Initializes the immutable information for this mode.
     *
     * @param mode The literal case-sensitive IRC mode's character of representation.
     * @param modeType This corresponds to the constant MODETYPE_* variables within this class -- it defines how this mode behaves when it is set/removed.
     */
    public ChannelModeDefinition(Character mode, char modeType) {
        super(mode, true, false,
                modeType == MODETYPE_A_NICKADDRESS || modeType == MODETYPE_B_ALWAYSPARAM, // when removed 
                modeType == MODETYPE_A_NICKADDRESS || modeType == MODETYPE_B_ALWAYSPARAM || modeType == MODETYPE_C_SETPARAM); // when added
        
        this.modeType = modeType;
        this.listLimit = -1;
    }  
    
    /**
     * Returns a value that corresponds with the contant MODETYPE_* variables
     * within this class, which signifies how this mode behaves in regard to
     * its parameter (if any) when it is set or removed.
     *
     * @return A value corresponding to the MODETYPE_ constants.
     */
    public char getModeType() {
        return modeType;
    }
    
    /**
     * Determines if this mode type is designed to keep some sort of list that
     * is added or removed to by associating a parameter as the thing to be added
     * or removed. This is basically a type A (MODETYPE_A_NICKADDRESS) mode.
     * The most obvious example is +b or -b, which adds or removes a hostmask
     * from the channel ban list.
     *
     * Note that modes that affect users directly (like +o, +v, etc.) are not
     * considered to be a listable mode, even though they sort of act in that
     * capacity. It is rare to see a mode other than +b which behaves like this,
     * although some servers may have such a special mode.
     *
     * As a side note, if you set such a mode of this type on a channel with
     * no parameter, you should get back a list of the current entries in the
     * channel associated with that mode (example: sending "MODE #chan +b" should
     * cause the server to send you back the ban list for that channel.
     *
     * @return True if this is a listing mode, false otherwise.
     */
    public boolean isListable() {
        return this.getModeType() == this.MODETYPE_A_NICKADDRESS;
    }    

    /**
     * Returns the list limit for modes of type MODETYPE_A_NICKADDRESS. That is,
     * listable modes (example: +b). This does not include nick prefix modes like
     * +o, etc. Will return -1 if it does not apply for this mode type or 
     * it is currently unknown.
     *
     * @return List limit or -1 if not applicable or unknown.
     */
    public int getListLimit() {
        return listLimit;
    }
    
    /**
     * Determines if this channel definition currently has a listing limit.
     *
     * @return True if listing limit, false otherwise.
     * @see #getListLimit()
     */
    public boolean hasListLimit() {
        return this.getListLimit() > -1;
    }

    /**
     * Sets the listing limit for this mode. Should be set to -1 if a listing limit
     * does not apply for this mode, or it is unknown.
     *
     * @param listLimit The listing limit, or -1.
     */
    public void setListLimit(int listLimit) {
        this.listLimit = listLimit;
    }

    /**
     * Determines if this is a ban exception mode. This is normally mode 'e',
     * but according to numeric 005 ISUPPORT, it could be assigned to a different
     * mode. Note that this mode may be a ban exception mode even though
     * there server did not mark it as so. Don't rely on it for definite
     * accuracy.
     *
     * @return True if this is a ban exception mode, false otherwise.
     */
    public boolean isBanExceptionMode() {
        return banExceptionMode;
    }

    /**
     * Determines if this is a ban exception mode.
     *
     * @param banExceptionMode Set to true if this is a ban exception mode, false otherwise.
     */
    public void setBanExceptionMode(boolean banExceptionMode) {
        this.banExceptionMode = banExceptionMode;
    }

    /**
     * Determines if this is an invite exception mode. This is normally mode 'I',
     * but according to numeric 005 ISUPPORT, it could be assigned to a different
     * mode. Note that this mode may be an invite exception mode even though
     * there server did not mark it as so. Don't rely on it for definite
     * accuracy.
     *
     * @return True if this is an invite exception mode, false otherwise.
     */
    public boolean isInviteExceptionMode() {
        return inviteExceptionMode;
    }

    /**
     * Determines if this is an invite exception mode.
     *
     * @param inviteExceptionMode Set to true if this is an invite exception mode, false otherwise.
     */
    public void setInviteExceptionMode(boolean inviteExceptionMode) {
        this.inviteExceptionMode = inviteExceptionMode;
    }
   
    
    public String toString() {
        return super.toString() + " TYPE:" + this.getModeType() + " LISTLIM:" + this.getListLimit() + " LISTABLE:" + this.isListable();
    }
}
