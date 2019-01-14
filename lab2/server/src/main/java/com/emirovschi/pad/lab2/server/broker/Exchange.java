package com.emirovschi.pad.lab2.server.broker;

import com.emirovschi.pad.lab2.common.data.ByteMessage;
import com.emirovschi.pad.lab2.common.data.Message;
import com.emirovschi.pad.lab2.server.broker.route.DeadLetterRoute;
import com.emirovschi.pad.lab2.server.broker.route.Routable;
import com.emirovschi.pad.lab2.server.broker.route.Route;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Exchange extends Routable
{
    private final String name;
    private final Collection<Route> deadLettersRoutes;

    public Exchange(final String name)
    {
        this.name = name;
        this.deadLettersRoutes = new HashSet<>();
    }

    public String getName()
    {
        return name;
    }

    @Override
    public void bind(final Route route)
    {
        if (route instanceof DeadLetterRoute)
        {
            deadLettersRoutes.add(route);
        }
        else
        {
            super.bind(route);
        }
    }

    @Override
    public void unbind(final Route route)
    {
        if (route instanceof DeadLetterRoute)
        {
            deadLettersRoutes.add(route);
        }
        else
        {
            super.unbind(route);
        }
    }

    public void publish(final ByteMessage message, final String routingKey)
    {
        final List<Queue> destinations = getDestinations(routes, routingKey).collect(toList());

        if (destinations.size() > 0)
        {
            destinations.forEach(queue -> queue.add(message));
        }
        else
        {
            getDestinations(deadLettersRoutes, routingKey).forEach(queue -> queue.add(message));
        }
    }

    private Stream<Queue> getDestinations(final Collection<Route> routes, final String routingKey)
    {
        return routes.stream()
            .filter(route -> route.matches(routingKey))
            .map(Route::getQueue)
            .distinct();
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Exchange))
        {
            return false;
        }
        final Exchange exchange = (Exchange) o;
        return Objects.equals(getName(), exchange.getName());
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(getName());
    }
}
