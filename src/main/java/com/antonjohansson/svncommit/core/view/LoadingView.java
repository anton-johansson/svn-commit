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

import static javafx.application.Platform.runLater;
import static javafx.scene.control.ProgressIndicator.INDETERMINATE_PROGRESS;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;

/**
 * An overlay view that shows a loading indicator.
 *
 * @author Anton Johansson
 */
public class LoadingView extends AbstractView
{
	@FXML private StackPane contentPane;
	@FXML private StackPane progressPane;
	@FXML private ProgressBar progressBar;

	/**
	 * Sets the content view.
	 *
	 * @param view The view to set as content.
	 */
	public void setContent(View view)
	{
		runLater(() -> contentPane.getChildren().setAll(view.getParent()));
	}

	/**
	 * Sets whether or not the view should be loading.
	 *
	 * @param loading Whether or not the view is currently loading.
	 */
	public void setLoading(boolean loading)
	{
		runLater(() ->
		{
			progressPane.setVisible(loading);
			progressBar.setProgress(0);
			progressBar.setProgress(INDETERMINATE_PROGRESS);
		});
	}

	/**
	 * Gets whether or not the view is loading.
	 *
	 * @return Returns {@code true} if the view is loading.
	 */
	public boolean isLoading()
	{
		return progressPane.isVisible();
	}
}
