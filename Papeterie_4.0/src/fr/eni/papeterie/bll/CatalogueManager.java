package fr.eni.papeterie.bll;

import java.util.List;

import fr.eni.papeterie.bo.Article;
import fr.eni.papeterie.bo.Ramette;
import fr.eni.papeterie.bo.Stylo;
import fr.eni.papeterie.dal.ArticleDAO;
import fr.eni.papeterie.dal.DALException;
import fr.eni.papeterie.dal.DAOFactory;

public class CatalogueManager {
	
	//Attributs
	private ArticleDAO daoArticle = DAOFactory.getArticleDAO();
	
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
	 * @throws BLLException 
	 * @throws DALException 
	 */
	public List<Article> getCatalogue() throws BLLException {
		
		try {
			return this.daoArticle.selectAll();
		} catch (DALException e) {
			throw new BLLException(" Erreur récupération catalogue ", e);
		}
	}

	public void addArticle(Article article) throws BLLException, DALException
	{
		this.daoArticle.insert(article);
	}
	public void updateArticle(Article a) throws BLLException {
		
		
	}
	public void removeArticle(int index) throws BLLException {
		
	}
	public void validerArticle(Article a) throws BLLException {
		
		boolean valid = true;
		StringBuilder sb = new StringBuilder();
		
		if(a == null) {
			valid = false;
			throw new BLLException("\n\tArticle null.\n");
		}
		if(a.getReference() == null || a.getReference().trim().isEmpty()) {
			sb.append(" La référence article est obligatoire");
			valid = false;
		}
		if(a.getMarque() == null || a.getMarque().trim().isEmpty()) {
			valid = false;
		}
		if(a.getDesignation() == null || a.getDesignation().trim().isEmpty()) {
			sb.append(" La désignation est obligatoire");
			valid = false;
		}
		if(a instanceof Ramette && ((Ramette) a).getGrammage() <= 0)
		{
			sb.append("Le grammage doir avoir une valeur strictement positive.\n");
			valid = false;
		}
		if(a instanceof Stylo && ((Stylo) a).getCouleur().trim().isEmpty())
		{
			sb.append("La couleur du stylo doit être renseignée.\n");
			valid = false;
		}
		if(!valid)
		{
			throw new BLLException(sb.toString());
		}
	}
	public Article getArticle(int index) throws BLLException {
		
		Article article = null;
		return article;
		
	}
	
	public void setDaoArticle(ArticleDAO daoArticle) {
		this.daoArticle = daoArticle;
	}
}
