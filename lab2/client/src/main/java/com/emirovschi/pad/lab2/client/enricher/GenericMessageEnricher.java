package com.emirovschi.pad.lab2.client.enricher;

import com.emirovschi.pad.lab2.common.data.Message;

public abstract class GenericMessageEnricher<T> implements MessageEnricher
{
    private final Class<T> tClass;

    public GenericMessageEnricher(final Class<T> tClass)
    {
        this.tClass = tClass;
    }

    @Override
    public boolean shouldApply(final Message message)
    {
        return tClass.isInstance(message.getMessage());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Message apply(final Message message)
    {
        return applyInternal((Message<T>) message);
    }

    protected abstract Message<T> applyInternal(final Message<T> message);
}
