package com.emirovschi.pad.lab2.common.commands.data;

import java.util.Map;

public class ListenCommand extends AbstractReplyCommand
{
    private static final String QUEUE = "exchange";
    private static final String TAG = "tag";

    public ListenCommand(final String queue, final String tag)
    {
        put(QUEUE, queue);
        put(TAG, tag);
    }

    public ListenCommand(final Map<String, byte[]> data)
    {
        super(data);
    }

    public String getQueue()
    {
        return getAsString(QUEUE);
    }

    public String getTag()
    {
        return getAsString(TAG);
    }
}
