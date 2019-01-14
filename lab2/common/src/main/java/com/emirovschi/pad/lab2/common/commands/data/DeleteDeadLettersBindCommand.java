package com.emirovschi.pad.lab2.common.commands.data;

import java.util.Map;

public class DeleteDeadLettersBindCommand extends DeleteBindCommand
{
    public DeleteDeadLettersBindCommand(final String exchange, final String queue, final String routingKey)
    {
        super(exchange, queue, routingKey);
    }

    public DeleteDeadLettersBindCommand(final Map<String, byte[]> data)
    {
        super(data);
    }
}
