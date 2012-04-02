package be.virtualsushi.jfx.dao;

import java.util.List;

import be.virtualsushi.jfx.model.Article;

/**
 * Common interface for data access level objects.
 * 
 * @author Pavel Sitnikov (van.frag@gmail.com)
 * 
 */
public interface ArticleDao {

	void save(Article article);

	void delete(Article article);

	void deleteById(Long id);

	void bunchDelete(List<Article> articles);

	Article findById(Long id);

	List<Article> finaAll();

}
