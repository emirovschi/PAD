package com.emirovschi.pad.lab2.chat.data;

import javax.annotation.Nullable;
import java.util.UUID;

public class ChatMessage
{
    private String id;
    private String replyTo;
    private User user;
    private String channel;
    private String text;

    public ChatMessage()
    {
    }

    public ChatMessage(final String channel, final String text, final String replyTo)
    {
        this(null, channel, text, replyTo);
    }

    public ChatMessage(final User user, final String channel, final String text, final String replyTo)
    {
        this.id = UUID.randomUUID().toString();
        this.user = user;
        this.channel = channel;
        this.text = text;
        this.replyTo = replyTo;
    }

    public String getId()
    {
        return id;
    }

    public User getUser()
    {
        return user;
    }

    public String getChannel()
    {
        return channel;
    }

    public String getText()
    {
        return text;
    }

    @Nullable
    public String getReplyTo()
    {
        return replyTo;
    }

    @Override
    public String toString()
    {
        return "ChatMessage{" +
            "id='" + id + '\'' +
            ", replyTo='" + replyTo + '\'' +
            ", user='" + user + '\'' +
            ", channel='" + channel + '\'' +
            ", text='" + text + '\'' +
            '}';
    }
}
