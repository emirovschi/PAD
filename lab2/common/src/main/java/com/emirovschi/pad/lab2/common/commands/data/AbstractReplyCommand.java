package com.emirovschi.pad.lab2.common.commands.data;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractReplyCommand extends AbstractCommand implements ReplyCommand
{
    private static final String ID = "id";
    private static final String REPLY_ID = "replyId";
    private static final ThreadLocal<AtomicLong> COMMAND_ID = ThreadLocal.withInitial(AtomicLong::new);

    public AbstractReplyCommand()
    {
        put(ID, COMMAND_ID.get().getAndIncrement());
    }

    public AbstractReplyCommand(final ReplyCommand reply)
    {
        this();
        put(REPLY_ID, reply.getId());
    }

    public AbstractReplyCommand(final Map<String, byte[]> data)
    {
        super(data);
    }

    public long getId()
    {
        return getAsLong(ID);
    }

    public Long getReplyId()
    {
        return getData().containsKey(REPLY_ID) ? getAsLong(REPLY_ID) : null;
    }
}
