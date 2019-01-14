package com.emirovschi.pad.lab2.chat;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatLoginController
{
    private final ChatClient client;
    private final Stage primaryStage;
    private final Scene chatScene;

    public ChatLoginController(
            final ChatClient client,
            final Stage primaryStage,
            final ChatLoginView chatLoginView,
            final Scene chatScene)
    {
        this.client = client;
        this.primaryStage = primaryStage;
        this.chatScene = chatScene;
        chatLoginView.onSubmit(this::submit);
    }

    private void submit(final String name)
    {
        client.register(name);
        primaryStage.setScene(chatScene);
        primaryStage.setResizable(true);
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(200);
        primaryStage.setTitle("Chat - " + name);
    }
}
