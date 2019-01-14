package com.emirovschi.pad.lab2.client;

import com.emirovschi.pad.lab2.common.commands.CommandFactory;
import com.emirovschi.pad.lab2.common.commands.CommandProcessor;
import com.emirovschi.pad.lab2.common.commands.ReflectionCommandFactory;
import com.emirovschi.pad.lab2.common.commands.VoidCommandProcessor;
import com.emirovschi.pad.lab2.common.io.ConnectionFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketClientFactory
{
    private ConnectionFactory connectionFactory;
    private String serverIp;
    private int serverPort;

    public SocketClientFactory(final ExecutorService executorService)
    {
        final CommandProcessor commandProcessor = new VoidCommandProcessor();
        final CommandFactory commandFactory = new ReflectionCommandFactory();
        this.connectionFactory = new ClientConnectionFactory(executorService, commandFactory, commandProcessor);
    }

    public Client create()
    {
        return new SocketClient(serverIp, serverPort, connectionFactory);
    }

    public void setConnectionFactory(final ConnectionFactory connectionFactory)
    {
        this.connectionFactory = connectionFactory;
    }

    public void setServerIp(final String serverIp)
    {
        this.serverIp = serverIp;
    }

    public void setServerPort(final int serverPort)
    {
        this.serverPort = serverPort;
    }
}
