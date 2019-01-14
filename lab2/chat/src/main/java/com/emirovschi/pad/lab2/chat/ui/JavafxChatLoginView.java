package com.emirovschi.pad.lab2.chat.ui;

import com.emirovschi.pad.lab2.chat.ChatLoginView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.function.Consumer;

public class JavafxChatLoginView extends HBox implements ChatLoginView
{
    private final TextField nameInput;
    private final Button submitButton;

    public JavafxChatLoginView()
    {
        nameInput = new TextField();
        nameInput.setPromptText("Username");

        submitButton = new Button();
        submitButton.setText("Enter");
        submitButton.setDisable(true);

        nameInput.textProperty()
                .addListener((observable, oldValue, newValue) -> submitButton.setDisable(newValue.length() < 3));

        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(20));
        setSpacing(20);
        getChildren().add(nameInput);
        getChildren().add(submitButton);
        setHgrow(nameInput, Priority.ALWAYS);
    }

    @Override
    public void onSubmit(final Consumer<String> onSubmit)
    {
        nameInput.setOnAction(event -> action(onSubmit));
        submitButton.setOnAction(event -> action(onSubmit));
    }

    private void action(final Consumer<String> onSubmit)
    {
        if (nameInput.getText().length() >= 3)
        {
            nameInput.setDisable(true);
            submitButton.setDisable(true);
            onSubmit.accept(nameInput.getText());
        }
    }

    @Override
    public void reset()
    {
        nameInput.setText("");
        nameInput.setDisable(false);
    }
}
