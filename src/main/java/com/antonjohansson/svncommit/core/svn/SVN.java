package com.antonjohansson.svncommit.core.svn;

import com.antonjohansson.svncommit.core.domain.SvnItem;
import com.antonjohansson.svncommit.core.utils.Bash;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.io.IOUtils.readLines;

import java.io.File;
import java.util.Collection;

/**
 * Provides utility methods for working with SVN.
 *
 * @author Anton Johansson
 */
public final class SVN
{
	private static final String COMPARE_COMMAND_PATTERN = "meld '%F'";

	private SVN() {}

	/**
	 * Gets a collection of all modified files.
	 *
	 * @param directory The directory to get modified files from.
	 * @return Returns the collection of modified files.
	 */
	public static Collection<SvnItem> getModifiedItems(File directory)
	{
		return Bash.execute(s -> readLines(s), directory, "svn status")
			.stream()
			.map(Converter::convertFile)
			.collect(toList());
	}

	/**
	 * Brings up the Meld compare tool for the given file.
	 *
	 * @param directory The directory which the file to compare is contained in.
	 * @param fileName The file to compare.
	 */
	public static void compare(File directory, String fileName)
	{
		String command = COMPARE_COMMAND_PATTERN.replace("%F", fileName);
		Bash.execute(directory, command);
	}
}