package com.emirovschi.pad.lab2.server.broker.route;

import com.emirovschi.pad.lab2.server.broker.Exchange;
import com.emirovschi.pad.lab2.server.broker.Queue;

import java.util.regex.PatternSyntaxException;

public class DeadLetterRoute extends Route
{
    public DeadLetterRoute(final Exchange exchange, final Queue queue, final String routeKey) throws PatternSyntaxException
    {
        super(exchange, queue, routeKey);
    }
}
