package com.emirovschi.pad.lab2.client.translators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.IOException;

public class YamlMessageTranslator extends AbstractMessageTranslator
{
    private final ObjectMapper objectMapper = new YAMLMapper();

    public YamlMessageTranslator()
    {
        super("application/yaml");
    }

    @Override
    protected <T> byte[] translateData(final T data)
    {
        try
        {
            return objectMapper.writeValueAsBytes(data);
        }
        catch (final JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected <T> T translateData(final byte[] data, final Class<T> type)
    {
        try
        {
            return objectMapper.readValue(data, type);
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
