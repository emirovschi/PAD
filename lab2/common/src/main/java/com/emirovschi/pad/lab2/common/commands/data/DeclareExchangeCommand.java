package com.emirovschi.pad.lab2.common.commands.data;

import java.util.Map;

public class DeclareExchangeCommand extends AbstractReplyCommand
{
    private static final String NAME = "name";

    public DeclareExchangeCommand(final String name)
    {
        put(NAME, name);
    }

    public DeclareExchangeCommand(final Map<String, byte[]> data)
    {
        super(data);
    }

    public String getName()
    {
        return getAsString(NAME);
    }
}
