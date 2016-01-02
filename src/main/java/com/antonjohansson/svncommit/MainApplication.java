package com.antonjohansson.svncommit;

import com.antonjohansson.svncommit.domain.SvnItem;
import com.antonjohansson.svncommit.svn.SVN;
import com.antonjohansson.svncommit.ui.Alerter;
import com.antonjohansson.svncommit.ui.SvnItemTable;

import static javafx.application.Platform.runLater;
import static javafx.collections.FXCollections.observableArrayList;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Defines the main application.
 *
 * @author Anton Johansson
 */
public class MainApplication extends Application
{
	private SvnItemTable table;

	@Override
	public void start(Stage stage) throws Exception
	{
		Optional<String> path = getPath();
		if (!path.isPresent())
		{
			return;
		}
		File directory = new File(path.get());

		table = new SvnItemTable();
		table.setEnterHandler(new CompareEnterHandler(directory));
		table.setSpaceHandler(new SwitchDoCommitSpaceHandler());

		StackPane root = new StackPane();
		root.getChildren().add(table);
		stage.setScene(new Scene(root));
		stage.setWidth(1200);
		stage.setHeight(300);
		stage.setTitle("svn-commit");
		stage.show();

		runLater(() ->
		{
			Collection<SvnItem> modifiedItems = SVN.getModifiedItems(directory);
			ObservableList<SvnItem> items = observableArrayList(modifiedItems);
			table.setItems(items);
		});
	}

	private Optional<String> getPath()
	{
		if (getParameters().getRaw().isEmpty())
		{
			Alerter.error("The path parameter is missing.");
			return Optional.empty();
		}
		if (getParameters().getRaw().size() > 1)
		{
			Alerter.error("Too many arguments was specified.");
			return Optional.empty();
		}
		return Optional.of(getParameters().getRaw().iterator().next());
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

			items.forEach(i -> SVN.compare(directory, i.fileNameProperty().getValue()));
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
			boolean allMarked = items.stream().allMatch(i -> i.doCommitProperty().get());
			boolean doCommit = !allMarked;
			items.forEach(i -> i.doCommitProperty().setValue(doCommit));
		}
	}
}
