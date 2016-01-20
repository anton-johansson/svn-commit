package com.antonjohansson.svncommit.core.view.common;

import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

/**
 * Represents a view containing piped console output.
 *
 * @author Anton Johansson
 */
public class ConsoleView extends StackPane
{
	private final ImageView icon;
	private final TextArea view;
	private final HBox pane;

	/**
	 * Constructs a new {@link ConsoleView}.
	 */
	public ConsoleView()
	{
		icon = new ImageView();

		view = new TextArea();
		view.setEditable(false);
		view.prefWidthProperty().bind(widthProperty());
		view.prefHeightProperty().bind(heightProperty());
		view.setFont(Font.font("Ubuntu Mono"));

		pane = new HBox();
		pane.getChildren().add(icon);
		pane.setVisible(false);
		pane.setMouseTransparent(true);

		AnchorPane anchor = new AnchorPane();
		anchor.getChildren().add(pane);
		anchor.setMouseTransparent(true);
		AnchorPane.setRightAnchor(pane, 28.);
		AnchorPane.setBottomAnchor(pane, 26.);

		getChildren().add(view);
		getChildren().add(anchor);
	}

	/**
	 * Sets the icon of the overlay.
	 *
	 * @param iconName The name of the icon to set.
	 */
	public void setIcon(String iconName)
	{
		Image image = new Image(iconName + ".png");
		icon.setImage(image);
		pane.setVisible(true);
	}

	/**
	 * Appends a log message.
	 *
	 * @param log The log to append.
	 */
	public void append(String log)
	{
		this.view.appendText(log);
	}
}