package com.emirovschi.pad.lab2.chat;

import com.emirovschi.pad.lab2.chat.data.ChatMessage;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Consumer;

public interface ChatClient extends Closeable
{
    void connect() throws IOException;

    void register(final String name);

    void joinChannel(final String channel) throws IOException;

    void leaveChannel(final String channel) throws IOException;

    void sendMessage(final ChatMessage message) throws IOException;

    void onMessage(final Consumer<ChatMessage> message);

    void onError(final Consumer<String> onError);
}