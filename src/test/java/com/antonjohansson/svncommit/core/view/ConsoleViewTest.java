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
import static org.hamcrest.Matchers.not;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Unit tests of {@link ConsoleView}.
 *
 * @author Anton Johansson
 */
public class ConsoleViewTest extends ApplicationTest
{
	private ConsoleView view;

	@Override
	public void start(Stage stage) throws Exception
	{
		String name = ConsoleView.class.getSimpleName() + ".fxml";
		URL location = ConsoleView.class.getResource(name);
		try
		{
			FXMLLoader loader = new FXMLLoader(location);
			Parent parent = loader.load();
			view = loader.getController();
			view.setParent(parent);

			stage.setScene(new Scene(parent));
			stage.show();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Test
	public void test_append()
	{
		verifyThat("#console", hasText(""));
		
		view.append("some test string" + lineSeparator());
		verifyThat("#console", hasText("some test string" + lineSeparator()));
		
		view.append("another string" + lineSeparator());
		verifyThat("#console", hasText("some test string" + lineSeparator() + "another string" + lineSeparator()));
	}

	@Test
	public void test_showIcon()
	{
		verifyThat("#icon", not(isVisible()));

		view.showIcon("success");
		verifyThat("#icon", isVisible());
	}
}
