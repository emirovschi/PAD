package com.emirovschi.pad.lab2.common.commands;

import com.emirovschi.pad.lab2.common.commands.data.ReplyCommand;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Consumer;

public interface CommandConsumer<T extends ReplyCommand> extends Consumer<T>, Closeable, Runnable
{
    void stop() throws IOException;
}
