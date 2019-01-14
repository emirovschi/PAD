package com.emirovschi.pad.lab2.common.data;

import java.util.Properties;

public class Message<T>
{
    private static final String CORRELATION = "X-Correlation";

    private final T message;
    private final Properties properties;

    public Message(final T message)
    {
        this.message = message;
        this.properties = new Properties();
    }

    public Message(final T message, final Properties properties)
    {
        this.message = message;
        this.properties = properties;
    }

    public Message(final T message, final Properties properties, final String correlation)
    {
        this.message = message;
        this.properties = properties;

        correlate(correlation);
    }

    public T getMessage()
    {
        return message;
    }

    public Properties getProperties()
    {
        return properties;
    }

    public String getCorrelation()
    {
        return getProperties().getProperty(CORRELATION);
    }

    public void correlate(final String correlationId)
    {
        getProperties().put(CORRELATION, correlationId);
    }
}
