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

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Manages subversion.
 *
 * @author Anton Johansson
 */
public interface Subversion
{
	/**
	 * Gets a collection of all modified files.
	 *
	 * @return Returns the collection of modified files.
	 */
	Collection<ModifiedItem> getModifiedItems();

	/**
	 * Brings up the Meld compare tool for the given file.
	 *
	 * @param fileName The file to compare.
	 */
	void compare(String fileName);

	/**
	 * Performs an {@code svn update} on the path.
	 *
	 * @param onData The consumer that will accept log output.
	 * @param onComplete The task to run when the update is complete.
	 */
	void update(Consumer<String> onData, Consumer<Boolean> onComplete);

	/**
	 * Commits the given file paths with given message.
	 *
	 * @param message The message to use in the commit.
	 * @param filePaths The path of the files to commit.
	 * @param onData The consumer that will accept log output.
	 * @param onComplete The task to run when the update is complete.
	 */
	void commit(String message, Collection<String> filePaths, Consumer<String> onData, Consumer<Boolean> onComplete);

	/**
	 * Adds the given file.
	 *
	 * @param fileName The file name of the file to add.
	 */
	void add(String fileName);

	/**
	 * Deletes the given file.
	 *
	 * @param fileName The file name of the file to delete.
	 */
	void delete(String fileName);
}
