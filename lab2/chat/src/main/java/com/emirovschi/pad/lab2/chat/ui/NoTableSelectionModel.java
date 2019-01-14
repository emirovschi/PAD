package com.emirovschi.pad.lab2.chat.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;

public class NoTableSelectionModel<T> extends TableViewSelectionModel<T>
{
    /**
     * Builds a default TableViewSelectionModel instance with the provided
     * TableView.
     *
     * @param tableView The TableView upon which this selection model should
     *                  operate.
     * @throws NullPointerException TableView can not be null.
     */
    public NoTableSelectionModel(final TableView<T> tableView)
    {
        super(tableView);
    }

    @Override
    public ObservableList<TablePosition> getSelectedCells()
    {
        return FXCollections.emptyObservableList();
    }

    @Override
    public boolean isSelected(final int row, final TableColumn<T, ?> column)
    {
        return false;
    }

    @Override
    public void select(final int row, final TableColumn<T, ?> column)
    {

    }

    @Override
    public void clearAndSelect(final int row, final TableColumn<T, ?> column)
    {

    }

    @Override
    public void clearSelection(final int row, final TableColumn<T, ?> column)
    {

    }

    @Override
    public void selectLeftCell()
    {

    }

    @Override
    public void selectRightCell()
    {

    }

    @Override
    public void selectAboveCell()
    {

    }

    @Override
    public void selectBelowCell()
    {

    }
}
