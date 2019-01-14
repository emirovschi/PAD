package com.emirovschi.pad.lab2.common.commands.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GenericStreamProcessor implements StreamProcessor
{
    private final Delimiters delimiters;
    private final InputStream input;
    private final OutputStream output;

    public GenericStreamProcessor(final Delimiters delimiters, final InputStream input, final OutputStream output)
    {
        this.delimiters = delimiters;
        this.input = input;
        this.output = output;
    }

    @Override
    public ReadResult read(final String delimiter) throws IOException
    {
        int data;
        boolean toEscape = false;
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        final int currentDelimiter = delimiters.getDelimiter(delimiter);
        final int endDelimiter = delimiters.getEndDelimiter();
        final int escape = delimiters.getEscape();

        while (((data = input.read()) != currentDelimiter && data != endDelimiter || toEscape) && data != -1)
        {
            if (toEscape && (data == currentDelimiter || data == endDelimiter || data == escape))
            {
                buffer.write(data);
            }
            else if (data != escape)
            {
                buffer.write(data);
            }

            toEscape = (!toEscape || data != escape) && data == escape;
        }

        if (data == -1)
        {
            throw new IOException("Client disconnected");
        }

        return new ReadResult(buffer.toByteArray(), data == endDelimiter);
    }

    @Override
    public void write(final byte[] data, final String delimiter) throws IOException
    {
        for (final byte b : data)
        {
            if (delimiters.isDelimiter(b))
            {
                output.write(delimiters.getEscape());
            }
            output.write(b);
        }

        output.write(delimiters.getDelimiter(delimiter));
    }

    @Override
    public void flush() throws IOException
    {
        output.write(delimiters.getEndDelimiter());
        output.flush();
    }
}
