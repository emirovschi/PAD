package com.emirovschi.pad.lab2.common.commands;

import com.emirovschi.pad.lab2.common.commands.data.Command;
import com.emirovschi.pad.lab2.common.commands.CommandProcessor;
import com.emirovschi.pad.lab2.common.io.Connection;

import java.io.IOException;

public class EchoCommandProcessor implements CommandProcessor
{
    @Override
    public void process(final Connection connection, final Command command) throws IOException
    {
        System.out.println("Echo command " + command.getCommandType());
        connection.send(command);
    }
}
