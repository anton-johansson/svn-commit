package com.antonjohansson.svncommit.ui;

import static javafx.scene.control.Alert.AlertType.ERROR;

import javafx.scene.control.Alert;

public final class Alerter
{
	private Alerter() {}

	public static void error(String contentText)
	{
		Alert alert = new Alert(ERROR);
		alert.setTitle("Error in svn-commit");
		alert.setHeaderText("An error occurred when running svn-commit");
		alert.setContentText(contentText);
		alert.showAndWait();
	}
}
