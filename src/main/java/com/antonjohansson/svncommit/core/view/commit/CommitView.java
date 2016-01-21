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
import com.antonjohansson.svncommit.core.view.utils.Alerter;

import static java.lang.System.lineSeparator;
import static javafx.geometry.Orientation.VERTICAL;

import java.io.File;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * View that shows commitable items in a folder.
 *
 * @author Anton Johansson
 */
class CommitView extends Pane
{
	private static final int COMMIT_PANE_WIDTH = 100;
	private static final int PADDING = 4;

	private final File path;
	private final Collection<SvnItem> items;
	private final ICommitHandler commitHandler;
	private final TextArea commitMessageTextArea;
	private final TextField activityIdTextField;

	CommitView(File path, ObservableList<SvnItem> items, ICommitHandler commitHandler)
	{
		this.path = path;
		this.items = items;
		this.commitHandler = commitHandler;

		SvnItemTable table = new SvnItemTable();
		table.setItems(items);
		table.setEnterHandler(new CompareEnterHandler(path));
		table.setSpaceHandler(new SwitchDoCommitSpaceHandler());
		table.prefWidthProperty().bind(widthProperty());
		table.prefHeightProperty().bind(heightProperty());

		commitMessageTextArea = new TextArea();
		commitMessageTextArea.prefWidthProperty().bind(widthProperty().subtract(COMMIT_PANE_WIDTH));

		activityIdTextField = new TextField();
		activityIdTextField.setPromptText("Activity ID");
		activityIdTextField.setPrefWidth(COMMIT_PANE_WIDTH - PADDING * 2);

		Button commitButton = new Button("Commit");
		commitButton.setPrefWidth(COMMIT_PANE_WIDTH - PADDING * 2);
		commitButton.setOnMouseClicked(e -> onCommit());

		VBox commitPane = new VBox(PADDING);
		commitPane.getChildren().add(activityIdTextField);
		commitPane.getChildren().add(commitButton);
		commitPane.setPadding(new Insets(PADDING));

		HBox hbox = new HBox();
		hbox.getChildren().add(commitMessageTextArea);
		hbox.getChildren().add(commitPane);

		SplitPane splitter = new SplitPane();
		splitter.setOrientation(VERTICAL);
		splitter.getItems().setAll(table, hbox);

		getChildren().add(splitter);
	}

	/**
	 * Triggers the commit handler.
	 */
	private void onCommit()
	{
		StringBuilder message = new StringBuilder();
		if (!activityIdTextField.getText().isEmpty())
		{
			message.append("Task: ")
				.append(activityIdTextField.getText().trim())
				.append(lineSeparator());
		}
		message.append(commitMessageTextArea.getText());

		Collection<String> filePaths = items.stream()
			.filter(s -> s.isDoCommit())
			.map(s -> s.getFileName())
			.collect(Collectors.toList());

		commitHandler.onCommit(path, message.toString(), filePaths);
	}

	/**
	 * Handles Enter clicks by bringing up compare windows for each item.
	 *
	 * @author Anton Johansson
	 */
	private static class CompareEnterHandler implements Consumer<Collection<SvnItem>>
	{
		private final File directory;

		CompareEnterHandler(File directory)
		{
			this.directory = directory;
		}

		@Override
		public void accept(Collection<SvnItem> items)
		{
			int size = items.size();
			if (size > 5)
			{
				Alerter.error("I won't open up that many compare windows!");
				return;
			}
			if (size > 1 && !Alerter.confirm("Do you want to open up %d compare windows?", size))
			{
				return;
			}

			items.forEach(i -> SVN.compare(directory, i.getFileName()));
		}
	}

	/**
	 * Handles Space clicks by switching the 'doCommit' property.
	 *
	 * @author Anton Johansson
	 */
	private static class SwitchDoCommitSpaceHandler implements Consumer<Collection<SvnItem>>
	{
		@Override
		public void accept(Collection<SvnItem> items)
		{
			boolean allMarked = items.stream().allMatch(i -> i.isDoCommit());
			boolean doCommit = !allMarked;
			items.stream()
				.filter(s -> s.getStatus().isDoCommitByDefault())
				.forEach(i -> i.setDoCommit(doCommit));
		}
	}
}
