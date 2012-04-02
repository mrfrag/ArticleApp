package be.virtualsushi.jfx.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;

import be.virtualsushi.jfx.model.Article;

/**
 * Implementation of data access layer using Hibernate ORM. There is no complex
 * transactions and sessions management just simple open/commit/close on each DB
 * hit. Using hibernate.cfg.xml and annotations way (see {@link Article}).
 * 
 * @author Pavel Sitnikov (van.frag@gmail.com)
 * 
 */
public class ArticleDaoHiberante implements ArticleDao {

	private SessionFactory sessionFactory;

	private Session currentSession;

	public ArticleDaoHiberante(String url, String dbname, String username, String password) throws Exception {
		Configuration configuration = new Configuration().configure("/hibernate.cfg.xml");
		configuration.setProperty("hibernate.connection.url", String.format("jdbc:mysql://%s:3306/%s", url, dbname));
		configuration.setProperty("hibernate.connection.username", username);
		configuration.setProperty("hibernate.connection.password", password);
		sessionFactory = configuration.buildSessionFactory(new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry());
	}

	public void save(Article article) {
		openSession().saveOrUpdate(article);
		closeSession(true);
	}

	public void delete(Article article) {
		openSession().delete(article);
		closeSession(true);
	}

	public void deleteById(Long id) {
		delete(findById(id));
	}

	public Article findById(Long id) {
		Article result = (Article) openSession().get(Article.class, id);
		closeSession(true);
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Article> finaAll() {
		List<Article> result = (List<Article>) openSession().createCriteria(Article.class).list();
		closeSession(true);
		return result;
	}

	private Session openSession() {
		if (currentSession == null) {
			currentSession = sessionFactory.openSession();
			currentSession.beginTransaction();
		}
		return currentSession;
	}

	private void closeSession(boolean successful) {
		if (currentSession != null) {
			if (successful) {
				currentSession.getTransaction().commit();
			} else {
				currentSession.getTransaction().rollback();
			}
			currentSession.close();
			currentSession = null;
		}
	}

	public void bunchDelete(List<Article> articles) {
		for (Article article : articles) {
			delete(article);
		}
	}

}
