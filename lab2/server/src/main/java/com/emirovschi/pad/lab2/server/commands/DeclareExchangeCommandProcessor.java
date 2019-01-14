package com.emirovschi.pad.lab2.server.commands;

import com.emirovschi.pad.lab2.common.commands.AbstractConfirmCommandProcessor;
import com.emirovschi.pad.lab2.common.broker.BrokerService;
import com.emirovschi.pad.lab2.common.commands.data.Command;
import com.emirovschi.pad.lab2.common.commands.data.DeclareExchangeCommand;
import com.emirovschi.pad.lab2.common.io.Connection;

import java.io.IOException;

public class DeclareExchangeCommandProcessor extends AbstractConfirmCommandProcessor<DeclareExchangeCommand>
{
    private final BrokerService brokerService;

    public DeclareExchangeCommandProcessor(final BrokerService brokerService)
    {
        this.brokerService = brokerService;
    }

    @Override
    public boolean canProcess(final Command command)
    {
        return command instanceof DeclareExchangeCommand;
    }

    @Override
    protected void processCommand(final Connection connection, final DeclareExchangeCommand command) throws IOException
    {
        brokerService.declareExchange(command.getName());
    }
}
