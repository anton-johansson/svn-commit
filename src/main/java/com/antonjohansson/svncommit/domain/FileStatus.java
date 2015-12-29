package com.antonjohansson.svncommit.domain;

/**
 * Defines various file statuses.
 *
 * @author Anton Johansson
 */
public enum FileStatus
{
	/** The file is not under source control. */
	UNVERSIONED('?', "Unversioned", false),

	/** The file is not under source control, but is set to be added. */
	ADDED('A', "Added", true),

	/** The file is under source control and is modified. */
	MODIFIED('M', "Modified", true),

	/** The file is under source control, but will be deleted upon commit. */
	DELETED('D', "Deleted", true);

	private final char key;
	private final String caption;
	private final boolean doCommitByDefault;

	private FileStatus(char key, String caption, boolean doCommitByDefault)
	{
		this.key = key;
		this.caption = caption;
		this.doCommitByDefault = doCommitByDefault;
	}

	public String getCaption()
	{
		return caption;
	}

	public boolean isDoCommitByDefault()
	{
		return doCommitByDefault;
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
