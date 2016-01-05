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
package com.antonjohansson.svncommit.core.view.commit;

import com.antonjohansson.svncommit.core.domain.SvnItem;
import com.antonjohansson.svncommit.core.view.utils.LoadingOverlay;

import static javafx.collections.FXCollections.observableArrayList;
import static javafx.scene.input.KeyCode.F5;

import java.io.File;

import javafx.collections.ObservableList;
import javafx.scene.Scene;

/**
 * Factory that it used to create {@link Scene} instances for the commit view.
 *
 * @author Anton Johansson
 */
public class CommitSceneFactory
{
	/**
	 * Creates a commit scene.
	 *
	 * @param directory The directory where the commitable files area contained.
	 * @return Returns a {@link Scene}.
	 */
	public static Scene create(File directory)
	{
		ObservableList<SvnItem> items = observableArrayList();
		CommitView commitView = new CommitView(directory, items);
		RefreshCommand refreshCommand = new RefreshCommand(directory, items);

		LoadingOverlay overlay = new LoadingOverlay();
		overlay.setContentNode(commitView);
		overlay.load(refreshCommand);

		Scene scene = new Scene(overlay);
		scene.setOnKeyPressed(e ->
		{
			if (F5.equals(e.getCode()))
			{
				overlay.load(refreshCommand);
			}
		});
		return scene;
	}
}
