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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

/**
 * Integration test of {@link SvnCommitApplication} that tests running the
 * application with the 'update' argument.
 *
 * @author Anton Johansson
 */
public class SvnCommitApplicationUpdateTest extends AbstractSvnCommitApplicationTest
{
	public SvnCommitApplicationUpdateTest()
	{
		super("--application", "update", "--path", ".");
	}

	@Test
	public void test_update() throws Exception
	{
		sleep(100);

		TextArea console = getNode("console");
		String expected = "Skipped '.'" + lineSeparator() + "Summary of conflicts:" + lineSeparator() + "  Skipped paths: 1" + lineSeparator();
		String actual = console.getText();
		assertEquals(expected, actual);

		ImageView icon = getNode("icon");
		assertTrue(icon.isVisible());
	}
}
