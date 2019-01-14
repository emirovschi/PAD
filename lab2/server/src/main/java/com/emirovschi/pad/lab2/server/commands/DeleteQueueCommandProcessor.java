package com.emirovschi.pad.lab2.server.commands;

import com.emirovschi.pad.lab2.common.commands.AbstractConfirmCommandProcessor;
import com.emirovschi.pad.lab2.common.broker.BrokerService;
import com.emirovschi.pad.lab2.common.commands.data.Command;
import com.emirovschi.pad.lab2.common.commands.data.DeleteQueueCommand;
import com.emirovschi.pad.lab2.common.io.Connection;

import java.io.IOException;

public class DeleteQueueCommandProcessor extends AbstractConfirmCommandProcessor<DeleteQueueCommand>
{
    private final BrokerService brokerService;

    public DeleteQueueCommandProcessor(final BrokerService brokerService)
    {
        this.brokerService = brokerService;
    }

    @Override
    public boolean canProcess(final Command command)
    {
        return command instanceof DeleteQueueCommand;
    }

    @Override
    protected void processCommand(final Connection connection, final DeleteQueueCommand command) throws IOException
    {
        brokerService.deleteQueue(command.getName());
    }
}
