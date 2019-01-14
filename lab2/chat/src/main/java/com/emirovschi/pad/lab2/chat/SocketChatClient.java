package com.emirovschi.pad.lab2.chat;

import com.emirovschi.pad.lab2.chat.data.ChatMessage;
import com.emirovschi.pad.lab2.chat.data.User;
import com.emirovschi.pad.lab2.client.Client;
import com.emirovschi.pad.lab2.client.MessageService;
import com.emirovschi.pad.lab2.common.data.Message;

import java.io.IOException;
import java.util.Observable;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Consumer;

import static com.emirovschi.pad.lab2.chat.MessageNameEnricher.USER_ID;
import static com.emirovschi.pad.lab2.chat.MessageNameEnricher.USER_NAME;
import static com.emirovschi.pad.lab2.client.translators.AbstractMessageTranslator.CONTENT_TYPE;

public class SocketChatClient implements ChatClient
{
    private static final String APPLICATION_TEXT = "application/text";
    private static final String CHANNEL = "channel";
    private static final String PRIVATE_EXCHANGE = "private-messages";

    private final String id;
    private final Client client;
    private final MessageService messageService;
    private final Observable onMessage;

    public SocketChatClient(final Client client, final MessageService messageService)
    {
        this.client = client;
        this.messageService = messageService;
        this.id = UUID.randomUUID().toString();
        this.onMessage = new Observable()
        {
            @Override
            public void notifyObservers(final Object arg)
            {
                setChanged();
                super.notifyObservers(arg);
            }
        };
    }

    @Override
    public void connect() throws IOException
    {
        client.connect();
        client.declareQueue(id);

        try
        {
            client.declareExchange(PRIVATE_EXCHANGE);
        }
        catch (final IOException exception)
        {
            System.out.println("Private exchange is already created");
        }

        client.bind(PRIVATE_EXCHANGE, id, id);

        messageService.subscribe(id, id, onMessage::notifyObservers, String.class);
    }

    @Override
    public void register(final String name)
    {
        messageService.enrich(new MessageNameEnricher(id, name));
    }

    @Override
    public void joinChannel(final String channel) throws IOException
    {
        try
        {
            client.declareExchange(channel);
        }
        catch (final IOException exception)
        {
            System.out.println("Exchange already created");
        }

        client.bind(channel, id, ".*");
    }

    @Override
    public void leaveChannel(final String channel) throws IOException
    {
        client.deleteBind(channel, id, ".*");
    }

    @Override
    public void sendMessage(final ChatMessage message) throws IOException
    {
        final String replyTo = message.getReplyTo() != null ? message.getReplyTo() : UUID.randomUUID().toString();

        final Properties properties = new Properties();
        properties.setProperty(CONTENT_TYPE, APPLICATION_TEXT);
        properties.setProperty(CHANNEL, message.getChannel());

        final Message<String> request = new Message<>(message.getText(), properties, replyTo);

        try
        {
            messageService.publish(message.getChannel(), request);
        }
        catch (final IOException exception)
        {
            messageService.publish(PRIVATE_EXCHANGE, request, id);

            if (!id.equals(message.getChannel()))
            {
                properties.setProperty(CHANNEL, id);
                messageService.publish(PRIVATE_EXCHANGE, request, message.getChannel());
            }
        }
    }

    @Override
    public void onMessage(final Consumer<ChatMessage> message)
    {
        onMessage.addObserver((observable, object) -> message.accept(createMessage((Message<String>) object)));
    }

    private ChatMessage createMessage(final Message<String> message)
    {
        final String userId = message.getProperties().getProperty(USER_ID);
        final String userName = message.getProperties().getProperty(USER_NAME);
        final String channel = message.getProperties().getProperty(CHANNEL);
        final String text = message.getMessage();
        final String replyTo = message.getCorrelation();

        return new ChatMessage(new User(userId, userName), channel, text, replyTo);
    }

    @Override
    public void onError(final Consumer<String> onError)
    {
        client.onError(onError);
    }

    @Override
    public void close() throws IOException
    {
        client.stopConsumer(id);
        client.deleteBind(PRIVATE_EXCHANGE, id, id);
        client.deleteQueue(id);
        client.close();
    }
}
