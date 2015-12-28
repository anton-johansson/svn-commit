package com.antonjohansson.svncommit.ui;

import com.antonjohansson.svncommit.domain.DbUpdateLocation;
import com.antonjohansson.svncommit.domain.SvnItem;

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

	/** The width of the replication combo box column. */
	private static final double REPLICATION_WIDTH = 160;

	/** The offset, to avoid a scroll bar. */
	private static final int OFFSET = 2;

	public SvnItemTable()
	{
		setEditable(true);

		TableColumn<SvnItem, Boolean> doCommit = new TableColumn<>("");
		doCommit.setCellValueFactory(new PropertyValueFactory<>("doCommit"));
		doCommit.setCellFactory(CheckBoxTableCell.forTableColumn(doCommit));
		doCommit.setPrefWidth(DO_COMMIT_WIDTH);
		doCommit.setEditable(true);
		getColumns().add(doCommit);

		TableColumn<SvnItem, String> fileName = new TableColumn<>("File name");
		fileName.setCellValueFactory(new PropertyValueFactory<>("fileName"));
		fileName.prefWidthProperty().bind(widthProperty().subtract(DO_COMMIT_WIDTH + REPLICATION_WIDTH + OFFSET));
		getColumns().add(fileName);

		TableColumn<SvnItem, DbUpdateLocation> replication = new TableColumn<>("Replicate");
		replication.setCellValueFactory(new PropertyValueFactory<>("replication"));
		replication.setCellFactory(ComboBoxTableCell.forTableColumn(new ReplicationStringConverter(), DbUpdateLocation.values()));
		replication.setPrefWidth(REPLICATION_WIDTH);
		getColumns().add(replication);
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
