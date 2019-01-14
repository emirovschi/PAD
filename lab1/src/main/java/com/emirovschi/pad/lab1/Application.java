package com.emirovschi.pad.lab1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Application
{
    public static void main(String[] args)
    {
        final long startTime = System.currentTimeMillis();

        final DefaultRunnable[] tasks = new DefaultRunnable[7];
        tasks[0] = new DefaultRunnable("Thread1", startTime, 1);
        tasks[1] = new DefaultRunnable("Thread2", startTime, 1, tasks[0]);
        tasks[4] = new DefaultRunnable("Thread5", startTime, 1, tasks[1]);

        tasks[5] = new DefaultRunnable("Thread6", startTime, 1);
        tasks[3] = new DefaultRunnable("Thread4", startTime, 1, tasks[5]);
        tasks[6] = new DefaultRunnable("Thread7", startTime, 1, tasks[3]);

        tasks[2] = new DefaultRunnable("Thread3", startTime, 1, tasks[0], tasks[1], tasks[3], tasks[4], tasks[5], tasks[6]);

        for (Runnable runnable : tasks)
        {
            new Thread(runnable).start();
        }
    }
}
