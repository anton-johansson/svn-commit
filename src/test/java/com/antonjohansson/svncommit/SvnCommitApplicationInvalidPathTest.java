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

import static java.util.Arrays.asList;
import static javafx.scene.input.MouseButton.PRIMARY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Window;

/**
 * Integration test of {@link SvnCommitApplication} that tests running the
 * application with the 'update' argument and an invalid 'path' argument.
 *
 * @author Anton Johansson
 */
public class SvnCommitApplicationInvalidPathTest extends AbstractSvnCommitApplicationTest
{
	public SvnCommitApplicationInvalidPathTest()
	{
		super("--application", "update", "--path", "/some/invalid/path/that/does/not/exist");
	}

	@Test
	public void test_update_with_invalid_path() throws Exception
	{
		sleep(100);

		Set<String> expected = new HashSet<>(asList("Error", "Cannot run program \"bash\" (in directory \"/some/invalid/path/that/does/not/exist\"): error=2, No such file or directory"));
		Set<String> actual = new HashSet<>();
		for (Label label : getNodes(Label.class))
		{
			actual.add(label.getText());
		}
		assertEquals(expected, actual);

		Optional<Button> button = getNodes(Button.class).stream().findAny();
		assertTrue(button.isPresent());

		Window window = button.get().getScene().getWindow();
		assertTrue(window.isShowing());

		// Click the close button
		moveTo(button.get());
		clickOn(PRIMARY);

		assertFalse(window.isShowing());
	}
}
