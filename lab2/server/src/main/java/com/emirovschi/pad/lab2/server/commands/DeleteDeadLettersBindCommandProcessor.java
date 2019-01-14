package com.emirovschi.pad.lab2.server.commands;

import com.emirovschi.pad.lab2.common.commands.AbstractConfirmCommandProcessor;
import com.emirovschi.pad.lab2.common.broker.BrokerService;
import com.emirovschi.pad.lab2.common.commands.data.Command;
import com.emirovschi.pad.lab2.common.commands.data.DeleteDeadLettersBindCommand;
import com.emirovschi.pad.lab2.common.io.Connection;

import java.io.IOException;

public class DeleteDeadLettersBindCommandProcessor extends AbstractConfirmCommandProcessor<DeleteDeadLettersBindCommand>
{
    private final BrokerService brokerService;

    public DeleteDeadLettersBindCommandProcessor(final BrokerService brokerService)
    {
        this.brokerService = brokerService;
    }

    @Override
    public boolean canProcess(final Command command)
    {
        return command instanceof DeleteDeadLettersBindCommand;
    }

    @Override
    protected void processCommand(final Connection connection, final DeleteDeadLettersBindCommand command) throws IOException
    {
        brokerService.deleteDeadLettersBind(command.getExchange(), command.getQueue(), command.getRoutingKey());
    }
}
