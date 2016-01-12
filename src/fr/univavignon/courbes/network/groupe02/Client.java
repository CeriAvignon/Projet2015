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
 * @author Marie et Mary    
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
	 * Fonction permettant de récupérer l'adresse ip du client
	 * 
	 * @param null
	 */
	@Override
	public String getIp() 
	{
		return this.ip;	//juste pour l'interface utilisateur, pour qu'ils puisssent afficher l'ip.
	}
	
	/**
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
	 * Fonction permettant de récupérer le port du client
	 * 
	 * @param null
	 */
	@Override
	public int getPort() 
	{
		return this.port; //juste pour l'interface utilisateur, pour qu'ils puisssent afficher le port.
	}
	
	/**
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
	 * Fonction permettant de lancer un client
	 * 
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
	 * Fonction permettant de fermer un client lorsqu'il se déconnecte du serveur
	 * 
	 * @param null
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

	
	@Override
	public void setErrorHandler(ErrorHandler errorHandler) 
	{
		
	}
		
	@Override
	public void setProfileHandler(ClientProfileHandler profileHandler) 
	{
		
	} 
	@Override
	public boolean addProfile(Profile profile)
	{
		
		return false;
	}

	@Override
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

	/*public String retrieveText() 
	{
		final BufferedReader rec; //Pour recevoir		
		try 
		{
			socketClient = new Socket(this.ip, this.port);
			//flux pour recevoir
			rec = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
			
			Thread receive = new Thread(new Runnable()
			{
				String message;
				
				@Override
				public void run()
				{
					try
					{
						message = rec.readLine();
						while(message != null)
						{
							System.out.println("Server :" + message);
							message = rec.readLine();
						}
						System.out.println("Server disconnected");
						socketClient.close();
					}
					catch(IOException e)
					{
						e.printStackTrace();
					}
				}
			});
			receive.start();
		}
		catch (UnknownHostException e) 
	     {
	         e.printStackTrace();
	     } 
	     catch (IOException e) 
	     {
	         e.printStackTrace();
	     }
		return null;
	}

	@Override
	public void sendText(final String message) 
	{
		final PrintWriter out; // Pour envoyer
		
		try 
		{
			socketClient = new Socket(this.ip, this.port);
			out = new PrintWriter(socketClient.getOutputStream()); //flux pour envoyer
			
			Thread send = new Thread(new Runnable()
			{				
				@Override
				public void run()
				{
					out.println(message);
					out.flush();
				}
			});
			send.start();
		} 
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}		
	}*/
	
	
}