package com.emirovschi.pad.lab2.server.commands;

import com.emirovschi.pad.lab2.common.commands.AbstractConfirmCommandProcessor;
import com.emirovschi.pad.lab2.common.broker.BrokerService;
import com.emirovschi.pad.lab2.common.commands.data.Command;
import com.emirovschi.pad.lab2.common.commands.data.DeleteExchangeCommand;
import com.emirovschi.pad.lab2.common.io.Connection;

import java.io.IOException;

public class DeleteExchangeCommandProcessor extends AbstractConfirmCommandProcessor<DeleteExchangeCommand>
{
    private final BrokerService brokerService;

    public DeleteExchangeCommandProcessor(final BrokerService brokerService)
    {
        this.brokerService = brokerService;
    }

    @Override
    public boolean canProcess(final Command command)
    {
        return command instanceof DeleteExchangeCommand;
    }

    @Override
    protected void processCommand(final Connection connection, final DeleteExchangeCommand command) throws IOException
    {
        brokerService.deleteExchange(command.getName());
    }
}
