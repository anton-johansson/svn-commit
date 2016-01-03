package com.antonjohansson.svncommit.application.commit;

import com.antonjohansson.svncommit.core.domain.SvnItem;
import com.antonjohansson.svncommit.core.svn.SVN;

import java.io.File;
import java.util.Collection;
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
		return SVN.getModifiedItems(directory);
	}

	@Override
	public void accept(Collection<SvnItem> modifiedItems)
	{
		items.clear();
		items.addAll(modifiedItems);
	}
}
