package com.emirovschi.pad.lab2.client.translators;

import com.emirovschi.pad.lab2.common.data.ByteMessage;
import com.emirovschi.pad.lab2.common.data.Message;

public interface MessageTranslator
{
    ByteMessage serialize(final Message object);

    <T> Message<T> deserialize(ByteMessage message, Class<T> type);
}
