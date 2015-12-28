package com.antonjohansson.svncommit.ui;

import static javafx.scene.control.Alert.AlertType.ERROR;

import javafx.scene.control.Alert;

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
	 * @param contentText The content to put into the dialog.
	 */
	public static void error(String contentText)
	{
		Alert alert = new Alert(ERROR);
		alert.setTitle("Error in svn-commit");
		alert.setHeaderText("An error occurred when running svn-commit");
		alert.setContentText(contentText);
		alert.showAndWait();
	}
}
