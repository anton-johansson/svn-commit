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
package com.antonjohansson.svncommit.core.view;

import static javafx.scene.control.ProgressIndicator.INDETERMINATE_PROGRESS;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

/**
 * Unit tests of {@link LoadingView}.
 *
 * @author Anton Johansson
 */
public class LoadingViewTest extends AbstractViewTest<LoadingView>
{
	private StackPane contentPane;
	private StackPane progressPane;
	private ProgressBar progressBar;

	@Override
	protected void initNodes()
	{
		contentPane = getNode("contentPane");
		progressPane = getNode("progressPane");
		progressBar = getNode("progressBar");
	}

	@Test
	public void test_initial_state_is_not_loading()
	{
		assertThat(progressPane.isVisible(), is(false));
		assertThat(contentPane.getChildren().isEmpty(), is(true));
		assertThat(view.isLoading(), is(false));
	}

	@Test
	public void test_setting_content()
	{
		View content = new DummyView();

		assertThat(contentPane.getChildren().isEmpty(), is(true));
		assertThat(view.isLoading(), is(false));
		view.setContent(content);
		sleep(100);
		assertThat(contentPane.getChildren().size(), is(1));
		assertThat(contentPane.getChildren().get(0), is(content.getParent()));
		assertThat(view.isLoading(), is(false));
	}

	@Test
	public void test_loading()
	{
		view.setContent(new DummyView());

		assertThat(view.isLoading(), is(false));
		view.setLoading(true);
		sleep(100);
		assertThat(view.isLoading(), is(true));
		assertThat(progressPane.isVisible(), is(true));
		assertThat(progressBar.getProgress(), is(INDETERMINATE_PROGRESS));
		view.setLoading(false);
		sleep(100);
		assertThat(view.isLoading(), is(false));
		assertThat(progressPane.isVisible(), is(false));
	}

	/**
	 * Dummy implementation of {@link View}, used by tests.
	 *
	 * @author Anton Johansson
	 */
	private class DummyView extends AbstractView
	{
		private DummyView()
		{
			setParent(new TextField());
		}
	}
}
