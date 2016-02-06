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

import javafx.application.Platform;

/**
 * Holds the forced exit runner.
 *
 * @author Anton Johansson
 */
public class ForcedExit
{
	private static Runnable exit = () -> Platform.exit();

	/**
	 * Sets the forced exit.
	 *
	 * @param exit The forced exit.
	 */
	public static void setExit(Runnable exit)
	{
		ForcedExit.exit = exit;
	}

	/**
	 * Runs the forced exit.
	 */
	public static void exit()
	{
		exit.run();
	}
}
