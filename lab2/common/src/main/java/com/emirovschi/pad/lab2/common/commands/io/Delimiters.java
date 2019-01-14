package com.emirovschi.pad.lab2.common.commands.io;

import java.io.IOException;
import java.util.Map;

import static java.util.Collections.emptyMap;

public class Delimiters
{
    private final int escape;
    private final int endDelimiter;
    private final Map<String, Integer> otherDelimiters;

    public Delimiters()
    {
        this(emptyMap());
    }

    public Delimiters(final Map<String, Integer> otherDelimiters)
    {
        this(1, 2, otherDelimiters);
    }

    public Delimiters(final int escape, final int endDelimiter, final Map<String, Integer> otherDelimiters)
    {
        this.escape = escape;
        this.endDelimiter = endDelimiter;
        this.otherDelimiters = otherDelimiters;
    }

    public int getEscape()
    {
        return escape;
    }

    public int getEndDelimiter()
    {
        return endDelimiter;
    }

    public int getDelimiter(final String delimiter) throws IOException
    {
        if (!otherDelimiters.containsKey(delimiter))
        {
            throw new IOException("Unknown delimiter");
        }
        return otherDelimiters.get(delimiter);
    }

    public boolean isDelimiter(final int delimiter)
    {
        return escape == delimiter || endDelimiter == delimiter || otherDelimiters.containsValue(delimiter);
    }
}
