/**
 * Copyright 2015 Anton Johansson
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
package com.antonjohansson.svncommit.core.view.commit;

import com.antonjohansson.svncommit.core.domain.SvnItem;
import com.antonjohansson.svncommit.core.svn.SVN;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Refreshes the list of commitable files.
 *
 * @author Anton Johansson
 */
class RefreshCommand implements Consumer<Collection<SvnItem>>, Supplier<Collection<SvnItem>>
{
	private final File directory;
	private final Collection<SvnItem> items;

	RefreshCommand(File directory, Collection<SvnItem> items)
	{
		this.directory = directory;
		this.items = items;
	}

	@Override
	public Collection<SvnItem> get()
	{
		Collection<SvnItem> modifiedItems = SVN.getModifiedItems(directory);
		for (final SvnItem modifiedItem : modifiedItems)
		{
			Consumer<SvnItem> action = oldItem ->
			{
				modifiedItem.setDoCommit(oldItem.isDoCommit());
				modifiedItem.setReplication(oldItem.getReplication());
			};
			getOldItem(modifiedItem).ifPresent(action);
		}
		return modifiedItems;
	}

	private Optional<SvnItem> getOldItem(SvnItem modifiedItem)
	{
		return items.stream()
			.filter(s -> s.getFileName().equals(modifiedItem.getFileName()))
			.findAny();
	}

	@Override
	public void accept(Collection<SvnItem> modifiedItems)
	{
		items.clear();
		items.addAll(modifiedItems);
	}
}
