package com.emirovschi.pad.lab2.common.commands.io;

public class ReadResult
{
    private final byte[] data;
    private final boolean endOfCommand;

    public ReadResult(final byte[] data, final boolean endOfCommand)
    {
        this.data = data;
        this.endOfCommand = endOfCommand;
    }

    public byte[] getData()
    {
        return data;
    }

    public boolean isEndOfCommand()
    {
        return endOfCommand;
    }
}
