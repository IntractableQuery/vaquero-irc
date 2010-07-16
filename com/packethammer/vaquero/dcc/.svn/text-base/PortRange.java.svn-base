/**
 * Represents a port range with a starting port and an ending port. The starting
 * port is always less than or equal to the ending port.
 */

package com.packethammer.vaquero.dcc;

public class PortRange {
    private int start;
    private int end;
    
    /** 
     * Initializes this port range with start and end ports.
     *
     * @param start The first port in the port range.
     * @param end The last port in the port range.
     * @throw IllegalArgumentException If the end port comes before the start port.
     */
    public PortRange(int start, int end) {
        if(end < start) {
            throw new IllegalArgumentException("The end port (" + end + ") comes before the start port (" + start +"); must be end >= start");
        }
        
        this.start = start;
        this.end = end;
    }
    
    /**
     * Initializes this port range with a single port as the start and end of 
     * the port range.
     *
     * @param port The port.
     */
    public PortRange(int port) {
        this(port, port);
    }
    

    /**
     * Returns the starting port for this port range.
     */
    public int getStart() {
        return start;
    }

    /** 
     * Returns the ending port for the port range.
     */
    public int getEnd() {
        return end;
    }    
    
    public boolean equals(Object o) {
        if(o instanceof PortRange) {
            PortRange p = (PortRange) o;
            return this.getStart() == p.getStart() && this.getEnd() == p.getEnd();
        } else {
            throw new IllegalArgumentException("Object is not PortRange");
        }
    }
}
