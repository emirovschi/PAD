package com.emirovschi.pad.lab2.client;

import com.emirovschi.pad.lab2.client.enricher.MessageEnricher;
import com.emirovschi.pad.lab2.client.translators.MessageTranslator;
import com.emirovschi.pad.lab2.common.data.ByteMessage;
import com.emirovschi.pad.lab2.common.data.Message;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class DefaultMessageService implements MessageService
{
    private final Client client;
    private final MessageTranslator messageTranslator;
    private List<MessageEnricher> messageEnrichers;

    public DefaultMessageService(final Client client, final MessageTranslator messageTranslator)
    {
        this.client = client;
        this.messageTranslator = messageTranslator;
        this.messageEnrichers = new LinkedList<>();
    }

    @Override
    public <T> void publish(final String exchange, final Message<T> message) throws IOException
    {
        client.publish(exchange, serialize(message));
    }

    @Override
    public <T> void publish(final String exchange, final Message<T> message, final String routingKey) throws IOException
    {
        client.publish(exchange, serialize(message), routingKey);
    }

    private <T> ByteMessage serialize(final Message<T> message) throws IOException
    {
        final Iterator<MessageEnricher> iterator = messageEnrichers.stream()
            .filter(enricher -> enricher.shouldApply(message))
            .iterator();

        return Stream.<Message>iterate(message, m -> iterator.next().apply(m))
            .filter(m -> !iterator.hasNext())
            .findFirst()
            .map(messageTranslator::serialize)
            .orElseThrow(() -> new IOException("Could not send null message"));
    }

    @Override
    public <T> void subscribe(final String queue, final String tag, final Consumer<Message<T>> consumer, final Class<T> type) throws IOException
    {
        client.subscribe(queue, tag, message -> consumer.accept(messageTranslator.deserialize(message, type)));
    }

    @Override
    public <T> void consume(final String queue, final Consumer<Message<T>> consumer, final Class<T> type) throws IOException
    {
        client.consume(queue, messages -> consumer.accept(messageTranslator.deserialize(messages, type)));
    }

    @Override
    public <T> void listen(final String queue, final String tag, final Consumer<Message<T>> consumer, final Class<T> type) throws IOException
    {
        client.listen(queue, tag, message -> consumer.accept(messageTranslator.deserialize(message, type)));
    }

    @Override
    public <T> void enrich(final MessageEnricher messageEnricher)
    {
        messageEnrichers.add(messageEnricher);
    }

    private <T> void consumeMessages(final Consumer<Message<T>> consumer, final List<ByteMessage> messages, final Class<T> type)
    {
        messages.stream().map(message -> messageTranslator.deserialize(message, type)).forEach(consumer);
    }
}
