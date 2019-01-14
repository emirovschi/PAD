package com.emirovschi.pad.lab2.common.commands.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.Queue;

public class QueueOutputStream extends OutputStream
{
    private final Queue<Byte> queue = new ArrayDeque<>();

    @Override
    public void write(final int b) throws IOException
    {
        queue.add((byte) b);
    }

    public byte[] read()
    {
        byte[] buffer = new byte[queue.size()];

        for (int i = 0; i < buffer.length; i++)
        {
            buffer[i] = queue.poll();
        }

        return buffer;
    }
}
