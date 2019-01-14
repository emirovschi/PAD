package com.emirovschi.pad.lab2.common.broker;

import com.emirovschi.pad.lab2.common.data.ByteMessage;
import com.emirovschi.pad.lab2.common.util.ThrowableConsumer;

import java.io.IOException;
import java.util.List;

public interface BrokerService
{
    void declareExchange(String name) throws IOException;

    void declareQueue(String name) throws IOException;

    void bind(String exchange, String queue, String routingKey) throws IOException;

    void bindDeadLetters(String exchange, String queue, String routingKey) throws IOException;

    void deleteExchange(String name) throws IOException;

    void deleteQueue(String name) throws IOException;

    void deleteBind(String exchange, String queue, String routingKey) throws IOException;

    void deleteDeadLettersBind(String exchange, String queue, final String routingKey) throws IOException;

    void publish(String exchange, ByteMessage message) throws IOException;

    void publish(String exchange, ByteMessage message, final String routingKey) throws IOException;

    void subscribe(String queue, String tag, ThrowableConsumer<ByteMessage, IOException> consumer) throws IOException;

    void consume(String queue, ThrowableConsumer<ByteMessage, IOException> consumer) throws IOException;

    void listen(String queue, String tag, ThrowableConsumer<ByteMessage, IOException> consumer) throws IOException;

    void stopConsumer(String tag) throws IOException;
}
