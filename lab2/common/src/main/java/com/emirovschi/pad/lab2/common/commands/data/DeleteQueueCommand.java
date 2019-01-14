package com.emirovschi.pad.lab2.common.commands.data;

import java.util.Map;

public class DeleteQueueCommand extends AbstractReplyCommand
{
    private static final String NAME = "name";

    public DeleteQueueCommand(final String name)
    {
        put(NAME, name);
    }

    public DeleteQueueCommand(final Map<String, byte[]> data)
    {
        super(data);
    }

    public String getName()
    {
        return getAsString(NAME);
    }
}
