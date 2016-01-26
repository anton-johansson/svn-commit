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

import com.antonjohansson.svncommit.core.view.utils.Alerter;
import com.antonjohansson.svncommit2.core.controller.AbstractController;
import com.antonjohansson.svncommit2.core.controller.Controller;
import com.antonjohansson.svncommit2.core.domain.ModifiedItem;
import com.antonjohansson.svncommit2.core.utils.Subversion;
import com.antonjohansson.svncommit2.core.view.ConsoleView;
import com.antonjohansson.svncommit2.core.view.DialogFactory;
import com.antonjohansson.svncommit2.core.view.LoadingView;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import com.google.inject.Inject;
import com.google.inject.Provider;

import javafx.scene.input.KeyCode;

/**
 * Commit implementation of {@link Controller}.
 *
 * @author Anton Johansson
 */
class CommitController extends AbstractController<LoadingView>
{
	private final Map<KeyCode, Runnable> keyMappings = keyMappings();
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
		commitView.setOnKeyPressed(event ->
		{
			Runnable handler = keyMappings.get(event.getCode());
			Optional.ofNullable(handler).ifPresent(h -> h.run());
		});
		commitView.setOnMouseDoubleClicked(this::compare);
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

	private void compare()
	{
		long size = commitView.selectedItems().count();
		if (size > 5)
		{
			Alerter.error("I won't open up that many compare windows!");
			return;
		}
		if (size > 1 && !Alerter.confirm("Do you want to open up %d compare windows?", size))
		{
			return;
		}
		commitView.selectedItems().forEach(i -> subversion.compare(i.getFileName()));
	}

	private void changeDoCommit()
	{
		boolean allMarked = commitView.selectedItems().allMatch(i -> i.isDoCommit());
		boolean doCommit = !allMarked;
		commitView.selectedItems()
				.filter(s -> s.getStatus().isCommitable())
				.forEach(i -> i.setDoCommit(doCommit));
	}

	private Collection<ModifiedItem> getModifiedItems()
	{
		Collection<ModifiedItem> modifiedItems = subversion.getModifiedItems();
		for (ModifiedItem modifiedItem : modifiedItems)
		{
			items.stream()
					.filter(modifiedItem::isSamePath)
					.findAny()
					.ifPresent(item ->
					{
						modifiedItem.setDoCommit(item.isDoCommit());
						modifiedItem.setReplication(item.getReplication());
					});
		}
		return modifiedItems;
	}

	private Map<KeyCode, Runnable> keyMappings()
	{
		Map<KeyCode, Runnable> keyMappings = new HashMap<>();
		keyMappings.put(KeyCode.F5, this::refresh);
		keyMappings.put(KeyCode.SPACE, this::changeDoCommit);
		keyMappings.put(KeyCode.ENTER, this::compare);
		return keyMappings;
	}
}
