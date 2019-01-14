package com.emirovschi.pad.lab2.common.commands;

import com.emirovschi.pad.lab2.common.commands.data.Command;
import com.emirovschi.pad.lab2.common.io.Connection;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class DelegateCommandProcessor implements CommandProcessor
{
    private final Collection<AbstractDelegatedCommandProcessor> commandProcessors;

    public DelegateCommandProcessor(final Collection<AbstractDelegatedCommandProcessor> commandProcessors)
    {
        this.commandProcessors = commandProcessors;
    }

    @Override
    public void process(final Connection connection, final Command command) throws IOException
    {
        commandProcessors.stream()
            .filter(commandProcessor -> commandProcessor.canProcess(command))
            .findFirst()
            .orElseThrow(() -> new IOException("Invalid command type [" + command.getCommandType() + "] received from [" + connection + "]"))
            .process(connection, command);
    }
}
