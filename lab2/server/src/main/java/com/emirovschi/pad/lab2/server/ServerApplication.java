package com.emirovschi.pad.lab2.server;

import com.emirovschi.pad.lab2.common.commands.AbstractDelegatedCommandProcessor;
import com.emirovschi.pad.lab2.common.commands.CommandFactory;
import com.emirovschi.pad.lab2.common.commands.DelegateCommandProcessor;
import com.emirovschi.pad.lab2.common.commands.ReflectionCommandFactory;
import com.emirovschi.pad.lab2.common.commands.CommandProcessor;
import com.emirovschi.pad.lab2.common.broker.BrokerService;
import com.emirovschi.pad.lab2.common.io.ConnectionFactory;
import com.emirovschi.pad.lab2.server.broker.InMemoryBrokerService;
import com.emirovschi.pad.lab2.server.commands.BindCommandProcessor;
import com.emirovschi.pad.lab2.server.commands.BindDeadLettersCommandProcessor;
import com.emirovschi.pad.lab2.server.commands.DeclareExchangeCommandProcessor;
import com.emirovschi.pad.lab2.server.commands.DeclareQueueCommandProcessor;
import com.emirovschi.pad.lab2.server.commands.DeleteBindCommandProcessor;
import com.emirovschi.pad.lab2.server.commands.DeleteDeadLettersBindCommandProcessor;
import com.emirovschi.pad.lab2.server.commands.DeleteExchangeCommandProcessor;
import com.emirovschi.pad.lab2.server.commands.DeleteQueueCommandProcessor;
import com.emirovschi.pad.lab2.server.commands.ListenCommandProcessor;
import com.emirovschi.pad.lab2.server.commands.SendMessageCommandProcessor;
import com.emirovschi.pad.lab2.server.commands.SubscribeCommandProcessor;
import com.emirovschi.pad.lab2.server.commands.ConsumeMessageCommandProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApplication
{
    public static void main(String[] args)
    {
        final ExecutorService executorService = new CustomThreadPoolExecutor(SocketServer.MAX_CONNECTIONS);
        final ExecutorService clientExecutorService = Executors.newFixedThreadPool(200);
        final CommandFactory commandFactory = new ReflectionCommandFactory();

        final BrokerService brokerService = new InMemoryBrokerService(clientExecutorService);

        final Collection<AbstractDelegatedCommandProcessor> commandProcessors = new ArrayList<>();
        commandProcessors.add(new DeclareExchangeCommandProcessor(brokerService));
        commandProcessors.add(new DeclareQueueCommandProcessor(brokerService));
        commandProcessors.add(new BindCommandProcessor(brokerService));
        commandProcessors.add(new BindDeadLettersCommandProcessor(brokerService));

        commandProcessors.add(new DeleteExchangeCommandProcessor(brokerService));
        commandProcessors.add(new DeleteQueueCommandProcessor(brokerService));
        commandProcessors.add(new DeleteBindCommandProcessor(brokerService));
        commandProcessors.add(new DeleteDeadLettersBindCommandProcessor(brokerService));

        commandProcessors.add(new SendMessageCommandProcessor(brokerService));
        commandProcessors.add(new SubscribeCommandProcessor(brokerService));
        commandProcessors.add(new ConsumeMessageCommandProcessor(brokerService));
        commandProcessors.add(new ListenCommandProcessor(brokerService));

        final CommandProcessor commandProcessor = new DelegateCommandProcessor(commandProcessors);

        final ConnectionFactory connectionFactory = new ConnectionFactory(clientExecutorService, commandFactory, commandProcessor);

        try (final Server server = new SocketServer(27015, executorService, connectionFactory))
        {
            server.run();
        }
        catch (IOException e)
        {
            System.out.println("Server error:" + e.getMessage());
        }
        finally
        {
            clientExecutorService.shutdown();
        }
    }
}
