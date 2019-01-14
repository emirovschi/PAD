package com.emirovschi.pad.lab2.server.commands;

import com.emirovschi.pad.lab2.common.broker.BrokerService;
import com.emirovschi.pad.lab2.common.commands.AbstractDelegatedCommandProcessor;
import com.emirovschi.pad.lab2.common.commands.data.Command;
import com.emirovschi.pad.lab2.common.commands.data.ConsumeMessageCommand;
import com.emirovschi.pad.lab2.common.commands.data.MessageCommand;
import com.emirovschi.pad.lab2.common.io.Connection;

import java.io.IOException;

public class ConsumeMessageCommandProcessor extends AbstractDelegatedCommandProcessor<ConsumeMessageCommand>
{
    private final BrokerService brokerService;

    public ConsumeMessageCommandProcessor(final BrokerService brokerService)
    {
        this.brokerService = brokerService;
    }

    @Override
    public boolean canProcess(final Command command)
    {
        return command.getClass().equals(ConsumeMessageCommand.class);
    }

    @Override
    protected void internalProcess(final Connection connection, final ConsumeMessageCommand command) throws IOException
    {
        brokerService.consume(command.getQueue(), message -> connection.sendAndConfirm(new MessageCommand(command, message)));
    }
}
