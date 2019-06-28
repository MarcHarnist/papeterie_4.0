package fr.eni.papeterie.bll;

import java.util.List;

import fr.eni.papeterie.bo.Article;
import fr.eni.papeterie.bo.Ramette;
import fr.eni.papeterie.bo.Stylo;
import fr.eni.papeterie.dal.ArticleDAO;
import fr.eni.papeterie.dal.DALException;
import fr.eni.papeterie.dal.DAOFactory;

public class CatalogueManager {
	
	private ArticleDAO daoArticle;
	
	public CatalogueManager() {
		daoArticle = DAOFactory.getArticleDAO();
	}
	
	
	public List<Article> getCatalogue() throws BLLException{
		List<Article> listeArticle = null;
		try {
			listeArticle = daoArticle.selectAll();
		} catch (DALException e) {
			throw new BLLException("Erreur recuperation catalogue", e);
		}
		return listeArticle;
	}
	
	public void addArticle(Article a) throws BLLException{
		if(a.getIdArticle()!=null) {
			throw new BLLException("Article déjà existant");
		}		
		
		try {
			validerArticle(a);
			daoArticle.insert(a);
		} catch (DALException e) {
			throw new BLLException("Erreur de création d'aticle", e);
		}
		
	}
	
	public void updateArticle(Article a) throws BLLException{
		
		try {
			validerArticle(a);
			daoArticle.update(a);
		} catch (DALException e) {
			throw new BLLException("Erreur de l'update article " + a, e);
		}
		
	}
	

	public void removeArticle(int index) throws BLLException{
		
		try {
			daoArticle.delete(index);
		} catch (DALException e) {
			throw new BLLException("Erreur de la suppression de l'article id - " + index, e);
		}
		
	}
	
	public void validerArticle(Article a) throws BLLException{
		boolean valide = true;
		StringBuilder sb = new StringBuilder();
		
		if(a==null) {
			throw new BLLException("Article null");
		}
		
		if(a.getReference()==null || a.getReference().trim().isEmpty()) {
			sb.append("La reference article est obligatoire.\n");
			valide = false;
			
		}
		
		if(a.getMarque()==null || a.getMarque().trim().isEmpty()) {
			sb.append("La marque est obligatoire.\n");
			valide = false;
		}
		
		if(a.getDesignation()==null || a.getDesignation().trim().isEmpty()) {
			sb.append("La designation est obligatoire.\n");
			valide = false;
		}
		
		if(a.getQteStock()<0) {
			sb.append("La quantité en stock doit être positive.\n");
			valide = false;
		}
		
		if(a instanceof Ramette && ((Ramette) a).getGrammage()<=0) {
			sb.append("Le grammage doit avoir une valeur strictement positive.\n");
			valide = false;
		}
		
		if(a instanceof Stylo && ((Stylo) a).getCouleur().trim().isEmpty()) {
			sb.append("La couleur pour un stylo est obligatoire.\n");
			valide = false;
		}
		
		if(!valide) {
			throw new BLLException(sb.toString());
		}

	}
	
	
	
	public Article getArticle(int index) throws BLLException{
		Article art = null;
		
		try {
			art = daoArticle.selectById(index);
		} catch (DALException e) {
			throw new BLLException("Erreur de la lecture de l'article - id : " + index, e);
		}
		
		return art;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
