package com.emirovschi.pad.lab2.common.util;

@FunctionalInterface
public interface ThrowableConsumer<T, E extends Exception>
{
    void accept(T t) throws E;
}
