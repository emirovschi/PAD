package com.emirovschi.pad.lab2.server.commands;

import com.emirovschi.pad.lab2.common.broker.BrokerService;
import com.emirovschi.pad.lab2.common.commands.data.ListenCommand;
import com.emirovschi.pad.lab2.common.commands.data.MessageCommand;
import com.emirovschi.pad.lab2.common.io.Connection;

import java.io.IOException;

public class ListenCommandProcessor extends AbstractSubscribeCommand<ListenCommand, MessageCommand>
{
    public ListenCommandProcessor(final BrokerService brokerService)
    {
        super(brokerService, ListenCommand.class);
    }

    @Override
    protected void processCommand(final Connection connection, final ListenCommand command) throws IOException
    {
        getBrokerService().listen(
            command.getQueue(),
            getTag(connection, command),
            message -> send(connection, command, new MessageCommand(command, message)));
    }
}
