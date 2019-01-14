package com.emirovschi.pad.lab2.common.commands.io;

import com.emirovschi.pad.lab2.common.commands.CommandFactory;
import com.emirovschi.pad.lab2.common.commands.CommandHandler;
import com.emirovschi.pad.lab2.common.commands.data.Command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StreamCommandHandler implements CommandHandler
{
    public static final String FIELD_DELIMITER = "fieldDelimiter";
    public static final String VALUE_DELIMITER = "valueDelimiter";
    public static final String PROPERTY_DELIMITER = "propertyDelimiter";

    private final StreamProcessor streamProcessor;
    private final CommandFactory commandFactory;

    public StreamCommandHandler(final StreamProcessor streamProcessor, final CommandFactory commandFactory)
    {
        this.streamProcessor = streamProcessor;
        this.commandFactory = commandFactory;
    }

    @Override
    public <T extends Command> T receive() throws IOException
    {
        final ReadResult commandTypeReadResult = streamProcessor.read(FIELD_DELIMITER);
        final String commandType = new String(commandTypeReadResult.getData());

        final Map<String, byte[]> data = new HashMap<>();
        boolean endOfCommand = false;

        while (!endOfCommand)
        {
            final ReadResult keyReadResult = streamProcessor.read(VALUE_DELIMITER);
            endOfCommand = keyReadResult.isEndOfCommand();

            if (!endOfCommand)
            {
                final ReadResult valueReadResult = streamProcessor.read(PROPERTY_DELIMITER);
                data.put(new String(keyReadResult.getData()), valueReadResult.getData());
                endOfCommand = valueReadResult.isEndOfCommand();
            }
        }

        return commandFactory.createCommand(commandType, data);
    }

    @Override
    public void send(final Command command) throws IOException
    {
        streamProcessor.write(command.getCommandType().getBytes(), FIELD_DELIMITER);

        for (final Map.Entry<String, byte[]> entry : command.getData().entrySet())
        {
            streamProcessor.write(entry.getKey().getBytes(), VALUE_DELIMITER);
            streamProcessor.write(entry.getValue(), PROPERTY_DELIMITER);
        }

        streamProcessor.flush();
    }
}
