package com.emirovschi.pad.lab2.chat;

import com.emirovschi.pad.lab2.client.enricher.GenericMessageEnricher;
import com.emirovschi.pad.lab2.common.data.Message;

public class MessageNameEnricher extends GenericMessageEnricher<String>
{
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";

    private final String id;
    private final String name;

    public MessageNameEnricher(final String id, final String name)
    {
        super(String.class);
        this.id = id;
        this.name = name;
    }

    @Override
    protected Message<String> applyInternal(final Message<String> message)
    {
        message.getProperties().put(USER_ID, id);
        message.getProperties().put(USER_NAME, name);
        return message;
    }
}
