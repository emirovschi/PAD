package com.emirovschi.pad.lab2.common.commands;

import com.emirovschi.pad.lab2.common.commands.data.ConfirmActionCommand;
import com.emirovschi.pad.lab2.common.commands.data.ReplyCommand;
import com.emirovschi.pad.lab2.common.io.Connection;

import java.io.IOException;

public abstract class AbstractConfirmCommandProcessor<T extends ReplyCommand> extends AbstractDelegatedCommandProcessor<T>
{
    @Override
    protected void internalProcess(final Connection connection, final T command) throws IOException
    {
        String message = null;
        try
        {
            processCommand(connection, command);
        }
        catch (final Exception exception)
        {
            message = exception.getMessage();
        }
        connection.send(new ConfirmActionCommand(command, message));
    }

    protected abstract void processCommand(final Connection connection, final T command) throws IOException;
}
