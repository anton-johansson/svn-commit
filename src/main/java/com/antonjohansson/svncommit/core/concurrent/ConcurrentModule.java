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

import static java.util.concurrent.Executors.newSingleThreadExecutor;

import java.util.concurrent.ExecutorService;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * Contains IOC bindings for the concurrent parts of the application.
 *
 * @author Anton Johansson
 */
public class ConcurrentModule extends AbstractModule
{
	/** {@inheritDoc} */
	@Override
	protected void configure()
	{
		bind(ExecutorService.class).toInstance(newSingleThreadExecutor());
		bind(Worker.class).to(WorkerImpl.class).in(Singleton.class);
	}
}
