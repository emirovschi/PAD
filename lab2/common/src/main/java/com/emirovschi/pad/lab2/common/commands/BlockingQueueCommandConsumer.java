package com.emirovschi.pad.lab2.common.commands;

import com.emirovschi.pad.lab2.common.commands.data.ConfirmActionCommand;
import com.emirovschi.pad.lab2.common.commands.data.ReplyCommand;
import com.emirovschi.pad.lab2.common.io.Connection;
import com.emirovschi.pad.lab2.common.util.ThrowableConsumer;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class BlockingQueueCommandConsumer<T extends ReplyCommand> implements CommandConsumer<T>
{
    private final ReplyCommand originalCommand;
    private final BlockingQueue<T> queue;
    private final ThrowableConsumer<T, IOException> consumer;
    private final Connection connection;

    private boolean stop;

    public BlockingQueueCommandConsumer(
        final ReplyCommand originalCommand,
        final ThrowableConsumer<T, IOException> consumer,
        final Connection connection)
    {
        this.originalCommand = originalCommand;
        this.queue = new LinkedBlockingQueue<>();
        this.consumer = consumer;
        this.connection = connection;
        this.stop = false;
    }

    @Override
    public void stop() throws IOException
    {
        connection.closeInbox(originalCommand);
        close();
    }

    @Override
    public void accept(final T message)
    {
        queue.offer(message);
    }

    @Override
    public void close() throws IOException
    {
        stop = true;
        connection.closeInbox(originalCommand);
    }

    @Override
    public void run()
    {
        while (!stop)
        {
            try
            {
                final T command = queue.poll(10, TimeUnit.MILLISECONDS);

                if (command != null)
                {
                    consumer.accept(command);
                    connection.send(new ConfirmActionCommand(command));
                }
                Thread.sleep(10);
            }
            catch (InterruptedException | IOException e)
            {
                System.out.println("Command consumer closed " + e);
                closeQuietly();
            }
        }
    }

    private void closeQuietly()
    {
        try
        {
            close();
        }
        catch (IOException e)
        {
            System.out.println("Could not close server handler:" + e.getMessage());
        }
    }
}
