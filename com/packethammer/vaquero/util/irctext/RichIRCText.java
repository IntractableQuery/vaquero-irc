/*
 * This represents "rich" IRC text. That is, text that may contain color,
 * bold, reverse, underline, etc. control characters. This class facilitates
 * common operations on such a string of text and can parse it into an easily
 * digestible format. It can also be used to build such IRC text programatically.
 *
 * Note that this class does not preserve the original IRC text you give it;
 * once it is parsed, it is stored in a more suitable form. You can actually
 * optimize human-created coded text by parsing it with this class and pulling
 * it out again.
 *
 * This class only supports the most common/universal of control characters. 
 * These include bold, underline, reverse, normal, and color.
 */

package com.packethammer.vaquero.util.irctext;

import java.awt.Color;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RichIRCText {
    // TODO: this class is pretty ugly -- it eventually needs to be made more elegant.

    /** Make text bold. */
    public static final char ASCIICODE_BOLD = 2;
    /** Underline text. */
    public static final char ASCIICODE_UNDERLINE = 31;
    /** Reverse foreground/background colors */
    public static final char ASCIICODE_REVERSE = 22;
    /** Colorize text (color specifier follows) */
    public static final char ASCIICODE_COLOR = 3;
    /** Return text to normal (close bold/underline/reverse, etc.) */
    public static final char ASCIICODE_NORMAL = 15;
    
    private List<IRCFormattedTextPortion> text;
    
    /**
     * Given a string of text that may or may not contain IRC formatting, this
     * will parse it and store it internally.
     *
     * @param text IRC control code formatted text.
     */
    public RichIRCText(String text) {
        // TODO: rewrite this class to use more or less 100% regex in parsing
        StringBuilder curText = new StringBuilder();
        ArrayList<String> choppedText = new ArrayList();
        char txt[] = text.toCharArray();
        for(char c : txt) {
            char ascii = c;
            if(ascii == ASCIICODE_BOLD || ascii == ASCIICODE_UNDERLINE || ascii == ASCIICODE_REVERSE || ascii == ASCIICODE_NORMAL || ascii == ASCIICODE_COLOR) {
                choppedText.add(curText.toString());
                curText = new StringBuilder();
            }
            
            curText.append(ascii);
        }
        choppedText.add(curText.toString());
        
              
        boolean bold = false;
        boolean underline = false;
        boolean reverse = false;
        IRCColor backColor = null;
        IRCColor foreColor = null;
        ArrayList<IRCFormattedTextPortion> textPortions = new ArrayList();
        Pattern colorPattern = Pattern.compile("^([0-9]{0,2})(,[0-9]{0,2})?");
        for(String formattedText : choppedText) {
            if(formattedText.length() > 0) {
                char ascii = formattedText.charAt(0);
                String cleanText = formattedText;
                
                if(ascii == ASCIICODE_BOLD) {
                    bold = !bold;
                    cleanText = formattedText.substring(1);
                } else if(ascii == ASCIICODE_UNDERLINE) {
                    underline = !underline;
                    cleanText = formattedText.substring(1);
                } else if(ascii == ASCIICODE_REVERSE) {
                    reverse = !reverse;
                    cleanText = formattedText.substring(1);
                } else if(ascii == ASCIICODE_NORMAL) {
                    bold = underline = reverse = false;
                    backColor = foreColor = null;
                    cleanText = formattedText.substring(1);
                } else if(ascii == ASCIICODE_COLOR) {
                    // this could could probably benefit from better knowlege of regex, but it does the job pretty well
                    Matcher m = colorPattern.matcher(formattedText.substring(1));
                    if(m.find()) {
                        MatchResult r = m.toMatchResult();
                        String foreground = r.group(1);
                        String background = r.group(2);

                        int codeArgLength = 0;
                        if(foreground != null) {
                            codeArgLength += foreground.length();
                        } 
                        
                        if(background != null) {
                            codeArgLength += background.length();
                            background = background.substring(1);
                        } 
                        
                        cleanText = formattedText.substring(codeArgLength + 1);
                        
                        if(foreground.length() == 0) {
                            // empty foreground, reset foreground
                            foreColor = null;
                        } else {
                            // foreground has a color
                            foreColor = new IRCColor(Integer.parseInt(foreground));
                        }
  
                        if(background == null) {
                            // no comma, no background
                            if(foreground.length() == 0) {
                                // full color reset
                                backColor = null;
                            }
                        } else if(background.length() == 0) {
                            // there was a comma, empty background, so reset foreground
                            backColor = null;
                        } else {
                            // background has a color
                            backColor = new IRCColor(Integer.parseInt(background));
                        }
                        
                        //System.out.println("[COLOR IN CODE]" +  foreColor + ":" + backColor + ":" + cleanText);
                    } else {
                        // this is a color code with on args -- turn off both colors
                        backColor = foreColor = null;
                        //System.out.println("[NO COLOR IN CODE]" + foreColor + ":" + backColor + ":" + cleanText);
                    }
                }
                
                // Now we save the formatted text
                IRCFormattedTextPortion portion = new IRCFormattedTextPortion(cleanText);
                portion.setBold(bold);
                portion.setReversed(reverse);
                portion.setUnderlined(underline);
                portion.setBackgroundColor(backColor);
                portion.setForegroundColor(foreColor);
                textPortions.add(portion);

            }
           
        }
        
        this.text = textPortions;
    }   
    
    /**
     * Returns the formatted IRC text in an easily readable short format.
     */
    public String toDebugString() {
        return text.toString();
    }
    
    /**
     * Returns a string stripped of all control codes.
     */
    public String toPlaintextString() {
        StringBuilder plaintext = new StringBuilder();
        for(IRCFormattedTextPortion portion : text) {
            plaintext.append(portion.getText());
        }
        return plaintext.toString();
    }
    
    /**
     * Returns this string in proper IRC format -- same as calling toIRCString()
     * @see #toIRCString()
     */
    public String toString() {
        return this.toIRCString();
    }
        
    /**
     * Returns a properly-formatted IRC text string, including control codes for
     * bold, underline, etc.
     */
    public String toIRCString() {
        boolean bold = false;
        boolean underline = false;
        boolean reverse = false;
        IRCColor backColor = null;
        IRCColor foreColor = null;
        
        StringBuilder ircText = new StringBuilder();
        
        for(IRCFormattedTextPortion portion : text) {
            if(portion.isBold() != bold) {
                bold = portion.isBold();
                ircText.append(ASCIICODE_BOLD);
            }
            if(portion.isReversed() != reverse) {
                reverse = portion.isReversed();
                ircText.append(ASCIICODE_REVERSE);
            }
            if(portion.isUnderlined() != underline) {
                underline = portion.isUnderlined();
                ircText.append(ASCIICODE_UNDERLINE);
            }
            if(colorsDifferent(portion.getForegroundColor(), foreColor) || colorsDifferent(portion.getBackgroundColor(), backColor)) {
                String colorCodes = null;
                if(colorsDifferent(portion.getForegroundColor(), foreColor) && colorsDifferent(portion.getBackgroundColor(), backColor)) {
                    foreColor = portion.getForegroundColor();
                    backColor = portion.getBackgroundColor();   
                    colorCodes = (ASCIICODE_COLOR) + genSafeColorCoding(foreColor, backColor, portion.getText());

                } else if(colorsDifferent(portion.getForegroundColor(), foreColor)) {
                    foreColor = portion.getForegroundColor();
                    colorCodes = (ASCIICODE_COLOR) + genSafeColorCoding(foreColor, null, portion.getText());
                } else if(colorsDifferent(portion.getBackgroundColor(), backColor)) {
                    backColor = portion.getBackgroundColor();
                    colorCodes = (ASCIICODE_COLOR) + genSafeColorCoding(foreColor, backColor, portion.getText());
                } 
                
                ircText.append(colorCodes);                              
            }
            
            ircText.append(portion.getText());
        }
        
        return ircText.toString();
    }
    
    /**
     * Generates a properly-formatted HTML string that reflects all of the 
     * properties of this text, including color. This makes use of the SPAN
     * tag in conjunction with the CSS 'STYLE' property to provide colored
     * backgrounds for text. Automatically converts characters other than
     * digits and english ASCII letters to HTML's encoded characters which
     * also results in proper conversion of unicode characters.
     *
     * Note that this method is not quite as smart as toIRCString() -- it 
     * generates opening and closing tags in a straightforward manner without
     * regard to previous tags. The result is you may get some redundant tags.
     *
     * @param defaultForeColor The default forecolor to use, or null for none.
     * @param defaultBackColor The default backcolor to use, or null for none.
     */
    public String toHTMLString(IRCColor defaultForeColor, IRCColor defaultBackColor) {
        boolean bold = false;
        boolean underline = false;
        boolean reverse = false;
        IRCColor previousBackColor = defaultBackColor;
        IRCColor previousForeColor = defaultForeColor;
        
        StringBuilder html = new StringBuilder();
        
        for(IRCFormattedTextPortion portion : text) {
            if(portion.getText().length() > 0) {
                // determine background render color (if any)
                IRCColor renderBack = null;
                if(portion.hasBackgroundColor()) {
                    renderBack = portion.getBackgroundColor();
                } else if(defaultBackColor != null) {
                    renderBack = defaultBackColor;
                }
                
                // determine foreground render color (if any)
                IRCColor renderFore = null;
                if(portion.hasForegroundColor()) {
                    renderFore = portion.getForegroundColor();
                } else if(defaultForeColor != null) {
                    renderFore = defaultForeColor;
                }
                
                // if the text is reversed, swap fore- and back-colors
                if(portion.isReversed()) {
                    IRCColor tmp = renderBack;
                    renderBack = renderFore;
                    renderFore = tmp;
                }
                
                if(renderBack != null)
                    html.append("<SPAN style=\"background-color:" + IRCColor.getCodeHTMLColor(renderBack.getColorCode()) + "\">");

                if(renderBack != null)
                    html.append("<FONT color=" + IRCColor.getCodeHTMLColor(renderFore.getColorCode()) + ">");

                if(portion.isBold())
                    html.append("<B>");
                if(portion.isUnderlined())
                    html.append("<U>");

                //html.append(portion.getText());
                for(char c : portion.getText().toCharArray()) {
                    // Character's methods won't work here since unicode characters will pass isLetter()
                    if( (c >= 48 && c <= 57) || (c >= 65 && c <= 90) ||  (c >= 97 && c <= 122) ) {
                        html.append(c);
                    } else {
                        html.append("&#" + ((int) c) + ";");
                    }
                }



                if(portion.isUnderlined())
                    html.append("</U>");
                if(portion.isBold())
                    html.append("</B>");
                if(renderFore != null) {
                    html.append("</FONT>");
                }
                if(renderBack != null) {
                    html.append("</SPAN>");
                }
            }
        }
        
        return html.toString();
    }
    
    /**
     * Determines if two colors are different. Arguments can be null.
     */
    private boolean colorsDifferent(IRCColor color1, IRCColor color2) {
        if(color1 == null && color2 == null)
            return false;
        else if(color1 != null && color2 != null)
            return !color1.equals(color2);
        else
            return true;
    }
    
    /**
     * Given color codes text that follows the color char and the text following it, this
     * will adjust the color-coded text to be safe with that text.
     */
    private String genSafeColorCoding(IRCColor foreground, IRCColor background, String txt) {
       DecimalFormat format = new DecimalFormat("00");
       if(foreground != null && background != null) {
           // #,#
           String backTxt = background.getColorCode() + "";
           if(txt.length() > 0 && Character.isDigit(txt.charAt(0)) && backTxt.length() <= 1) 
               backTxt = format.format(background.getColorCode());
           return foreground.getColorCode() + "," + backTxt;
       } else if(foreground != null) {
           // #
           String foreTxt = foreground.getColorCode() + "";
           if(txt.length() > 0 && Character.isDigit(txt.charAt(0)) && foreTxt.length() <= 1) 
               foreTxt = format.format(foreground.getColorCode());
           return foreTxt;
       } else if(background != null) {
           // ,#
           String backTxt = background.getColorCode() + "";
           if(txt.length() > 0 && Character.isDigit(txt.charAt(0)) && backTxt.length() <= 1) 
               backTxt = format.format(background.getColorCode());
           return "," + backTxt;
       } else {
           // forecolor and backcolor are null, which means full color reset
           return "";
       }
    }
    
    /**
     * Given a string that may or may not have IRC formatting data (control 
     * codes) in it, this will remove the formatting specifiers and return a 
     * plaintext string.
     */
    public static String strip(String text) {
        RichIRCText txt = new RichIRCText(text);
        return txt.toPlaintextString();
    }
   
}
