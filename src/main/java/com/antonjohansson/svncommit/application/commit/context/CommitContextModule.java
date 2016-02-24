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

import static com.google.inject.multibindings.Multibinder.newSetBinder;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;

/**
 * Contains IOC bindings for the commit context menu.
 *
 * @author Anton Johansson
 */
public class CommitContextModule extends AbstractModule
{
	/** {@inheritDoc} */
	@Override
	protected void configure()
	{
		bind(CommitContextMenu.class).to(CommitContextMenuImpl.class);

		multibinder().addBinding().to(AddMenuItem.class);
		multibinder().addBinding().to(DeleteMenuItem.class);
	}

	private Multibinder<AbstractRowContextMenuItem<ModifiedItem>> multibinder()
	{
		return newSetBinder(binder(), new TypeLiteral<AbstractRowContextMenuItem<ModifiedItem>>() {});
	}
}
