package com.antonjohansson.svncommit.application.commit.context;

import com.antonjohansson.svncommit.core.domain.ModifiedItem;
import com.antonjohansson.svncommit.core.view.AbstractRowContextMenuItem;

import java.util.Collection;

import javafx.scene.control.ContextMenu;

/**
 * Context menu for the commit view.
 *
 * @author Anton Johansson
 */
public interface CommitContextMenu
{
	/**
	 * Gets the actual context menu.
	 * @return Returns the actual context menu.
	 */
	ContextMenu getContextMenu();

	/**
	 * Gets all menu items.
	 *
	 * @return Returns all menu items.
	 */
	Collection<AbstractRowContextMenuItem<ModifiedItem>> getMenuItems();
}
