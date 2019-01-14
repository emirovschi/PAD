package com.emirovschi.pad.lab2.common.commands;

import com.emirovschi.pad.lab2.common.commands.data.Command;

import java.util.Map;

public class ReflectionCommandFactory implements CommandFactory
{
    private static final String PACKAGE = ReflectionCommandFactory.class.getPackage().getName() + ".data.";

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Command> T createCommand(final String type, final Map<String, byte[]> data)
    {
        try
        {
            return (T) Class.forName(PACKAGE + type).getConstructor(Map.class).newInstance(data);
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Bad command type, expected [" + type + "]: " + e.getMessage());
        }
    }
}
