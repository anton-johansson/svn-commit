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

import org.jemmy.fx.AppExecutor;
import org.jemmy.fx.NodeDock;
import org.jemmy.fx.SceneDock;
import org.jemmy.fx.control.TextInputControlDock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javafx.scene.image.ImageView;

/**
 * Unit tests of {@link ConsoleView}.
 *
 * @author Anton Johansson
 */
public class ConsoleViewTest extends Assert
{
	private SceneDock scene;
	private TextInputControlDock console;
	private NodeDock icon;
	private static boolean initialized;

	@Before
	public void setUp()
	{
		if (!initialized)
		{
			Runtime.getRuntime().addShutdownHook(new Thread(() -> Thread.currentThread().interrupt()));
			AppExecutor.executeNoBlock(TestApp.class);
			initialized = true;
		}
		
		scene = new SceneDock();
		console = new TextInputControlDock(scene.asParent(), "console");
		icon = new NodeDock(scene.asParent(), "icon");
	}
	
	private ImageView icon()
	{
		return (ImageView) icon.control();
	}

	@Test
	public void test_append()
	{
		TestApp.view.append("some test string");
		
		String actual = console.getText();
		String expected = "some test string";
		
		assertEquals(expected, actual);
	}

	@Test
	public void test_showIcon()
	{
		assertFalse(icon().isVisible());

		TestApp.view.showIcon("success");

		assertTrue(icon().isVisible());
	}
}
