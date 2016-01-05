/**
 * Copyright 2015 Anton Johansson
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
package com.antonjohansson.svncommit.core.view.utils;

import static javafx.application.Platform.runLater;
import static javafx.geometry.Pos.CENTER;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Overlay pane that can show a loading indicator on top of a content.
 *
 * @author Anton Johansson
 */
public class LoadingOverlay extends StackPane
{
	private final Node loadingNode;

	public LoadingOverlay()
	{
		loadingNode = new OverlayPane();
		loadingNode.setVisible(false);
	}

	/**
	 * Sets the content node.
	 *
	 * @param contentNode The node to use as content.
	 */
	public void setContentNode(Node contentNode)
	{
		getChildren().clear();
		getChildren().add(contentNode);
		getChildren().add(loadingNode);
	}

	/**
	 * Loads data.
	 *
	 * @param loader The loader to use.
	 */
	public synchronized <R, L extends Consumer<R> & Supplier<R>> void load(L loader)
	{
		load(loader, loader);
	}

	/**
	 * Loads data.
	 *
	 * @param backgroundAction The action that loads the data in a worker thread.
	 * @param uiAction The action to consume the loaded data and put in the GUI.
	 */
	public synchronized <R> void load(Supplier<R> backgroundAction, Consumer<R> uiAction)
	{
		if (loadingNode.isVisible())
		{
			// If we're already loading, do nothing...
			return;
		}

		loadingNode.setVisible(true);
		new Thread(() ->
		{
			R result = backgroundAction.get();
			runLater(() ->
			{
				uiAction.accept(result);
				loadingNode.setVisible(false);
			});
		}).start();
	}

	/**
	 * Pane with translucent background and a centered progress indicator.
	 *
	 * @author Anton Johansson
	 */
	private static class OverlayPane extends Pane
	{
		private static final Color BACKGROUND_COLOR = Color.color(0.8, 0.8, 0.8, 0.9);
		private static final Background BACKGROUND = new Background(new BackgroundFill(BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY));

		private OverlayPane()
		{
			ProgressIndicator content = new ProgressIndicator();

			HBox hbox = new HBox();
			hbox.setAlignment(CENTER);
			hbox.minWidthProperty().bind(widthProperty());
			hbox.getChildren().add(content);

			VBox vbox = new VBox();
			vbox.setAlignment(CENTER);
			vbox.minHeightProperty().bind(heightProperty());
			vbox.getChildren().add(hbox);

			setBackground(BACKGROUND);
			getChildren().add(vbox);
		}
	}
}
