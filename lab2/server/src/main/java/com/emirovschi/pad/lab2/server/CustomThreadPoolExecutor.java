package com.emirovschi.pad.lab2.server;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CustomThreadPoolExecutor extends ThreadPoolExecutor
{
    public CustomThreadPoolExecutor(final int size)
    {
        super(size, size, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    }

    @Override
    public void execute(final Runnable command)
    {
        if (getActiveCount() == getCorePoolSize())
        {
            throw new RejectedExecutionException();
        }

        super.execute(command);
    }
}
