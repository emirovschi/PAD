package com.emirovschi.pad.lab2.common.data;

import java.util.Properties;

public class ByteMessage extends Message<byte[]>
{
    public ByteMessage(final byte[] message, final Properties properties)
    {
        super(message, properties);
    }

    public ByteMessage(final byte[] message, final Properties properties, final String correlation)
    {
        super(message, properties, correlation);
    }
}
