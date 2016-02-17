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
