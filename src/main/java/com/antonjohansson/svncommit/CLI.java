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

import static org.apache.commons.cli.Option.builder;

import org.apache.commons.cli.Options;

/**
 * Contains options and constants for the CLI.
 *
 * @author Anton Johansson
 */
class CLI
{
	/** Argument for which application to start. */
	static final String APPLICATION = "application";

	/** Argument for the location of the configuration file. */
	static final String CONFIGURATION = "configuration";

	/** Argument of the path to perform SVN operations on. */
	static final String PATH = "path";

	/** Argument for showing the application version. */
	static final String VERSION = "version";

	/**
	 * Contains the {@link Options options} used for the CLI.
	 */
	static final Options OPTIONS = new Options()
			.addOption(builder("a")
					.longOpt(APPLICATION)
					.desc("the application to run, e. g. 'commit'")
					.hasArg()
					.argName("APP")
					.build())
			.addOption(builder("c")
					.longOpt(CONFIGURATION)
					.desc("the location of the configuration file")
					.hasArg()
					.argName("PATH")
					.build())
			.addOption(builder("p")
					.longOpt(PATH)
					.desc("the path to execute SVN commands in")
					.hasArg()
					.argName("PATH")
					.build())
			.addOption(builder("v")
					.longOpt(VERSION)
					.desc("prints the version of the application")
					.build());
}
