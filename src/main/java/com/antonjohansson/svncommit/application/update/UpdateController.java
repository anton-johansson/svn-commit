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
package com.antonjohansson.svncommit.application.update;

import com.antonjohansson.svncommit.core.concurrent.Worker;
import com.antonjohansson.svncommit.core.controller.AbstractController;
import com.antonjohansson.svncommit.core.controller.Controller;
import com.antonjohansson.svncommit.core.utils.Subversion;
import com.antonjohansson.svncommit.core.view.ConsoleView;

import com.google.inject.Inject;

/**
 * Update-implementation of {@link Controller}.
 *
 * @author Anton Johansson
 */
class UpdateController extends AbstractController<ConsoleView>
{
	private final Subversion subversion;
	private final Worker worker;

	@Inject
	UpdateController(ConsoleView view, Subversion subversion, Worker worker)
	{
		super(view);
		this.subversion = subversion;
		this.worker = worker;
	}

	/** {@inheritDoc} */
	@Override
	public void initialize()
	{
		worker.submit(() -> subversion.update(view::append, view::showCompletionIcon));
	}
}
