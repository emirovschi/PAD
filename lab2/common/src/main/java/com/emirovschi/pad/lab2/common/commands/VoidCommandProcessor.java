package com.emirovschi.pad.lab2.common.commands;

import com.emirovschi.pad.lab2.common.commands.data.Command;
import com.emirovschi.pad.lab2.common.io.Connection;

import java.io.IOException;

public class VoidCommandProcessor implements CommandProcessor
{
    @Override
    public void process(final Connection connection, final Command command) throws IOException
    {
        System.out.println("Voiding received command " + command.getCommandType());
    }
}
