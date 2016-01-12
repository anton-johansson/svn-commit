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

import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

/**
 * View that shows SVN update progress.
 *
 * @author Anton Johansson
 */
public class UpdateView extends Pane
{
	private final TextArea log;

	UpdateView()
	{
		log = new TextArea();
		log.setEditable(false);
		log.prefWidthProperty().bind(widthProperty());
		log.prefHeightProperty().bind(heightProperty());
		log.setFont(Font.font("Ubuntu Mono"));

		getChildren().add(log);
	}

	/**
	 * Appends data in the log text area.
	 *
	 * @param data The data to append.
	 */
	void append(String data)
	{
		String text = log.getText().concat(data);
		log.setText(text);
	}
}
