package be.virtualsushi.jfx.control;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Simple vertical container to display error and validation messages. Contains
 * one title {@link Label} and one or more message {@link Label}s.
 * Styles defined in style.css
 * 
 * @author Pavel Sitnikov (van.frag@gmail.com)
 * 
 */
public class ValidationErrorPanel extends VBox {

	private Label title;
	private List<Label> messages;

	public ValidationErrorPanel() {
		getStyleClass().add("validation-panel");
		title = new Label();
		title.getStyleClass().add("validation-title");
		getChildren().add(title);
		messages = new ArrayList<Label>();
	}

	public void clearMessages() {
		for (Label message : messages) {
			getChildren().remove(message);
		}
		messages.clear();
	}

	public void setTitleText(String text) {
		title.setText(text);
	}

	public void addMessage(String text) {
		Label message = new Label();
		message.getStyleClass().add("validation-message");
		message.setText(text);
		messages.add(message);
		getChildren().add(message);
	}

	public void setMessageText(String text) {
		clearMessages();
		addMessage(text);
	}
}
