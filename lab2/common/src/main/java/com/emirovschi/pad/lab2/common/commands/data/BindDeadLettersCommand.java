package com.emirovschi.pad.lab2.common.commands.data;

import java.util.Map;

public class BindDeadLettersCommand extends BindCommand
{
    public BindDeadLettersCommand(final String exchange, final String queue, final String routingKey)
    {
        super(exchange, queue, routingKey);
    }

    public BindDeadLettersCommand(final Map<String, byte[]> data)
    {
        super(data);
    }
}
