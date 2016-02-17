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
	}

	private Multibinder<AbstractRowContextMenuItem<ModifiedItem>> multibinder()
	{
		return newSetBinder(binder(), new TypeLiteral<AbstractRowContextMenuItem<ModifiedItem>>() {});
	}
}
