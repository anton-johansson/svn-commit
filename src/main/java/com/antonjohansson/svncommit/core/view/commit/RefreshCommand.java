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
