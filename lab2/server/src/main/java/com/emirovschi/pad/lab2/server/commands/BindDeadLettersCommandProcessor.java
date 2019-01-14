package com.emirovschi.pad.lab2.server.commands;

import com.emirovschi.pad.lab2.common.commands.AbstractConfirmCommandProcessor;
import com.emirovschi.pad.lab2.common.broker.BrokerService;
import com.emirovschi.pad.lab2.common.commands.data.BindDeadLettersCommand;
import com.emirovschi.pad.lab2.common.commands.data.Command;
import com.emirovschi.pad.lab2.common.io.Connection;

import java.io.IOException;

public class BindDeadLettersCommandProcessor extends AbstractConfirmCommandProcessor<BindDeadLettersCommand>
{
    private final BrokerService brokerService;

    public BindDeadLettersCommandProcessor(final BrokerService brokerService)
    {
        this.brokerService = brokerService;
    }

    @Override
    public boolean canProcess(final Command command)
    {
        return command instanceof BindDeadLettersCommand;
    }

    @Override
    protected void processCommand(final Connection connection, final BindDeadLettersCommand command) throws IOException
    {
        brokerService.bindDeadLetters(command.getExchange(), command.getQueue(), command.getRoutingKey());
    }
}
