package be.virtualsushi.jfx.dao;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import be.virtualsushi.jfx.model.Article;

/**
 * 
 * Simple test to check Hibernate's part.
 * 
 * @author Pavel Sitnikov (van.frag@gmail.com)
 * 
 */
public class ArticleDaoHibernateTest extends TestCase {

	private ArticleDao articleDao;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		articleDao = new ArticleDaoHiberante("localhost", "articleapp", "root", "root");
	}

	@Test
	public void test() {
		Article article = new Article();
		article.setCode("DFGFS-WWEF-FVDF");
		article.setName("Cool article");
		article.setDescription("This is cool article suitable for everything.");
		article.setPrice(999.99f);
		article.setReduction(1.99f);

		articleDao.save(article);

		List<Article> articles = articleDao.finaAll();
		assertTrue(articles.size() == 1);
		assertTrue(articles.get(0).getCode().equals(article.getCode()));
	}

}
