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

import com.antonjohansson.svncommit.core.domain.SvnItem;
import com.antonjohansson.svncommit.core.svn.Converter;
import com.antonjohansson.svncommit.core.utils.Bash;

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
	public Collection<SvnItem> getModifiedItems()
	{
		return shell.execute(s -> readLines(s), "svn status")
			.stream()
			.map(Converter::convertFile)
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
	public void update(Consumer<String> onData, Runnable onComplete)
	{
		shell.executeAndPipeOutput(onData, onData, onComplete, "svn update");
	}

	/** {@inheritDoc} */
	@Override
	public void commit(String message, Collection<String> filePaths, Consumer<String> onData, Consumer<String> onError, Runnable onComplete)
	{
		File temporaryFile = Bash.getTemporaryFile(asList(message), "commit-message");

		StringBuilder command = new StringBuilder("svn commit")
			.append(" --file '")
			.append(temporaryFile.getAbsolutePath())
			.append("'");

		filePaths.forEach(c -> command.append(" '").append(c).append("'"));

		shell.executeAndPipeOutput(onData, onError, onComplete, command.toString());
	}
}
