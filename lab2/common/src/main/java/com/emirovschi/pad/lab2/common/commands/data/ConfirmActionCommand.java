package com.emirovschi.pad.lab2.common.commands.data;

import java.util.Map;

public class ConfirmActionCommand extends AbstractReplyCommand
{
    private static final String ERROR_MESSAGE = "errorMessage";

    public ConfirmActionCommand(final ReplyCommand replyCommand)
    {
        super(replyCommand);
    }

    public ConfirmActionCommand(final ReplyCommand replyCommand, final String errorMessage)
    {
        super(replyCommand);
        if (errorMessage != null)
        {
            put(ERROR_MESSAGE, errorMessage);
        }
    }

    public ConfirmActionCommand(final Map<String, byte[]> data)
    {
        super(data);
    }

    public boolean isSuccessful()
    {
        return !getData().containsKey(ERROR_MESSAGE);
    }

    public String getErrorMessage()
    {
        return getAsString(ERROR_MESSAGE);
    }
}
