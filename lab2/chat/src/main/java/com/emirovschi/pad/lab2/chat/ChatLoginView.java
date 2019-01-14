package com.emirovschi.pad.lab2.chat;

import java.util.function.Consumer;

public interface ChatLoginView
{
    void onSubmit(Consumer<String> onSubmit);

    void reset();
}
