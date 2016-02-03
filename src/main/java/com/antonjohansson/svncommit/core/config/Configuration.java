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
package com.antonjohansson.svncommit.core.config;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

/**
 * Contains configurations for the svn-commit application.
 *
 * @author Anton Johansson
 */
public class Configuration
{
	private final boolean replicationEnabled;

	/**
	 * Constructs a new {@link Configuration} with the default properties.
	 */
	public Configuration()
	{
		this.replicationEnabled = false;
	}

	/**
	 * Constructs a new {@link Configuration} with properties read from the given file.
	 *
	 * @param configurationFile The file to read properties from.
	 */
	public Configuration(File configurationFile)
	{
		try
		{
			Properties properties = new Properties();
			properties.load(FileUtils.openInputStream(configurationFile));

			this.replicationEnabled = Boolean.parseBoolean(properties.getProperty("replication-enabled"));
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not read configuration file", e);
		}
	}

	public boolean isReplicationEnabled()
	{
		return replicationEnabled;
	}
}
