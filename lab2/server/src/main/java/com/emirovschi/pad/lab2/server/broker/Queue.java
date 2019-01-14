package com.emirovschi.pad.lab2.server.broker;

import com.emirovschi.pad.lab2.common.data.ByteMessage;
import com.emirovschi.pad.lab2.server.broker.consumer.MessageConsumer;
import com.emirovschi.pad.lab2.server.broker.route.Routable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Observable;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Queue extends Routable
{
    private final String name;
    private final BlockingDeque<ByteMessage> queue;
    private final Collection<MessageConsumer> messageConsumers;
    private final Observable addObservable;

    public Queue(final String name)
    {
        this.name = name;
        this.queue = new LinkedBlockingDeque<>();
        this.messageConsumers = new ArrayList<>();
        this.addObservable = new Observable()
        {
            @Override
            public void notifyObservers(final Object arg)
            {
                setChanged();
                super.notifyObservers(arg);
            }
        };
    }

    public String getName()
    {
        return name;
    }

    public BlockingDeque<ByteMessage> getQueue()
    {
        return queue;
    }

    public Observable getAddObservable()
    {
        return addObservable;
    }

    public void add(final ByteMessage message)
    {
        addObservable.notifyObservers(message);
        queue.addLast(message);
    }

    public void add(final MessageConsumer messageConsumer)
    {
        messageConsumers.add(messageConsumer);
    }

    public void remove(final MessageConsumer messageConsumer)
    {
        messageConsumers.remove(messageConsumer);
    }

    @Override
    public void remove()
    {
        super.remove();
        getAddObservable().deleteObservers();
        messageConsumers.forEach(MessageConsumer::stop);
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Queue))
        {
            return false;
        }
        final Queue queue = (Queue) o;
        return Objects.equals(getName(), queue.getName());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getName());
    }
}
