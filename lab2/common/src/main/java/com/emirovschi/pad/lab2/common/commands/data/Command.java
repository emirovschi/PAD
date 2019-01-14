package com.emirovschi.pad.lab2.common.commands.data;

import java.util.Map;

public interface Command
{
    String getCommandType();

    Map<String, byte[]> getData();
}
