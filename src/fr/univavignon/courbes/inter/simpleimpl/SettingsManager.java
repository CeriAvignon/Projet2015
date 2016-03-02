package fr.univavignon.courbes.inter.simpleimpl;

/*
 * Courbes
 * Copyright 2015-16 L3 Info UAPV 2015-16
 * 
 * This file is part of Courbes.
 * 
 * Courbes is free software: you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation, 
 * either version 2 of the License, or (at your option) any later version.
 * 
 * Courbes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Courbes. If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

import fr.univavignon.courbes.common.Constants;

/**
 * Classe chargée de gérer les réglages du jeu enregistrés,
 * par exemple la dernière adresse IP utilisée pour se connecter
 * à un serveur.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class SettingsManager
{	/** Fichier contenant les profils */
	private static final String SETTINGS_FILE = "res/settings/settings.txt";

	////////////////////////////////////////////////////////////////
	////	DONNEES DE CONNEXION
	////////////////////////////////////////////////////////////////
	/** Insique quelle implémentation du moteur réseau on veut utiliser */
	private static NetEngineImpl netEngineImpl = NetEngineImpl.SOCKET;
	/** Dernière adresse IP utilisée pour se connecter à un serveur */
	private static String lastServerIp = Constants.DEFAULT_IP;
	/** Dernier port TCP utilisé pour se connecter à un serveur */
	private static int lastServerPort = Constants.DEFAULT_PORT;
	/** Dernier port TCP utilisé en tant que serveur */
	private static int lastPort = Constants.DEFAULT_PORT;
	
	/**
	 * Indique quelle implémentation du Moteur Réseau
	 * on veut utiliser.
	 *
	 * @author	L3 Info UAPV 2015-16
	 */
	public enum NetEngineImpl
	{	/** Utiliser l'implémentation de base avec sockets */
		SOCKET,
		/** Utiliser l'implementation avec Kryonet */
		KRYONET;
	}
	
	/**
	 * Renvoie l'implémentation du Moteur Réseau utilisée.
	 * 
	 * @return
	 * 		Dernière IP utilisée.
	 */
	public static NetEngineImpl getNetEngineImpl()
	{	return netEngineImpl;
	}

	/**
	 * Modifie l'implémentation du Moteur Réseau utilisée.
	 * 
	 * @param netEngineImpl
	 * 		Nouvelle implémentation du Moteur RéseaU.
	 */
	public static void setNetEngineImpl(NetEngineImpl netEngineImpl)
	{	SettingsManager.netEngineImpl = netEngineImpl;
		recordSettings();
	}

	/**
	 * Renvoie la dernière adresse IP utilisée pour se connecter
	 * à un serveur.
	 * 
	 * @return
	 * 		Dernière IP utilisée.
	 */
	public static String getLastServerIp()
	{	return lastServerIp;
	}

	/**
	 * Modifie la dernière adresse IP utilisée pour se connecter
	 * à un serveur.
	 * 
	 * @param lastServerIp
	 * 		Nouvelle dernière IP utilisée.
	 */
	public static void setLastServerIp(String lastServerIp)
	{	SettingsManager.lastServerIp = lastServerIp;
		recordSettings();
	}

	/**
	 * Renvoie le dernier port TCP utilisé pour se connecter
	 * à un serveur.
	 * 
	 * @return
	 * 		Dernier port utilisé.
	 */
	public static int getLastServerPort()
	{	return lastServerPort;
	}

	/**
	 * Modifie le dernier port TCP utilisé pour se connecter
	 * à un serveur.
	 * 
	 * @param lastServerPort
	 * 		Nouveau dernier port utilisé.
	 */
	public static void setLastServerPort(int lastServerPort)
	{	SettingsManager.lastServerPort = lastServerPort;
		recordSettings();
	}

	/**
	 * Renvoie le dernier port TCP utilisé en tant que serveur.
	 * 
	 * @return
	 * 		Dernier port utilisé.
	 */
	public static int getLastPort()
	{	return lastPort;
	}

	/**
	 * Modifie le dernier port TCP utilisé en tant que serveur.
	 * 
	 * @param lastPort
	 * 		Dernier port utilisé.
	 */
	public static void setLastPort(int lastPort)
	{	SettingsManager.lastPort = lastPort;
		recordSettings();
	}

	////////////////////////////////////////////////////////////////
	////	DONNEES GRAPHIQUES
	////////////////////////////////////////////////////////////////
	/** Largeur de l'aire de jeu */
	private static int boardWidth = 800;
	/** Hauteur de l'aire de jeu */
	private static int boardHeight = 800;
	
	/**
	 * Renvoie la largeur de l'aire de jeu, en pixels.
	 * 
	 * @return
	 * 		Largeur de l'aire de jeu.
	 */
	public static int getBoardWidth()
	{	return boardWidth;
	}
	
	/**
	 * Renvoie la hauteur de l'aire de jeu, en pixels.
	 * 
	 * @return
	 * 		Hauteur de l'aire de jeu.
	 */
	public static int getBoardHeight()
	{	return boardHeight;
	}
	
	/**
	 * Modifie la largeur de l'aire de jeu.
	 * 
	 * @param boardWidth
	 * 		Nouvelle largeur de l'aire de jeu, en pixels.
	 */
	public static void setBoardWidth(int boardWidth)
	{	SettingsManager.boardWidth = boardWidth;
		recordSettings();
	}
	
	/**
	 * Modifie la hauteur de l'aire de jeu.
	 * 
	 * @param boardHeight
	 * 		Nouvelle hauteur de l'aire de jeu, en pixels.
	 */
	public static void setBoardHeight(int boardHeight)
	{	SettingsManager.boardHeight = boardHeight;
		recordSettings();
	}
	
	/**
	 * Renvoie la largeur de la fenêtre, en pixels.
	 * 
	 * @return
	 * 		Largeur de la fenêtre.
	 */
	public static int getWindowWidth()
	{	int result = Constants.WINDOW_MARGIN + boardWidth + Constants.WINDOW_MARGIN + Constants.SCORE_WIDTH + Constants.WINDOW_MARGIN;
		return result;
	}
	
	/**
	 * Renvoie la hauteur de la fenêtre, en pixels.
	 * 
	 * @return
	 * 		Hauteur de la fenêtre.
	 */
	public static int getWindowHeight()
	{	int result = Constants.WINDOW_MARGIN + boardHeight + Constants.WINDOW_MARGIN;
		return result;
	}
	
	////////////////////////////////////////////////////////////////
	////	METHODES GENERALES
	////////////////////////////////////////////////////////////////
	/**
	 * Enregistre les réglages dans un fichier texte.
	 */
	public static void recordSettings()
	{	try
		{	FileOutputStream fos = new FileOutputStream(SETTINGS_FILE);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			PrintWriter writer = new PrintWriter(osw);
			
			// données de connexion
			writer.write(netEngineImpl.toString()+"\n");
			writer.write(lastServerIp+"\n");
			writer.write(lastServerPort+"\n");
			writer.write(lastPort+"\n");
			
			// données graphiques
			writer.write(boardWidth+"\n");
			writer.write(boardHeight+"\n");
			
			// TODO on peut rajouter d'autres réglages similaires ici
			// pensez à les rajouter aussi dans la méthode de chargement, 
			// en respectant le même ordre
			
			writer.flush();
			writer.close();
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
	}
	
	/**
	 * Charge les réglages à partir d'un fichier texte.
	 */
	public static void loadSettings()
	{	File file = new File(SETTINGS_FILE);
		if(!file.exists())
		{	System.err.println("Le fichier de configuration \""+file+"\" n'a pas pu être trouvé : on en crée un par défaut.");
			recordSettings();
		}
		else
		{	try
			{	// on ouvre le fichier en lecture
				FileInputStream fis = new FileInputStream(file);
				InputStreamReader isr = new InputStreamReader(fis);
				Scanner scanner = new Scanner(isr);
				
				// données de connexion
				String netEngineImplStr = scanner.nextLine();
				netEngineImpl = NetEngineImpl.valueOf(netEngineImplStr);
				lastServerIp = scanner.nextLine();
				String lastServerPortStr = scanner.nextLine();
				lastServerPort = Integer.parseInt(lastServerPortStr);
				String lastPortStr = scanner.nextLine();
				lastPort = Integer.parseInt(lastPortStr);
				
				// données graphiques
				String boardWidthStr = scanner.nextLine();
				boardWidth = Integer.parseInt(boardWidthStr);
				String boardHeightStr = scanner.nextLine();
				boardHeight = Integer.parseInt(boardHeightStr);

				// TODO complétez les éventuels réglages supplémentaires ici, 
				// de façon symétrique au chargement (même ordre)
				
				// on ferme le fichier
				scanner.close();  
			}
			catch(Exception e)
			{	System.err.println("Problème lors de la lecture du fichier de configuration \""+file+"\" : on en crée un nouveau.");
				recordSettings();
				//e.printStackTrace();
			}
		}
	}
}
