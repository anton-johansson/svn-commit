/**
 * Copyright (c) Anton Johansson <antoon.johansson@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.antonjohansson.svncommit.application.commit;

import com.antonjohansson.svncommit.application.commit.context.CommitContextMenu;
import com.antonjohansson.svncommit.core.domain.DbUpdateLocation;
import com.antonjohansson.svncommit.core.domain.FileStatus;
import com.antonjohansson.svncommit.core.domain.ModifiedItem;
import com.antonjohansson.svncommit.core.view.AbstractView;
import com.antonjohansson.svncommit.core.view.View;

import static java.lang.System.lineSeparator;
import static javafx.application.Platform.runLater;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.scene.control.SelectionMode.MULTIPLE;
import static javafx.scene.paint.Color.LIGHTGRAY;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.google.inject.Provider;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

/**
 * Commit implementation of {@link View}.
 *
 * @author Anton Johansson
 */
public class CommitView extends AbstractView
{
	private static final double DO_COMMIT_WIDTH = 40;
	private static final double STATUS_WIDTH = 100;
	private static final double REPLICATION_WIDTH = 160;
	private static final int OFFSET = 16;

	private final ObservableList<ModifiedItem> items;
	@FXML private TableView<ModifiedItem> tableView;
	@FXML private TextArea commitMessage;
	@FXML private TextField activityID;
	private TableColumn<ModifiedItem, DbUpdateLocation> replicationColumn;
	private Consumer<String> commitHandler;

	/**
	 * Constructs a new {@link CommitView}.
	 */
	public CommitView()
	{
		items = observableArrayList();
	}

	/**
	 * Sets the commit handler.
	 *
	 * @param commitHandler The handler.
	 */
	public void setCommitHandler(Consumer<String> commitHandler)
	{
		this.commitHandler = commitHandler;
	}

	/**
	 * Sets the modified items in the commit view.
	 *
	 * @param items The items to set.
	 */
	public void setItems(Collection<ModifiedItem> items)
	{
		runLater(() -> this.items.setAll(items));
	}

	/**
	 * Gets the selected items as a stream.
	 *
	 * @return Returns the stream of selected items.
	 */
	public Stream<ModifiedItem> selectedItems()
	{
		return tableView.getSelectionModel().getSelectedItems().stream();
	}

	/**
	 * Sets the visibility of the replication columns.
	 *
	 * @param replicationColumnsVisible Whether or not the replication columns should be visible.
	 */
	public void setReplicationColumnsVisible(boolean replicationColumnsVisible)
	{
		replicationColumn.setVisible(replicationColumnsVisible);
	}

	/**
	 * Occurs when the 'Commit' button is pressed.
	 */
	@FXML
	private void onCommit()
	{
		StringBuilder message = new StringBuilder();
		if (!activityID.getText().isEmpty())
		{
			message.append("Task: ")
				.append(activityID.getText().trim())
				.append(lineSeparator());
		}
		message.append(commitMessage.getText());

		commitHandler.accept(message.toString());
	}

	/**
	 * Sets the key-press event handler.
	 *
	 * @param eventHandler The handler.
	 */
	public void setOnKeyPressed(EventHandler<KeyEvent> eventHandler)
	{
		getParent().setOnKeyPressed(eventHandler);
		tableView.setOnKeyPressed(eventHandler);
	}

	/**
	 * Sets the mouse double-click event handler.
	 *
	 * @param runnable The runnable to run on double-click.
	 */
	public void setOnMouseDoubleClicked(Runnable runnable)
	{
		tableView.setOnMouseClicked(event ->
		{
			if (event.getClickCount() == 2)
			{
				runnable.run();
			}
		});
	}

	/**
	 * Initializes the view.
	 */
	public void initialize(Provider<CommitContextMenu> contextMenuProvider, Runnable refreshCommand)
	{
		initializeTableView();
		initializeTableViewContextMenu(contextMenuProvider, refreshCommand);
	}

	private void initializeTableView()
	{
		tableView.getSelectionModel().setSelectionMode(MULTIPLE);
		tableView.setItems(items);

		TableColumn<ModifiedItem, Boolean> doCommit = new TableColumn<>("");
		doCommit.setCellValueFactory(p -> p.getValue().doCommitProperty());
		doCommit.setCellFactory(CheckBoxTableCell.forTableColumn(doCommit));
		doCommit.setPrefWidth(DO_COMMIT_WIDTH);
		doCommit.setEditable(true);
		doCommit.setResizable(false);
		tableView.getColumns().add(doCommit);

		TableColumn<ModifiedItem, FileStatus> status = new TableColumn<>("Status");
		status.setCellValueFactory(p -> p.getValue().statusProperty());
		status.setCellFactory(s ->
		{
			return new TableCell<ModifiedItem, FileStatus>()
			{
				@Override
				protected void updateItem(FileStatus item, boolean empty)
				{
					super.updateItem(item, empty);
					setText(empty ? "" : item.getCaption());
				}
			};
		});
		status.setPrefWidth(STATUS_WIDTH);
		tableView.getColumns().add(status);

		TableColumn<ModifiedItem, String> fileName = new TableColumn<>("Path");
		fileName.setCellValueFactory(p -> p.getValue().fileNameProperty());
		fileName.prefWidthProperty().bind(tableView.widthProperty().subtract(DO_COMMIT_WIDTH + STATUS_WIDTH + REPLICATION_WIDTH + OFFSET));
		tableView.getColumns().add(fileName);

		replicationColumn = new TableColumn<>("Replicate");
		replicationColumn.setCellValueFactory(p -> p.getValue().replicationProperty());
		replicationColumn.setCellFactory(p -> new ReplicationCell());
		replicationColumn.setPrefWidth(REPLICATION_WIDTH);
		tableView.getColumns().add(replicationColumn);
	}

	private void initializeTableViewContextMenu(Provider<CommitContextMenu> provider, Runnable refreshCommand)
	{
		tableView.setRowFactory(tableView ->
		{
			CommitContextMenu contextMenu = provider.get();
			TableRow<ModifiedItem> row = new TableRow<>();
			row.setContextMenu(contextMenu.getContextMenu());
			row.itemProperty().addListener(e -> configureMenuItem(row, contextMenu, refreshCommand));
			return row;
		});
	}

	private void configureMenuItem(TableRow<ModifiedItem> row, CommitContextMenu contextMenu, Runnable refreshCommand)
	{
		ModifiedItem item = row.getItem();
		contextMenu.getMenuItems()
				.stream()
				.filter(menuItem -> item != null)
				.forEach(menuItem -> menuItem.configure(item, refreshCommand));
	}

	/**
	 * Cell used for the 'Replication' column.
	 *
	 * @author Anton Johansson
	 */
	private class ReplicationCell extends ComboBoxTableCell<ModifiedItem, DbUpdateLocation>
	{
		private ReplicationCell()
		{
			super(new ReplicationStringConverter(), DbUpdateLocation.values());
		}

		@Override
		public void updateItem(DbUpdateLocation location, boolean empty)
		{
			super.updateItem(location, empty);
			ModifiedItem item = (ModifiedItem) getTableRow().getItem();
			if (item != null)
			{
				boolean canReplicate = item.canReplicate();
				setEditable(canReplicate);
				if (!canReplicate)
				{
					setTextFill(LIGHTGRAY);
				}
			}
		}
	}

	/**
	 * Converts {@link DbUpdateLocation} to a user friendly string.
	 *
	 * @author Anton Johansson
	 */
	private static class ReplicationStringConverter extends StringConverter<DbUpdateLocation>
	{
		@Override
		public String toString(DbUpdateLocation object)
		{
			return object.getCaption();
		}

		@Override
		public DbUpdateLocation fromString(String string)
		{
			throw new RuntimeException("Cannot convert to DbUpdateLocation from a string.");
		}
	}
}
