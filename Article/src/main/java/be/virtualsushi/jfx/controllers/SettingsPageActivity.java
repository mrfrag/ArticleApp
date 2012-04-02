package be.virtualsushi.jfx.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import org.apache.commons.lang3.StringUtils;

import be.virtualsushi.jfx.AppActivitiesNames;
import be.virtualsushi.jfx.control.ValidationErrorPanel;

import com.zenjava.jfxflow.actvity.SimpleView;

/**
 * Database connection settings edit page.
 * 
 * @author Pavel Sitnikov (van.frag@gmail.com)
 * 
 */
public class SettingsPageActivity extends BasicAppActivity<SimpleView<VBox>> {

	public static final String INITIAL = "initial";

	@FXML
	protected VBox root;

	@FXML
	protected TextField urlField, dbnameField, usernameField;

	@FXML
	protected PasswordField passwordField;

	@FXML
	protected Button cancelButton, applyButton;

	private ValidationErrorPanel validationErrorPanel;

	@FXML
	public void handleApplyAction(ActionEvent event) {
		if (validate()) {
			if (root.getChildren().contains(validationErrorPanel)) {
				root.getChildren().remove(validationErrorPanel);
			}
			getDaoManager().updateConnection(urlField.getText(), dbnameField.getText(), usernameField.getText(), passwordField.getText());
			if (getDaoManager().isDaoCreated()) {
				goBack(event);
			} else {
				validationErrorPanel.setMessageText(getResources().getString("settingDbConnectionErrorMessage"));
				root.getChildren().add(1, validationErrorPanel);
			}
		} else {
			validationErrorPanel.setMessageText(getResources().getString("settingValidationErrorMessage"));
			root.getChildren().add(1, validationErrorPanel);
		}
	}

	@FXML
	public void goBack(ActionEvent event) {
		goTo(AppActivitiesNames.HOME);
	}

	@Override
	protected void initialize() {
		super.initialize();

		validationErrorPanel = new ValidationErrorPanel();
		validationErrorPanel.setTitleText(getResources().getString("settingValidationErrorTitle"));

		urlField.textProperty().addListener(getFieldValueChangeListener());
		dbnameField.textProperty().addListener(getFieldValueChangeListener());
		usernameField.textProperty().addListener(getFieldValueChangeListener());
		passwordField.textProperty().addListener(getFieldValueChangeListener());

	}

	private ChangeListener<String> getFieldValueChangeListener() {
		return new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				applyButton.setDisable(!validate());
			}
		};
	}

	@Override
	protected void activated() {
		super.activated();
		
		cancelButton.setVisible(!getParameter(INITIAL, Boolean.class, false));

		if (StringUtils.isNotBlank(getDaoManager().getUrl())) {
			urlField.setText(getDaoManager().getUrl());
			dbnameField.setText(getDaoManager().getDbname());
			usernameField.setText(getDaoManager().getUsername());
			passwordField.setText(getDaoManager().getPassword());
		}

		applyButton.setDisable(!validate());
	}

	private boolean validate() {
		return StringUtils.isNotBlank(urlField.getText()) && StringUtils.isNotBlank(dbnameField.getText()) && StringUtils.isNotBlank(usernameField.getText());
	}

}
