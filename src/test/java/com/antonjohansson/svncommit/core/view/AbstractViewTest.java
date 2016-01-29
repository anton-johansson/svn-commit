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

import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.service.query.NodeQuery;

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
		URL resource = AbstractViewTest.class.getResource("/" + viewClass.getName().replace(".", "/") + ".fxml");
		System.out.println(resource);
		
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
	 * Gets a node by its unique identifier.
	 * 
	 * @param identifier The identifier of the node.
	 * @return Returns the node.
	 */
	protected <N extends Node> N getNode(String identifier)
	{
		NodeQuery query = FxAssert.assertContext().getNodeFinder().lookup("#" + identifier);
		return query.queryFirst();
	}
}
