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
import com.antonjohansson.svncommit2.core.view.ConsoleView;
import com.antonjohansson.svncommit2.core.view.DialogFactory;
import com.antonjohansson.svncommit2.core.view.LoadingView;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static javafx.scene.input.KeyCode.F5;

import java.util.Collection;
import java.util.function.Consumer;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Commit implementation of {@link Controller}.
 *
 * @author Anton Johansson
 */
class CommitController extends AbstractController<LoadingView>
{
	private final CommitView commitView;
	private final LoadingView loadingView;
	private final Provider<ConsoleView> consoleViewProvider;
	private final DialogFactory dialogFactory;
	private final Subversion subversion;
	private Collection<ModifiedItem> items = emptyList();

	@Inject
	CommitController(
			CommitView commitView,
			LoadingView loadingView,
			Provider<ConsoleView> consoleViewProvider,
			DialogFactory dialogFactory,
			Subversion subversion)
	{
		super(loadingView);
		this.commitView = commitView;
		this.loadingView = loadingView;
		this.consoleViewProvider = consoleViewProvider;
		this.dialogFactory = dialogFactory;
		this.subversion = subversion;
	}

	/** {@inheritDoc} */
	@Override
	public void initialize()
	{
		initializeHandlers();

		loadingView.setContent(commitView);
		refresh();
	}

	private void initializeHandlers()
	{
		commitView.getParent().setOnKeyPressed(event ->
		{
			if (F5.equals(event.getCode()))
			{
				refresh();
			}
		});

		commitView.setCommitHandler(message ->
		{
			ConsoleView consoleView = consoleViewProvider.get();

			dialogFactory.create()
				.view(consoleView)
				.onClose(event -> refresh())
				.width(600.0)
				.height(300.0)
				.show();

			Consumer<String> onData = data -> consoleView.append(data);
			Consumer<Boolean> onComplete = success -> consoleView.showIcon(success ? "success" : "failed");
			Collection<String> paths = items.stream()
				.filter(s -> s.isDoCommit())
				.map(s -> s.getFileName())
				.collect(toList());

			subversion.commit(message, paths, onData, onData, onComplete);
		});
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
			items = getModifiedItems();
			commitView.setItems(items);
			loadingView.setLoading(false);
		};
		new Thread(loader).start();
	}

	private Collection<ModifiedItem> getModifiedItems()
	{
		Collection<ModifiedItem> modifiedItems = subversion.getModifiedItems();
		modifiedItems.stream().forEach(modifiedItem ->
		{
			items.stream()
					.filter(modifiedItem::isSamePath)
					.findAny()
					.ifPresent(item ->
					{
						modifiedItem.setDoCommit(item.isDoCommit());
						modifiedItem.setReplication(item.getReplication());
					});
		});
		return modifiedItems;
	}
}
