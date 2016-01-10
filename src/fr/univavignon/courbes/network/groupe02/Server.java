package fr.univavignon.courbes.network.groupe02;

import java.util.List;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.network.ServerCommunication;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetSocketAddress;

/**
 * 
 * @author Marie et Mary    
 * 
 * Création de la classe server qui implémente les fonction de l'interface serverCommunication
 *
 */
public class Server implements ServerCommunication
{
	/**
	 * 	Variable de type integer contenant le port par défaut du serveur
	 */
	protected int port = 2345;
	
	/**
	 *  Variabke de type String contenant l'adresse ip du serveur
	 */
	protected String ip;

	/**
	 *  Variable de type boolean permettant d'attendre une connexion
	 *  tant qu'elle est à 1
	 */
	protected boolean isRunning = false;
	
	/**
	 *  Variable contenant le socket du serveur
	 */
	private ServerSocket server = null;
	
	@Override
	public void launchServer() 
	{
		try 
		{
			server = new ServerSocket(port);
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		
		Thread t = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				while(isRunning)
				{
					try
					{
						Socket socketClient = server.accept();
					}
					catch(IOException e)
					{
						e.printStackTrace();
						server = null;
					}
				}
			}
			
		});
		t.start();
			
	}

	@Override
	public void closeServer() 
	{
		if(server != null)
		{
			try
			{
				server.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
				server = null;
			}
		}
	}

	@Override
	public void sendPlayers(List<Profile> profiles) 
	{
		
	}

	@Override
	public void sendPointThreshold(int pointThreshold) 
	{
		
	}

	@Override
	public void sendBoard(Board board) 
	{
		
	}

	@Override
	public Map<Integer, Direction> retrieveCommands() 
	{
		return null;
	}

	@Override
	public void sendText(String message) 
	{
		Thread t = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				while(isRunning)
				{
					try
					{
						Socket SocketClient = server.accept();
						String message="";
						BufferedWriter write = new BufferedWriter(new OutputStreamWriter(SocketClient.getOutputStream()));
					}
					catch(IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		});
	}


	@Override
	public String getIp() 
	{
		return this.ip;
	}

	@Override
	public int getPort() 
	{
		return this.port;
	}

	@Override
	public void setPort(int port) 
	{
		this.port = port;
	}

	@Override
	public String[] retrieveText() {
		
		return null;
	}
	
}