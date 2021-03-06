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
package com.antonjohansson.svncommit.application.commit;

import com.antonjohansson.svncommit.application.commit.context.CommitContextMenu;
import com.antonjohansson.svncommit.core.concurrent.DummyWorker;
import com.antonjohansson.svncommit.core.config.Configuration;
import com.antonjohansson.svncommit.core.domain.FileStatus;
import com.antonjohansson.svncommit.core.domain.ModifiedItem;
import com.antonjohansson.svncommit.core.utils.Subversion;
import com.antonjohansson.svncommit.core.view.ConsoleView;
import com.antonjohansson.svncommit.core.view.DialogFactory;
import com.antonjohansson.svncommit.core.view.LoadingView;
import com.antonjohansson.svncommit.core.view.View;

import static java.lang.Thread.sleep;
import static java.util.Arrays.asList;
import static javafx.scene.input.KeyCode.F5;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Unit tests of {@link CommitController}.
 *
 * @author Anton Johansson
 */
public class CommitControllerTest extends Assert
{
	private static final List<ModifiedItem> MODIFIED_ITEMS = asList(
			modifiedItem(1),
			modifiedItem(2),
			modifiedItem(3),
			modifiedItem(4),
			modifiedItem(5));

	@Mock private Configuration configuration;
	@Mock private CommitView commitView;
	@Mock private ConsoleView consoleView;
	@Mock private DialogFactory dialogFactory;
	@Mock private Subversion subversion;
	@Mock private CommitContextMenu commitContextMenu;
	private StubbedLoadingView loadingView;
	private EventHandler<KeyEvent> onKeyPressedHandler;
	private CommitController controller;

	@Before
	@SuppressWarnings("unchecked")
	public void setUp()
	{
		Answer<Void> onKeyPressedAnswer = invocation ->
		{
			onKeyPressedHandler = (EventHandler<KeyEvent>) invocation.getArguments()[0];
			return null;
		};

		initMocks(this);
		loadingView = new StubbedLoadingView();
		controller = new CommitController(configuration, commitView, loadingView, () -> consoleView, dialogFactory, subversion, new DummyWorker(), () -> commitContextMenu);

		when(configuration.isReplicationEnabled()).thenReturn(true);
		when(subversion.getModifiedItems()).thenReturn(MODIFIED_ITEMS);
		doAnswer(onKeyPressedAnswer).when(commitView).setOnKeyPressed(any());

		assertThat(loadingView.isLoading(), is(false));
		controller.initialize();
	}

	@Test
	public void test_that_initialize_triggers_a_refresh() throws Exception
	{
		sleep(100);
		assertThat(loadingView.isLoading(), is(false));
		verify(commitView).initialize(any(), any());
		verify(commitView).setCommitHandler(any());
		verify(commitView).setOnKeyPressed(any());
		verify(commitView).setOnMouseDoubleClicked(any());
		verify(commitView).setReplicationColumnsVisible(true);
		verify(commitView).setItems(MODIFIED_ITEMS);
		verifyNoMoreInteractions(commitView);
	}

	@Test
	public void test_that_F5_triggers_a_refresh() throws Exception
	{
		KeyEvent event = keyEvent(F5);
		onKeyPressedHandler.handle(event);
		onKeyPressedHandler.handle(event);

		verify(commitView, times(3)).setItems(MODIFIED_ITEMS);
	}

	private KeyEvent keyEvent(KeyCode keyCode)
	{
		return new KeyEvent(null, null, KeyEvent.KEY_PRESSED, "", "", keyCode, false, false, false, false);
	}

	private static ModifiedItem modifiedItem(int id)
	{
		int fileStatusIndex = id % FileStatus.values().length;
		FileStatus fileStatus = FileStatus.values()[fileStatusIndex];
		return new ModifiedItem("fileName" + id, fileStatus);
	}

	/**
	 * Stubbed extension of {@link LoadingView}.
	 *
	 * @author Anton Johansson
	 */
	public class StubbedLoadingView extends LoadingView
	{
		private View content;
		private boolean loading;

		@Override
		public void setContent(View content)
		{
			this.content = content;
		}

		public View getContent()
		{
			return content;
		}

		@Override
		public void setLoading(boolean loading)
		{
			this.loading = loading;
		}

		@Override
		public boolean isLoading()
		{
			return loading;
		}
	}
}
