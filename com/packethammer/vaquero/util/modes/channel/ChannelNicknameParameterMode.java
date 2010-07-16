/*
 * Represents a mode that targets a user's nickname when beinf removed AND added.
 */

package com.packethammer.vaquero.util.modes.channel;

public class ChannelNicknameParameterMode extends ChannelMode {
    public ChannelNicknameParameterMode(char mode, String nickname, boolean addingMode) {
        super(new Character(mode), nickname, addingMode);
    }
    
    /**
     * Sets the nickname to operate on.
     *
     * @param nickname The nickname of the user.
     */
    public void setNickname(String nickname) {
        super.setParameter(nickname);
    }
    
    /**
     * Returns the nickname being operated on.
     *
     * @return IRC nickname.
     */
    public String getNickname() {
        return this.getParameter();
    }
            
    /**
     * Sets this mode's parameter.
     *
     * @param parameter The mode's parameter.
     */
    public void setParameter(String parameter) {
        this.setNickname(parameter);
    }
}
