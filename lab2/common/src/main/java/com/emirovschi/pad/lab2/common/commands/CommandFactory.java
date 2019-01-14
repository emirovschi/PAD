package com.emirovschi.pad.lab2.common.commands;

import com.emirovschi.pad.lab2.common.commands.data.Command;

import java.util.Map;

public interface CommandFactory
{
    <T extends Command> T createCommand(final String type, final Map<String, byte[]> data);
}
