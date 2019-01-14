package com.emirovschi.pad.lab2.common.commands;

import com.emirovschi.pad.lab2.common.commands.data.Command;
import com.emirovschi.pad.lab2.common.io.Connection;

import java.io.IOException;

public abstract class AbstractDelegatedCommandProcessor<T extends Command> implements CommandProcessor
{
    public abstract boolean canProcess(final Command command);

    protected abstract void internalProcess(final Connection connection, final T command) throws IOException;

    @SuppressWarnings("unchecked")
    @Override
    public void process(final Connection connection, final Command command) throws IOException
    {
        internalProcess(connection, (T) command);
    }
}
