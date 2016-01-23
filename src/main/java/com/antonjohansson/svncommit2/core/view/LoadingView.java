package com.antonjohansson.svncommit2.core.view;

import static javafx.application.Platform.runLater;

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
		runLater(() ->
		{
			contentPane.getChildren().clear();
			contentPane.getChildren().add(view.getParent());
		});
	}

	/**
	 * Sets whether or not the view should be loading.
	 *
	 * @param loading Whether or not the view is currently loading.
	 */
	public void setLoading(boolean loading)
	{
		progressPane.setVisible(loading);
		progressBar.setProgress(0);
	}
}
