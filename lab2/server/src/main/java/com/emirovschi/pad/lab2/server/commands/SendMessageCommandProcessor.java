package com.emirovschi.pad.lab2.server.commands;

import com.emirovschi.pad.lab2.common.commands.AbstractConfirmCommandProcessor;
import com.emirovschi.pad.lab2.common.broker.BrokerService;
import com.emirovschi.pad.lab2.common.commands.data.Command;
import com.emirovschi.pad.lab2.common.commands.data.SendMessageCommand;
import com.emirovschi.pad.lab2.common.io.Connection;

import java.io.IOException;

public class SendMessageCommandProcessor extends AbstractConfirmCommandProcessor<SendMessageCommand>
{
    private final BrokerService brokerService;

    public SendMessageCommandProcessor(final BrokerService brokerService)
    {
        this.brokerService = brokerService;
    }

    @Override
    public boolean canProcess(final Command command)
    {
        return command instanceof SendMessageCommand;
    }

    @Override
    protected void processCommand(final Connection connection, final SendMessageCommand command) throws IOException
    {
        if (command.getRoutingKey() == null)
        {
            this.brokerService.publish(command.getExchange(), command.getMessage());
        }
        else
        {
            this.brokerService.publish(command.getExchange(), command.getMessage(), command.getRoutingKey());
        }
    }
}
