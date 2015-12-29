package com.antonjohansson.svncommit.svn;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.antonjohansson.svncommit.domain.FileStatus;
import com.antonjohansson.svncommit.domain.SvnItem;

/**
 * Provides utility methods for converting SVN information.
 *
 * @author Anton Johansson
 */
public final class Converter
{
	private static final Pattern PATTERN = Pattern.compile("^(M|A|\\?)\\s*(.*)$");

	private Converter() {}

	/**
	 * Converts given status line to an {@link SvnItem}.
	 *
	 * @param statusLine The status line to convert.
	 * @return Returns the converted {@link SvnItem}.
	 */
	public static SvnItem convertFile(String statusLine)
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

		return new SvnItem(fileName, fileStatus);
	}
}
