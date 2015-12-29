package com.antonjohansson.svncommit.utils;

import static java.io.File.createTempFile;
import static java.lang.System.lineSeparator;
import static java.util.Arrays.asList;
import static org.apache.commons.io.FileUtils.writeStringToFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * Provides utility for executing bash commands.
 *
 * @author Anton Johansson
 */
public final class Bash
{
	private Bash() {}

	/**
	 * Executes given command lines in given directory.
	 *
	 * @param directory The directory to execute command lines within.
	 * @param commandLines The command lines to execute.
	 */
	public static void execute(File directory, String... commandLines)
	{
		File scriptFile = getTemporaryScriptFile(asList(commandLines));

		try
		{
			execute(directory, scriptFile);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not execute temporary bash script", e);
		}
	}

	/**
	 * Executes given command lines in given directory, and applies a function
	 * to the returned input stream and returns the value from that function.
	 *
	 * @param function The function to apply to the input stream.
	 * @param directory The directory to execute command lines within.
	 * @param commandLines The command lines to execute.
	 * @return Returns the result of the applied function.
	 */
	public static <R> R execute(ThrowingFunction<InputStream, R, IOException> function, File directory, String... commandLines)
	{
		File scriptFile = getTemporaryScriptFile(asList(commandLines));

		try
		{
			Process process = execute(directory, scriptFile);
			return function.apply(process.getInputStream());
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not execute temporary bash script", e);
		}
	}

	private static Process execute(File directory, File scriptFile) throws IOException
	{
		return new ProcessBuilder("bash", scriptFile.getAbsolutePath())
			.directory(directory)
			.start();
	}

	private static File getTemporaryScriptFile(Collection<String> lines)
	{
		try
		{
			File temporaryFile = createTempFile("svn-commit-temporary-script", "tmp");
			writeStringToFile(temporaryFile, "#!/bin/bash".concat(lineSeparator()), true);
			for (String line : lines)
			{
				writeStringToFile(temporaryFile, line.concat(lineSeparator()), true);
			}
			return temporaryFile;
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not create temporary bash script", e);
		}
	}
}