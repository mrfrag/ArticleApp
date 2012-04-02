package be.virtualsushi.jfx.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

/**
 * Manages DAO instances and updates them if connection parameters have been
 * changed. Also checks than connection to the database can be created for the
 * provided connection parameters.
 * 
 * @author Pavel Stinikov (van.frag@gmail.com)
 * 
 */
public class DaoManager {

	public static final String PROPERTIES_FILE_PATH = System.getProperty("user.home") + File.separator + "app.properties";
	public static final String URL_PROPERTY = "url";
	public static final String DBNAME_PROPERTY = "dbname";
	public static final String USERNAME_PROPERTY = "username";
	public static final String PASSWORD_PROPERTY = "password";

	private String url;
	private String dbname;
	private String username;
	private String password;

	private ArticleDao articleDao;

	/**
	 * true if dao have been created successfully. If during application's
	 * initialization daoCreated is false then settings page will be shown prior
	 * to main page.
	 */
	private boolean daoCreated;

	public DaoManager() {
		if (checkDbConnectionPropertiesExists()) {
			loadProperties();
		} else {
			daoCreated = false;
		}
	}

	private boolean checkDbConnectionPropertiesExists() {
		return new File(PROPERTIES_FILE_PATH).exists();
	}

	public boolean isDaoCreated() {
		return daoCreated;
	}

	public void setDaoCreated(boolean daoCreated) {
		this.daoCreated = daoCreated;
	}

	public ArticleDao getArticleDao() {
		return articleDao;
	}

	public void updateConnection(String url, String dbname, String username, String password) {
		this.setUrl(url);
		this.setDbname(dbname);
		this.setUsername(username);
		this.setPassword(password);
		updateDao();
		if (daoCreated) {
			storeProperties();
		}
	}

	private void updateDao() {
		try {
			articleDao = new ArticleDaoHiberante(getUrl(), getDbname(), getUsername(), getPassword());
			articleDao.finaAll();
			daoCreated = true;
		} catch (Exception e) {
			daoCreated = false;
		}
	}

	private void storeProperties() {
		Properties properties = new Properties();

		properties.put(URL_PROPERTY, getUrl());
		properties.put(DBNAME_PROPERTY, getDbname());
		properties.put(USERNAME_PROPERTY, getUsername());
		properties.put(PASSWORD_PROPERTY, getPassword());
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(PROPERTIES_FILE_PATH);
			properties.store(out, null);
		} catch (Exception e) {
			throw new RuntimeException("Can't store application properties.", e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	private void loadProperties() {
		Properties properties = new Properties();
		FileInputStream in = null;
		try {
			in = new FileInputStream(PROPERTIES_FILE_PATH);
			properties.load(in);
		} catch (Exception e) {
			throw new RuntimeException("Can't read application properties.", e);
		} finally {
			IOUtils.closeQuietly(in);
		}
		updateConnection(properties.getProperty(URL_PROPERTY), properties.getProperty(DBNAME_PROPERTY), properties.getProperty(USERNAME_PROPERTY),
				properties.getProperty(PASSWORD_PROPERTY));
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
