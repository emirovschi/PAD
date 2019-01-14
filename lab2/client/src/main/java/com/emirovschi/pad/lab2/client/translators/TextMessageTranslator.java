package com.emirovschi.pad.lab2.client.translators;

import java.nio.charset.Charset;

public class TextMessageTranslator extends AbstractMessageTranslator
{
    public TextMessageTranslator()
    {
        super("application/text");
    }

    @Override
    protected <T> byte[] translateData(final T object)
    {
        return object.toString().getBytes();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T translateData(final byte[] data, final Class<T> type)
    {
        if (type != String.class)
        {
            throw new RuntimeException("Text translator can convert only string objects");
        }
        return (T) new String(data, Charset.forName("utf8"));
    }
}
