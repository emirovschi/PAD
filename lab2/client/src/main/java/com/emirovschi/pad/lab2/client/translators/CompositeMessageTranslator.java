package com.emirovschi.pad.lab2.client.translators;

import com.emirovschi.pad.lab2.common.data.ByteMessage;
import com.emirovschi.pad.lab2.common.data.Message;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.emirovschi.pad.lab2.client.translators.AbstractMessageTranslator.CONTENT_TYPE;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class CompositeMessageTranslator implements MessageTranslator
{
    private final MessageTranslator defaultTranslator;
    private final Map<String, MessageTranslator> translators;

    public CompositeMessageTranslator(final List<AbstractMessageTranslator> messageTranslators)
    {
        this.defaultTranslator = messageTranslators.stream().findFirst().orElseThrow(() -> new RuntimeException("No translators"));
        this.translators = messageTranslators.stream()
            .collect(toMap(AbstractMessageTranslator::getContentType, identity()));
    }

    @Override
    public ByteMessage serialize(final Message message)
    {
        return getMessageTranslator(message)
            .orElse(defaultTranslator)
            .serialize(message);
    }

    @Override
    public <T> Message<T> deserialize(final ByteMessage message, final Class<T> type)
    {
        return getMessageTranslator(message)
            .orElseThrow(() -> new RuntimeException("Unknown content type"))
            .deserialize(message, type);
    }

    private Optional<MessageTranslator> getMessageTranslator(final Message message)
    {
        return ofNullable(message.getProperties().get(CONTENT_TYPE))
            .map(Object::toString)
            .map(translators::get);
    }
}
