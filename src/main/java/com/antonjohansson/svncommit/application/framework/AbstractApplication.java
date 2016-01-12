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
package com.antonjohansson.svncommit.application.framework;

import com.antonjohansson.svncommit.core.utils.SvnCommitException;
import com.antonjohansson.svncommit.core.view.utils.Alerter;

import java.io.File;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public abstract class AbstractApplication extends Application
{
	/** {@inheritDoc} */
	@Override
	public final void start(Stage stage) throws Exception
	{
		try
		{
			Scene scene = getScene();

			stage.setScene(scene);
			stage.setWidth(getStartUpWidth());
			stage.setHeight(getStartUpHeight());
			stage.setTitle("svn-commit");
			stage.getIcons().add(new Image("svn.png"));
			stage.show();

			configure(stage, scene);
		}
		catch (SvnCommitException e)
		{
			Alerter.error(e.getMessage());
		}
	}

	/**
	 * Configures extra things on the {@link Stage stage}.
	 *
	 * @param stage The stage to configure.
	 * @param scene The scene of the stage.
	 */
	protected void configure(Stage stage, Scene scene)
	{
	}

	/**
	 * Gets the start-up width for this application.
	 *
	 * @return Returns the start-up width.
	 */
	protected double getStartUpWidth()
	{
		return 1200.0;
	}

	/**
	 * Gets the start-up height for this application.
	 *
	 * @return Returns the start-up height.
	 */
	private double getStartUpHeight()
	{
		return 300.0;
	}

	/**
	 * Gets the scene to use for this application.
	 *
	 * @return Returns the scene.
	 * @throws SvnCommitException Thrown when an error occurred while getting the scene.
	 */
	protected abstract Scene getScene() throws SvnCommitException;

	/**
	 * Utility method that can be used to get directory parameter.
	 * <p>
	 * Only usable for applications with only one single parameter, which is the directory.
	 *
	 * @return Returns the {@link File directory}.
	 * @throws SvnCommitException Thrown if the parameter was missing, or if there was too many parameters.
	 */
	protected File getDirectory() throws SvnCommitException
	{
		List<String> parameters = getParameters().getRaw();
		if (parameters.isEmpty())
		{
			throw new SvnCommitException("The directory parameter is missing.");
		}
		if (parameters.size() > 1)
		{
			throw new SvnCommitException("Too many arguments was specified.");
		}
		String path = parameters.iterator().next();
		return new File(path);
	}
}
