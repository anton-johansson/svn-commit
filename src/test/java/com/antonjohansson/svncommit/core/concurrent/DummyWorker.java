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

/**
 * Dummy implementation of {@link Worker} that instantly runs tasks on the same
 * thread. Used in unit tests.
 *
 * @author Anton Johansson
 */
public class DummyWorker implements Worker
{
	/** {@inheritDoc} */
	@Override
	public void submit(Runnable task)
	{
		task.run();
	}

	/** {@inheritDoc} */
	@Override
	public void shutdown()
	{
	}
}
