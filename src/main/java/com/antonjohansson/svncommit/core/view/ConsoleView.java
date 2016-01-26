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
package com.antonjohansson.svncommit.core.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * View that contains console output and can show an overlay icon
 * when it has finished printing to the console.
 *
 * @author Anton Johansson
 */
public class ConsoleView extends AbstractView
{
	@FXML private TextArea console;
	@FXML private ImageView icon;

	/**
	 * Appends text to the console.
	 *
	 * @param text The text to append.
	 */
	public void append(String text)
	{
		console.appendText(text);
	}

	/**
	 * Shows the icon overlay with the given icon.
	 *
	 * @param iconName The name of the icon to show.
	 */
	public void showIcon(String iconName)
	{
		Image image = new Image(iconName + ".png");

		icon.setVisible(true);
		icon.setImage(image);
	}
}
