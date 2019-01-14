package com.emirovschi.pad.lab2.server.broker.consumer;

import com.emirovschi.pad.lab2.server.broker.Queue;

public abstract class AbstractMessageConsumer implements MessageConsumer, Runnable
{
    private final Queue queue;
    private boolean stop;

    public AbstractMessageConsumer(final Queue queue)
    {
        this.queue = queue;
        this.stop = false;
    }

    @Override
    public void stop()
    {
        stop = true;
    }

    @Override
    public void run()
    {
        while (!stop)
        {
            runInternal();
        }
        queue.remove(this);
    }

    protected Queue getQueue()
    {
        return queue;
    }

    protected abstract void runInternal();
}
