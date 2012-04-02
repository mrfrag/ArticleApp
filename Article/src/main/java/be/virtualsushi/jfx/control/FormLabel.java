package be.virtualsushi.jfx.control;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;

/**
 * Extends basic {@link Label}, just automatically adds ":" to it's textProperty.
 * 
 * @author Pavel Sitnikov (van.frga@gmail.com)
 * 
 */
public class FormLabel extends Label {

	private boolean stopPropagation;

	public FormLabel() {
		textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (stopPropagation) {
					stopPropagation = false;
					return;
				}
				stopPropagation = true;
				setText(newValue + ":");
			}
		});
	}

}
