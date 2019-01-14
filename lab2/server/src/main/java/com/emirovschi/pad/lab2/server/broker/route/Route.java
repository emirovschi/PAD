package com.emirovschi.pad.lab2.server.broker.route;

import com.emirovschi.pad.lab2.server.broker.Exchange;
import com.emirovschi.pad.lab2.server.broker.Queue;

import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Route
{
    private final Exchange exchange;
    private final Queue queue;
    private final Pattern routeKey;

    public Route(final Exchange exchange, final Queue queue, final String routeKey) throws PatternSyntaxException
    {
        this.exchange = exchange;
        this.queue = queue;
        this.routeKey = Pattern.compile(routeKey);
    }

    public void bind()
    {
        this.exchange.bind(this);
        this.queue.bind(this);
    }

    public void unbind()
    {
        this.exchange.unbind(this);
        this.queue.unbind(this);
    }

    public Exchange getExchange()
    {
        return exchange;
    }

    public Queue getQueue()
    {
        return queue;
    }

    public Pattern getRouteKey()
    {
        return routeKey;
    }

    public boolean matches(final String routingKey)
    {
        return this.routeKey.matcher(routingKey).matches();
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Route))
        {
            return false;
        }
        final Route route = (Route) o;
        return Objects.equals(getExchange(), route.getExchange()) &&
            Objects.equals(getQueue(), route.getQueue()) &&
            Objects.equals(getRouteKey(), route.getRouteKey());
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(getExchange(), getQueue(), getRouteKey());
    }

    public boolean equals(final String exchange, final String queue, final String routingKey)
    {
        return Objects.equals(getExchange().getName(), exchange) &&
            Objects.equals(getQueue().getName(), queue) &&
            Objects.equals(getRouteKey().pattern(), routingKey);
    }
}
