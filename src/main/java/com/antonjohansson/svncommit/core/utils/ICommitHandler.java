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

import java.io.File;
import java.util.Collection;

/**
 * Handles commits.
 *
 * @author Anton Johansson
 */
@FunctionalInterface
public interface ICommitHandler
{
	/**
	 * Called when the application should commit to SVN.
	 *
	 * @param directory The directory which the files to commit are contained in.
	 * @param message The message to use.
	 * @param filePaths The collection of file paths to commit.
	 */
	void onCommit(File directory, String message, Collection<String> filePaths);
}
