/*
 * This defines a channel type, which is typically identified by its character
 * prefix. Some IRC servers may limit one's ability to join over X number of 
 * channels of a certain type.
 *
 * Some properties of this class are mutable.
 */

package com.packethammer.vaquero.parser.tracking.definitions;

public class ChannelTypeDefinition {
    private Character prefix;
    private int joinLimit;
    
    /**
     * Initializes a channel type definition with essential information.
     *
     * @param prefix The chararacter that prefixes the name of this type of channel.
     */
    public ChannelTypeDefinition(Character prefix) {
        this.prefix = prefix;
        setJoinLimit(-1);
    }

    /**
     * Returns the character that prefixes channel names of this type.
     *
     * @return Prefix character.
     */
    public Character getPrefix() {
        return prefix;
    }

    /**
     * Returns the join limit for this channel type, or -1 if it is not known at this time.
     * 
     * @return Maximum join limit or -1 if unknown.
     */
    public int getJoinLimit() {
        return joinLimit;
    }
    
    /**
     * Determines if we know the join limit or not.
     *
     * @return True if we know the join limit, false otherwise.
     */
    public boolean knowJoinLimit() {
        return this.getJoinLimit() > -1;
    }

    /**
     * Sets the join limit for this channel type. Set to -1 to indicate it is not known.
     *
     * @param joinLimit The join limit or -1.
     */
    public void setJoinLimit(int joinLimit) {
        this.joinLimit = joinLimit;
    }
    
    public int compareTo(Object o) {
        if(o instanceof ChannelTypeDefinition) {
            ChannelTypeDefinition c = (ChannelTypeDefinition) o;
            return this.getPrefix().compareTo(c.getPrefix());
        } else {
            throw new IllegalArgumentException("object is not of type " + this.getClass().getCanonicalName());
        }
    }
    
    public boolean equals(Object o) {
        return compareTo(o) == 0;
    }
    
    public int hashCode() {
        return this.getPrefix().hashCode();
    } 
    
    public String toString() {
        return "CHANPREFIX:" + this.getPrefix() + " JOINLIMIT:" + this.getJoinLimit();
    }
}
