package com.emirovschi.pad.lab2.chat.data;

import java.util.Objects;

public class Channel
{
    private String id;
    private String name;
    private int newMessages;

    public Channel()
    {
    }

    public Channel(final String name)
    {
        this(name, name, 0);
    }

    public Channel(final String id, final String name)
    {
        this(id, name, 0);
    }

    public Channel(final String id, final String name, final int newMessages)
    {
        this.id = id;
        this.name = name;
        this.newMessages = newMessages;
    }

    public String getId()
    {
        return id;
    }

    public void setId(final String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public int getNewMessages()
    {
        return newMessages;
    }

    public void setNewMessages(final int newMessages)
    {
        this.newMessages = newMessages;
    }

    public String getTitle()
    {
        final String prefix = id.equals(name) ? "#" : "";
        final String suffix = newMessages > 0 ? " (" + newMessages + ")" : "";
        return prefix + name + suffix;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Channel))
        {
            return false;
        }
        final Channel channel = (Channel) o;
        return Objects.equals(getId(), channel.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId());
    }
}
