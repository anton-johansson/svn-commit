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

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests of {@link Configuration}.
 *
 * @author Anton Johansson
 */
public class ConfigurationTest extends Assert
{
	@Test
	public void test_default_values()
	{
		Configuration configuration = new Configuration();
		assertFalse(configuration.isReplicationEnabled());
	}

	@Test
	public void test_loaded_values()
	{
		Configuration configuration = new Configuration(new File("src/test/resources/configuration.properties"));
		assertTrue(configuration.isReplicationEnabled());
	}

	@Test(expected = RuntimeException.class)
	public void test_that_non_existing_files_causes_exception()
	{
		new Configuration(new File("non-existing-file.properties"));
	}
}
