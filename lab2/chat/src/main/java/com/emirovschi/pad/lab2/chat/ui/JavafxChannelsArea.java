package com.emirovschi.pad.lab2.chat.ui;

import com.emirovschi.pad.lab2.chat.data.Channel;
import com.emirovschi.pad.lab2.chat.ui.data.JavafxChannel;
import com.emirovschi.pad.lab2.common.util.ThrowableConsumer;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Optional;
import java.util.function.BiConsumer;

import static com.emirovschi.pad.lab2.chat.ui.JavafxUtils.uniStringConverter;
import static java.util.Optional.ofNullable;

public class JavafxChannelsArea extends VBox
{
    private final TextField channel;
    private final Button connect;
    private final ListView<JavafxChannel> channels;
    private final Button disconnect;

    public JavafxChannelsArea()
    {
        channel = new TextField();
        channel.setPromptText("Join channel");

        connect = new Button("Join");
        connect.setDisable(true);
        connect.setMaxWidth(Double.MAX_VALUE);
        channel.textProperty().addListener((observable, oldValue, newValue) -> connect.setDisable(newValue.length() < 3));

        channels = new ListView<>();
        channels.setCellFactory(param -> channelCellFactory());
        channels.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        disconnect = new Button("Disconnect");
        disconnect.setDisable(true);
        disconnect.setMaxWidth(Double.MAX_VALUE);

        setSpacing(20);
        getChildren().add(channel);
        getChildren().add(connect);
        getChildren().add(channels);
        getChildren().add(disconnect);
        setVgrow(channels, Priority.ALWAYS);
    }

    private ListCell<JavafxChannel> channelCellFactory()
    {
        final TextFieldListCell<JavafxChannel> cell = new TextFieldListCell<>();
        cell.itemProperty().addListener((observable, oldValue, newValue) -> ofNullable(newValue).map(JavafxChannel::getColor).ifPresent(cell::setTextFill));
        cell.setConverter(uniStringConverter(Channel::getTitle));
        return cell;
    }

    public void onConnect(final ThrowableConsumer<String, Exception> onConnect)
    {
        channel.setOnAction(event -> connect(onConnect));
        connect.setOnAction(event -> connect(onConnect));
    }

    public void onDisconnect(final ThrowableConsumer<String, Exception> onDisconnect)
    {
        disconnect.setOnAction(event -> disconnect(onDisconnect));
    }

    public void onChannelChange(final BiConsumer<Integer, String> onChannelChange)
    {
        channels.getSelectionModel().selectedIndexProperty()
            .addListener((observable, oldValue, newValue) -> selectChannel((int) newValue, onChannelChange));
    }

    public void selectOrAddChannel(final JavafxChannel channel, final ThrowableConsumer<JavafxChannel, Exception> callback)
    {
        final int position = channels.getItems().indexOf(channel);
        if (position >= 0)
        {
            channels.getSelectionModel().select(position);
        }
        else
        {
            try
            {
                callback.accept(channel);
                channels.getItems().add(channel);
                channels.getSelectionModel().selectLast();
            }
            catch (final Exception exception)
            {
                new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            }
        }
    }

    public JavafxChannel add(final JavafxChannel channel)
    {
        channels.getItems().add(channel);
        return channel;
    }

    public Optional<JavafxChannel> getChannel(final String id)
    {
        return channels.getItems().stream()
            .filter(c -> c.getId().equals(id))
            .findFirst();
    }

    public JavafxChannel getSelectedChannel()
    {
        return channels.getSelectionModel().getSelectedItem();
    }

    public void refresh()
    {
        channels.refresh();
    }

    private void connect(final ThrowableConsumer<String, Exception> onConnect)
    {
        final String newChannel = this.channel.getText();

        if (newChannel.length() >= 3)
        {
            final JavafxChannel channel = channels.getItems().stream()
                .filter(c -> c.getName().equals(newChannel))
                .findFirst()
                .orElseGet(() -> new JavafxChannel(newChannel));

            selectOrAddChannel(channel, c -> onConnect.accept(c.getId()));

            this.channel.setText("");
        }
    }

    private void disconnect(final ThrowableConsumer<String, Exception> onDisconnect)
    {
        final int selectedIndex = channels.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0)
        {
            try
            {
                final JavafxChannel channel = channels.getItems().get(selectedIndex);

                if (channel.getId().equals(channel.getName()))
                {
                    onDisconnect.accept(channel.getName());
                }

                channels.getItems().remove(selectedIndex);
            }
            catch (final Exception exception)
            {
                new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            }
        }
    }

    private void selectChannel(final int newChannel, final BiConsumer<Integer, String> onSelectChannel)
    {
        disconnect.setDisable(newChannel < 0);

        if (newChannel >= 0)
        {
            final JavafxChannel channel = channels.getItems().get(newChannel);
            onSelectChannel.accept(newChannel, channel.getId());
            channel.setNewMessages(0);
            channels.refresh();
        }
        else
        {
            onSelectChannel.accept(newChannel, null);
        }
    }
}
