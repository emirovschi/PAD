package com.emirovschi.pad.lab2.common.commands.data;

import com.google.common.base.Charsets;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCommand implements Command
{
    private final Map<String, byte[]> data;

    public AbstractCommand()
    {
        this.data = new HashMap<>();
    }

    public AbstractCommand(final Map<String, byte[]> data)
    {
        this.data = new HashMap<>(data);
    }

    @Override
    public String getCommandType()
    {
        return getClass().getSimpleName();
    }

    @Override
    public Map<String, byte[]> getData()
    {
        return data;
    }

    protected byte[] get(final String key)
    {
        return getData().get(key);
    }

    protected String getAsString(final String key)
    {
        return new String(get(key), Charsets.UTF_8);
    }

    protected boolean getAsBoolean(final String key)
    {
        return get(key).length > 0 && get(key)[0] != 0;
    }

    protected int getAsInt(final String key)
    {
        return ByteBuffer.wrap(get(key)).getInt();
    }

    protected long getAsLong(final String key)
    {
        return ByteBuffer.wrap(get(key)).getLong();
    }

    protected void put(final String key, final byte[] data)
    {
        this.data.put(key, data);
    }

    protected void put(final String key, final String data)
    {
        this.data.put(key, data.getBytes(Charsets.UTF_8));
    }

    protected void put(final String key, final boolean data)
    {
        this.data.put(key, new byte[]{(byte) (data ? 1 :0)});
    }

    protected void put(final String key, final int data)
    {
        this.data.put(key, ByteBuffer.allocate(4).putInt(data).array());
    }

    protected void put(final String key, final long data)
    {
        this.data.put(key, ByteBuffer.allocate(8).putLong(data).array());
    }
}
