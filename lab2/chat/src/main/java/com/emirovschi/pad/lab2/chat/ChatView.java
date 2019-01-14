package com.emirovschi.pad.lab2.chat;

import com.emirovschi.pad.lab2.chat.data.ChatMessage;
import com.emirovschi.pad.lab2.common.util.ThrowableConsumer;

import java.util.List;
import java.util.function.Consumer;

public interface ChatView
{
    void addMessage(ChatMessage chatMessage);

    void setMessages(final List<ChatMessage> messages);

    void onSend(final ThrowableConsumer<ChatMessage, Exception> onSend);

    void onConnect(final ThrowableConsumer<String, Exception> onConnect);

    void onDisconnect(final ThrowableConsumer<String, Exception> onDisconnect);

    void onChannelChange(final Consumer<String> onChannelChange);
}
