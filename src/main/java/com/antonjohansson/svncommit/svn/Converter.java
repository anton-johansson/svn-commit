package com.antonjohansson.svncommit.svn;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.antonjohansson.svncommit.domain.SvnItem;

/**
 * Provides utility methods for converting SVN information.
 *
 * @author Anton Johansson
 */
public final class Converter
{
	private Converter() {}

	/**
	 * Converts given status line to an {@link SvnItem}.
	 *
	 * @param statusLine The status line to convert.
	 * @return Returns the converted {@link SvnItem}.
	 */
	public static SvnItem convertFile(String statusLine)
	{
		Pattern pattern = Pattern.compile("^(M|A|\\?)\\s*(.*)$");
		Matcher matcher = pattern.matcher(statusLine);

		if (!matcher.matches())
		{
			throw new RuntimeException("File did not match: " + statusLine);
		}

		String status = matcher.group(1);
		String fileName = matcher.group(2);

		return new SvnItem(fileName);
	}
}
