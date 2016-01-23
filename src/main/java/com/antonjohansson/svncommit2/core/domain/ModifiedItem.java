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
package com.antonjohansson.svncommit2.core.domain;

import static com.antonjohansson.svncommit2.core.domain.DbUpdateLocation.NONE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class ModifiedItem
{
	private static final Pattern PATTERN = Pattern.compile("^(M|A|D|\\?|!)\\s*(.*)$");

	private final BooleanProperty doCommitProperty = new SimpleBooleanProperty();
	private final Property<FileStatus> statusProperty = new SimpleObjectProperty<>();
	private final StringProperty fileNameProperty = new SimpleStringProperty();
	private final Property<DbUpdateLocation> replicationProperty = new SimpleObjectProperty<>(NONE);

	public ModifiedItem(String fileName, FileStatus status)
	{
		this.fileNameProperty.setValue(fileName);
		this.statusProperty.setValue(status);
		this.doCommitProperty.setValue(status.isCommitable());
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


	/**
	 * Converts given status line to an {@link ModifiedItem}.
	 *
	 * @param statusLine The status line to convert.
	 * @return Returns the converted {@link ModifiedItem}.
	 */
	public static ModifiedItem convertItem(String statusLine)
	{
		Matcher matcher = PATTERN.matcher(statusLine);

		if (!matcher.matches())
		{
			throw new RuntimeException("File did not match: " + statusLine);
		}

		String status = matcher.group(1);
		if (status.length() != 1)
		{
			throw new RuntimeException("status must be one character");
		}

		String fileName = matcher.group(2);
		FileStatus fileStatus = FileStatus.getStatus(status.charAt(0));

		return new ModifiedItem(fileName, fileStatus);
	}
}
