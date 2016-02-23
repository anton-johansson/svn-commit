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
package com.antonjohansson.svncommit.application.commit.context;

import com.antonjohansson.svncommit.core.domain.ModifiedItem;
import com.antonjohansson.svncommit.core.view.AbstractRowContextMenuItem;

import java.util.Collection;
import java.util.Set;

import com.google.inject.Inject;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * Default implementation of {@link CommitContextMenu}.
 *
 * @author Anton Johansson
 */
class CommitContextMenuImpl extends ContextMenu implements CommitContextMenu
{
	private final Set<AbstractRowContextMenuItem<ModifiedItem>> menuItems;

	/**
	 * Constructs a new {@link CommitContextMenuImpl}.
	 */
	@Inject
	CommitContextMenuImpl(Set<AbstractRowContextMenuItem<ModifiedItem>> menuItems)
	{
		super(menuItems.stream().toArray(MenuItem[]::new));
		this.menuItems = menuItems;
	}

	/** {@inheritDoc} */
	@Override
	public ContextMenu getContextMenu()
	{
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public Collection<AbstractRowContextMenuItem<ModifiedItem>> getMenuItems()
	{
		return menuItems;
	}
}
