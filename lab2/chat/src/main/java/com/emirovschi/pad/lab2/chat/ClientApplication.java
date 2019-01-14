package com.emirovschi.pad.lab2.chat;

import com.emirovschi.pad.lab2.chat.ui.JavafxChatLoginView;
import com.emirovschi.pad.lab2.chat.ui.JavafxChatView;
import com.emirovschi.pad.lab2.client.Client;
import com.emirovschi.pad.lab2.client.MessageService;
import com.emirovschi.pad.lab2.client.MessageServiceFactory;
import com.emirovschi.pad.lab2.client.SocketClientFactory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientApplication extends Application
{
    private static ChatClient CLIENT;

    @Override
    public void start(final Stage primaryStage)
    {
        final JavafxChatLoginView chatLoginView = new JavafxChatLoginView();
        final Scene chatLoginScene = new Scene(chatLoginView, 300, 100);

        final JavafxChatView chatView = new JavafxChatView();
        final Scene chatScene = new Scene(chatView, 640, 480);

        primaryStage.setTitle("Chat");
        primaryStage.setResizable(false);
        primaryStage.setScene(chatLoginScene);

        final ChatLoginController chatLoginController = new ChatLoginController(CLIENT, primaryStage, chatLoginView, chatScene);
        final ChatController chatController = new ChatController(CLIENT, chatView);

        try
        {
            CLIENT.connect();
            CLIENT.onError(error -> Platform.runLater(() ->
            {
                new Alert(Alert.AlertType.ERROR, error).showAndWait();
                primaryStage.close();
            }));
            primaryStage.show();
        }
        catch (final Exception exception)
        {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            exception.printStackTrace();
            primaryStage.close();
        }
    }

    public static void main(final String[] args)
    {
        final ExecutorService executorService = Executors.newFixedThreadPool(200);

        final SocketClientFactory clientFactory = new SocketClientFactory(executorService);
        clientFactory.setServerIp("localhost");
        clientFactory.setServerPort(27015);

        final MessageServiceFactory messageServiceFactory = new MessageServiceFactory();
        final Client client = clientFactory.create();
        final MessageService messageService = messageServiceFactory.create(client);

        try (final ChatClient chatClient = new SocketChatClient(client, messageService))
        {
            CLIENT = chatClient;
            launch(args);
        }
        catch (final Exception e)
        {
            System.out.println("Failed to start application: " + e);
            e.printStackTrace();
        }
        finally
        {
            executorService.shutdown();
        }
    }
}
