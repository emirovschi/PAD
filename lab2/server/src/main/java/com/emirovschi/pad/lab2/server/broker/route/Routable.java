package com.emirovschi.pad.lab2.server.broker.route;

import java.util.Collection;
import java.util.LinkedList;

public class Routable
{
    protected final Collection<Route> routes;

    public Routable()
    {
        this.routes = new LinkedList<>();;
    }

    public void bind(final Route route)
    {
        this.routes.add(route);
    }

    public void unbind(final Route route)
    {
        this.routes.remove(route);
    }

    public void unbind()
    {
        this.routes.forEach(Route::unbind);
    }

    public void remove()
    {
        unbind();
    }
}
