package com.emirovschi.pad.lab2.server.broker.consumer;

import com.emirovschi.pad.lab2.common.data.ByteMessage;
import com.emirovschi.pad.lab2.common.util.ThrowableConsumer;
import com.emirovschi.pad.lab2.server.broker.Queue;

import java.io.IOException;

public class SingleMessageConsumer extends AbstractMessageConsumer
{
    private final ThrowableConsumer<ByteMessage, IOException> consumer;

    public SingleMessageConsumer(final Queue queue, final ThrowableConsumer<ByteMessage, IOException> consumer)
    {
        super(queue);
        this.consumer = consumer;
    }

    @Override
    public void runInternal()
    {
        ByteMessage message = null;
        try
        {
            message = getQueue().getQueue().poll();

            if (message != null)
            {
                consumer.accept(message);
            }
        }
        catch (final IOException exception)
        {
            System.out.println("Message consumer stopped " + exception);
            getQueue().getQueue().addFirst(message);
            stop();
        }
    }
}
