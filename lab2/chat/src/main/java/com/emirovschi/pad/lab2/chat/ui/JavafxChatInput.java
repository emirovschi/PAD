package com.emirovschi.pad.lab2.chat.ui;

import com.emirovschi.pad.lab2.chat.ui.data.JavafxChatMessage;
import com.emirovschi.pad.lab2.common.util.ThrowableConsumer;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public class JavafxChatInput extends HBox
{
    private final TextField input;
    private final Button submitButton;
    private final ColorAdjust defaultColorAdjust;

    private Runnable onReply;
    private final ImageView reply;

    public JavafxChatInput(final String replyImage, final ColorAdjust defaultColorAdjust)
    {
        this.defaultColorAdjust = defaultColorAdjust;

        reply = new ImageView();
        reply.setImage(new Image(getClass().getResourceAsStream(replyImage)));
        reply.setFitWidth(20);
        reply.setFitHeight(20);
        reply.setCursor(Cursor.HAND);
        reply.setEffect(defaultColorAdjust);
        reply.setOnMouseClicked(event -> ofNullable(onReply).ifPresent(Runnable::run));

        submitButton = new Button();
        submitButton.setText("Send");
        submitButton.setDisable(true);

        input = new TextField();
        input.setPromptText("Write message");
        input.textProperty().addListener((observable, oldValue, newValue) -> submitButton.setDisable(newValue.length() == 0));

        setSpacing(20);
        getChildren().add(reply);
        getChildren().add(input);
        getChildren().add(submitButton);
        setHgrow(input, Priority.ALWAYS);
    }

    public void onSend(final ThrowableConsumer<String, Exception> onSubmit)
    {
        input.setOnAction(event -> send(onSubmit));
        submitButton.setOnAction(event -> send(onSubmit));
    }

    private void send(final ThrowableConsumer<String, Exception> onSubmit)
    {
        final String message = input.getText();

        if (message.length() > 0)
        {
            input.setDisable(true);

            try
            {
                onSubmit.accept(message);
                input.setText("");
            }
            catch (final Exception exception)
            {
                new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
                exception.printStackTrace();
            }

            input.setDisable(false);
        }
    }

    public void setReply(final JavafxChatMessage reply)
    {
        this.reply.setEffect(ofNullable(reply).map(JavafxChatMessage::getColorAdjust).orElse(defaultColorAdjust));
    }

    public void onReply(final Runnable onReply)
    {
        this.onReply = onReply;
    }
}
