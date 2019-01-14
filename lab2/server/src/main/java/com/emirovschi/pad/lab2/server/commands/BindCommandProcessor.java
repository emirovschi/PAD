package com.emirovschi.pad.lab2.server.commands;

import com.emirovschi.pad.lab2.common.commands.AbstractConfirmCommandProcessor;
import com.emirovschi.pad.lab2.common.broker.BrokerService;
import com.emirovschi.pad.lab2.common.commands.data.BindCommand;
import com.emirovschi.pad.lab2.common.commands.data.Command;
import com.emirovschi.pad.lab2.common.io.Connection;

import java.io.IOException;

public class BindCommandProcessor extends AbstractConfirmCommandProcessor<BindCommand>
{
    private final BrokerService brokerService;

    public BindCommandProcessor(final BrokerService brokerService)
    {
        this.brokerService = brokerService;
    }

    @Override
    public boolean canProcess(final Command command)
    {
        return command.getClass().equals(BindCommand.class);
    }

    @Override
    protected void processCommand(final Connection connection, final BindCommand command) throws IOException
    {
        brokerService.bind(command.getExchange(), command.getQueue(), command.getRoutingKey());
    }
}
