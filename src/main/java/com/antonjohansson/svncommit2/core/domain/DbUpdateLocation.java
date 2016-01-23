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
