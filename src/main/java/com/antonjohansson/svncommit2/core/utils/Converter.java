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
package com.antonjohansson.svncommit2.core.utils;

import com.antonjohansson.svncommit2.core.domain.FileStatus;
import com.antonjohansson.svncommit2.core.domain.ModifiedItem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides utility methods for converting SVN information.
 *
 * @author Anton Johansson
 */
public final class Converter
{
	private static final Pattern PATTERN = Pattern.compile("^(M|A|D|\\?|!)\\s*(.*)$");

	private Converter() {}

	/**
	 * Converts given status line to an {@link ModifiedItem}.
	 *
	 * @param statusLine The status line to convert.
	 * @return Returns the converted {@link ModifiedItem}.
	 */
	public static ModifiedItem modifiedItem(String statusLine)
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
