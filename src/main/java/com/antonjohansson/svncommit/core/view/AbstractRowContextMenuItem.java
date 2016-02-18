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

import com.google.common.base.Predicate;

import javafx.scene.control.MenuItem;

/**
 * Abstract skeleton for context menu items for table view rows in the application.
 *
 * @param <T> The type of of items that the table view holds.
 *
 * @author Anton Johansson
 */
public abstract class AbstractRowContextMenuItem<T> extends MenuItem
{
	/**
	 * Constructs a new {@link AbstractRowContextMenuItem}.
	 *
	 * @param title The title of the menu item.
	 */
	protected AbstractRowContextMenuItem(String title)
	{
		super(title);
	}

	/**
	 * Enables or disables this menu item depending on the predicate and the given item.
	 *
	 * @param item The item of the row.
	 */
	public void enable(T item)
	{
		boolean enabled = predicate().apply(item);
		setDisable(!enabled);
	}

	/**
	 * Gets the predicate that determines whether or not this menu item
	 * should be enabled for the current row.
	 *
	 * @return Returns the predicate.
	 */
	protected abstract Predicate<T> predicate();
}
