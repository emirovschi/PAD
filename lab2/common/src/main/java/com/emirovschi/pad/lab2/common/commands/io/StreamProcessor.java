package com.emirovschi.pad.lab2.common.commands.io;

import java.io.IOException;

public interface StreamProcessor
{
    ReadResult read(final String delimiter) throws IOException;

    void write(final byte[] data, final String delimiter) throws IOException;

    void flush() throws IOException;
}
