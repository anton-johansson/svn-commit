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

import static java.lang.String.format;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.WARNING;
import static javafx.scene.control.ButtonType.NO;
import static javafx.scene.control.ButtonType.YES;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyEvent.KEY_PRESSED;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.DialogPane;
import javafx.scene.input.KeyEvent;

/**
 * Utility class for alerting error messages.
 *
 * @author Anton Johansson
 */
public final class Alerter
{
	private Alerter() {}

	/**
	 * Alerts given error message.
	 *
	 * @param errorMessage The content to put into the dialog.
	 */
	public static void error(String errorMessage)
	{
		Alert alert = new Alert(ERROR);
		alert.setTitle("svn-commit");
		alert.setContentText(errorMessage);
		alert.showAndWait();
	}

	/**
	 * Asks a yes-or-no-question and returns {@code true} if the user
	 * answered 'Yes'.
	 *
	 * @param question The question to ask.
	 * @param args Arguments to the question.
	 * @return Returns whether or not the user answered 'Yes'.
	 */
	public static boolean confirm(String question, Object... args)
	{
		Alert alert = new Alert(WARNING);
		alert.setTitle("svn-commit");
		alert.setContentText(format(question, args));
		alert.getButtonTypes().setAll(NO, YES);

		// Workaround to make [Enter] fire the focused item
		EventHandler<KeyEvent> fireOnEnter = event ->
		{
			if (ENTER.equals(event.getCode()))
			{
				((Button) event.getTarget()).fire();
			}
		};
		DialogPane dialog = alert.getDialogPane();
		dialog.getButtonTypes().stream()
			.map(dialog::lookupButton)
			.forEach(b -> b.addEventHandler(KEY_PRESSED, fireOnEnter));

		ButtonData answer = alert.showAndWait().get().getButtonData();
		return YES.getButtonData().equals(answer);
	}
}
