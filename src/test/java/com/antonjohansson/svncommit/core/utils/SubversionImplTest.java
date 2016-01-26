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
package com.antonjohansson.svncommit.core.utils;

import com.antonjohansson.svncommit.core.domain.ModifiedItem;
import com.antonjohansson.svncommit.core.utils.Shell;
import com.antonjohansson.svncommit.core.utils.Subversion;
import com.antonjohansson.svncommit.core.utils.SubversionImpl;
import com.antonjohansson.svncommit.core.utils.ThrowingFunction;

import static com.antonjohansson.svncommit.core.domain.FileStatus.ADDED;
import static com.antonjohansson.svncommit.core.domain.FileStatus.MODIFIED;
import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;

/**
 * Unit tests of {@link SubversionImpl}.
 *
 * @author Anton Johansson
 */
public class SubversionImplTest extends Assert
{
	@Mock private Shell shell;
	private Subversion subversion;

	@Before
	public void setUp()
	{
		initMocks(this);
		subversion = new SubversionImpl(shell);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void test_getModifiedItems()
	{
		when(shell.execute(any(ThrowingFunction.class), eq("svn status"))).thenReturn(asList("A     test.txt", "M     modified-item.txt"));

		Collection<ModifiedItem> actual = subversion.getModifiedItems();
		Collection<ModifiedItem> expected = asList(
				new ModifiedItem("test.txt", ADDED),
				new ModifiedItem("modified-item.txt", MODIFIED));

		assertEquals(expected, actual);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void test_update()
	{
		Answer<Void> answer = invocation ->
		{
			Consumer<String> onData = (Consumer<String>) invocation.getArguments()[0];
			Consumer<String> onError = (Consumer<String>) invocation.getArguments()[1];
			Consumer<Boolean> onComplete = (Consumer<Boolean>) invocation.getArguments()[2];

			onData.accept("success-line - ");
			onError.accept("error-line");
			onComplete.accept(true);

			return null;
		};
		doAnswer(answer).when(shell).executeAndPipeOutput(any(), any(), any(), eq("svn update"));

		AtomicReference<String> output = new AtomicReference<String>("");
		AtomicBoolean success = new AtomicBoolean(false);

		subversion.update(o -> output.set(output.get().concat(o)), s -> success.set(s));

		assertEquals("success-line - error-line", output.get());
		assertTrue(success.get());
	}

	@Test
	public void test_compare()
	{
		subversion.compare("some-file.txt");

		verify(shell).execute("meld 'some-file.txt'");
		verifyNoMoreInteractions(shell);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void test_commit()
	{
		final String commitCommand = "svn commit --file '/file-with-message' 'test1.txt' 'test2.txt'";

		Answer<Void> answer = invocation ->
		{
			Consumer<String> onData = (Consumer<String>) invocation.getArguments()[0];
			Consumer<String> onError = (Consumer<String>) invocation.getArguments()[1];
			Consumer<Boolean> onComplete = (Consumer<Boolean>) invocation.getArguments()[2];

			onData.accept("success-line");
			onError.accept("error-line");
			onComplete.accept(true);

			return null;
		};
		doAnswer(answer).when(shell).executeAndPipeOutput(any(), any(), any(), eq(commitCommand));

		when(shell.getTemporaryFile(asList("some-commit-message"), "commit-message")).thenReturn(new File("/file-with-message"));

		AtomicReference<String> output = new AtomicReference<String>("");
		AtomicReference<String> error = new AtomicReference<String>("");
		AtomicBoolean success = new AtomicBoolean(false);

		subversion.commit(
				"some-commit-message",
				asList("test1.txt", "test2.txt"),
				o -> output.set(output.get().concat(o)),
				e -> error.set(error.get().concat(e)),
				s -> success.set(s));

		verify(shell).executeAndPipeOutput(any(), any(), any(), eq(commitCommand));
		assertEquals("success-line", output.get());
		assertEquals("error-line", error.get());
		assertTrue(success.get());
	}
}
