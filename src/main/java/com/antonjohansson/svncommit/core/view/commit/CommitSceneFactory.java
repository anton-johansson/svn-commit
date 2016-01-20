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
package com.antonjohansson.svncommit.core.view.commit;

import com.antonjohansson.svncommit.core.domain.SvnItem;
import com.antonjohansson.svncommit.core.svn.SVN;
import com.antonjohansson.svncommit.core.utils.ICommitHandler;
import com.antonjohansson.svncommit.core.view.common.ConsoleView;
import com.antonjohansson.svncommit.core.view.utils.LoadingOverlay;

import static javafx.collections.FXCollections.observableArrayList;
import static javafx.scene.input.KeyCode.F5;
import static javafx.stage.Modality.APPLICATION_MODAL;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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

		RefreshCommand refreshCommand = new RefreshCommand(directory, items);
		LoadingOverlay overlay = new LoadingOverlay();

		DefaultCommitHandler handler = new DefaultCommitHandler(overlay, refreshCommand);
		CommitView commitView = new CommitView(directory, items, handler);

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

	/**
	 * Default implementation of {@link ICommitHandler}.
	 *
	 * @author Anton Johansson
	 */
	private static class DefaultCommitHandler implements ICommitHandler
	{
		private final LoadingOverlay overlay;
		private final RefreshCommand refreshCommand;

		DefaultCommitHandler(LoadingOverlay overlay, RefreshCommand refreshCommand)
		{
			this.overlay = overlay;
			this.refreshCommand = refreshCommand;
		}

		/** {@inheritDoc} */
		@Override
		public void onCommit(File directory, String message, Collection<String> filePaths)
		{
			Stage stage = new Stage();
			stage.initModality(APPLICATION_MODAL);

			ConsoleView view = new ConsoleView();
			stage.setScene(new Scene(view));
			stage.setWidth(600);
			stage.setHeight(250);
			stage.setTitle("svn-commit");
			stage.getIcons().add(new Image("svn.png"));
			stage.setOnCloseRequest(e -> overlay.load(refreshCommand));
			stage.show();

			AtomicBoolean isFailed = new AtomicBoolean(false);

			Consumer<String> onError = s ->
			{
				view.append(s);
				isFailed.set(true);
			};

			Runnable onComplete = () ->
			{
				view.setIcon(isFailed.get() ? "failed" : "success");
			};

			SVN.commit(directory, message, filePaths, view::append, onError, onComplete);
		}
	}
}
