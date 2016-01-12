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
package com.antonjohansson.svncommit.application.update;

import com.antonjohansson.svncommit.application.framework.AbstractApplication;
import com.antonjohansson.svncommit.core.utils.SvnCommitException;
import com.antonjohansson.svncommit.core.view.update.UpdateViewFactory;

import static javafx.scene.input.KeyCode.ESCAPE;

import java.io.File;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Defines an application that handles stand-alone SVN update commands.
 *
 * @author Anton Johansson
 */
public class UpdateApplication extends AbstractApplication
{
	/** {@inheritDoc} */
	@Override
	protected Scene getScene() throws SvnCommitException
	{
		File directory = getDirectory();
		return new Scene(UpdateViewFactory.create(directory));
	}

	/** {@inheritDoc} */
	@Override
	protected void configure(Stage stage, Scene scene)
	{
		scene.setOnKeyPressed(e ->
		{
			if (ESCAPE.equals(e.getCode()))
			{
				stage.close();
			}
		});
	}
}
