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
		super(menuItems.stream().toArray(size -> new MenuItem[size]));
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
