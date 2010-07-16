/*
 * Represents a portion of IRC text that may or may not have multiple text
 * modifiers, like color, bold, etc.
 *
 * To consider this visually, just remember that every time a control code in
 * a string of text is encountered in a parser, a new instance of this class
 * would be created, perhaps inheriting some properties from a previous formatting.
 */

package com.packethammer.vaquero.util.irctext;

public class IRCFormattedTextPortion {
    private String text; 
    private boolean bold;
    private boolean underlined;
    private boolean colored;
    private boolean reversed;
    private IRCColor backgroundColor;
    private IRCColor foregroundColor;

    /**
     * Creates a default unformatted text portion.
     *
     * @param text The text to encapsulate.
     */
    public IRCFormattedTextPortion(String text) {
        this.setText(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isUnderlined() {
        return underlined;
    }

    public void setUnderlined(boolean underlined) {
        this.underlined = underlined;
    }
    
    /**
     * Determines if this IRC text is "reversed" color-wise. Note that this does
     * not affect this class' color accessors; reversed is actually quite open
     * to interpretation on many graphical IRC clients, and wouldn't even be
     * handled here anyway.
     */
    public boolean isReversed() {
        return reversed;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }

    /**
     * Determines if this text has a background color.
     */
    public boolean hasBackgroundColor() {
        return this.getBackgroundColor() != null;
    }
    
    /**
     * Returns the background color, or null if none exists.
     */
    public IRCColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(IRCColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    
    /**
     * Determines if this text has a foreground color.
     */
    public boolean hasForegroundColor() {
        return this.getForegroundColor() != null;
    }    

    /**
     * Returns the foreground color, or null if none exists.
     */
    public IRCColor getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(IRCColor foregroundColor) {
        this.foregroundColor = foregroundColor;
    }    
    
    public String toString() {
        return "[B:" + this.isBold() + " R:" + this.isReversed() + " U:" + this.isUnderlined() + " FC:" + this.getForegroundColor() + " BC:" + this.getBackgroundColor() + "]" + this.getText();
    }
}
