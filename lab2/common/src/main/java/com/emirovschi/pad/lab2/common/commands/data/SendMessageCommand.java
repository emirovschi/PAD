package com.emirovschi.pad.lab2.common.commands.data;

import com.emirovschi.pad.lab2.common.data.ByteMessage;

import java.util.Map;

public class SendMessageCommand extends MessageCommand
{
    private static final String EXCHANGE = "exchange";
    private static final String ROUTING_KEY = "routingKey";

    public SendMessageCommand(final String exchange, final ByteMessage message, final String routingKey)
    {
        this(exchange, message);
        put(ROUTING_KEY, routingKey);
    }

    public SendMessageCommand(final String exchange, final ByteMessage message)
    {
        super(message);
        put(EXCHANGE, exchange);
    }

    public SendMessageCommand(final Map<String, byte[]> data)
    {
        super(data);
    }

    public String getExchange()
    {
        return getAsString(EXCHANGE);
    }

    public String getRoutingKey()
    {
        return getData().containsKey(ROUTING_KEY) ? getAsString(ROUTING_KEY) : null;
    }
}
