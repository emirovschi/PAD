package com.emirovschi.pad.lab2.common.commands.data;

import java.util.Map;

public class SubscribeCommand extends AbstractReplyCommand
{
    private static final String QUEUE = "exchange";

    public SubscribeCommand(final String queue)
    {
        put(QUEUE, queue);
    }

    public SubscribeCommand(final Map<String, byte[]> data)
    {
        super(data);
    }

    public String getQueue()
    {
        return getAsString(QUEUE);
    }
}
