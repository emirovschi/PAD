package com.emirovschi.pad.lab2.common.commands;

import com.emirovschi.pad.lab2.common.commands.data.Command;

import java.io.IOException;

public interface CommandHandler
{
    <T extends Command> T receive() throws IOException;

    void send(Command command) throws IOException;
}
