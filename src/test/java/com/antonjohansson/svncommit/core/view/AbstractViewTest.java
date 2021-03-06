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

import java.io.IOException;
import java.net.URL;

import org.testfx.framework.junit.ApplicationTest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Abstract skeleton for testing {@link View} implementations.
 *
 * @author Anton Johansson
 */
public abstract class AbstractViewTest<V extends View> extends ApplicationTest
{
	private final Class<V> viewClass;

	protected V view;

	/**
	 * Constructs a new {@link AbstractViewTest}.
	 */
	protected AbstractViewTest()
	{
		this.viewClass = Utils.getParameterClass(getClass());
	}

	/** {@inheritDoc} */
	@Override
	public final void start(Stage stage) throws Exception
	{
		String name = viewClass.getSimpleName() + ".fxml";
		URL location = viewClass.getResource(name);

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

		initNodes();
	}

	/**
	 * Initializes the nodes that should be verified.
	 */
	protected abstract void initNodes();

	/**
	 * @see ViewAssertUtils#getNode(String)
	 */
	protected <N extends Node> N getNode(String identifier)
	{
		return ViewAssertUtils.getNode(identifier);
	}
}
