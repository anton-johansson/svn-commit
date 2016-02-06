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

/**
 * The exception that is thrown upon known errors, which will be displayed as a
 * message dialog.
 *
 * @author Anton Johansson
 */
public class SvnCommitException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new {@link SvnCommitException}.
	 *
	 * @param message The message to display.
	 */
	public SvnCommitException(String message)
	{
		super(message);
	}

	/**
	 * Constructs a new {@link SvnCommitException}.
	 *
	 * @param message The message to display.
	 * @param cause The cause of the exception.
	 */
	public SvnCommitException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
