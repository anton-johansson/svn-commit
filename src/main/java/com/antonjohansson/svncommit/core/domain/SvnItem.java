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
package com.antonjohansson.svncommit.core.domain;

import static com.antonjohansson.svncommit.core.domain.DbUpdateLocation.NONE;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

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

	public boolean isDoCommit()
	{
		return doCommitProperty.getValue();
	}

	public void setDoCommit(boolean doCommit)
	{
		doCommitProperty.setValue(doCommit);
	}

	public FileStatus getStatus()
	{
		return statusProperty.getValue();
	}

	public void setStatus(FileStatus status)
	{
		statusProperty.setValue(status);
	}

	public String getFileName()
	{
		return fileNameProperty.getValue();
	}

	public void setFileName(String fileName)
	{
		fileNameProperty.setValue(fileName);
	}

	public DbUpdateLocation getReplication()
	{
		return replicationProperty.getValue();
	}

	public void setReplication(DbUpdateLocation replication)
	{
		replicationProperty.setValue(replication);
	}

	/**
	 * Flips the 'Do commit' property.
	 */
	public void flip()
	{
		setDoCommit(!isDoCommit());
	}

	/**
	 * Gets whether or not this item can be replicated.
	 *
	 * @return Returns {@code true} if this item can be replicated.
	 */
	public boolean canReplicate()
	{
		String extension = FilenameUtils.getExtension(fileNameProperty.getValue());
		return StringUtils.equalsIgnoreCase(extension, "SQL");
	}
}
