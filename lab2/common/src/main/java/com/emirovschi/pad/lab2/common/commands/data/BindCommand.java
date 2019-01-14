package com.emirovschi.pad.lab2.common.commands.data;

import java.util.Map;

public class BindCommand extends AbstractReplyCommand
{
    private static final String EXCHANGE = "exchange";
    private static final String QUEUE = "queue";
    private static final String ROUTING_KEY = "routingKey";

    public BindCommand(final String exchange, final String queue, final String routingKey)
    {
        put(EXCHANGE, exchange);
        put(QUEUE, queue);
        put(ROUTING_KEY, routingKey);
    }

    public BindCommand(final Map<String, byte[]> data)
    {
        super(data);
    }

    public String getExchange()
    {
        return getAsString(EXCHANGE);
    }

    public String getQueue()
    {
        return getAsString(QUEUE);
    }

    public String getRoutingKey()
    {
        return getAsString(ROUTING_KEY);
    }
}
