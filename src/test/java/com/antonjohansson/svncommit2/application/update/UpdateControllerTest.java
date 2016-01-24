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
package com.antonjohansson.svncommit2.application.update;

import com.antonjohansson.svncommit2.core.utils.Subversion;
import com.antonjohansson.svncommit2.core.view.ConsoleView;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.function.Consumer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;

/**
 * Unit tests of {@link UpdateController}.
 *
 * @author Anton Johansson
 */
public class UpdateControllerTest extends Assert
{
	@Mock private ConsoleView consoleView;
	@Mock private Subversion subversion;
	private UpdateController controller;

	@Before
	public void setUp()
	{
		initMocks(this);
		controller = new UpdateController(consoleView, subversion);
		setUpMocks();
	}

	private void setUpMocks()
	{
		@SuppressWarnings("unchecked")
		Answer<Void> answer = invocation ->
		{
			Consumer<String> onData = (Consumer<String>) invocation.getArguments()[0];
			Consumer<Boolean> onComplete = (Consumer<Boolean>) invocation.getArguments()[1];

			onData.accept("some log line #1");
			onData.accept("some other line #2");
			onComplete.accept(true);

			return null;
		};

		doAnswer(answer).when(subversion).update(any(), any());
	}

	@Test
	public void test_that_initialize_calls_subversion_and_pipes_to_view()
	{
		controller.initialize();

		InOrder inOrder = inOrder(consoleView, subversion);
		inOrder.verify(subversion).update(any(), any());
		inOrder.verify(consoleView).append("some log line #1");
		inOrder.verify(consoleView).append("some other line #2");
		inOrder.verify(consoleView).showIcon("success");
		verifyNoMoreInteractions(consoleView, subversion);
	}
}
