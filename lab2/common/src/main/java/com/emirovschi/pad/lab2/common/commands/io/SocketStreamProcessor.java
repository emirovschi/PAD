package com.emirovschi.pad.lab2.common.commands.io;

import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import static com.emirovschi.pad.lab2.common.commands.io.StreamCommandHandler.FIELD_DELIMITER;
import static com.emirovschi.pad.lab2.common.commands.io.StreamCommandHandler.PROPERTY_DELIMITER;
import static com.emirovschi.pad.lab2.common.commands.io.StreamCommandHandler.VALUE_DELIMITER;

public class SocketStreamProcessor extends GenericStreamProcessor
{
    public static final Map<String, Integer> DEFAULT_DELIMITERS = ImmutableMap.<String, Integer>builder()
        .put(FIELD_DELIMITER, 3)
        .put(VALUE_DELIMITER, 4)
        .put(PROPERTY_DELIMITER, 5)
        .build();

    public SocketStreamProcessor(final Socket socket) throws IOException
    {
        this(new Delimiters(DEFAULT_DELIMITERS), socket);
    }

    public SocketStreamProcessor(final Delimiters delimiters, final Socket socket) throws IOException
    {
        super(delimiters, socket.getInputStream(), socket.getOutputStream());
    }
}
