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

import com.antonjohansson.svncommit.core.domain.ModifiedItem;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.io.IOUtils.readLines;

import java.io.File;
import java.util.Collection;
import java.util.function.Consumer;

import com.google.inject.Inject;

/**
 * Default implementation of {@link Subversion}.
 *
 * @author Anton Johansson
 */
class SubversionImpl implements Subversion
{
	private static final String COMPARE_COMMAND_PATTERN = "meld '%F'";

	private final Shell shell;

	/**
	 * Constructs a new {@link SubversionImpl} instance.
	 *
	 * @param shell The shell to use.
	 */
	@Inject
	SubversionImpl(Shell shell)
	{
		this.shell = shell;
	}

	/** {@inheritDoc} */
	@Override
	public Collection<ModifiedItem> getModifiedItems()
	{
		return shell.execute(s -> readLines(s), "svn status")
			.stream()
			.map(Converter::modifiedItem)
			.collect(toList());
	}

	/** {@inheritDoc} */
	@Override
	public void compare(String fileName)
	{
		String command = COMPARE_COMMAND_PATTERN.replace("%F", fileName);
		shell.execute(command);
	}

	/** {@inheritDoc} */
	@Override
	public void update(Consumer<String> onData, Consumer<Boolean> onComplete)
	{
		shell.executeAndPipeOutput(onData, onData, onComplete, "svn update");
	}

	/** {@inheritDoc} */
	@Override
	public void commit(String message, Collection<String> filePaths, Consumer<String> onData, Consumer<Boolean> onComplete)
	{
		File temporaryFile = shell.getTemporaryFile(asList(message), "commit-message");

		StringBuilder command = new StringBuilder("svn commit")
			.append(" --file '")
			.append(temporaryFile.getAbsolutePath())
			.append("'");

		filePaths.forEach(c -> command.append(" '").append(c).append("'"));

		shell.executeAndPipeOutput(onData, onData, onComplete, command.toString());
	}

	/** {@inheritDoc} */
	@Override
	public void add(String fileName)
	{
		shell.execute("svn add '" + fileName + "'");
	}

	/** {@inheritDoc} */
	@Override
	public void delete(String fileName)
	{
		shell.execute("svn delete '" + fileName + "'");
	}
}
