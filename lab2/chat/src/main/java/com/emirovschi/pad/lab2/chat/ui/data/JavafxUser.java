package com.emirovschi.pad.lab2.chat.ui.data;

import com.emirovschi.pad.lab2.chat.data.User;
import com.emirovschi.pad.lab2.chat.ui.JavafxUtils;
import javafx.scene.paint.Color;

public class JavafxUser extends User
{
    private Color color;

    public JavafxUser(final User user)
    {
        this(user.getId(), user.getName());
    }

    public JavafxUser(final String id, final String name)
    {
        super(id, name);
        this.color = JavafxUtils.getColor(id);
    }

    public Color getColor()
    {
        return color;
    }
}
