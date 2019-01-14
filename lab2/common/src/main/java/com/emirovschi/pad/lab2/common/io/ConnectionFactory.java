package com.emirovschi.pad.lab2.common.io;

import com.emirovschi.pad.lab2.common.commands.CommandFactory;
import com.emirovschi.pad.lab2.common.commands.CommandHandler;
import com.emirovschi.pad.lab2.common.commands.CommandProcessor;
import com.emirovschi.pad.lab2.common.commands.io.Delimiters;
import com.emirovschi.pad.lab2.common.commands.io.SocketStreamProcessor;
import com.emirovschi.pad.lab2.common.commands.io.StreamCommandHandler;
import com.emirovschi.pad.lab2.common.commands.io.StreamProcessor;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class ConnectionFactory
{
    protected final ExecutorService executorService;
    protected final CommandProcessor commandProcessor;
    protected final CommandFactory commandFactory;
    protected final Delimiters delimiters;

    public ConnectionFactory(
        final ExecutorService executorService,
        final CommandFactory commandFactory,
        final CommandProcessor commandProcessor)
    {
        this(executorService, commandProcessor, commandFactory, null);
    }

    public ConnectionFactory(
        final ExecutorService executorService,
        final CommandProcessor commandProcessor,
        final CommandFactory commandFactory,
        final Delimiters delimiters)
    {
        this.executorService = executorService;
        this.commandProcessor = commandProcessor;
        this.commandFactory = commandFactory;
        this.delimiters = delimiters;
    }

    public Connection create(final Socket socket) throws IOException
    {
        final StreamProcessor streamProcessor = delimiters == null
            ? new SocketStreamProcessor(socket) : new SocketStreamProcessor(delimiters, socket);
        final CommandHandler commandHandler = new StreamCommandHandler(streamProcessor, commandFactory);
        return new SocketConnection(executorService, commandProcessor, commandHandler, socket);
    }
}
