package com.emirovschi.pad.lab2.client.translators;

import com.emirovschi.pad.lab2.common.data.ByteMessage;
import com.emirovschi.pad.lab2.common.data.Message;

import java.util.Properties;

public abstract class AbstractMessageTranslator implements MessageTranslator
{
    public static final String CONTENT_TYPE = "Content-type";

    private final String contentType;

    protected AbstractMessageTranslator(final String contentType)
    {
        this.contentType = contentType;
    }

    @Override
    public ByteMessage serialize(final Message object)
    {
        final Properties properties = new Properties();
        properties.putAll(object.getProperties());
        properties.setProperty(CONTENT_TYPE, contentType);

        final ByteMessage message = new ByteMessage(translateData(object.getMessage()), properties);

        if (object.getCorrelation() != null)
        {
            message.correlate(object.getCorrelation());
        }

        return message;
    }

    @Override
    public <T> Message<T> deserialize(final ByteMessage message, final Class<T> type)
    {
        if (!message.getProperties().getProperty(CONTENT_TYPE).equals(contentType))
        {
            throw new RuntimeException("Bad content type");
        }

        final T payload = translateData(message.getMessage(), type);
        return new Message<>(payload, message.getProperties(), message.getCorrelation());
    }

    protected abstract <T> byte[] translateData(final T object);

    protected abstract <T> T translateData(byte[] data, Class<T> type);

    public String getContentType()
    {
        return this.contentType;
    }
}
