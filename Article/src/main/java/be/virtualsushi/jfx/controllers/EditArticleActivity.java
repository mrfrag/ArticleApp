package be.virtualsushi.jfx.controllers;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import be.virtualsushi.jfx.control.NumberTextField;
import be.virtualsushi.jfx.control.ValidationErrorPanel;
import be.virtualsushi.jfx.model.Article;

import com.zenjava.jfxflow.actvity.SimpleView;
import com.zenjava.jfxflow.transition.HorizontalPosition;

/**
 * Article edit activity. If article id is passed in the parameters map then
 * works in "edit mode" and loads article from the database, if no parameters
 * passed then works in "add mode" (with delete button hidden).
 * 
 * @author Pavel Sitnikov (van.frag@gmail.com)
 * 
 */
public class EditArticleActivity extends BasicAppActivity<SimpleView<VBox>> {

	public static final String ARTICLE_ID = "articleId";

	@FXML
	protected TextField codeField, nameField;

	@FXML
	protected NumberTextField priceField, reductionField;

	@FXML
	protected Button saveButton, editButton, deleteButton;

	@FXML
	protected TextArea descriptionField;

	@FXML
	protected VBox root;

	@FXML
	protected Label titleLabel;

	private Article currentArticle;

	private ValidationErrorPanel validationErrorPanel;

	@Override
	protected void initialize() {
		super.initialize();

		validationErrorPanel = new ValidationErrorPanel();
		validationErrorPanel.setTitleText(getResources().getString("editArticleValidationErrorTitle"));
	}

	@Override
	protected void activated() {
		super.activated();

		clearValidationErrorMessage();

		Long articleId = getParameter(ARTICLE_ID, Long.class, null);
		if (articleId != null) {
			// edit mode
			currentArticle = getDaoManager().getArticleDao().findById(articleId);
			codeField.setText(currentArticle.getCode());
			nameField.setText(currentArticle.getName());
			descriptionField.setText(currentArticle.getDescription());
			priceField.setText(String.valueOf(currentArticle.getPrice()));
			reductionField.setText(String.valueOf(currentArticle.getReduction()));
			titleLabel.setText(getResources().getString("editArticle"));
		} else {
			// add new mode
			currentArticle = new Article();
			titleLabel.setText(getResources().getString("addArticle"));
			clearFields();
		}
		deleteButton.setVisible(articleId != null);

	}

	@FXML
	public void handleSaveAction(ActionEvent event) {

		clearValidationErrorMessage();

		currentArticle.setCode(codeField.getText());
		currentArticle.setName(nameField.getText());
		currentArticle.setDescription(descriptionField.getText());
		try {
			currentArticle.setPrice(Float.parseFloat(priceField.getText()));
		} catch (Exception e) {
		}
		try {
			currentArticle.setReduction(Float.parseFloat(reductionField.getText()));
		} catch (Exception e) {
		}
		List<String> validationResults = Article.validate(currentArticle);
		// Not empty validation messages list means that validation is not OK.
		// Display validationErrorPanel.
		if (validationResults.isEmpty()) {
			getDaoManager().getArticleDao().save(currentArticle);
			goToHome(event);
		} else {
			validationErrorPanel.clearMessages();
			for (String validationMessage : validationResults) {
				validationErrorPanel.addMessage(validationMessage);
			}
			root.getChildren().add(1, validationErrorPanel);
		}
	}

	@FXML
	public void goToHome(ActionEvent event) {
		getNavigationManager().goBack();
	}

	@FXML
	public void handleDeleteAction(ActionEvent event) {
		getDaoManager().getArticleDao().delete(currentArticle);
		goToHome(event);
	}

	@Override
	protected HorizontalPosition getEntrySide() {
		return HorizontalPosition.left;
	}

	@Override
	protected HorizontalPosition getExitSide() {
		return HorizontalPosition.left;
	}

	private void clearFields() {
		codeField.clear();
		nameField.clear();
		descriptionField.clear();
		priceField.clear();
		reductionField.clear();
	}

	private void clearValidationErrorMessage() {
		if (root.getChildren().contains(validationErrorPanel)) {
			root.getChildren().remove(validationErrorPanel);
		}
	}

}
