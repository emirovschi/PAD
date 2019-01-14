package com.emirovschi.pad.lab2.server.broker;

import com.emirovschi.pad.lab2.common.broker.BrokerService;
import com.emirovschi.pad.lab2.common.commands.data.MessageCommand;
import com.emirovschi.pad.lab2.common.data.ByteMessage;
import com.emirovschi.pad.lab2.common.util.ThrowableConsumer;
import com.emirovschi.pad.lab2.server.broker.consumer.AbstractMessageConsumer;
import com.emirovschi.pad.lab2.server.broker.consumer.ListenMessageConsumer;
import com.emirovschi.pad.lab2.server.broker.consumer.MessageConsumer;
import com.emirovschi.pad.lab2.server.broker.consumer.SingleMessageConsumer;
import com.emirovschi.pad.lab2.server.broker.route.DeadLetterRoute;
import com.emirovschi.pad.lab2.server.broker.route.Route;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.regex.PatternSyntaxException;

public class InMemoryBrokerService implements BrokerService
{
    private final ExecutorService executorService;
    private final Map<String, Exchange> exchanges;
    private final Map<String, Queue> queues;
    private final Map<String, MessageConsumer> messageConsumers;
    private final Collection<Route> routes;

    public InMemoryBrokerService(final ExecutorService executorService)
    {
        this.executorService = executorService;
        this.exchanges = new HashMap<>();
        this.queues = new HashMap<>();
        this.messageConsumers = new HashMap<>();
        this.routes = new HashSet<>();
    }

    @Override
    public void declareExchange(final String name) throws IOException
    {
        if (exchanges.containsKey(name))
        {
            throw new IOException("Exchange already exists");
        }
        exchanges.put(name, new Exchange(name));
    }

    @Override
    public void declareQueue(final String name) throws IOException
    {
        if (queues.containsKey(name))
        {
            throw new IOException("Queue already exists");
        }
        queues.put(name, new Queue(name));
    }

    @Override
    public void bind(final String exchange, final String queue, final String routingKey) throws IOException
    {
        if (!exchanges.containsKey(exchange))
        {
            throw new IOException("Unknown exchange");
        }
        if (!queues.containsKey(queue))
        {
            throw new IOException("Unknown queue");
        }

        try
        {
            final Route route = new Route(exchanges.get(exchange), queues.get(queue), routingKey);
            route.bind();
            routes.add(route);
        }
        catch (final PatternSyntaxException exception)
        {
            throw new IOException("Invalid pattern: " + exception.getMessage());
        }
    }

    @Override
    public void bindDeadLetters(final String exchange, final String queue, final String routingKey) throws IOException
    {
        if (!exchanges.containsKey(exchange))
        {
            throw new IOException("Unknown exchange");
        }
        if (!queues.containsKey(queue))
        {
            throw new IOException("Unknown queue");
        }

        try
        {
            final DeadLetterRoute route = new DeadLetterRoute(exchanges.get(exchange), queues.get(queue), routingKey);
            route.bind();
            routes.add(route);
        }
        catch (final PatternSyntaxException exception)
        {
            throw new IOException("Invalid pattern: " + exception.getMessage());
        }

    }

    @Override
    public void deleteExchange(final String name) throws IOException
    {
        if (!exchanges.containsKey(name))
        {
            throw new IOException("Unknown exchange");
        }

        exchanges.get(name).remove();
        exchanges.remove(name);
    }

    @Override
    public void deleteQueue(final String name) throws IOException
    {
        if (!queues.containsKey(name))
        {
            throw new IOException("Unknown queue");
        }

        queues.get(name).remove();
        queues.remove(name);
    }

    @Override
    public void deleteBind(final String exchange, final String queue, final String routingKey) throws IOException
    {
        deleteBind(Route.class, exchange, queue, routingKey);
    }

    @Override
    public void deleteDeadLettersBind(final String exchange, final String queue, final String routingKey) throws IOException
    {
        deleteBind(DeadLetterRoute.class, exchange, queue, routingKey);
    }

    private void deleteBind(final Class type, final String exchange, final String queue, final String routingKey) throws IOException
    {
        final Route route = routes.stream()
            .filter(r -> r.getClass().equals(type))
            .filter(r -> r.equals(exchange, queue, routingKey))
            .findFirst()
            .orElseThrow(() -> new IOException("Unkown binding"));

        route.unbind();
        routes.remove(route);
    }

    @Override
    public void publish(final String exchange, final ByteMessage message) throws IOException
    {
        if (!exchanges.containsKey(exchange))
        {
            throw new IOException("Unknown exchange");
        }

        exchanges.get(exchange).publish(message, "");
    }

    @Override
    public void publish(final String exchange, final ByteMessage message, final String routingKey) throws IOException
    {
        if (!exchanges.containsKey(exchange))
        {
            throw new IOException("Unknown exchange");
        }

        exchanges.get(exchange).publish(message, routingKey);
    }

    @Override
    public void subscribe(final String queue, final String tag, final ThrowableConsumer<ByteMessage, IOException> consumer) throws IOException
    {
        startConsumer(queue, tag, q -> new SingleMessageConsumer(q, consumer));
    }

    @Override
    public void consume(final String queue, ThrowableConsumer<ByteMessage, IOException> consumer) throws IOException
    {
        if (!queues.containsKey(queue))
        {
            throw new IOException("Unknown queue");
        }

        ByteMessage message = null;
        try
        {
            message = queues.get(queue).getQueue().poll();

            if (message != null)
            {
                consumer.accept(message);
            }
        }
        catch (final IOException exception)
        {
            System.out.println("Couldn't consume message" + exception);
            queues.get(queue).getQueue().addFirst(message);
        }
    }

    @Override
    public void listen(final String queue, final String tag, final ThrowableConsumer<ByteMessage, IOException> consumer) throws IOException
    {
        if (!queues.containsKey(queue))
        {
            throw new IOException("Unknown queue");
        }

        messageConsumers.put(tag, new ListenMessageConsumer(queues.get(queue), consumer));
    }

    private void startConsumer(final String queue, final String tag, final Function<Queue, AbstractMessageConsumer> messageConsumerSupplier) throws IOException
    {
        if (!queues.containsKey(queue))
        {
            throw new IOException("Unknown queue");
        }

        final AbstractMessageConsumer messageConsumer = messageConsumerSupplier.apply(queues.get(queue));
        messageConsumers.put(tag, messageConsumer);
        queues.get(queue).add(messageConsumer);
        executorService.execute(messageConsumer);
    }

    @Override
    public void stopConsumer(final String tag) throws IOException
    {
        if (!messageConsumers.containsKey(tag))
        {
            throw new IOException("Unknown consumer");
        }

        messageConsumers.get(tag).stop();
        messageConsumers.remove(tag);
    }
}
