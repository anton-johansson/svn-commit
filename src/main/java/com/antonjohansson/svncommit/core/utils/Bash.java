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

	/**
	 * Executes given script file in given directory.
	 *
	 * @param directory The directory to execute command lines within.
	 * @param scriptFile The script file to execute.
	 * @return Returns the process.
	 */
	private static Process execute(File directory, File scriptFile) throws IOException
	{
		return new ProcessBuilder("bash", scriptFile.getAbsolutePath())
			.directory(directory)
			.start();
	}

	/**
	 * Creates a temporary bash script with the given command lines.
	 *
	 * @param commandLines The command lines to add to the bash script.
	 * @return Returns the bash script {@link File}.
	 */
	private static File getTemporaryScriptFile(Collection<String> commandLines)
	{
		try
		{
			File temporaryFile = createTempFile("svn-commit-temporary-script", "tmp");
			writeStringToFile(temporaryFile, "#!/bin/bash".concat(lineSeparator()), true);
			for (String commandLine : commandLines)
			{
				writeStringToFile(temporaryFile, commandLine.concat(lineSeparator()), true);
			}
			return temporaryFile;
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not create temporary bash script", e);
		}
	}
}
