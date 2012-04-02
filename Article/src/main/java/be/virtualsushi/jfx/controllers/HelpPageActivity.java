package be.virtualsushi.jfx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import com.zenjava.jfxflow.actvity.SimpleView;

/**
 * Simple page which displays some help text.
 * 
 * @author Pavel Stinikov (van.frag@gmail.com)
 * 
 */
public class HelpPageActivity extends BasicAppActivity<SimpleView<AnchorPane>> {

	@FXML
	public void handleBackAction(ActionEvent event) {
		getNavigationManager().goBack();
	}

}
