package com.emirovschi.pad.lab2.client;

import com.emirovschi.pad.lab2.client.enricher.MessageEnricher;
import com.emirovschi.pad.lab2.common.data.Message;

import java.io.IOException;
import java.util.function.Consumer;

public interface MessageService
{
    <T> void publish(String exchange, Message<T> message) throws IOException;

    <T> void publish(String exchange, Message<T> message, String routingKey) throws IOException;

    <T> void subscribe(String queue, String tag, Consumer<Message<T>> consumer, Class<T> type) throws IOException;

    <T> void consume(String queue, Consumer<Message<T>> consumer, Class<T> type) throws IOException;

    <T> void listen(String queue, String tag, Consumer<Message<T>> consumer, Class<T> type) throws IOException;

    <T> void enrich(final MessageEnricher messageEnricher);
}
