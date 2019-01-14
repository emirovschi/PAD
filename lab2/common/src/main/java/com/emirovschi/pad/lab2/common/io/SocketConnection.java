package com.emirovschi.pad.lab2.common.io;

import com.emirovschi.pad.lab2.common.commands.BlockingQueueCommandConsumer;
import com.emirovschi.pad.lab2.common.commands.CommandConsumer;
import com.emirovschi.pad.lab2.common.commands.CommandHandler;
import com.emirovschi.pad.lab2.common.commands.CommandProcessor;
import com.emirovschi.pad.lab2.common.commands.data.Command;
import com.emirovschi.pad.lab2.common.commands.data.ConfirmActionCommand;
import com.emirovschi.pad.lab2.common.commands.data.ReplyCommand;
import com.emirovschi.pad.lab2.common.util.ThrowableConsumer;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class SocketConnection implements Connection
{
    private static final ThreadLocal<AtomicLong> CONNECTION_ID = ThreadLocal.withInitial(AtomicLong::new);

    private final Long id;
    private Consumer<String> onError;
    private final ExecutorService executorService;
    private final CommandProcessor commandProcessor;
    private final CommandHandler commandHandler;
    private final Socket socket;
    private final Map<Long, CompletableFuture<ReplyCommand>> requests;
    private final Map<Long, CommandConsumer<ReplyCommand>> commandConsumers;

    public SocketConnection(
        final ExecutorService executorService,
        final CommandProcessor commandProcessor,
        final CommandHandler commandHandler,
        final Socket socket)
    {
        this.id = CONNECTION_ID.get().getAndIncrement();
        this.executorService = executorService;
        this.commandProcessor = commandProcessor;
        this.commandHandler = commandHandler;
        this.socket = socket;
        this.requests = new HashMap<>();
        this.commandConsumers = new HashMap<>();
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
            System.out.println("An error occurred with client [" + socket + "]: " + exception.getMessage());
            if (onError != null)
            {
                onError.accept(exception.getMessage());
            }
        }
    }

    private void internalRun() throws IOException
    {
        while (isConnected())
        {
            final Command command = commandHandler.receive();
            System.out.println("Got command [" + command.getCommandType() + "] from [" + socket + "]");

            final Optional<ReplyCommand> reply = Optional.of(command)
                .filter(ReplyCommand.class::isInstance)
                .map(ReplyCommand.class::cast)
                .filter(replyCommand -> replyCommand.getReplyId() != null);

            if (reply.isPresent())
            {
                processReply(reply.get());
            }
            else
            {
                commandProcessor.process(this, command);
            }
        }
    }

    private void processReply(final ReplyCommand replyCommand) throws IOException
    {
        if (requests.containsKey(replyCommand.getReplyId()))
        {
            requests.get(replyCommand.getReplyId()).complete(replyCommand);
            requests.remove(replyCommand.getReplyId());
        }
        else if (commandConsumers.containsKey(replyCommand.getReplyId()))
        {
            commandConsumers.get(replyCommand.getReplyId()).accept(replyCommand);
        }
        else
        {
            send(new ConfirmActionCommand(replyCommand, "No request found for this reply"));
        }
    }

    @Override
    public boolean isConnected()
    {
        return socket.isConnected();
    }

    @Override
    public long getId()
    {
        return id;
    }

    @Override
    public void onError(final Consumer<String> onError)
    {
        this.onError = onError;
    }

    @Override
    public synchronized void send(final Command command) throws IOException
    {
        try
        {
            System.out.println("Sending command [" + command.getCommandType() + "] to [" + socket + "]");
            commandHandler.send(command);
        }
        catch (final IOException exception)
        {
            System.out.println("Could not send message to [" + socket + "]: " + exception.getMessage());
            closeQuietly();
            throw exception;
        }
    }

    @Override
    public void sendAndConfirm(final ReplyCommand command) throws IOException
    {
        final ConfirmActionCommand response = request(command);

        if (!response.isSuccessful())
        {
            throw new IOException(response.getErrorMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ReplyCommand> T request(ReplyCommand command) throws IOException
    {
        try
        {
            return this.<T>requestAsync(command).get();
        }
        catch (final InterruptedException e)
        {
            throw new IOException("Request was interrupted", e);
        }
        catch (final ExecutionException e)
        {
            throw new IOException("Request failed", e);
        }
    }

    @Override
    public <T extends ReplyCommand> CommandConsumer<T> openInbox(final ReplyCommand command, final ThrowableConsumer<T, IOException> consumer) throws IOException
    {
        sendAndConfirm(command);
        final CommandConsumer<T> commandConsumer = new BlockingQueueCommandConsumer<>(command, consumer, this);
        commandConsumers.put(command.getId(), (CommandConsumer<ReplyCommand>) commandConsumer);
        executorService.execute(commandConsumer);
        return commandConsumer;
    }

    @Override
    public void closeInbox(final ReplyCommand command) throws IOException
    {
        commandConsumers.remove(command.getId());
    }

    @Override
    public <T extends ReplyCommand> CompletableFuture<T> requestAsync(final ReplyCommand command) throws IOException
    {
        final CompletableFuture<T> completableFuture = new CompletableFuture<>();
        requests.put(command.getId(), (CompletableFuture<ReplyCommand>) completableFuture);
        send(command);
        return completableFuture;
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

    @Override
    public void close() throws IOException
    {
        new ArrayList<>(commandConsumers.values()).forEach(consumer ->
        {
            try
            {
                consumer.close();
            }
            catch (IOException e)
            {
                System.out.println("Failed to close consumer:" + e.getMessage());
            }
        });
        socket.close();
    }

    @Override
    public String toString()
    {
        return "Connection [" + socket.toString() + "]";
    }
}
