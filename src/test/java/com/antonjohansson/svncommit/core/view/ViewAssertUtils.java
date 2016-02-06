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

import static org.hamcrest.Matchers.isA;

import java.util.Set;

import org.junit.Assert;
import org.testfx.api.FxAssert;
import org.testfx.service.query.NodeQuery;

import javafx.scene.Node;

/**
 * Assert utilities for view tests.
 *
 * @author Anton Johansson
 */
public class ViewAssertUtils extends Assert
{
	/**
	 * Gets a node by its unique identifier.
	 *
	 * @param identifier The identifier of the node.
	 * @return Returns the node.
	 */
	public static <N extends Node> N getNode(String identifier)
	{
		NodeQuery query = FxAssert.assertContext().getNodeFinder().lookup("#" + identifier);
		return query.queryFirst();
	}

	/**
	 * Gets a set of nodes by their style class.
	 *
	 * @param styleClass The style class of the nodes.
	 * @return Returns the set of nodes.
	 */
	public static <N extends Node> Set<N> getNodesByStyle(String styleClass)
	{
		NodeQuery query = FxAssert.assertContext().getNodeFinder().lookup("." + styleClass);
		return query.queryAll();
	}

	/**
	 * Gets a set of nodes by their types.
	 *
	 * @param type The type of the nodes to get.
	 * @return Returns the set of nodes.
	 */
	public static <N extends Node> Set<N> getNodes(Class<N> type)
	{
		NodeQuery query = FxAssert.assertContext().getNodeFinder().lookup(isA(type));
		return query.queryAll();
	}
}
