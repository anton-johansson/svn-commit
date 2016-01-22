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

import com.antonjohansson.svncommit.core.utils.ThrowingFunction;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * Provides access to the operating systems shell.
 *
 * @author Anton Johansson
 */
public interface Shell
{
	/**
	 * Executes given command lines in given directory.
	 *
	 * @param commandLines The command lines to execute.
	 */
	void execute(String... commandLines);

	/**
	 * Executes given command lines in given directory, and applies a function
	 * to the returned input stream and returns the value from that function.
	 *
	 * @param function The function to apply to the input stream.
	 * @param commandLines The command lines to execute.
	 * @return Returns the result of the applied function.
	 */
	<R> R execute(ThrowingFunction<InputStream, R, IOException> function, String... commandLines);

	/**
	 * Executes given command lines in given directory, and pipes the output
	 * to the given {@code onData} consumer.
	 *
	 * @param onData The consumer that will accept output from the process stream.
	 * @param onError The consumer that will accept errors from the process stream.
	 * @param onComplete The task to execute when the process is complete.
	 * @param commandLines The command lines to execute.
	 */
	void executeAndPipeOutput(Consumer<String> onData, Consumer<String> onError, Runnable onComplete, String... commandLines);

	/**
	 * Creates a temporary file with the given lines.
	 *
	 * @param lines The lines to add to the file.
	 * @return Returns the {@link File}.
	 */
	File getTemporaryFile(Collection<String> lines, String prefix);
}
