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
import static javafx.stage.Modality.APPLICATION_MODAL;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Factory for building new modal dialogs.
 *
 * @author Anton Johansson
 */
public class DialogFactory
{
	/**
	 * Constructs a new {@link DialogFactory}.
	 */
	DialogFactory()
	{
	}

	/**
	 * Shows an error message, blocking the current thread.
	 *
	 * @param errorMessage The error message to show.
	 */
	public void error(String errorMessage)
	{
		Alert alert = new Alert(ERROR);
		alert.setTitle("svn-commit");
		alert.setContentText(errorMessage);
		alert.getDialogPane().setPrefSize(600, 200);
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
	public boolean confirm(String question, Object... args)
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
		dialog.getButtonTypes()
				.stream()
				.map(dialog::lookupButton)
				.forEach(b -> b.addEventHandler(KEY_PRESSED, fireOnEnter));

		ButtonData answer = alert.showAndWait().get().getButtonData();
		return YES.getButtonData().equals(answer);
	}

	/**
	 * Creates a new dialog.
	 *
	 * @return Returns the builder that configures the dialog.
	 */
	public DialogBuilder create()
	{
		return new DialogBuilder();
	}

	/**
	 * Builder for creating new modal dialogs.
	 *
	 * @author Anton Johansson
	 */
	public static class DialogBuilder
	{
		private Scene scene;
		private EventHandler<WindowEvent> handler;
		private double width;
		private double height;

		/**
		 * Constructs a new {@link DialogBuilder}.
		 */
		private DialogBuilder()
		{
		}

		/**
		 * Sets the view of the dialog.
		 *
		 * @param view The view to set.
		 * @return Returns the builder.
		 */
		public DialogBuilder view(View view)
		{
			scene = new Scene(view.getParent());
			return this;
		}

		/**
		 * Sets the on-close event handler for the dialog.
		 *
		 * @param handler The handler to set.
		 * @return Returns the builder.
		 */
		public DialogBuilder onClose(EventHandler<WindowEvent> handler)
		{
			this.handler = handler;
			return this;
		}

		/**
		 * Sets the width of the dialog.
		 *
		 * @param width The width of the dialog.
		 * @return Returns the builder.
		 */
		public DialogBuilder width(double width)
		{
			this.width = width;
			return this;
		}

		/**
		 * Sets the height of the dialog.
		 *
		 * @param height The height of the dialog.
		 * @return Returns the builder.
		 */
		public DialogBuilder height(double height)
		{
			this.height = height;
			return this;
		}

		/**
		 * Shows the created dialog.
		 *
		 * @return Returns the {@link Stage} instance.
		 */
		public Stage show()
		{
			Stage stage = new Stage();
			stage.initModality(APPLICATION_MODAL);
			stage.setScene(scene);
			stage.setWidth(width);
			stage.setHeight(height);
			stage.setTitle("svn-commit");
			stage.getIcons().add(new Image("svn.png"));
			stage.setOnCloseRequest(handler);
			stage.show();
			return stage;
		}
	}
}
