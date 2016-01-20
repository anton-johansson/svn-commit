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
package com.antonjohansson.svncommit.core.view.update;

import com.antonjohansson.svncommit.core.svn.SVN;
import com.antonjohansson.svncommit.core.view.common.ConsoleView;

import java.io.File;

import javafx.scene.layout.Pane;

/**
 * Factory that it used to create {@link UpdateView} instances.
 *
 * @author Anton Johansson
 */
public class UpdateViewFactory
{
	/**
	 * Creates an update view.
	 *
	 * @param directory The directory where the update command should be executed.
	 * @return Returns a {@link Pane}.
	 */
	public static Pane create(File directory)
	{
		ConsoleView view = new ConsoleView();
		SVN.update(directory, view::append, () -> view.setIcon("success"));
		return view;
	}
}
