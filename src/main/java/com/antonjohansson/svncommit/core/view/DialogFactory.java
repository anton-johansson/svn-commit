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

import static javafx.stage.Modality.APPLICATION_MODAL;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
		 */
		public void show()
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
		}
	}
}
