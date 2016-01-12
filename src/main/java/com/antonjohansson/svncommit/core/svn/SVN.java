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
package com.antonjohansson.svncommit.core.svn;

import com.antonjohansson.svncommit.core.domain.SvnItem;
import com.antonjohansson.svncommit.core.utils.Bash;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.io.IOUtils.readLines;

import java.io.File;
import java.util.Collection;
import java.util.function.Consumer;

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

	/**
	 * Performs an SVN update on the given path.
	 *
	 * @param path The path to perform SVN update on.
	 * @param onData The consumer that will accept log output.
	 * @param onComplete The task to run when the update is complete.
	 */
	public static void update(File path, Consumer<String> onData, Runnable onComplete)
	{
		Bash.executeAndPipeOutput(onData, onComplete, path, "svn update");
	}

	/**
	 * Commits the given file paths with given message.
	 *
	 * @param message The message to use in the commit.
	 * @param filePaths The path of the files to commit.
	 */
	public static void commit(String message, Collection<String> filePaths)
	{
		System.out.println("Committing:");
		System.out.println(message);
		System.out.println(filePaths);
	}
}
