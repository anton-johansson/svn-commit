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

import com.antonjohansson.svncommit.core.view.update.UpdateViewFactory;
import com.antonjohansson.svncommit.core.view.utils.Alerter;

import static javafx.scene.input.KeyCode.ESCAPE;

import java.io.File;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Defines an application that handles stand-alone SVN update commands.
 *
 * @author Anton Johansson
 */
public class UpdateApplication extends Application
{
	@Override
	public void start(Stage stage) throws Exception
	{
		Optional<File> path = getPath();
		if (!path.isPresent())
		{
			return;
		}

		Scene scene = new Scene(UpdateViewFactory.create(path.get()));
		stage.setScene(scene);
		stage.setWidth(800);
		stage.setHeight(300);
		stage.setTitle("svn-commit");
		stage.show();

		scene.setOnKeyPressed(e ->
		{
			if (ESCAPE.equals(e.getCode()))
			{
				stage.close();
			}
		});
	}

	private Optional<File> getPath()
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
		String path = parameters.iterator().next();
		return Optional.of(new File(path));
	}
}
