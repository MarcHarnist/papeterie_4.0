package fr.eni.papeterie.bll;

import java.util.ArrayList;
import java.util.List;

import fr.eni.papeterie.bo.Article;
import fr.eni.papeterie.bo.Ramette;
import fr.eni.papeterie.bo.Stylo;
import fr.eni.papeterie.dal.DALException;

public class AppliTestBLL {

	public static void main(String[] args) throws DALException, BLLException {
	
		// Instanciation du jeu d'essai
		List<Article> articles = new ArrayList<>();
		Stylo stylo = new Stylo("Bic", "BBOrange", "Bic bille Orange", 1.2f, 20, "bleu");
		articles.add(stylo);
		articles.add(new Ramette("Clairef", "CRA4S", "Ramette A4 Sup", 9f, 20, 80));
		articles.add(new Stylo("Stypen", "PlumeS", "Stylo Plume Stypen", 5.5f, 20, "jaune"));
		articles.add(new Stylo("Waterman", "WOBGreen", "Waterman Orion Bille vert", 4.2f, 35, "vert"));
		articles.add(new Ramette("ProDesign", "ForLaser", "A4 Special laser", 5.5f, 55, 100));

		CatalogueManager mger = new CatalogueManager();

		// Ajout d'un article au catalogue
		try {
			for (Article art : articles) {
				mger.addArticle(art);
			}
			System.out.println("\n/**\n* - Lister tous les articles du catalogue\n*/ ");
			System.out.println(mger.getCatalogue());

		} catch (BLLException e) {
			e.printStackTrace();
		}

		// Modification d'un article
		try {
			((Stylo) stylo).setCouleur("noir");
			((Stylo) stylo).setDesignation("Bic bille noir");
			((Stylo) stylo).setReference("BBNoir");
			mger.updateArticle(stylo);
			System.out.println("\n/**  Article après modification  : " + stylo.toString());
			System.out.println("*");
			System.out.println("*/");
		} catch (BLLException e) {
			e.printStackTrace();
		}

		// Suppression d'un article
		try {
			System.out.println("\n/**\n* - Suppression de du stylo " + stylo.getReference()+ "\n*/ ");
			mger.removeArticle(stylo.getIdArticle());
			System.out.println("\n/**\n* - Lister tous les articles du catalogue\n*/ ");
			System.out.println(mger.getCatalogue());
		} catch (BLLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Récupération d'un article en connaissant son identifiant
		System.out.println("\n/**\n* - Récupération d'un article en connaissant son identifiant\n*/ ");
		System.out.println(mger.getArticle(1));
		
		
	}

}
