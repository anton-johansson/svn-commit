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

import static com.antonjohansson.svncommit.core.domain.FileStatus.UNVERSIONED;

import com.google.common.base.Predicate;

/**
 * Context menu item for running an {@code svn add} command.
 *
 * @author Anton Johansson
 */
class AddMenuItem extends AbstractRowContextMenuItem<ModifiedItem>
{
	/**
	 * Constructs a new {@link AddMenuItem}.
	 */
	AddMenuItem()
	{
		super("SVN Add");
	}

	/** {@inheritDoc} */
	@Override
	public Predicate<ModifiedItem> predicate()
	{
		return modiifedItem -> UNVERSIONED.equals(modiifedItem.getStatus());
	}
}
