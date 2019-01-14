package com.emirovschi.pad.lab1;

import java.util.concurrent.CountDownLatch;

public class DefaultRunnable implements Runnable
{
    private final String name;
    private final long start;
    private final long sleep;
    private final CountDownLatch countDownLatch;
    private final DefaultRunnable[] dependencies;

    public DefaultRunnable(final String name, final long start, final long sleep, final DefaultRunnable... dependencies)
    {
        this.name = name;
        this.start = start;
        this.sleep = sleep;
        this.countDownLatch = new CountDownLatch(1);
        this.dependencies = dependencies;
    }

    public void run()
    {
        try
        {
            System.out.println("Start " + name);

            for (DefaultRunnable dependency : dependencies)
            {
                dependency.await();
            }

            System.out.println("Running " + name + " at " + (System.currentTimeMillis() - start) / 1000);
            Thread.sleep(sleep * 1000);
            System.out.println("Finish " + name + " at " + (System.currentTimeMillis() - start) / 1000);
            countDownLatch.countDown();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void await()
    {
        try
        {
            countDownLatch.await();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
