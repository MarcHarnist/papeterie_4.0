package fr.eni.papeterie.dal;

import java.util.List;

import fr.eni.papeterie.bo.Article;

public interface ArticleDAO {
	
	Article selectById(int id) throws DALException;
	List<Article> selectAll() throws DALException;
	void update(Article data) throws DALException;
	void insert(Article data) throws DALException;
	void delete(int id) throws DALException;
	List<Article> selectByMarque(String marque) throws DALException;
	List<Article> selectByMotCle(String motCle) throws DALException;

}
