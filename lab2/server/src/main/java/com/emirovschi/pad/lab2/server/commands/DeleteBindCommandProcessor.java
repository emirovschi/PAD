package com.emirovschi.pad.lab2.server.commands;

import com.emirovschi.pad.lab2.common.commands.AbstractConfirmCommandProcessor;
import com.emirovschi.pad.lab2.common.broker.BrokerService;
import com.emirovschi.pad.lab2.common.commands.data.Command;
import com.emirovschi.pad.lab2.common.commands.data.DeleteBindCommand;
import com.emirovschi.pad.lab2.common.io.Connection;

import java.io.IOException;

public class DeleteBindCommandProcessor extends AbstractConfirmCommandProcessor<DeleteBindCommand>
{
    private final BrokerService brokerService;

    public DeleteBindCommandProcessor(final BrokerService brokerService)
    {
        this.brokerService = brokerService;
    }

    @Override
    public boolean canProcess(final Command command)
    {
        return command.getClass().equals(DeleteBindCommand.class);
    }

    @Override
    protected void processCommand(final Connection connection, final DeleteBindCommand command) throws IOException
    {
        brokerService.deleteBind(command.getExchange(), command.getQueue(), command.getRoutingKey());
    }
}
