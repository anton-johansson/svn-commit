/**
 * Copyright 2015 Anton Johansson
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
package com.antonjohansson.svncommit.core.view.commit;

import com.antonjohansson.svncommit.core.domain.DbUpdateLocation;
import com.antonjohansson.svncommit.core.domain.FileStatus;
import com.antonjohansson.svncommit.core.domain.SvnItem;

import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.SPACE;
import static javafx.scene.paint.Color.LIGHTGRAY;

import java.util.Collection;
import java.util.function.Consumer;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.StringConverter;

/**
 * The table for showing files to be committed.
 *
 * @author Anton Johansson
 */
class SvnItemTable extends TableView<SvnItem>
{
	/** The width of the commit check box column. */
	private static final double DO_COMMIT_WIDTH = 40;

	/** The width of the status column. */
	private static final double STATUS_WIDTH = 100;

	/** The width of the replication combo box column. */
	private static final double REPLICATION_WIDTH = 160;

	/** The offset, to avoid a scroll bar. */
	private static final int OFFSET = 2;

	private Consumer<Collection<SvnItem>> enterHandler;
	private Consumer<Collection<SvnItem>> spaceHandler;

	public SvnItemTable()
	{
		setEditable(true);
		setHandlers();
		getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		TableColumn<SvnItem, Boolean> doCommit = new TableColumn<>("");
		doCommit.setCellValueFactory(p -> p.getValue().doCommitProperty());
		doCommit.setCellFactory(CheckBoxTableCell.forTableColumn(doCommit));
		doCommit.setPrefWidth(DO_COMMIT_WIDTH);
		doCommit.setEditable(true);
		doCommit.setResizable(false);
		getColumns().add(doCommit);

		TableColumn<SvnItem, FileStatus> status = new TableColumn<>("Status");
		status.setCellValueFactory(p -> p.getValue().statusProperty());
		status.setCellFactory(s ->
		{
			return new TableCell<SvnItem, FileStatus>()
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
		getColumns().add(status);

		TableColumn<SvnItem, String> fileName = new TableColumn<>("File name");
		fileName.setCellValueFactory(p -> p.getValue().fileNameProperty());
		fileName.prefWidthProperty().bind(widthProperty().subtract(DO_COMMIT_WIDTH + STATUS_WIDTH + REPLICATION_WIDTH + OFFSET));
		getColumns().add(fileName);

		TableColumn<SvnItem, DbUpdateLocation> replication = new TableColumn<>("Replicate");
		replication.setCellValueFactory(p -> p.getValue().replicationProperty());
		replication.setCellFactory(p -> new ReplicationCell());
		replication.setPrefWidth(REPLICATION_WIDTH);
		getColumns().add(replication);
	}

	private void setHandlers()
	{
		setOnMouseClicked(e ->
		{
			if (e.getClickCount() == 2)
			{
				enterHandler.accept(getSelectedItems());
			}
		});
		setOnKeyPressed(e ->
		{
			if (ENTER.equals(e.getCode()))
			{
				enterHandler.accept(getSelectedItems());
			}
			if (SPACE.equals(e.getCode()))
			{
				spaceHandler.accept(getSelectedItems());
			}
		});
	}

	/**
	 * Sets a handler to execute when the enter key (or double click) is pressed.
	 *
	 * @param enterHandler The handler to set.
	 */
	public void setEnterHandler(Consumer<Collection<SvnItem>> enterHandler)
	{
		this.enterHandler = enterHandler;
	}

	/**
	 * Sets a handler to execute when the space key is pressed.
	 *
	 * @param spaceHandler The handler to set.
	 */
	public void setSpaceHandler(Consumer<Collection<SvnItem>> spaceHandler)
	{
		this.spaceHandler = spaceHandler;
	}

	private Collection<SvnItem> getSelectedItems()
	{
		return getSelectionModel().getSelectedItems();
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

	/**
	 * Cell used for the 'Replication' column.
	 *
	 * @author Anton Johansson
	 */
	private class ReplicationCell extends ComboBoxTableCell<SvnItem, DbUpdateLocation>
	{
		private ReplicationCell()
		{
			super(new ReplicationStringConverter(), DbUpdateLocation.values());
		}

		@Override
		public void updateItem(DbUpdateLocation location, boolean empty)
		{
			super.updateItem(location, empty);
			SvnItem item = (SvnItem) getTableRow().getItem();
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
}
