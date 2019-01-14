package com.emirovschi.pad.lab2.server.commands;

import com.emirovschi.pad.lab2.common.broker.BrokerService;
import com.emirovschi.pad.lab2.common.commands.data.MessageCommand;
import com.emirovschi.pad.lab2.common.commands.data.SubscribeCommand;
import com.emirovschi.pad.lab2.common.io.Connection;

import java.io.IOException;

public class SubscribeCommandProcessor extends AbstractSubscribeCommand<SubscribeCommand, MessageCommand>
{
    public SubscribeCommandProcessor(final BrokerService brokerService)
    {
        super(brokerService, SubscribeCommand.class);
    }

    @Override
    protected void processCommand(final Connection connection, final SubscribeCommand command) throws IOException
    {
        getBrokerService().subscribe(
            command.getQueue(),
            getTag(connection, command),
            message -> send(connection, command, new MessageCommand(command, message)));
    }
}
