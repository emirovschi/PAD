package com.emirovschi.pad.lab2.client.enricher;

import com.emirovschi.pad.lab2.common.data.Message;

public interface MessageEnricher
{
    boolean shouldApply(Message message);

    Message apply(Message message);
}
