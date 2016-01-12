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
package com.antonjohansson.svncommit.core.view.update;

import com.antonjohansson.svncommit.core.svn.SVN;

import java.io.File;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Factory that it used to create {@link UpdateView} instances.
 *
 * @author Anton Johansson
 */
public class UpdateViewFactory
{
	/**
	 * Creates an update view.
	 *
	 * @param directory The directory where the update command should be executed.
	 * @return Returns a {@link Pane}.
	 */
	public static Pane create(File path)
	{
		UpdateView view = new UpdateView();

		HBox pane = new HBox();
		pane.getChildren().add(new ImageView("success.png"));
		pane.setVisible(false);

		AnchorPane anchor = new AnchorPane();
		anchor.getChildren().add(pane);
		AnchorPane.setRightAnchor(pane, 12.);
		AnchorPane.setBottomAnchor(pane, 10.);

		StackPane stackPane = new StackPane();
		stackPane.getChildren().add(view);
		stackPane.getChildren().add(anchor);

		SVN.update(path, log -> view.append(log), () -> pane.setVisible(true));
		return stackPane;
	}
}
