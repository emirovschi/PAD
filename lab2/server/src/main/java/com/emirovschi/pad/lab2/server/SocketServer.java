package com.emirovschi.pad.lab2.server;

import com.emirovschi.pad.lab2.common.commands.data.ConnectResponseCommand;
import com.emirovschi.pad.lab2.common.io.Connection;
import com.emirovschi.pad.lab2.common.io.ConnectionFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

public class SocketServer implements Server
{
    public static final int MAX_CONNECTIONS = 20;

    private final int port;
    private final ExecutorService executorService;
    private final ConnectionFactory connectionFactory;

    private ServerSocket serverSocket;

    public SocketServer(
        final int port,
        final ExecutorService executorService,
        final ConnectionFactory connectionFactory)
    {
        this.port = port;
        this.executorService = executorService;
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void run()
    {
        try
        {
            internalRun();
        }
        catch (final Exception exception)
        {
            System.out.println("An error occurred while starting the client: " + exception.getMessage());
        }
    }

    private void internalRun() throws IOException
    {
        serverSocket = new ServerSocket(port, MAX_CONNECTIONS);
        System.out.println("Server listening on port " + port);

        while (!serverSocket.isClosed())
        {
            accept();
        }
    }

    private void accept()
    {
        try
        {
            final Socket socket = serverSocket.accept();
            System.out.println("Client connected " + socket.toString());
            final Connection connection = connectionFactory.create(socket);

            try
            {
                executorService.execute(() -> handleConnection(connection));
            }
            catch (final RejectedExecutionException e)
            {
                System.out.println("Server full, rejecting:" + socket);
                connection.send(new ConnectResponseCommand(false));
            }
        }
        catch (final IOException e)
        {
            System.out.println("Client connection failed:" + e.getMessage());
        }
    }

    private void handleConnection(final Connection connection)
    {
        try
        {
            connection.send(new ConnectResponseCommand(true));
            connection.run();
        }
        catch (final IOException e)
        {
            System.out.println("An error occurred during while handling client [" + connection +"]: " + e.getMessage());
        }
    }

    @Override
    public void close() throws IOException
    {
        if (serverSocket == null)
        {
            throw new IOException("Failed to stop server as it was not started");
        }

        serverSocket.close();
    }
}
