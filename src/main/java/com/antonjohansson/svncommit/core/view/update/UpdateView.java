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
