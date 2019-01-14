package com.emirovschi.pad.lab2.client;

import com.emirovschi.pad.lab2.client.translators.AbstractMessageTranslator;
import com.emirovschi.pad.lab2.client.translators.CompositeMessageTranslator;
import com.emirovschi.pad.lab2.client.translators.JsonMessageTranslator;
import com.emirovschi.pad.lab2.client.translators.MessageTranslator;
import com.emirovschi.pad.lab2.client.translators.TextMessageTranslator;
import com.emirovschi.pad.lab2.client.translators.XmlMessageTranslator;
import com.emirovschi.pad.lab2.client.translators.YamlMessageTranslator;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class MessageServiceFactory
{
    private MessageTranslator messageTranslator;

    public MessageServiceFactory()
    {
        final List<AbstractMessageTranslator> messageTranslators = ImmutableList.<AbstractMessageTranslator>builder()
            .add(new JsonMessageTranslator())
            .add(new XmlMessageTranslator())
            .add(new YamlMessageTranslator())
            .add(new TextMessageTranslator())
            .build();

        messageTranslator = new CompositeMessageTranslator(messageTranslators);
    }

    public MessageService create(final Client client)
    {
        return new DefaultMessageService(client, messageTranslator);
    }

    public void setMessageTranslator(final MessageTranslator messageTranslator)
    {
        this.messageTranslator = messageTranslator;
    }

}
