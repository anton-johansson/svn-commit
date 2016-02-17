package com.antonjohansson.svncommit.core.view;

import com.google.common.base.Predicate;

import javafx.scene.control.MenuItem;

/**
 * Abstract skeleton for context menu items for table view rows in the application.
 *
 * @param <T> The type of of items that the table view holds.
 *
 * @author Anton Johansson
 */
public abstract class AbstractRowContextMenuItem<T> extends MenuItem
{
	/**
	 * Constructs a new {@link AbstractRowContextMenuItem}.
	 *
	 * @param title The title of the menu item.
	 */
	protected AbstractRowContextMenuItem(String title)
	{
		super(title);
	}

	/**
	 * Enables or disables this menu item depending on the predicate and the given item.
	 *
	 * @param item The item of the row.
	 */
	public void enable(T item)
	{
		boolean enabled = predicate().apply(item);
		setDisable(!enabled);
	}

	/**
	 * Gets the predicate that determines whether or not this menu item
	 * should be enabled for the current row.
	 *
	 * @return Returns the predicate.
	 */
	protected abstract Predicate<T> predicate();
}
