package com.emirovschi.pad.lab2.server.commands;

import com.emirovschi.pad.lab2.common.commands.AbstractConfirmCommandProcessor;
import com.emirovschi.pad.lab2.common.broker.BrokerService;
import com.emirovschi.pad.lab2.common.commands.data.AbstractReplyCommand;
import com.emirovschi.pad.lab2.common.commands.data.Command;
import com.emirovschi.pad.lab2.common.io.Connection;

import java.io.IOException;

public abstract class AbstractSubscribeCommand<T extends AbstractReplyCommand, R extends AbstractReplyCommand> extends AbstractConfirmCommandProcessor<T>
{
    private final BrokerService brokerService;
    private final Class<T> commandType;

    public AbstractSubscribeCommand(final BrokerService brokerService, final Class<T> commandType)
    {
        this.brokerService = brokerService;
        this.commandType = commandType;
    }

    @Override
    public boolean canProcess(final Command command)
    {
        return command.getClass().equals(commandType);
    }

    protected String getTag(final Connection connection, final T command)
    {
        return connection.getId() + "-" + command.getId();
    }

    protected void send(final Connection connection, final T command, final R responseCommand) throws IOException
    {
        try
        {
            connection.sendAndConfirm(responseCommand);
        }
        catch (final IOException sendException)
        {
            final String tag = getTag(connection, command);
            System.out.println("Failed to publish message: " + sendException.getMessage() + ". Stopping consumer: " + tag);
            try
            {
                this.brokerService.stopConsumer(tag);
            }
            catch (final IOException stopException)
            {
                System.out.println("Consumer already stopped:" + stopException.getMessage());
            }
            throw sendException;
        }
    }

    public BrokerService getBrokerService()
    {
        return brokerService;
    }
}
