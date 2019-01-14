package com.emirovschi.pad.lab2.chat.ui;

import com.emirovschi.pad.lab2.chat.data.ChatMessage;
import com.emirovschi.pad.lab2.chat.ui.data.JavafxChatMessage;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.groupingBy;

public class JavafxChatThreads
{
    private final ObservableList<JavafxChatMessage> messages;

    private JavafxChatMessage currentReply;
    private Consumer<JavafxChatMessage> onReply;

    public JavafxChatThreads(final ObservableList<JavafxChatMessage> messages)
    {
        this.messages = messages;
        this.messages.addListener((ListChangeListener<JavafxChatMessage>) c -> updateThreads());
    }

    public void setReply(final JavafxChatMessage chatMessage)
    {
        currentReply = chatMessage;
        updateThreads();
    }

    public void clearReply()
    {
        currentReply = null;
        updateThreads();
    }

    public void onReply(final Consumer<JavafxChatMessage> onReply)
    {
        this.onReply = onReply;
    }

    public String getReply()
    {
        return getCurrentReply().map(ChatMessage::getReplyTo).orElse(null);
    }

    private void updateThreads()
    {
        ofNullable(onReply).ifPresent(e -> e.accept(getCurrentReply().orElse(null)));

        final Map<String, List<JavafxChatMessage>> threads = messages.stream()
            .collect(groupingBy(ChatMessage::getReplyTo));

        messages.stream()
            .filter(message -> threads.keySet().contains(message.getReplyTo()))
            .forEach(JavafxChatMessage::resetColor);

        threads.entrySet().stream()
            .filter(this::isThread)
            .forEach(entry -> entry.getValue().forEach(JavafxChatMessage::setColor));
    }

    private boolean isThread(final Map.Entry<String, List<JavafxChatMessage>> entry)
    {
        return entry.getValue().size() > 1
            || getCurrentReply().map(ChatMessage::getReplyTo).filter(entry.getKey()::equals).isPresent();
    }

    private Optional<JavafxChatMessage> getCurrentReply()
    {
        return ofNullable(currentReply);
    }
}
