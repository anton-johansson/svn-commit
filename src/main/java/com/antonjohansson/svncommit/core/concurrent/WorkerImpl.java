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
package com.antonjohansson.svncommit.core.concurrent;

import com.antonjohansson.svncommit.core.view.DialogFactory;

import static com.antonjohansson.svncommit.core.utils.ForcedExit.exit;

import java.util.concurrent.ExecutorService;

import com.google.inject.Inject;

import javafx.concurrent.Task;

/**
 * Default implementation of {@link Worker}.
 *
 * @author Anton Johansson
 */
class WorkerImpl implements Worker
{
	private final DialogFactory dialogFactory;
	private final ExecutorService service;

	/**
	 * Constructs a new {@link WorkerImpl}.
	 *
	 * @param dialogFactory The factory that creates dialogs.
	 * @param service The executor service.
	 */
	@Inject
	WorkerImpl(DialogFactory dialogFactory, ExecutorService service)
	{
		this.dialogFactory = dialogFactory;
		this.service = service;
	}

	/** {@inheritDoc} */
	@Override
	public void submit(Runnable task)
	{
		Task<Void> actualTask = new Task<Void>()
		{
			@Override
			protected Void call() throws Exception
			{
				task.run();
				return null;
			}
		};

		actualTask.setOnFailed(e ->
		{
			Throwable exception = actualTask.getException();
			dialogFactory.error(exception.getMessage());
			exit();
		});
		service.submit(actualTask);
	}

	/** {@inheritDoc} */
	@Override
	public void shutdown()
	{
		service.shutdown();
	}
}
