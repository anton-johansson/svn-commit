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
package com.antonjohansson.svncommit.core.utils;

import com.antonjohansson.svncommit.core.domain.FileStatus;
import com.antonjohansson.svncommit.core.domain.ModifiedItem;

import static com.antonjohansson.svncommit.core.domain.FileStatus.ADDED;
import static com.antonjohansson.svncommit.core.domain.FileStatus.DELETED;
import static com.antonjohansson.svncommit.core.domain.FileStatus.MISSING;
import static com.antonjohansson.svncommit.core.domain.FileStatus.MODIFIED;
import static com.antonjohansson.svncommit.core.domain.FileStatus.UNVERSIONED;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests of {@link Converter}.
 *
 * @author Anton Johansson
 */
public class ConverterTest extends Assert
{
	@Test(expected = RuntimeException.class)
	public void test_that_unknown_status_line_causes_exception()
	{
		Converter.modifiedItem("some-unknown-line");
	}

	@Test
	public void test_that_modified_file_is_converted()
	{
		assertConversion('M', MODIFIED);
	}

	@Test
	public void test_that_added_file_is_converted()
	{
		assertConversion('A', ADDED);
	}

	@Test
	public void test_that_unversioned_file_is_converted()
	{
		assertConversion('?', UNVERSIONED);
	}

	@Test
	public void test_that_deleted_file_is_converted()
	{
		assertConversion('D', DELETED);
	}

	@Test
	public void test_that_missing_file_is_converted()
	{
		assertConversion('!', MISSING);
	}

	private void assertConversion(char statusCharacter, FileStatus status)
	{
		ModifiedItem item = Converter.modifiedItem(statusCharacter + "      some/file/path.txt");

		assertEquals("some/file/path.txt", item.getFileName());
		assertEquals(status, item.getStatus());
	}
}
