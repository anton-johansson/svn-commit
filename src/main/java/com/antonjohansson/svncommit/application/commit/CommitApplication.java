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
package com.antonjohansson.svncommit.application.commit;

import com.antonjohansson.svncommit.core.view.commit.CommitSceneFactory;
import com.antonjohansson.svncommit.core.view.utils.Alerter;

import java.io.File;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Defines the application that handles the commit dialog.
 *
 * @author Anton Johansson
 */
public class CommitApplication extends Application
{
	@Override
	public void start(Stage stage) throws Exception
	{
		Optional<File> directory = getDirectory();
		if (!directory.isPresent())
		{
			return;
		}

		Scene scene = CommitSceneFactory.create(directory.get());
		stage.setScene(scene);
		stage.setWidth(1200);
		stage.setHeight(400);
		stage.setTitle("svn-commit");
		stage.show();
	}

	private Optional<File> getDirectory()
	{
		List<String> parameters = getParameters().getRaw();
		if (parameters.isEmpty())
		{
			Alerter.error("The path parameter is missing.");
			return Optional.empty();
		}
		if (parameters.size() > 1)
		{
			Alerter.error("Too many arguments was specified.");
			return Optional.empty();
		}
		String directory = parameters.iterator().next();
		return Optional.of(new File(directory));
	}
}
