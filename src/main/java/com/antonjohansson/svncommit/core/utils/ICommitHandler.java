package com.antonjohansson.svncommit.core.utils;

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
	 * @param message The message to use.
	 * @param filePaths The collection of file paths to commit.
	 */
	void onCommit(String message, Collection<String> filePaths);
}
