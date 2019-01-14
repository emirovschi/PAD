package com.emirovschi.pad.lab2.chat;

import com.emirovschi.pad.lab2.chat.data.ChatMessage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ChatController
{
    private final ChatClient client;
    private final ChatView chatView;
    private final Map<String, List<ChatMessage>> history;

    public ChatController(final ChatClient client, final ChatView chatView)
    {
        this.client = client;
        this.chatView = chatView;
        this.history = new HashMap<>();

        chatView.onConnect(this::connect);
        chatView.onDisconnect(this::disconnect);
        chatView.onChannelChange(this::changeChannel);
        chatView.onSend(this::send);

        client.onMessage(this::addMessage);
    }

    private void addMessage(final ChatMessage chatMessage)
    {
        if (!history.containsKey(chatMessage.getChannel()))
        {
            history.put(chatMessage.getChannel(), new LinkedList<>());
        }

        history.get(chatMessage.getChannel()).add(chatMessage);
        chatView.addMessage(chatMessage);
    }

    private void connect(final String channel) throws Exception
    {
        client.joinChannel(channel);
    }

    private void disconnect(final String channel) throws Exception
    {
        client.leaveChannel(channel);
        history.remove(channel);
    }

    private void changeChannel(final String channel)
    {
        chatView.setMessages(history.get(channel));
    }

    private void send(final ChatMessage message) throws Exception
    {
        client.sendMessage(message);
    }
}
