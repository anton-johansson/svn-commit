package com.antonjohansson.svncommit.application.commit;

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
	private final Collection<SvnItem> items;
	private final File directory;

	RefreshCommand(Collection<SvnItem> items, File directory)
	{
		this.items = items;
		this.directory = directory;
	}

	@Override
	public Collection<SvnItem> get()
	{
		Collection<SvnItem> modifiedItems = SVN.getModifiedItems(directory);
		for (final SvnItem modifiedItem : modifiedItems)
		{
			Consumer<SvnItem> action = oldItem ->
			{
				modifiedItem.doCommitProperty().setValue(oldItem.doCommitProperty().getValue());
				modifiedItem.replicationProperty().setValue(oldItem.replicationProperty().getValue());
			};
			getOldItem(modifiedItem).ifPresent(action);
		}
		return modifiedItems;
	}

	private Optional<SvnItem> getOldItem(SvnItem modifiedItem)
	{
		return items.stream()
			.filter(s -> s.fileNameProperty().getValue().equals(modifiedItem.fileNameProperty().getValue()))
			.findAny();
	}

	@Override
	public void accept(Collection<SvnItem> modifiedItems)
	{
		items.clear();
		items.addAll(modifiedItems);
	}
}
