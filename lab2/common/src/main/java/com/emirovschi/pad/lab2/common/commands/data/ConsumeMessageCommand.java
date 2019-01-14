package com.emirovschi.pad.lab2.common.commands.data;

import java.util.Map;

public class ConsumeMessageCommand extends AbstractReplyCommand
{
    private static final String QUEUE = "exchange";

    public ConsumeMessageCommand(final String queue)
    {
        put(QUEUE, queue);
    }

    public ConsumeMessageCommand(final Map<String, byte[]> data)
    {
        super(data);
    }

    public String getQueue()
    {
        return getAsString(QUEUE);
    }
}
