package com.emirovschi.pad.lab2.chat.ui;

import com.emirovschi.pad.lab2.chat.ui.data.JavafxChatMessage;
import com.emirovschi.pad.lab2.chat.ui.data.JavafxUser;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.function.Consumer;

import static java.util.Optional.ofNullable;

public class JavafxChatOutput extends TableView<JavafxChatMessage>
{
    private final String replyImage;
    private Consumer<JavafxChatMessage> onReply;
    private Consumer<JavafxUser> onUserSelect;

    public JavafxChatOutput(final String replyImage)
    {
        this.replyImage = replyImage;

        final TableColumn<JavafxChatMessage, JavafxChatMessage> replyColumn = new TableColumn<>();
        replyColumn.setMinWidth(24);
        replyColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue()));
        replyColumn.setCellFactory(this::replyCellFactory);

        final TableColumn<JavafxChatMessage, JavafxUser> nameColumn = new TableColumn<>();
        nameColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getJavafxUser()));
        nameColumn.setCellFactory(this::nameCellFactory);
        nameColumn.setMaxWidth(1f * Integer.MAX_VALUE * 25);

        final TableColumn<JavafxChatMessage, String> messageColumn = new TableColumn<>();
        messageColumn.setCellFactory(this::messageCellFactory);
        messageColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getText()));
        messageColumn.setMaxWidth(Double.MAX_VALUE);
        messageColumn.setMaxWidth(1f * Integer.MAX_VALUE * 75);

        setFocusTraversable(false);
        setEditable(true);
        widthProperty().addListener(this::hideMessagesHeader);
        getColumns().add(replyColumn);
        getColumns().add(nameColumn);
        getColumns().add(messageColumn);
        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        setSelectionModel(new NoTableSelectionModel<>(this));
        setPlaceholder(new Label("No messages"));
        setStyle("-fx-table-cell-border-color: transparent;");
    }

    private TableCell<JavafxChatMessage, JavafxChatMessage> replyCellFactory(TableColumn<JavafxChatMessage, JavafxChatMessage> param)
    {
        final TableCell<JavafxChatMessage, JavafxChatMessage> cell = new TableCell<>();
        final ImageView image = new ImageView();
        image.setImage(new Image(getClass().getResourceAsStream(replyImage)));
        image.setFitWidth(20);
        image.setFitHeight(20);
        image.visibleProperty().bind(cell.itemProperty().isNotNull());
        cell.itemProperty()
            .addListener((observable, oldValue, newValue) -> ofNullable(newValue).map(JavafxChatMessage::getColorAdjust).ifPresent(image::setEffect));
        cell.setGraphic(image);
        cell.itemProperty().isNull()
            .addListener((observable, oldValue, newValue) -> cell.setCursor(newValue ? Cursor.DEFAULT : Cursor.HAND));
        cell.setOnMouseClicked(event -> ofNullable(onReply).ifPresent(e -> e.accept(cell.getItem())));
        return cell;
    }

    private TableCell<JavafxChatMessage, JavafxUser> nameCellFactory(TableColumn<JavafxChatMessage, JavafxUser> param)
    {
        final TableCell<JavafxChatMessage, JavafxUser> cell = new TableCell<>();
        final Text text = new Text();
        text.setTextAlignment(TextAlignment.RIGHT);
        cell.setGraphic(text);
        cell.itemProperty().isNull()
            .addListener((observable, oldValue, newValue) -> cell.setCursor(newValue ? Cursor.DEFAULT : Cursor.HAND));
        cell.itemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
            {
                text.setText(newValue.getName());
                text.setFill(newValue.getColor());
            }
            else
            {
                text.setText(null);
            }
        });
        cell.setOnMouseClicked(event -> ofNullable(onUserSelect).ifPresent(e -> e.accept(cell.getItem())));
        text.wrappingWidthProperty().bind(cell.widthProperty().subtract(10));
        return cell;
    }

    private TableCell<JavafxChatMessage, String> messageCellFactory(TableColumn<JavafxChatMessage, String> param)
    {
        final TableCell<JavafxChatMessage, String> cell = new TableCell<>();
        final Text text = new Text();
        cell.setGraphic(text);
        text.wrappingWidthProperty().bind(cell.widthProperty().subtract(10));
        text.textProperty().bind(cell.itemProperty());
        return cell;
    }

    private void hideMessagesHeader(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth)
    {
        final Pane header = (Pane) lookup("TableHeaderRow");
        if (header.isVisible())
        {
            header.setMaxHeight(0);
            header.setMinHeight(0);
            header.setPrefHeight(0);
            header.setVisible(false);
        }
    }

    public void onReply(final Consumer<JavafxChatMessage> onReply)
    {
        this.onReply = onReply;
    }

    public void onUserSelect(final Consumer<JavafxUser> onUserSelect)
    {
        this.onUserSelect = onUserSelect;
    }
}
