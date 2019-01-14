package com.emirovschi.pad.lab2.common.io;

import com.emirovschi.pad.lab2.common.commands.CommandConsumer;
import com.emirovschi.pad.lab2.common.commands.data.Command;
import com.emirovschi.pad.lab2.common.commands.data.ReplyCommand;
import com.emirovschi.pad.lab2.common.util.ThrowableConsumer;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface Connection extends Runnable, Closeable
{
    boolean isConnected();

    long getId();

    void onError(Consumer<String> onError);

    void send(Command command) throws IOException;

    void sendAndConfirm(ReplyCommand command) throws IOException;

    <T extends ReplyCommand> CompletableFuture<T> requestAsync(ReplyCommand command) throws IOException;

    <T extends ReplyCommand> T request(ReplyCommand command) throws IOException;

    <T extends ReplyCommand> CommandConsumer<T> openInbox(ReplyCommand command, ThrowableConsumer<T, IOException> consumer) throws IOException;

    void closeInbox(ReplyCommand command) throws IOException;
}
