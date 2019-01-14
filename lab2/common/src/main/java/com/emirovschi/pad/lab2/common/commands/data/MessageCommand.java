package com.emirovschi.pad.lab2.common.commands.data;

import com.emirovschi.pad.lab2.common.data.ByteMessage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class MessageCommand extends AbstractReplyCommand
{
    protected static final String MESSAGE = "message";
    protected static final String PROPERTIES = "properties";

    public MessageCommand(final ByteMessage message)
    {
        init(message);
    }

    public MessageCommand(final ReplyCommand reply, final ByteMessage message)
    {
        super(reply);
        init(message);
    }

    private void init(final ByteMessage message)
    {
        put(MESSAGE, message.getMessage());
        put(PROPERTIES, serializeProperties(message.getProperties()));
    }

    public MessageCommand(final Map<String, byte[]> data)
    {
        super(data);
    }

    public ByteMessage getMessage()
    {
        return new ByteMessage(get(MESSAGE), deserializeProperties(get(PROPERTIES)));
    }

    public static byte[] serializeProperties(final Properties properties)
    {
        try
        {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            properties.store(byteArrayOutputStream, "");
            return byteArrayOutputStream.toByteArray();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static Properties deserializeProperties(final byte[] data)
    {
        final Properties properties = new Properties();

        try
        {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            properties.load(byteArrayInputStream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return properties;
    }
}
