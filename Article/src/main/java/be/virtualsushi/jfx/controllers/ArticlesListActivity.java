package be.virtualsushi.jfx.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import be.virtualsushi.jfx.AppActivitiesNames;
import be.virtualsushi.jfx.model.Article;

import com.zenjava.jfxflow.actvity.SimpleView;
import com.zenjava.jfxflow.transition.HorizontalPosition;

/**
 * Main page where list of all Articles will be displayed.
 * 
 * @author Pavel Sitnikov (van.frag@gmail.com)
 * 
 */
public class ArticlesListActivity extends BasicAppActivity<SimpleView<VBox>> {

	@FXML
	protected TableView<Article> articlesTable;

	@FXML
	protected TableColumn<Article, String> nameColumn, codeColumn, descriptionColumn;

	@FXML
	protected TableColumn<Article, Float> priceColumn, reductionColumn;

	@FXML
	protected Button editButton, deleteSelectedButton;

	@FXML
	protected Button helpButton;

	private HorizontalPosition currentSide;

	@Override
	protected void activated() {
		super.activated();

		deleteSelectedButton.setDisable(true);
		editButton.setDisable(true);

		updateTableData();

	}

	@Override
	protected void initialize() {
		articlesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		nameColumn.setCellValueFactory(new PropertyValueFactory<Article, String>("name"));
		codeColumn.setCellValueFactory(new PropertyValueFactory<Article, String>("code"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<Article, String>("description"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<Article, Float>("price"));
		reductionColumn.setCellValueFactory(new PropertyValueFactory<Article, Float>("reduction"));

		currentSide = HorizontalPosition.right;

	}

	/**
	 * Activates Help screen.
	 * 
	 * @param event
	 */
	public void goToHelp(ActionEvent event) {
		currentSide = HorizontalPosition.left;
		goTo(AppActivitiesNames.HELP);
	}

	/**
	 * Activates Article edit screen in "edit mode". Works if only one Article
	 * selected in the table.
	 * 
	 * @param event
	 */
	public void goToEdit(ActionEvent event) {
		currentSide = HorizontalPosition.right;
		goTo(AppActivitiesNames.EDIT, EditArticleActivity.ARTICLE_ID, articlesTable.getSelectionModel().getSelectedItem().getId());
	}

	/**
	 * Activates settings screen.
	 * 
	 * @param event
	 */
	public void goToSettings(ActionEvent event) {
		currentSide = HorizontalPosition.left;
		goTo(AppActivitiesNames.SETTINGS);
	}

	/**
	 * Listen for mouse pressed events on the table to manage delete and edit
	 * buttons state. Disable edit button if no or more than one items selected.
	 * Disable delete button if no items selected.
	 * 
	 * @param event
	 */
	public void handleArticlesTableMousePressed(MouseEvent event) {
		int selectionSize = articlesTable.getSelectionModel().getSelectedIndices().size();
		editButton.setDisable(selectionSize == 0 || selectionSize > 1);
		deleteSelectedButton.setDisable(selectionSize == 0);
	}

	/**
	 * Serves deletion of the selected articles.
	 * 
	 * @param event
	 */
	public void handleDeleteSelectedAction(ActionEvent event) {
		getDaoManager().getArticleDao().bunchDelete(articlesTable.getSelectionModel().getSelectedItems());
		updateTableData();
	}

	/**
	 * Activates Article edit screen in "add new mode"
	 * 
	 * @param event
	 */
	public void handleAddAction(ActionEvent event) {
		currentSide = HorizontalPosition.right;
		goTo(AppActivitiesNames.EDIT);
	}

	private void updateTableData() {
		if (articlesTable.getItems() != null) {
			articlesTable.getItems().clear();
			articlesTable.getItems().addAll(getDaoManager().getArticleDao().finaAll());
		} else {
			articlesTable.setItems(FXCollections.observableArrayList(getDaoManager().getArticleDao().finaAll()));
		}
	}

	@Override
	protected HorizontalPosition getEntrySide() {
		return currentSide;
	}

	@Override
	protected HorizontalPosition getExitSide() {
		return currentSide;
	}

}
