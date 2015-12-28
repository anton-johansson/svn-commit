package com.antonjohansson.svncommit.domain;

/**
 * Defines various locations within the DBUpdate repository where committed files should be replicated to.
 *
 * @author Anton Johansson
 */
public enum DbUpdateLocation
{
	/** The file should not be replicated to DBUpdate. */
	NONE("", "Do not replicate"),

	/** The file should be replicated to the Structure folder. */
	STRUCTURE("Structure", "Structure"),

	/** The file should be replicated to the StoredProcedures folder. */
	STORED_PROCEDURES("StoredProcedures", "Stored procedures"),

	/** The file should be replicated to the Scripts folder. */
	SCRIPTS("Scripts", "Scripts");

	private final String filePath;
	private final String caption;

	private DbUpdateLocation(String filePath, String caption)
	{
		this.filePath = filePath;
		this.caption = caption;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public String getCaption()
	{
		return caption;
	}
}
