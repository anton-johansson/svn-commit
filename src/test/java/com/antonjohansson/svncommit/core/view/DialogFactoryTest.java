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

import static com.antonjohansson.svncommit.core.view.ViewAssertUtils.getNodes;
import static com.antonjohansson.svncommit.core.view.ViewAssertUtils.getNodesByStyle;
import static javafx.application.Platform.runLater;
import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.MouseButton.PRIMARY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Unit tests of {@link DialogFactory}.
 *
 * @author Anton Johansson
 */
public class DialogFactoryTest extends ApplicationTest
{
	private final DialogFactory factory = new DialogFactory();

	@Override
	public void start(Stage stage) throws Exception
	{
	}

	@Test
	public void test_create()
	{
		AtomicBoolean isClosed = new AtomicBoolean(false);
		AtomicReference<Stage> stage = new AtomicReference<>(null);
		View view = new View()
		{
			@Override
			public void setParent(Parent parent)
			{
			}

			@Override
			public Parent getParent()
			{
				return new Label("Hello Dialog!");
			}
		};

		runLater(() ->
		{
			Stage createdStage = factory.create()
					.height(123.0)
					.width(456.0)
					.onClose(e -> isClosed.set(true))
					.view(view)
					.show();

			stage.set(createdStage);
		});

		sleep(1000);

		try
		{
			Optional<Label> label = getNodes(Label.class)
					.stream()
					.findAny();

			assertTrue(label.isPresent());
			assertEquals("Hello Dialog!", label.get().getText());

			Window window = label.get().getScene().getWindow();
			assertTrue(window.isShowing());
			assertEquals(456.0, window.getWidth(), 0.000001);
			assertEquals(123.0, window.getHeight(), 0.000001);
		}
		finally
		{
			runLater(() ->
			{
				stage.get().getOnCloseRequest().handle(null);
				stage.get().close();
			});
		}

		sleep(200);
		assertTrue(isClosed.get());
	}

	@Test
	public void test_error()
	{
		runLater(() ->
		{
			factory.error("some error message");
		});

		sleep(1000);

		// Check contents of the dialog
		Set<Label> label = getNodesByStyle("label");
		Iterator<Label> iterator = label.iterator();
		assertEquals("Error", iterator.next().getText());
		assertEquals("some error message", iterator.next().getText());
		assertFalse(iterator.hasNext());

		// Get the button to click
		Optional<Button> button = getNodes(Button.class)
				.stream()
				.findAny();

		Window window = button.get().getScene().getWindow();
		assertTrue(window.isShowing());

		// Verify that we found the button, then click it
		assertTrue(button.isPresent());
		moveTo(button.get());
		clickOn(PRIMARY);

		sleep(100);
		assertFalse(window.isShowing());
	}

	@Test
	public void test_confirm_yes()
	{
		assertConfirm("Yes", false, true);
	}

	@Test
	public void test_confirm_no()
	{
		assertConfirm("No", false, false);
	}

	@Test
	public void test_confirm_enter()
	{
		assertConfirm("", true, true);
	}

	private void assertConfirm(String buttonToClick, boolean pressEnterInsteadOfButton, boolean expectedResult)
	{
		AtomicBoolean confirmed = new AtomicBoolean(!expectedResult);
		runLater(() ->
		{
			boolean isConfirmed = factory.confirm("Hello %s?", "World");
			confirmed.set(isConfirmed);
		});
		sleep(1000);

		// Check contents of the dialog
		Set<Label> label = getNodesByStyle("label");
		Iterator<Label> iterator = label.iterator();
		assertEquals("Warning", iterator.next().getText());
		assertEquals("Hello World?", iterator.next().getText());
		assertFalse(iterator.hasNext());

		if (pressEnterInsteadOfButton)
		{
			// Press [Enter]
			press(A); // should have no effect
			release(A);
			press(ENTER);
			release(ENTER);
		}
		else
		{
			// Get the button to click
			Optional<Button> button = getNodes(Button.class)
					.stream()
					.filter(b -> buttonToClick.equals(b.getText()))
					.findAny();

			// Verify that we found the button, then click it
			assertTrue(button.isPresent());
			moveTo(button.get());
			clickOn(PRIMARY);
		}

		sleep(50);
		assertEquals(expectedResult, confirmed.get());
	}
}
