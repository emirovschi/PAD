package com.emirovschi.pad.lab2.chat.ui.data;

import com.emirovschi.pad.lab2.chat.data.Channel;
import com.emirovschi.pad.lab2.chat.ui.JavafxUtils;
import javafx.scene.paint.Color;

public class JavafxChannel extends Channel
{
    private Color color;

    public JavafxChannel(final Channel channel)
    {
        this(channel.getId(), channel.getName());
    }

    public JavafxChannel(final String name)
    {
        this(name, name);
    }

    public JavafxChannel(final String id, final String name)
    {
        super(id, name);

        this.color = id.equals(name) ? Color.BLACK : JavafxUtils.getColor(id);
    }

    public Color getColor()
    {
        return color;
    }
}
