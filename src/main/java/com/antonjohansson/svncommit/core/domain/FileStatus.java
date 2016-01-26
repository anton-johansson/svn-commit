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
package com.antonjohansson.svncommit.core.domain;

/**
 * Defines various file statuses.
 *
 * @author Anton Johansson
 */
public enum FileStatus
{
	/** The file is not under source control. */
	UNVERSIONED('?', "Unversioned", false),

	/** The file is under version control, but is missing in the working copy. */
	MISSING('!', "Missing", false),

	/** The file is not under source control, but is set to be added. */
	ADDED('A', "Added", true),

	/** The file is under source control and is modified. */
	MODIFIED('M', "Modified", true),

	/** The file is under source control, but will be deleted upon commit. */
	DELETED('D', "Deleted", true);

	private final char key;
	private final String caption;
	private final boolean commitable;

	private FileStatus(char key, String caption, boolean commitable)
	{
		this.key = key;
		this.caption = caption;
		this.commitable = commitable;
	}

	/**
	 * Gets the caption of this status.
	 *
	 * @return Returns the caption.
	 */
	public String getCaption()
	{
		return caption;
	}

	/**
	 * Gets whether or not this status is commitable.
	 *
	 * @return Returns {@code true} if this status is commitable.
	 */
	public boolean isCommitable()
	{
		return commitable;
	}

	/**
	 * Gets the status with given key.
	 *
	 * @param key The key to get status for.
	 * @return Returns the status.
	 */
	public static FileStatus getStatus(char key)
	{
		for (FileStatus status : FileStatus.values())
		{
			if (status.key == key)
			{
				return status;
			}
		}
		throw new IllegalArgumentException("key");
	}
}
