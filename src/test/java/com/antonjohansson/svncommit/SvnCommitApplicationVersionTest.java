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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Integration test of {@link SvnCommitApplication} that tests running the
 * application with the {@code --version} switch.
 *
 * @author Anton Johansson
 */
public class SvnCommitApplicationVersionTest extends Assert
{
	private PrintStream oldOutput;
	private PrintStream mockedOutput;

	@Before
	public void setUp()
	{
		oldOutput = System.out;
		mockedOutput = mock(PrintStream.class);
		System.setOut(mockedOutput);
	}

	@After
	public void tearDown()
	{
		System.setOut(oldOutput);
	}

	@Test
	public void test_print_version()
	{
		EntryPoint.main(new String[] { "--version" });

		verify(mockedOutput).println("Development");
		verifyNoMoreInteractions(mockedOutput);
	}
}
