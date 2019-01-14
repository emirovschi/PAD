package com.emirovschi.pad.lab2.client;

import com.emirovschi.pad.lab2.common.broker.BrokerService;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Consumer;

public interface Client extends BrokerService, Closeable
{
    void connect() throws IOException;

    void onError(Consumer<String> onError);
}
