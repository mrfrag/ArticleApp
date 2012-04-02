package be.virtualsushi.jfx;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import be.virtualsushi.jfx.controllers.ArticlesListActivity;
import be.virtualsushi.jfx.controllers.EditArticleActivity;
import be.virtualsushi.jfx.controllers.HelpPageActivity;
import be.virtualsushi.jfx.controllers.SettingsPageActivity;
import be.virtualsushi.jfx.dao.DaoManager;

import com.zenjava.jfxflow.actvity.FxmlLoader;
import com.zenjava.jfxflow.control.Browser;
import com.zenjava.jfxflow.navigation.DefaultNavigationManager;
import com.zenjava.jfxflow.navigation.Place;
import com.zenjava.jfxflow.navigation.RegexPlaceResolver;

/**
 * Application's entry point. Contains just a simple initialization things and
 * main scene construction.
 * 
 * @author Pavel Stinikov (van.frag@gmail.com)
 * 
 */
public class ArticleApplication extends Application {

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		FxmlLoader loader = new FxmlLoader();

		DefaultNavigationManager navigationManager = new DefaultNavigationManager();

		ResourceBundle resources = ResourceBundle.getBundle("messages");

		DaoManager daoManager = new DaoManager();

		// DI mb?
		EditArticleActivity editArticleActivity = loader.load("/be/virtualsushi/jfx/views/EditArticleForm.fxml", resources);
		editArticleActivity.setNavigationManager(navigationManager);
		editArticleActivity.setDaoManager(daoManager);
		editArticleActivity.setResources(resources);

		ArticlesListActivity articlesListActivity = loader.load("/be/virtualsushi/jfx/views/ArticlesList.fxml", resources);
		articlesListActivity.setNavigationManager(navigationManager);
		articlesListActivity.setDaoManager(daoManager);

		HelpPageActivity helpPageActivity = loader.load("/be/virtualsushi/jfx/views/HelpPage.fxml", resources);
		helpPageActivity.setNavigationManager(navigationManager);
		helpPageActivity.setDaoManager(daoManager);

		SettingsPageActivity settingsPageActivity = loader.load("/be/virtualsushi/jfx/views/SettingsPage.fxml", resources);
		settingsPageActivity.setNavigationManager(navigationManager);
		settingsPageActivity.setResources(resources);
		settingsPageActivity.setDaoManager(daoManager);

		Browser browser = new Browser(navigationManager);
		browser.setHeader(null);
		browser.getPlaceResolvers().add(new RegexPlaceResolver(AppActivitiesNames.HOME.name(), articlesListActivity));
		browser.getPlaceResolvers().add(new RegexPlaceResolver(AppActivitiesNames.EDIT.name(), editArticleActivity));
		browser.getPlaceResolvers().add(new RegexPlaceResolver(AppActivitiesNames.HELP.name(), helpPageActivity));
		browser.getPlaceResolvers().add(new RegexPlaceResolver(AppActivitiesNames.SETTINGS.name(), settingsPageActivity));

		if (daoManager.isDaoCreated()) {
			navigationManager.goTo(new Place(AppActivitiesNames.HOME.name()));
		} else {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(SettingsPageActivity.INITIAL, true);
			navigationManager.goTo(new Place(AppActivitiesNames.SETTINGS.name(), parameters));
		}

		BorderPane root = new BorderPane();
		root.setCenter(browser);
		Scene scene = new Scene(root, 800, 600);
		stage.setScene(scene);
		scene.getStylesheets().add("style.css");
		stage.show();

	}

}
