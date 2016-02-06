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

import static java.lang.System.lineSeparator;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

/**
 * Integration test of {@link SvnCommitApplication} that tests running the
 * application with the {@code --version} switch.
 *
 * @author Anton Johansson
 */
public class SvnCommitApplicationVersionTest extends AbstractSvnCommitApplicationTest
{
	private volatile PrintStream oldOutput;
	private volatile ByteArrayOutputStream output;

	public SvnCommitApplicationVersionTest() throws Exception
	{
		super("--version");
	}

	@Override
	public void setUp()
	{
		output = new ByteArrayOutputStream();
		PrintStream stream = new PrintStream(output);

		oldOutput = System.out;
		System.setOut(stream);
	}

	@Override
	public void tearDown()
	{
		System.setOut(oldOutput);
	}

	@Test
	public void test_print_version() throws Exception
	{
		String actual = output.toString();
		String expected = "Development" + lineSeparator();

		assertEquals(expected, actual);
	}
}
