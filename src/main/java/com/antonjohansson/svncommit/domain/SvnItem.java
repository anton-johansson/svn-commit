package com.antonjohansson.svncommit.domain;

import static com.antonjohansson.svncommit.domain.DbUpdateLocation.NONE;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Defines an item to commit.
 *
 * @author Anton Johansson
 */
public class SvnItem
{
	private final BooleanProperty doCommitProperty = new SimpleBooleanProperty();
	private final Property<FileStatus> statusProperty = new SimpleObjectProperty<>();
	private final StringProperty fileNameProperty = new SimpleStringProperty();
	private final Property<DbUpdateLocation> replicationProperty = new SimpleObjectProperty<>(NONE);

	public SvnItem(String fileName, FileStatus status)
	{
		this.fileNameProperty.setValue(fileName);
		this.statusProperty.setValue(status);
		this.doCommitProperty.setValue(status.isDoCommitByDefault());
	}

	public BooleanProperty doCommitProperty()
	{
		return doCommitProperty;
	}

	public Property<FileStatus> statusProperty()
	{
		return statusProperty;
	}

	public StringProperty fileNameProperty()
	{
		return fileNameProperty;
	}

	public Property<DbUpdateLocation> replicationProperty()
	{
		return replicationProperty;
	}

	/**
	 * Flips the 'Do commit' property.
	 */
	public void flip()
	{
		doCommitProperty.set(!doCommitProperty.get());
	}
}
