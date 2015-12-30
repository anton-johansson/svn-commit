package com.antonjohansson.svncommit.ui;

import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.SPACE;

import java.util.function.Consumer;

import com.antonjohansson.svncommit.domain.DbUpdateLocation;
import com.antonjohansson.svncommit.domain.FileStatus;
import com.antonjohansson.svncommit.domain.SvnItem;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

/**
 * The table for showing files to be committed.
 *
 * @author Anton Johansson
 */
public class SvnItemTable extends TableView<SvnItem>
{
	/** The width of the commit check box column. */
	private static final double DO_COMMIT_WIDTH = 40;

	/** The width of the status column. */
	private static final double STATUS_WIDTH = 100;

	/** The width of the replication combo box column. */
	private static final double REPLICATION_WIDTH = 160;

	/** The offset, to avoid a scroll bar. */
	private static final int OFFSET = 2;

	private Consumer<SvnItem> enterHandler;
	private Consumer<SvnItem> spaceHandler;

	public SvnItemTable()
	{
		setEditable(true);
		setHandlers();

		TableColumn<SvnItem, Boolean> doCommit = new TableColumn<>("");
		doCommit.setCellValueFactory(p -> p.getValue().doCommitProperty());
		doCommit.setCellFactory(CheckBoxTableCell.forTableColumn(doCommit));
		doCommit.setPrefWidth(DO_COMMIT_WIDTH);
		doCommit.setEditable(true);
		getColumns().add(doCommit);

		TableColumn<SvnItem, FileStatus> status = new TableColumn<>("Status");
		status.setCellValueFactory(new PropertyValueFactory<>("status"));
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
		fileName.setCellValueFactory(new PropertyValueFactory<>("fileName"));
		fileName.prefWidthProperty().bind(widthProperty().subtract(DO_COMMIT_WIDTH + STATUS_WIDTH + REPLICATION_WIDTH + OFFSET));
		getColumns().add(fileName);

		TableColumn<SvnItem, DbUpdateLocation> replication = new TableColumn<>("Replicate");
		replication.setCellValueFactory(new PropertyValueFactory<>("replication"));
		replication.setCellFactory(ComboBoxTableCell.forTableColumn(new ReplicationStringConverter(), DbUpdateLocation.values()));
		replication.setPrefWidth(REPLICATION_WIDTH);
		getColumns().add(replication);
	}

	private void setHandlers()
	{
		setOnMouseClicked(e ->
		{
			if (e.getClickCount() == 2)
			{
				enterHandler.accept(getSelectedItem());
			}
		});
		setOnKeyPressed(e ->
		{
			if (ENTER.equals(e.getCode()))
			{
				enterHandler.accept(getSelectedItem());
			}
			if (SPACE.equals(e.getCode()))
			{
				spaceHandler.accept(getSelectedItem());
			}
		});
	}

	/**
	 * Sets a handler to execute when the enter key (or double click) is pressed.
	 *
	 * @param enterHandler The handler to set.
	 */
	public void setEnterHandler(Consumer<SvnItem> enterHandler)
	{
		this.enterHandler = enterHandler;
	}

	/**
	 * Sets a handler to execute when the space key is pressed.
	 *
	 * @param spaceHandler The handler to set.
	 */
	public void setSpaceHandler(Consumer<SvnItem> spaceHandler)
	{
		this.spaceHandler = spaceHandler;
	}

	private SvnItem getSelectedItem()
	{
		return getSelectionModel().getSelectedItem();
	}

	/**
	 * Converts {@link DbUpdateLocation} to a user friendly string.
	 *
	 * @author Anton Johansson
	 */
	private class ReplicationStringConverter extends StringConverter<DbUpdateLocation>
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
