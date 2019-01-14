package com.emirovschi.pad.lab2.chat.ui.data;

import com.emirovschi.pad.lab2.chat.data.ChatMessage;
import com.emirovschi.pad.lab2.chat.data.User;
import javafx.scene.effect.ColorAdjust;

import static com.emirovschi.pad.lab2.chat.ui.JavafxUtils.getHue;

public class JavafxChatMessage extends ChatMessage
{
    private JavafxUser javafxUser;
    private ColorAdjust colorAdjust;

    public JavafxChatMessage(final ChatMessage chatMessage)
    {
        this(chatMessage.getUser(), chatMessage.getChannel(), chatMessage.getText(), chatMessage.getReplyTo());
    }

    public JavafxChatMessage(final User user, final String channel, final String text, final String replyTo)
    {
        super(user, channel, text, replyTo);
        this.javafxUser = new JavafxUser(user);
        this.colorAdjust = new ColorAdjust();
        this.colorAdjust.setHue(getHue(replyTo));
    }

    public ColorAdjust getColorAdjust()
    {
        return colorAdjust;
    }

    public void setColor()
    {
        this.colorAdjust.setSaturation(1);
        this.colorAdjust.setBrightness(0);
    }

    public void resetColor()
    {
        this.colorAdjust.setSaturation(0);
        this.colorAdjust.setBrightness(-1);
    }

    public JavafxUser getJavafxUser()
    {
        return javafxUser;
    }
}
