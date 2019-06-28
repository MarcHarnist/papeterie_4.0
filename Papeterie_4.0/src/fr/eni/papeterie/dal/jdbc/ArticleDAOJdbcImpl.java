/**
 * 
 */
package fr.eni.papeterie.dal.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import fr.eni.papeterie.bo.Article;
import fr.eni.papeterie.bo.Ramette;
import fr.eni.papeterie.bo.Stylo;
import fr.eni.papeterie.dal.ArticleDAO;
import fr.eni.papeterie.dal.DALException;

/**
 * @author Eni Ecole
 * 
 */
public class ArticleDAOJdbcImpl implements ArticleDAO {
	
	private static final String RAMETTE = "RAMETTE";
	private static final String STYLO = "STYLO";
	
	private static final String SQL_SELECT_BY_ID = "select idArticle, reference, marque, designation, "
			+ "prixUnitaire, qteStock, grammage, couleur, type "
			+ "from Articles where idArticle = ?";
	
	private static final String SQL_INSERT = "insert into articles(reference, marque, "
			+ "designation, prixUnitaire, qteStock, grammage, couleur, type)"
			+ " values (?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String SQL_SELECT_ALL = "select idArticle, reference, marque, designation, "
			+ "prixUnitaire, qteStock, grammage, couleur, type from Articles order by reference, idArticle";
	
	private static final String SQL_DELETE = "DELETE from Articles where idArticle=?";
	
	private static final String SQL_UPDATE = "UPDATE articles set reference=?"
			+ ", marque=?, designation=?, prixUnitaire=?, qteStock=?, "
			+ "grammage=?, couleur=? where idArticle=?";
	
	private static final String SQL_BY_MARQUE = "select idArticle, reference, marque, designation, "
			+ "prixUnitaire, qteStock, grammage, couleur, type from Articles where marque = ?";
	
	private static final String SQL_BY_MOT_CLE = "select idArticle, reference, marque, designation, "
			+"prixUnitaire, qteStock, grammage, couleur, type from Articles where designation like ?";
	
	public void update(Article article) throws DALException {
		
		try(Connection uneConnection = JdbcTools.getConnection();
				PreparedStatement rqt = uneConnection.prepareStatement(SQL_UPDATE);){
			
			rqt.setString(1, article.getReference());
			rqt.setString(2, article.getMarque());
			rqt.setString(3, article.getDesignation());
			rqt.setFloat(4, article.getPrixUnitaire());
			rqt.setInt(5, article.getQteStock());
			if(article instanceof Ramette) {
				rqt.setInt(6, ((Ramette) article).getGrammage());
				rqt.setNull(7, Types.NVARCHAR);
			}
			if(article instanceof Stylo) {
				rqt.setNull(6, Types.INTEGER);
				rqt.setString(7, ((Stylo) article).getCouleur());
			}
			rqt.setInt(8, article.getIdArticle());
			
			int nbLignesImpactees = rqt.executeUpdate();
			
			

		}catch(SQLException e) {
			throw new DALException("Update article failed - article = " + article, e);
		}
		
	}
	
	
	public void delete(int idArticle) throws DALException {
		try(Connection uneConnection = JdbcTools.getConnection();
				PreparedStatement rqt = uneConnection.prepareStatement(SQL_DELETE);){
			
			rqt.setInt(1, idArticle);
			rqt.executeUpdate();
		}catch(SQLException e) {
			throw new DALException("Delete article failed - id=" + idArticle, e);
		}
	}
	
	
	
	public List<Article> selectAll() throws DALException{
		List<Article> listeArticle = new ArrayList<>();
		try(Connection uneConnection = JdbcTools.getConnection();
				Statement stmt = uneConnection.createStatement();){
			
			ResultSet rs = stmt.executeQuery(SQL_SELECT_ALL);
			
			Article article = null;
			
			while(rs.next()) {
				// création instance de Stylo
				if(STYLO.equals(rs.getString("type").trim())) {
					article = new Stylo(rs.getInt("idArticle"), rs.getString("marque"), 
							rs.getString("reference"), rs.getString("designation"), 
							rs.getFloat("prixUnitaire"), rs.getInt("qteStock"), 
							rs.getString("couleur"));
				}
				// création instance de Ramette
				if(RAMETTE.equals(rs.getString("type").trim())) {
					article = new Ramette(rs.getInt("idArticle"), rs.getString("marque"), 
							rs.getString("reference"), rs.getString("designation"), 
							rs.getFloat("prixUnitaire"), rs.getInt("qteStock"), 
							rs.getInt("grammage"));
				}
				listeArticle.add(article);
			}

		}catch (SQLException e) {
			throw new DALException("selectAll failed - ", e);
		}

		return listeArticle;
	}
	
	public void insert(Article art) throws DALException {
		Connection uneConnection = null;
		PreparedStatement pStmt = null;
		
		
		try {
			
			
			// ouverture de la connexion à la base de données
			uneConnection = JdbcTools.getConnection();
			
			// création de la requete
			pStmt = uneConnection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
			
			// valoriser les paramètres de la requete
			pStmt.setString(1, art.getReference());
			pStmt.setString(2, art.getMarque());
			pStmt.setString(3, art.getDesignation());
			pStmt.setFloat(4, art.getPrixUnitaire());
			pStmt.setInt(5, art.getQteStock());
			
			if(art instanceof Ramette) {
				pStmt.setInt(6, ((Ramette) art).getGrammage());
				pStmt.setNull(7, java.sql.Types.NVARCHAR);
				pStmt.setString(8, RAMETTE);
			}
			if(art instanceof Stylo) {
				pStmt.setNull(6, java.sql.Types.INTEGER);
				pStmt.setString(7, ((Stylo) art).getCouleur());
				pStmt.setString(8, STYLO);
			}
			
			// exécution de la requete
			pStmt.executeUpdate();
			
			ResultSet rsId = pStmt.getGeneratedKeys();
			
			if(rsId.next()) {
				art.setIdArticle(rsId.getInt(1));
			}
			
			
		} catch (SQLException e) {
			throw new DALException("Erreur a l'ajout d'un article : " + art, e);
		}finally {
			try {
				if(pStmt!=null) {
					pStmt.close();
				}
				
				JdbcTools.closeConnection(uneConnection);
			}catch (SQLException e) {
				throw new DALException("Erreur a l'ajout d'un article : " + art, e);
			}
		}
		
		
	}
	
	
	public Article selectById(int id) throws DALException {
		Article article = null;
		Connection uneConnection = null;
		PreparedStatement rqt = null;
		ResultSet rs = null;
		
		
		try {
			
			
			// ouverture d'une connection
			uneConnection = JdbcTools.getConnection();
			
			
			rqt = uneConnection.prepareStatement(SQL_SELECT_BY_ID);
			
			rqt.setInt(1, id);
			
			rs = rqt.executeQuery();
			
			if(rs.next()) {
				// création instance de Stylo
				if(STYLO.equals(rs.getString("type").trim())) {
					article = new Stylo(rs.getInt("idArticle"), rs.getString("marque"), 
							rs.getString("reference"), rs.getString("designation"), 
							rs.getFloat("prixUnitaire"), rs.getInt("qteStock"), 
							rs.getString("couleur"));
				}
				// création instance de Ramette
				if(RAMETTE.equals(rs.getString("type").trim())) {
					article = new Ramette(rs.getInt("idArticle"), rs.getString("marque"), 
							rs.getString("reference"), rs.getString("designation"), 
							rs.getFloat("prixUnitaire"), rs.getInt("qteStock"), 
							rs.getInt("grammage"));
				}
			}
			
			
		} catch (SQLException e) {
			throw new DALException("selectById failed - id = " + id, e);
		}finally {
			try {
				if(rqt != null) {
					rqt.close();
				}
				
				JdbcTools.closeConnection(uneConnection);
			}catch (SQLException e) {
				throw new DALException("selectById failed - id = " + id, e);
			}
			
		}

		return article;
		
	}


	


	

	@Override
	public List<Article> selectByMarque(String marque) throws DALException {
		List<Article> listeArticle = new ArrayList<>();
		
		try(Connection uneConnection = JdbcTools.getConnection();
				PreparedStatement rqt = uneConnection.prepareStatement(SQL_BY_MARQUE);){
			
			rqt.setString(1, marque);
			
			ResultSet rs = rqt.executeQuery();
			
			while(rs.next()) {
				Article article = null;
				if(STYLO.equals(rs.getString("type").trim())) {
					article = new Stylo(rs.getInt("idArticle"), rs.getString("marque"), 
							rs.getString("reference"), rs.getString("designation"), 
							rs.getFloat("prixUnitaire"), rs.getInt("qteStock"), 
							rs.getString("couleur"));
				}
				if(RAMETTE.equals(rs.getString("type").trim())) {
					article = new Ramette(rs.getInt("idArticle"), rs.getString("marque"), 
							rs.getString("reference"), rs.getString("designation"), 
							rs.getFloat("prixUnitaire"), rs.getInt("qteStock"), 
							rs.getInt("grammage"));
				}
				listeArticle.add(article);
			}
			
		}catch(SQLException e) {
			throw new DALException("selectByMarque failed - marque = " + marque, e);
		}

		
		return listeArticle;
	}


	@Override
	public List<Article> selectByMotCle(String motCle) throws DALException {
List<Article> listeArticle = new ArrayList<>();
		
		try(Connection uneConnection = JdbcTools.getConnection();
				PreparedStatement rqt = uneConnection.prepareStatement(SQL_BY_MOT_CLE);){
			
			rqt.setString(1, "%" + motCle + "%");
			
			ResultSet rs = rqt.executeQuery();
			
			while(rs.next()) {
				Article article = null;
				if(STYLO.equals(rs.getString("type").trim())) {
					article = new Stylo(rs.getInt("idArticle"), rs.getString("marque"), 
							rs.getString("reference"), rs.getString("designation"), 
							rs.getFloat("prixUnitaire"), rs.getInt("qteStock"), 
							rs.getString("couleur"));
				}
				if(RAMETTE.equals(rs.getString("type").trim())) {
					article = new Ramette(rs.getInt("idArticle"), rs.getString("marque"), 
							rs.getString("reference"), rs.getString("designation"), 
							rs.getFloat("prixUnitaire"), rs.getInt("qteStock"), 
							rs.getInt("grammage"));
				}
				listeArticle.add(article);
			}
			
		}catch(SQLException e) {
			throw new DALException("selectByMotCle failed - motCle = " + motCle, e);
		}

		
		return listeArticle;
	}


}
