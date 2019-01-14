package com.emirovschi.pad.lab2.common.commands.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class QueueInputStream extends InputStream
{
    private final Queue<Byte> queue = new ArrayBlockingQueue<>(8192);

    @Override
    public int read() throws IOException
    {
        return queue.poll();
    }

    public void write(final byte data)
    {
        queue.add(data);
    }

    public void write(final byte[] data)
    {
        for (byte d : data)
        {
            write(d);
        }
    }
}
