package com.emirovschi.pad.lab2.chat.ui;

import com.emirovschi.pad.lab2.chat.ChatView;
import com.emirovschi.pad.lab2.chat.data.ChatMessage;
import com.emirovschi.pad.lab2.chat.ui.data.JavafxChannel;
import com.emirovschi.pad.lab2.chat.ui.data.JavafxChatMessage;
import com.emirovschi.pad.lab2.chat.ui.data.JavafxUser;
import com.emirovschi.pad.lab2.common.util.ThrowableConsumer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Optional.ofNullable;
import static javafx.scene.layout.VBox.setVgrow;

public class JavafxChatView extends HBox implements ChatView
{
    private static final ColorAdjust DEFAULT_COLOR_ADJUST = new ColorAdjust(0, 0, -1, 0);
    private static final String REPLY_IMAGE = "/reply_white.png";

    private final JavafxChatOutput output;
    private final JavafxChatInput input;
    private final JavafxChatThreads threads;
    private final JavafxChannelsArea channelsArea;
    private final VBox chatArea;

    public JavafxChatView()
    {
        output = new JavafxChatOutput(REPLY_IMAGE);
        output.onUserSelect(this::onUserSelect);

        input = new JavafxChatInput(REPLY_IMAGE, DEFAULT_COLOR_ADJUST);

        threads = new JavafxChatThreads(output.getItems());
        threads.onReply(input::setReply);

        output.onReply(threads::setReply);
        input.onReply(threads::clearReply);

        chatArea = new VBox();
        chatArea.setDisable(true);
        chatArea.setSpacing(20);
        chatArea.getChildren().add(output);
        chatArea.getChildren().add(input);
        setVgrow(output, Priority.ALWAYS);

        channelsArea = new JavafxChannelsArea();

        setPadding(new Insets(20));
        setSpacing(20);
        getChildren().add(channelsArea);
        getChildren().add(chatArea);
        setHgrow(chatArea, Priority.ALWAYS);
    }

    @Override
    public void addMessage(final ChatMessage chatMessage)
    {
        System.out.println(chatMessage);

        Platform.runLater(() -> renderMessage(chatMessage));
    }

    private void renderMessage(final ChatMessage chatMessage)
    {
        final JavafxChannel currentChannel = channelsArea.getSelectedChannel();

        if (chatMessage.getChannel().equals(currentChannel.getId()))
        {
            output.getItems().add(new JavafxChatMessage(chatMessage));
        }
        else
        {
            final JavafxChannel messageChannel = channelsArea.getChannel(chatMessage.getChannel())
                .orElseGet(() -> channelsArea.add(new JavafxChannel(chatMessage.getUser().getId(), chatMessage.getUser().getName())));
            messageChannel.setNewMessages(messageChannel.getNewMessages() + 1);
            channelsArea.refresh();
        }
    }

    @Override
    public void setMessages(final List<ChatMessage> messages)
    {
        output.getItems().clear();
        ofNullable(messages).orElseGet(Collections::emptyList).stream()
            .map(JavafxChatMessage::new)
            .forEach(output.getItems()::add);
    }

    @Override
    public void onSend(final ThrowableConsumer<ChatMessage, Exception> onSubmit)
    {
        input.onSend(message -> send(message, onSubmit));
    }

    private void send(final String message, final ThrowableConsumer<ChatMessage, Exception> onSubmit) throws Exception
    {
        final String channel = channelsArea.getSelectedChannel().getId();
        final String replyTo = threads.getReply();
        final ChatMessage chatMessage = new ChatMessage(channel, message, replyTo);

        onSubmit.accept(chatMessage);

        output.scrollTo(output.getItems().size() - 1);
        threads.clearReply();
    }

    @Override
    public void onConnect(final ThrowableConsumer<String, Exception> onConnect)
    {
        channelsArea.onConnect(onConnect);
    }

    @Override
    public void onDisconnect(final ThrowableConsumer<String, Exception> onDisconnect)
    {
        channelsArea.onDisconnect(onDisconnect);
    }

    @Override
    public void onChannelChange(final Consumer<String> onChannelChange)
    {
        channelsArea.onChannelChange((index, channel) -> changeChannel(index, channel, onChannelChange));
    }

    private void changeChannel(final int index, final String channel, final Consumer<String> onChannelChange)
    {
        chatArea.setDisable(index < 0);
        threads.clearReply();
        onChannelChange.accept(channel);
    }

    private void onUserSelect(final JavafxUser user)
    {
        if (user != null)
        {
            final JavafxChannel channel = channelsArea.getChannel(user.getId())
                .orElseGet(() -> new JavafxChannel(user.getId(), user.getName()));

            channelsArea.selectOrAddChannel(channel, __ -> {});
        }
    }
}
