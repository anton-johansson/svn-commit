package com.antonjohansson.svncommit.domain;

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
	private final BooleanProperty doCommit = new SimpleBooleanProperty(this, "doCommit", false);
	private final StringProperty fileName = new SimpleStringProperty(this, "fileName", "");
	private final Property<DbUpdateLocation> replication = new SimpleObjectProperty<>(this, "replication", DbUpdateLocation.NONE);

	public SvnItem()
	{}

	public SvnItem(String fileName)
	{
		setFileName(fileName);
	}

	public boolean isDoCommit()
	{
		return doCommit.getValue();
	}

	public void setDoCommit(boolean doCommit)
	{
		this.doCommit.setValue(doCommit);
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
