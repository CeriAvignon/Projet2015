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
import fr.univavignon.courbes.network.ClientCommunication;
/**
 * 
 * @author Marie et Mary    
 * 
 * On fait la classe Client
 *
 */
public class Client implements ClientCommunication
{

	protected Socket socketClient = null;
	protected int port = 2345;
	protected String ip;
	
	@Override
	public String getIp() 
	{
		return this.ip;	//juste pour l'interface utilisateur, pour qu'ils puisssent afficher l'ip.
	}

	@Override
	public void setIp(String ip) 
	{
		this.ip = ip;
	}

	@Override
	public int getPort() 
	{
		return this.port; //juste pour l'interface utilisateur, pour qu'ils puisssent afficher le port.
	}

	@Override
	public void setPort(int port) 
	{
		this.port = port;
	}

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
	public List<Profile> retrieveProfiles() 
	{
		
		return null;
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

	@Override
	public String retrieveText() 
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
	}
	
}