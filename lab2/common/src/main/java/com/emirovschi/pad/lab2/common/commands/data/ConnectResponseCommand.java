package com.emirovschi.pad.lab2.common.commands.data;

import java.util.Map;

public class ConnectResponseCommand extends AbstractCommand
{
    private static final String SUCCESS = "success";

    public ConnectResponseCommand(final boolean success)
    {
        put(SUCCESS, success);
    }

    public ConnectResponseCommand(final Map<String, byte[]> data)
    {
        super(data);
    }

    public boolean isSuccessfull()
    {
        return getAsBoolean(SUCCESS);
    }
}
