package com.emirovschi.pad.lab2.server.commands;

import com.emirovschi.pad.lab2.common.commands.AbstractConfirmCommandProcessor;
import com.emirovschi.pad.lab2.common.broker.BrokerService;
import com.emirovschi.pad.lab2.common.commands.data.Command;
import com.emirovschi.pad.lab2.common.commands.data.DeclareQueueCommand;
import com.emirovschi.pad.lab2.common.io.Connection;

import java.io.IOException;

public class DeclareQueueCommandProcessor extends AbstractConfirmCommandProcessor<DeclareQueueCommand>
{
    private final BrokerService brokerService;

    public DeclareQueueCommandProcessor(final BrokerService brokerService)
    {
        this.brokerService = brokerService;
    }

    @Override
    public boolean canProcess(final Command command)
    {
        return command instanceof DeclareQueueCommand;
    }

    @Override
    protected void processCommand(final Connection connection, final DeclareQueueCommand command) throws IOException
    {
        brokerService.declareQueue(command.getName());
    }
}
