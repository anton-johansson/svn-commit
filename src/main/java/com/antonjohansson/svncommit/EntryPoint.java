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
package com.antonjohansson.svncommit;

import com.antonjohansson.svncommit.application.commit.CommitApplication;
import com.antonjohansson.svncommit.application.commit.UpdateApplication;

import static javafx.application.Application.launch;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.apache.commons.lang3.ArrayUtils.remove;

/**
 * Contains the applications main entry-point.
 *
 * @author Anton Johansson
 */
public class EntryPoint
{
	/**
	 * Application main entry-point.
	 */
	public static void main(String[] arguments)
	{
		if (isEmpty(arguments))
		{
			System.err.println("Missing first argument");
			System.exit(1);
			return;
		}

		String[] innerArguments = remove(arguments, 0);
		switch (arguments[0])
		{
			case "commit":
				launch(CommitApplication.class, innerArguments);
				break;

			case "update":
				launch(UpdateApplication.class, innerArguments);
				break;

			default:
				System.err.println("Invalid usage");
				System.exit(1);
		}
	}
}
