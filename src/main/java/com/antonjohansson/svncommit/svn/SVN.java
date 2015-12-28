package com.antonjohansson.svncommit.svn;

import static java.io.File.createTempFile;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.apache.commons.io.IOUtils.readLines;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import com.antonjohansson.svncommit.domain.SvnItem;

/**
 * Provides utility methods for working with SVN.
 *
 * @author Anton Johansson
 */
public final class SVN
{
	private SVN() {}

	/**
	 * Gets a collection of all modified files.
	 *
	 * @param path The path to get modified files from.
	 * @return Returns the collection of modified files.
	 */
	public static Collection<SvnItem> getModifiedItems(String path)
	{
		File temporaryScriptFile = getTemporaryScriptFile();
		Collection<String> statusLines = getStatusLines(path, temporaryScriptFile);

		return statusLines.stream()
			.map(Converter::convertFile)
			.collect(toList());
	}

	private static Collection<String> getStatusLines(String path, File temporaryScriptFile)
	{
		ProcessBuilder pb = new ProcessBuilder("bash", temporaryScriptFile.getAbsolutePath());
		pb.directory(new File(path));

		try
		{
			Process process = pb.start();
			return readLines(process.getInputStream());
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not execute temporary bash script", e);
		}
	}

	private static File getTemporaryScriptFile()
	{
		try
		{
			File temporaryFile = createTempFile("script", "tmp");
			writeStringToFile(temporaryFile, "#!/bin/bash" + lineSeparator(), true);
			writeStringToFile(temporaryFile, "svn status" + lineSeparator(), true);
			return temporaryFile;
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not create temporary bash script", e);
		}
	}
}
