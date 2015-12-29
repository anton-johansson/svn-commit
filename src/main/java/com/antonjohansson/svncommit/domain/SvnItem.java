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
	private final BooleanProperty doCommit = new SimpleBooleanProperty(this, "doCommit");
	private final Property<FileStatus> status = new SimpleObjectProperty<>(this, "status");
	private final StringProperty fileName = new SimpleStringProperty(this, "fileName", "");
	private final Property<DbUpdateLocation> replication = new SimpleObjectProperty<>(this, "replication", NONE);

	public SvnItem(String fileName, FileStatus status)
	{
		setFileName(fileName);
		setStatus(status);
		setDoCommit(status.isDoCommitByDefault());
	}

	public boolean isDoCommit()
	{
		return doCommit.getValue();
	}

	public void setDoCommit(boolean doCommit)
	{
		this.doCommit.setValue(doCommit);
	}

	public BooleanProperty doCommitProperty()
	{
		return doCommit;
	}

	public FileStatus getStatus()
	{
		return this.status.getValue();
	}

	public void setStatus(FileStatus status)
	{
		this.status.setValue(status);
	}

	public String getFileName()
	{
		return fileName.getValue();
	}

	public void setFileName(String fileName)
	{
		this.fileName.setValue(fileName);
	}

	public void setReplication(DbUpdateLocation replication)
	{
		this.replication.setValue(replication);
	}

	public DbUpdateLocation getReplication()
	{
		return replication.getValue();
	}
}
