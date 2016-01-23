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
package com.antonjohansson.svncommit2.application.commit;

import com.antonjohansson.svncommit2.core.controller.AbstractController;
import com.antonjohansson.svncommit2.core.controller.Controller;
import com.antonjohansson.svncommit2.core.domain.ModifiedItem;
import com.antonjohansson.svncommit2.core.utils.Subversion;
import com.antonjohansson.svncommit2.core.view.LoadingView;

import static javafx.scene.input.KeyCode.F5;

import java.util.Collection;

import com.google.inject.Inject;

/**
 * Commit implementation of {@link Controller}.
 *
 * @author Anton Johansson
 */
class CommitController extends AbstractController<LoadingView>
{
	private final CommitView commitView;
	private final LoadingView loadingView;
	private final Subversion subversion;

	@Inject
	CommitController(CommitView commitView, LoadingView loadingView, Subversion subversion)
	{
		super(loadingView);
		this.commitView = commitView;
		this.loadingView = loadingView;
		this.subversion = subversion;
	}

	/** {@inheritDoc} */
	@Override
	public void initialize()
	{
		commitView.getParent().setOnKeyPressed(e ->
		{
			if (F5.equals(e.getCode()))
			{
				refresh();
			}
		});

		loadingView.setContent(commitView);
		refresh();
	}

	private synchronized void refresh()
	{
		if (loadingView.isLoading())
		{
			return;
		}

		loadingView.setLoading(true);
		Runnable loader = () ->
		{
			Collection<ModifiedItem> items = subversion.getModifiedItems();
			commitView.setItems(items);
			loadingView.setLoading(false);
		};
		new Thread(loader).start();
	}
}
