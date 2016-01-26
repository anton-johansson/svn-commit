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
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

import com.google.inject.Inject;

/**
 * Unix implementation of {@link Shell}.
 *
 * @author Anton Johansson
 */
class Bash implements Shell
{
	private final File path;

	/**
	 * Constructs a new {@link Bash} instance.
	 *
	 * @param path The path to execute bash scripts in.
	 */
	@Inject
	Bash(File path)
	{
		this.path = path;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(String... commandLines)
	{
		File scriptFile = getTemporaryScriptFile(asList(commandLines));

		try
		{
			execute(path, scriptFile);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not execute temporary bash script", e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public <R> R execute(ThrowingFunction<InputStream, R, IOException> function, String... commandLines)
	{
		File scriptFile = getTemporaryScriptFile(asList(commandLines));

		try
		{
			Process process = execute(path, scriptFile);
			return function.apply(process.getInputStream());
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not execute temporary bash script", e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void executeAndPipeOutput(Consumer<String> onData, Consumer<String> onError, Consumer<Boolean> onComplete, String... commandLines)
	{
		File scriptFile = getTemporaryScriptFile(asList(commandLines));

		try
		{
			Process process = execute(path, scriptFile);
			InputStream logStream = process.getInputStream();
			InputStream errorStream = process.getErrorStream();
			while (isAvailable(process, logStream, errorStream))
			{
				if (logStream.available() > 0)
				{
					accept(onData, logStream);
				}
				if (errorStream.available() > 0)
				{
					accept(onError, errorStream);
				}
			}
			int exitValue = process.exitValue();
			onComplete.accept(exitValue == 0);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not execute temporary bash script", e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public File getTemporaryFile(Collection<String> lines, String prefix)
	{
		try
		{
			File temporaryFile = createTempFile("svn-commit-" + prefix + "-", ".tmp");
			for (String line : lines)
			{
				writeStringToFile(temporaryFile, line.concat(lineSeparator()), true);
			}
			return temporaryFile;
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not create temporary file", e);
		}
	}

	private boolean isAvailable(Process process, InputStream logStream, InputStream errorStream) throws IOException
	{
		return logStream.available() > 0
			|| errorStream.available() > 0
			|| process.isAlive();
	}

	private void accept(Consumer<String> onData, InputStream logStream) throws IOException
	{
		byte[] buffer = new byte[1024];
		logStream.read(buffer);
		String output = new String(buffer);
		onData.accept(output);
	}

	/**
	 * Executes given script file in given directory.
	 *
	 * @param directory The directory to execute command lines within.
	 * @param scriptFile The script file to execute.
	 * @return Returns the process.
	 */
	private Process execute(File directory, File scriptFile) throws IOException
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
	private File getTemporaryScriptFile(Collection<String> commandLines)
	{
		Collection<String> lines = new ArrayList<>();
		lines.add("#!/bin/bash");
		lines.addAll(commandLines);
		return getTemporaryFile(lines, "temporary-script");
	}
}
