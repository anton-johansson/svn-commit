package com.antonjohansson.svncommit;
import static javafx.application.Platform.runLater;
import static javafx.collections.FXCollections.observableArrayList;

import java.util.Collection;
import java.util.Optional;

import com.antonjohansson.svncommit.domain.SvnItem;
import com.antonjohansson.svncommit.svn.SVN;
import com.antonjohansson.svncommit.ui.Alerter;
import com.antonjohansson.svncommit.ui.SvnItemTable;

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

		table = new SvnItemTable();

		StackPane root = new StackPane();
		root.getChildren().add(table);
		stage.setScene(new Scene(root));
		stage.setWidth(600);
		stage.setHeight(300);
		stage.setTitle("SVN Commit");
		stage.show();

		runLater(() ->
		{
			Collection<SvnItem> modifiedItems = SVN.getModifiedItems(path.get());
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
}
