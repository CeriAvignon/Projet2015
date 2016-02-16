package fr.univavignon.courbes.network.groupe02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ClientProfileHandler;
import fr.univavignon.courbes.inter.ErrorHandler;
import fr.univavignon.courbes.network.ClientCommunication;
/**
 * 
 * @author Marie V.
 * 
 * Création de la classe client qui implémente les fonction de l'interface clientCommunication
 *
 */
public class Client implements ClientCommunication
{
	/**
	 * 	Variable de type integer contenant le port du client
	 */
	protected int port = 2345;
	
	/** 
	 * 	Variable de type String contenant  contenant l'adresse ip du client
	 */
	protected String ip;
	
	/** 
	 *  Variable contenant le socket du client
	 */
	private Socket socketClient = null;
	
	/**
	 * @author Marie V. et Mary  P  
	 * Fonction permettant de récupérer l'adresse ip du client
	 * 
	 * @param null
	 * @return ip
	 */
	@Override
	public String getIp() 
	{
		return this.ip;	//juste pour l'interface utilisateur, pour qu'ils puisssent afficher l'ip.
	}
	
	/**
	 * @author Marie V.
	 * Fonction permettant de modifier l'adresse ip du client
	 * 
	 * @param ip
	 */
	@Override
	public void setIp(String ip) 
	{
		this.ip = ip;
	}
	
	/**
	 * @author Marie V.
	 * Fonction permettant de récupérer le port du client
	 * 
	 * @param null
	 * @return port
	 */
	@Override
	public int getPort() 
	{
		return this.port; //juste pour l'interface utilisateur, pour qu'ils puisssent afficher le port.
	}
	
	/**
	 * @author Marie V.
	 * Fonction permettant de récupérer modifier le port du client
	 * 
	 * @param port
	 */
	@Override
	public void setPort(int port) 
	{
		this.port = port;
	}
	
	/**
	 * @author Marie V.
	 * Fonction permettant de lancer un client
	 * 
	 * @return null
	 * @param null
	 */
	@Override
	public void launchClient() 
	{
	     try 
	     {
	         socketClient = new Socket(this.ip, this.port);
	     } 
	     catch (UnknownHostException e) 
	     {
	         e.printStackTrace();
	     } 
	     catch (IOException e) 
	     {
	         e.printStackTrace();
	     }
		
	}
	
	/**
	 * @author Marie V.
	 * Fonction permettant de fermer un client lorsqu'il se déconnecte du serveur
	 * 
	 * @param null
	 * @return null
	 */
	@Override
	public void closeClient() 
	{
			if(socketClient != null)
			{
				try
				{
					socketClient.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
					socketClient = null;
				}
			}
	}

	/**
	 * @author Marie V.
	 * Permet d'indiquer au Moteur Réseau l'objet
     * à utiliser pour prévenir d'une erreur lors de l'exécution.
     *  
	 * @param errorHandler
	 * @return null
	 */
	public void setErrorHandler(ErrorHandler errorHandler) 
	{
		
	}
		
	/**
	 * @author Marie V.
	 * Permet d'indiquer au Moteur Réseau l'objet
     * à utiliser pour prévenir d'une modification des joueurs
     *  
	 * @param profileHandler
	 * @return null
	 */
	public void setProfileHandler(ClientProfileHandler profileHandler) 
	{
		
	} 
	

	public boolean addProfile(Profile profile)
	{
		
		return false;
	}

	public void removeProfile(Profile profile) 
	{
		
	}

	@Override
	public Integer retrievePointThreshold() 
	{
		
		return null;
	}

	@Override
	public Board retrieveBoard() 
	{
		
		return null;
	}

	@Override
	public void sendCommands(Map<Integer, Direction> commands) 
	{
		
	}

	public List<Profile> retrieveProfiles() {
		// TODO Auto-generated method stub
		return null;
	}

	public String retrieveText() {
		// TODO Auto-generated method stub
		return null;
	}

	public void sendText(String message) {
		// TODO Auto-generated method stub
	}
}
