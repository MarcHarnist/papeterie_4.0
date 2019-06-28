package fr.eni.papeterie.dal;

import java.io.IOException;
import java.util.Properties;

public class Settings {

	
	// Les propriétes étant commune à toute l'application, nous pouvons
	//rendre cet attribut static.	
	private static Properties properties;

	static {
		// instantiation de l'attribut properties
		properties = new Properties();
		try {
			// chargement du fichier settings.properties
			properties.load(Settings.class.getResourceAsStream("settings.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getProperty(String key) {
		String parametre = properties.getProperty(key);
		return parametre;
	}
	
}
