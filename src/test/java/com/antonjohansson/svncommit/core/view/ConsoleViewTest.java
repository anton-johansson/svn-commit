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
package com.antonjohansson.svncommit.core.view;

import static java.lang.System.lineSeparator;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;

import org.junit.Test;

import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

/**
 * Unit tests of {@link ConsoleView}.
 *
 * @author Anton Johansson
 */
public class ConsoleViewTest extends AbstractViewTest<ConsoleView>
{
	private TextArea console;
	private ImageView icon;

	@Override
	protected void initNodes()
	{
		console = getNode("console");
		icon = getNode("icon");
	}

	@Test
	public void test_append()
	{
		verifyThat(console, hasText(""));

		view.append("some test string" + lineSeparator());
		verifyThat(console, hasText("some test string" + lineSeparator()));

		view.append("another string" + lineSeparator());
		verifyThat(console, hasText("some test string" + lineSeparator() + "another string" + lineSeparator()));
	}

	@Test
	public void test_showIcon()
	{
		assertFalse(icon.isVisible());
		assertNull(icon.getImage());

		view.showIcon("success");
		assertTrue(icon.isVisible());
		assertNotNull(icon.getImage());
	}
}
