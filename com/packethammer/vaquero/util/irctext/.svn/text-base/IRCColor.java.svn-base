/*
 * Represents an IRC color that generally is represented by an integer from 
 * 0 to 15. These integers map to a color that is generally consistent among
 * most IRC clients.
 *
 * IRC color codes are based off the "basic" 16 HTML colors. One source of these
 * is here: http://www.december.com/html/spec/color16.html
 *
 * The only color this class doesn't provide "correctly" is COLORCODE_ORANGE;
 * it is supposed to be "olive", but all the major clients render this as orange, 
 * so we might as well go along with everyone else.
 */

package com.packethammer.vaquero.util.irctext;

import java.awt.Color;

public class IRCColor {              
    public static final int COLORCODE_WHITE = 0;
    public static final int COLORCODE_BLACK = 1;
    public static final int COLORCODE_NAVY = 2;
    public static final int COLORCODE_GREEN = 3;
    public static final int COLORCODE_RED = 4;
    public static final int COLORCODE_MAROON = 5;
    public static final int COLORCODE_PURPLE = 6;
    public static final int COLORCODE_ORANGE = 7; // displays as olive on 256-color display (otherwise, it is literally orange)
    public static final int COLORCODE_YELLOW = 8;
    public static final int COLORCODE_LIME = 9;
    public static final int COLORCODE_AQUA = 10; // basically, cyan
    public static final int COLORCODE_TEAL = 11; 
    public static final int COLORCODE_BLUE = 12;
    public static final int COLORCODE_FUCHSIA = 13;
    public static final int COLORCODE_GRAY = 14;
    public static final int COLORCODE_SILVER = 15;
    
    /**
     * The index locations in this string array correspond to IRC color codes. 
     * The strings are the names of the colors.
     */
    public static final String[] COLOR_NAMES = {
        "White", "Black", "Navy Blue", "Green", "Red", "Maroon",
        "Purple", "Olive/Orange", "Yellow", "Lime Green", 
        "Teal", "Aqua/Cyan", "Blue", "Fuchsia",
        "Gray", "Silver"
        
    };
    
    /**
     * The index locations in this string correspond to IRC color codes. 
     * These colors are those that best correspond to the respective IRC
     * color code and are ideal for rendering the colors.
     */
     public static final Color[] IRC_COLORS = { Color.WHITE, 
                                                Color.BLACK,
                                                new Color(0, 0, 128),
                                                new Color(0, 128, 0),
                                                new Color(255, 0, 0),
                                                new Color(128, 0, 0),
                                                new Color(128, 0, 128),
                                                Color.ORANGE,
                                                new Color(255, 255, 0),
                                                new Color(0, 255, 0),
                                                new Color(0, 128, 128),
                                                new Color(0, 255, 255),
                                                new Color(0, 0, 255),
                                                new Color(255, 0, 255),
                                                new Color(128, 128, 128),
                                                new Color(192, 192, 192) };
                            
    private int colorCode;
     
    /**
     * Creates a new instance of IRCColor with a valid IRC color code.
     *
     * @param colorCode A color code from 0 to 15.
     */
    public IRCColor(int colorCode) {
        this.colorCode = colorCode;
    }
    
    /**
     * Returns the encapsulated color code.
     *
     * @return IRC color code.
     */
    public int getColorCode() {
        return colorCode;
    }
    
    /**
     * Returns an awt Color instance for a given color code, or null if an 
     * invalid color code was provided.
     *
     * @return Color or null.
     */
    public static Color getCodeColor(int code) {
        if(isCodeValid(code)) {
            return IRC_COLORS[code];
        } else {
            return null;
        }
    }
    
    /**
     * Returns the name for a given color code or null if an invalid color code
     * was provided.
     *
     * @return Color name or null.
     */
    public static String getCodeName(int code) {
        if(isCodeValid(code)) {
            return COLOR_NAMES[code];
        } else {
            return null;
        }
    }
    
    /** 
     * Returns a hexadecimal HTML color string for a given color code, or null
     * if an invalid color code was provided.
     *
     * @return HTML hexadecimal color code or null.
     */
    public static String getCodeHTMLColor(int code) {
        Color color = getCodeColor(code);
        if(color != null) {
            return "#" + Integer.toHexString((color.getRGB() & 0xffffff) | 0x1000000).substring(1);
        } else {
            return null;
        }
    }
    
    /**
     * Determines if a color code is valid.
     */
    public static boolean isCodeValid(int code) {
        return code >= 0 && code <= 15;
    }
    
    public boolean equals(IRCColor color) {
        return this.getColorCode() == color.getColorCode();
    }
    
    public String toString() {
        return getCodeName(this.getColorCode()) + "(" + this.getColorCode() + ")";
    }
}
