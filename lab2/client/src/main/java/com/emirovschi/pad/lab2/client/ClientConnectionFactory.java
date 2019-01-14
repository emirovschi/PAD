package com.emirovschi.pad.lab2.client;

import com.emirovschi.pad.lab2.common.commands.CommandFactory;
import com.emirovschi.pad.lab2.common.commands.CommandHandler;
import com.emirovschi.pad.lab2.common.commands.CommandProcessor;
import com.emirovschi.pad.lab2.common.commands.data.ConnectResponseCommand;
import com.emirovschi.pad.lab2.common.commands.io.Delimiters;
import com.emirovschi.pad.lab2.common.commands.io.SocketStreamProcessor;
import com.emirovschi.pad.lab2.common.commands.io.StreamCommandHandler;
import com.emirovschi.pad.lab2.common.commands.io.StreamProcessor;
import com.emirovschi.pad.lab2.common.io.Connection;
import com.emirovschi.pad.lab2.common.io.SocketConnection;
import com.emirovschi.pad.lab2.common.io.ConnectionFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class ClientConnectionFactory extends ConnectionFactory
{
    public ClientConnectionFactory(final ExecutorService executorService, final CommandFactory commandFactory, final CommandProcessor commandProcessor)
    {
        super(executorService, commandFactory, commandProcessor);
    }

    public ClientConnectionFactory(final ExecutorService executorService, final CommandProcessor commandProcessor, final CommandFactory commandFactory, final Delimiters delimiters)
    {
        super(executorService, commandProcessor, commandFactory, delimiters);
    }

    @Override
    public Connection create(final Socket socket) throws IOException
    {
        final StreamProcessor streamProcessor = delimiters == null
            ? new SocketStreamProcessor(socket) : new SocketStreamProcessor(delimiters, socket);
        final CommandHandler commandHandler = new StreamCommandHandler(streamProcessor, commandFactory);
        final Connection connection = new SocketConnection(executorService, commandProcessor, commandHandler, socket);

        final ConnectResponseCommand response = commandHandler.receive();

        if (!response.isSuccessfull())
        {
            throw new IOException("Server is full. Please try again later");
        }

        return connection;
    }
}
