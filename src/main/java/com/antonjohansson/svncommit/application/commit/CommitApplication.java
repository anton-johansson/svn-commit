package com.antonjohansson.svncommit.application.commit;

import com.antonjohansson.svncommit.core.domain.SvnItem;
import com.antonjohansson.svncommit.core.svn.SVN;
import com.antonjohansson.svncommit.core.ui.Alerter;
import com.antonjohansson.svncommit.core.ui.SvnItemTable;

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
 * Defines the application that handles the commit dialog.
 *
 * @author Anton Johansson
 */
public class CommitApplication extends Application
{
	private final ObservableList<SvnItem> items;

	public CommitApplication()
	{
		items = observableArrayList();
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		Optional<File> directory = getDirectory();
		if (!directory.isPresent())
		{
			return;
		}

		Scene scene = getScene(directory.get());
		stage.setScene(scene);
		stage.setWidth(1200);
		stage.setHeight(300);
		stage.setTitle("svn-commit");
		stage.show();

		runLater(() ->
		{
			Collection<SvnItem> modifiedItems = SVN.getModifiedItems(directory.get());
			items.setAll(modifiedItems);
		});
	}

	private Scene getScene(File directory)
	{
		SvnItemTable table = new SvnItemTable();
		table.setItems(items);
		table.setEnterHandler(new CompareEnterHandler(directory));
		table.setSpaceHandler(new SwitchDoCommitSpaceHandler());

		StackPane root = new StackPane();
		root.getChildren().add(table);

		Scene scene = new Scene(root);
		scene.setOnKeyPressed(e ->
		{

		});
		return scene;
	}

	private Optional<File> getDirectory()
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
		String directory = getParameters().getRaw().iterator().next();
		return Optional.of(new File(directory));
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
