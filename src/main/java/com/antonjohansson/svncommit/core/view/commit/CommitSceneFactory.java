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
