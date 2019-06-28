package fr.eni.papeterie.bll;

import java.util.List;

import fr.eni.papeterie.bo.Article;
import fr.eni.papeterie.bo.Catalogue;
import fr.eni.papeterie.dal.ArticleDAO;

public class CatalogueManager {
	
	
	//Attributs
	
	private ArticleDAO daoArticle;
	private List<Article> catalogue;
	
	/**
	 * Constructeur
	 * @param daoArticle : lien vers la dao (data access objet)
	 */
	public CatalogueManager(ArticleDAO daoArticle) {
		this.setDaoArticle(daoArticle);
	}
	public CatalogueManager() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the catalogue qui est une liste d'articles
	 */
	public List<Article> getCatalogue() {
		
		return this.catalogue;
	}

	public void addArticle(Article article) throws BLLException
	{
		this.catalogue.add(article);
	}
	public void updateArticle(Article a) throws BLLException {
		
	}
	public void removeArticle(int index) throws BLLException {
		
	}
	public void validerArticle(Article a) throws BLLException {
		
	}
	public Article getArticle(int index) throws BLLException {
		Article article = null;
		return article;
		
	}

	public ArticleDAO getDaoArticle() {
		return daoArticle;
	}
	
	public void setDaoArticle(ArticleDAO daoArticle) {
		this.daoArticle = daoArticle;
	}
}
