package com.emirovschi.pad.lab2.client;

import com.emirovschi.pad.lab2.common.commands.CommandConsumer;
import com.emirovschi.pad.lab2.common.commands.data.BindCommand;
import com.emirovschi.pad.lab2.common.commands.data.BindDeadLettersCommand;
import com.emirovschi.pad.lab2.common.commands.data.ConfirmActionCommand;
import com.emirovschi.pad.lab2.common.commands.data.DeclareExchangeCommand;
import com.emirovschi.pad.lab2.common.commands.data.DeclareQueueCommand;
import com.emirovschi.pad.lab2.common.commands.data.DeleteBindCommand;
import com.emirovschi.pad.lab2.common.commands.data.DeleteDeadLettersBindCommand;
import com.emirovschi.pad.lab2.common.commands.data.DeleteExchangeCommand;
import com.emirovschi.pad.lab2.common.commands.data.DeleteQueueCommand;
import com.emirovschi.pad.lab2.common.commands.data.ListenCommand;
import com.emirovschi.pad.lab2.common.commands.data.MessageCommand;
import com.emirovschi.pad.lab2.common.commands.data.ReplyCommand;
import com.emirovschi.pad.lab2.common.commands.data.SendMessageCommand;
import com.emirovschi.pad.lab2.common.commands.data.SubscribeCommand;
import com.emirovschi.pad.lab2.common.commands.data.ConsumeMessageCommand;
import com.emirovschi.pad.lab2.common.data.ByteMessage;
import com.emirovschi.pad.lab2.common.io.Connection;
import com.emirovschi.pad.lab2.common.io.ConnectionFactory;
import com.emirovschi.pad.lab2.common.util.ThrowableConsumer;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SocketClient implements Client
{
    private final String serverIp;
    private final int serverPort;
    private final ConnectionFactory connectionFactory;
    private final Map<String, CommandConsumer> commandConsumers;

    private Connection connection;

    public SocketClient(
        final String serverIp,
        final int serverPort,
        final ConnectionFactory connectionFactory)
    {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.connectionFactory = connectionFactory;
        this.commandConsumers = new HashMap<>();
    }

    @Override
    public void connect() throws IOException
    {
        try
        {
            final Socket socket = new Socket(serverIp, serverPort);
            connection = connectionFactory.create(socket);
            new Thread(connection).start();
        }
        catch (final Exception exception)
        {
            System.out.println("An error occurred with [" + connection + "]: " + exception.getMessage());
            throw new IOException("Could not connect to the server", exception);
        }
    }

    @Override
    public void onError(final Consumer<String> onError)
    {
        connection.onError(onError);
    }

    @Override
    public void declareExchange(final String name) throws IOException
    {
        connection.sendAndConfirm(new DeclareExchangeCommand(name));
    }

    @Override
    public void declareQueue(final String name) throws IOException
    {
        connection.sendAndConfirm(new DeclareQueueCommand(name));
    }

    @Override
    public void bind(final String exchange, final String queue, final String routingKey) throws IOException
    {
        connection.sendAndConfirm(new BindCommand(exchange, queue, routingKey));
    }

    @Override
    public void bindDeadLetters(final String exchange, final String queue, final String routingKey) throws IOException
    {
        connection.sendAndConfirm(new BindDeadLettersCommand(exchange, queue, routingKey));
    }

    @Override
    public void deleteExchange(final String name) throws IOException
    {
        connection.sendAndConfirm(new DeleteExchangeCommand(name));
    }

    @Override
    public void deleteQueue(final String name) throws IOException
    {
        connection.sendAndConfirm(new DeleteQueueCommand(name));
    }

    @Override
    public void deleteBind(final String exchange, final String queue, final String routingKey) throws IOException
    {
        connection.sendAndConfirm(new DeleteBindCommand(exchange, queue, routingKey));
    }

    @Override
    public void deleteDeadLettersBind(final String exchange, final String queue, final String routingKey) throws IOException
    {
        connection.sendAndConfirm(new DeleteDeadLettersBindCommand(exchange, queue, routingKey));
    }

    @Override
    public void publish(final String exchange, final ByteMessage message) throws IOException
    {
        connection.sendAndConfirm(new SendMessageCommand(exchange, message));
    }

    @Override
    public void publish(final String exchange, final ByteMessage message, final String routingKey) throws IOException
    {
        connection.sendAndConfirm(new SendMessageCommand(exchange, message, routingKey));
    }

    @Override
    public void subscribe(final String queue, final String tag, final ThrowableConsumer<ByteMessage, IOException> consumer) throws IOException
    {
        final SubscribeCommand subscribe = new SubscribeCommand(queue);
        this.<MessageCommand>registerConsumer(subscribe, tag, command -> consumer.accept(command.getMessage()));
    }

    @Override
    public void consume(final String queue, ThrowableConsumer<ByteMessage, IOException> consumer) throws IOException
    {
        final ConsumeMessageCommand subscribe = new ConsumeMessageCommand(queue);
        connection.<MessageCommand>requestAsync(subscribe).thenAccept(message -> {
            try
            {
                consumer.accept(message.getMessage());
                connection.send(new ConfirmActionCommand(message));
            }
            catch (IOException e)
            {
                try
                {
                    connection.send(new ConfirmActionCommand(message, "Failed to consume message"));
                }
                catch (IOException e1)
                {
                    System.out.println("Connection lost");
                }
            }
        });
    }

    @Override
    public void listen(final String queue, final String tag, final ThrowableConsumer<ByteMessage, IOException> consumer) throws IOException
    {
        final ListenCommand subscribe = new ListenCommand(queue, tag);
        this.<MessageCommand>registerConsumer(subscribe, tag, command -> consumer.accept(command.getMessage()));
    }

    @Override
    public void stopConsumer(final String tag) throws IOException
    {
        if (commandConsumers.containsKey(tag))
        {
            commandConsumers.get(tag).stop();
            commandConsumers.remove(tag);
        }
    }

    private <T extends ReplyCommand> void registerConsumer(
        final ReplyCommand subscribe, final String tag, final ThrowableConsumer<T, IOException> consumer) throws IOException
    {
        commandConsumers.put(tag, connection.openInbox(subscribe, consumer));
    }

    @Override
    public void close() throws IOException
    {
        connection.close();
    }
}
