package com.emirovschi.pad.lab2.server.broker.consumer;

import com.emirovschi.pad.lab2.common.data.ByteMessage;
import com.emirovschi.pad.lab2.common.util.ThrowableConsumer;
import com.emirovschi.pad.lab2.server.broker.Queue;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class ListenMessageConsumer implements MessageConsumer
{
    private final Queue queue;
    private final Observer observer;
    private final ThrowableConsumer<ByteMessage, IOException> consumer  ;

    public ListenMessageConsumer(final Queue queue, final ThrowableConsumer<ByteMessage, IOException> consumer)
    {
        this.queue = queue;
        this.consumer = consumer;
        this.observer = this::update;
        this.queue.getAddObservable().addObserver(this.observer);
    }

    private void update(final Observable observable, final Object object)
    {
        final ByteMessage message = (ByteMessage) object;
        try
        {
            consumer.accept(message);
        }
        catch (final IOException exception)
        {
            System.out.println("Message listener stopped " + exception);
            queue.getQueue().addFirst(message);
            stop();
        }
    }

    @Override
    public void stop()
    {
        queue.getAddObservable().deleteObserver(observer);
    }
}
