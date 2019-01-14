package com.emirovschi.pad.lab2.common.commands.io;

import com.emirovschi.pad.lab2.common.commands.CommandFactory;
import com.emirovschi.pad.lab2.common.commands.data.Command;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static com.emirovschi.pad.lab2.common.commands.io.SocketStreamProcessor.DEFAULT_DELIMITERS;
import static org.junit.Assert.assertEquals;

public class StreamCommandHandlerTest
{
    private final Delimiters delimiters = new Delimiters(DEFAULT_DELIMITERS);
    private final QueueInputStream input = new QueueInputStream();
    private final QueueOutputStream output = new QueueOutputStream();
    private final StreamProcessor streamProcessor = new GenericStreamProcessor(delimiters, input, output);
    private final CommandFactory commandFactory = new CommandFactory()
    {
        @Override
        public <T extends Command> T createCommand(final String type, final Map<String, byte[]> data)
        {
            return (T) new TestCommand(type, data);
        }
    };
    private final StreamCommandHandler handler = new StreamCommandHandler(streamProcessor, commandFactory);

    @Test
    public void shouldSendAndReceiveCommands() throws Exception
    {
        final Map<String, byte[]> testData1 = ImmutableMap.<String, byte[]>builder()
            .put("key11", "value11".getBytes())
            .put("key12", "value12".getBytes())
            .put("key13", new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
            .build();

        final Map<String, byte[]> testData2 = ImmutableMap.<String, byte[]>builder()
            .put("key21", "value21".getBytes())
            .put("key22", "value22".getBytes())
            .put("key23", new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
            .build();

        final TestCommand command1 = new TestCommand("test1", testData1);
        final TestCommand command2 = new TestCommand("test2", testData2);
        handler.send(command1);
        handler.send(command2);
        input.write(output.read());
        final TestCommand result1 = handler.receive();
        final TestCommand result2 = handler.receive();

        assertEquals(command1, result1);
        assertEquals(command2, result2);
    }

    private static class TestCommand implements Command
    {
        private String commandType;
        private Map<String, byte[]> data;

        public TestCommand(final String commandType, final Map<String, byte[]> data)
        {
            this.commandType = commandType;
            this.data = data;
        }

        @Override
        public String getCommandType()
        {
            return commandType;
        }

        @Override
        public Map<String, byte[]> getData()
        {
            return data;
        }

        @Override
        public boolean equals(final Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (!(o instanceof TestCommand))
            {
                return false;
            }
            final TestCommand command = (TestCommand) o;
            return Objects.equals(getCommandType(), command.getCommandType()) &&
                Objects.equals(getData().keySet(), command.getData().keySet()) &&
                equalsData(getData(), command.getData());
        }

        private boolean equalsData(final Map<String, byte[]> data, final Map<String, byte[]> other)
        {
            for (final String key : data.keySet())
            {
                if (!Arrays.equals(data.get(key), other.get(key)))
                {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(getCommandType(), getData());
        }
    }
}