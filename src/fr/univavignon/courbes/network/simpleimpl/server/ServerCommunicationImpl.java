package fr.univavignon.courbes.network.simpleimpl.server;

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

import fr.univavignon.courbes.network.ServerCommunication;
import fr.univavignon.courbes.network.simpleimpl.NetworkConstants;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.inter.ErrorHandler;
import fr.univavignon.courbes.inter.ServerConfigHandler;

/**
 * Classe fille de ClientCommunication, elle en implémente toutes les méthodes.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ServerCommunicationImpl implements ServerCommunication, Runnable
{	
	////////////////////////////////////////////////////////////////
	////	ADRESSE IP
	////////////////////////////////////////////////////////////////
	/** Variable qui contient l'adresse ip de ce serveur */
	private String ip;

	@Override
	public String getIp()
	{	return ip;
	}

	////////////////////////////////////////////////////////////////
	////	PORT
	////////////////////////////////////////////////////////////////
	/** Variable qui contient le port de ce serveur */
	private int port = 2345;

	@Override
	public int getPort()
	{	return port;
	}

	@Override
	public void setPort(int port)
	{	this.port = port;
	}
	
	////////////////////////////////////////////////////////////////
	////	HANDLER CONFIGURATION
	////////////////////////////////////////////////////////////////
	/** Handler normal */
	public ServerConfigHandler configHandler;

	@Override
	public void setConfigHandler(ServerConfigHandler configHandler)
	{	this.configHandler = configHandler;
	}
	/** Handler d'erreurs */
	public ErrorHandler errorHandler;

	@Override
	public void setErrorHandler(ErrorHandler errorHandler)
	{	this.errorHandler = errorHandler;
	}
	
	////////////////////////////////////////////////////////////////
	////	CONNEXION
	////////////////////////////////////////////////////////////////
	/** Processus principal du serveur (seulement pour la configuration= */
	private Thread mainThread;
	/** Socket de connexion */
	private ServerSocket serverSocket;
	/** Socket du client connecté au serveur */
	private Socket[] sockets = null;
	/** Flux d'entrée */
	public ObjectInputStream[] oiss;
	/** Flux de sortie */
	public ObjectOutputStream[] ooss;
	
	@Override
	public void launchServer()
	{	mainThread = new Thread(this, "Courbes-ServerThread-Main");
		mainThread.start();
	}
	
	@Override
	public void run()
	{	try
		{	do
			{	try
				{	serverSocket = new ServerSocket(port);
					Socket socket = serverSocket.accept();
					processClient(socket);
				}
				catch(UnknownHostException e)
				{	errorHandler.displayError("Impossible de se connecter au client.");
					e.printStackTrace();
				}
				catch(IOException e)
				{	// si c'est une SocketException, on la re-lève
					if(!(e instanceof SocketException))
					{	SocketException se = (SocketException)e;
						throw se;
					}
					// sinon, on la traite ici
					else
					{	errorHandler.displayError("Impossible de se connecter au client.");
						e.printStackTrace();
					}
				}
			}
			while(true);
		}
		catch(SocketException e)
		{	// rien à faire : c'est juste qu'un autre thread a fermé ce socket
		}

	}

	/**
	 * Initialise les objets et les theads nécessaires
	 * au traitement d'un client.
	 * 
	 * @param socket
	 * 		Socket utilisé pour communiquer avec le client.
	 * 
	 * @throws UnknownHostException
	 * 		Problème lors de la connexion au client.
	 * @throws IOException
	 * 		Problème lors de la connexion au client.
	 */
	private void processClient(Socket socket) throws UnknownHostException, IOException
	{	// on détermine le premier slot dispo
		int index = 0;
		while(index<sockets.length && sockets[index]==null)
			index++;
		
		// on ouvre le socket
		sockets[index] = new Socket(ip, port);
		
		// on récupère le flux d'entrée
		InputStream is = sockets[index].getInputStream();
		oiss[index] = new ObjectInputStream(is);
		
		// on récupère le flux de sortie
		OutputStream os = sockets[index].getOutputStream();
		ooss[index] = new ObjectOutputStream(os);
		
		// on crée un thread pour s'occuper des entrées
		srrs[index] = new ServerReadRunnable(this,index);
		Thread inThread = new Thread(srrs[index],"Courbes-Server-"+index+"-In");
		inThread.start();

		// et un autre pour les sorties
		swrs[index] = new ServerWriteRunnable(this,index);
		Thread outThread = new Thread(swrs[index],"Courbes-Server-"+index+"-Out");
		outThread.start();
	}
	
	@Override
	public void closeServer()
	{	// ferme le socket de connexion, ce qui doit entrainer la fin du thread associé
		try
		{	serverSocket.close();
			serverSocket = null;
		}
		catch(IOException e)
		{	e.printStackTrace();
		}
		
		// ferme les connexions avec les clients
		for(int i=0;i<sockets.length;i++)
			closeConnection(i);
	}
	
	/**
	 * Ferme la connection et tue les processus associés
	 * à un client donné.
	 * 
	 * @param index
	 * 		Numéro du client.
	 */
	private void closeConnection(int index)
	{	// on indique aux deux threads de se terminer (proprement)
		srrs[index].setActive(false);
		srrs[index] = null;
		swrs[index].setActive(false);
		swrs[index] = null;
		
		oiss[index] = null;
		ooss[index] = null;
		
		// on ferme la socket
		try
		{	sockets[index].close();
			sockets[index] = null;
		}
		catch (IOException e)
		{	e.printStackTrace();
			errorHandler.displayError("Erreur lors de la fermeture du socket numéro "+index);
		}
	}
	
	@Override
	public void setClientNumber(int clientNumber)
	{	if(sockets==null)
		{	sockets = new Socket[clientNumber];
			srrs = new ServerReadRunnable[clientNumber];
			swrs = new ServerWriteRunnable[clientNumber];
			oiss = new ObjectInputStream[clientNumber];
			ooss = new ObjectOutputStream[clientNumber];
			lastProfiles = new Profile[clientNumber];
	
		}
		else
		{	
			// on ferme éventuellement des connexions existantes
			if(clientNumber<sockets.length)
			{	for(int i=sockets.length-1;i>=clientNumber;i--)
				{	if(swrs[i]!=null)
						swrs[i].objects.offer(NetworkConstants.ANNOUNCE_REJECTED_PROFILE);	
					closeConnection(i);
				}
			}
			
			// on redimensionne les tableaux
			sockets = Arrays.copyOf(sockets, clientNumber);
			srrs = Arrays.copyOf(srrs, clientNumber);
			swrs = Arrays.copyOf(swrs, clientNumber);
			oiss = Arrays.copyOf(oiss, clientNumber);
			ooss = Arrays.copyOf(ooss, clientNumber);
			lastProfiles = Arrays.copyOf(lastProfiles, clientNumber);
		}
	}
	
	////////////////////////////////////////////////////////////////
	////	ENTREES
	////////////////////////////////////////////////////////////////
	/** Objet chargé de la communication en entrée avec le serveur */
	private ServerReadRunnable[] srrs;
	
	@Override
	public Direction[] retrieveCommands()
	{	Direction[] result = new Direction[sockets.length];
		
		for(int i=0;i<sockets.length;i++)
		{	ServerReadRunnable srr = srrs[i];
			if(srr==null)
				result[i] = Direction.NONE;
			else
			{	result[i] = srrs[i].directions.poll();
				if(result[i]==null)
					result[i] = Direction.NONE;
			}
		}
		
		return result;
	}
	
	////////////////////////////////////////////////////////////////
	////	SORTIES
	////////////////////////////////////////////////////////////////
	/** Objet chargé de la communication en sortie avec le serveur */
	private ServerWriteRunnable[] swrs;
	/** Dernière version de la liste de profils */
	private Profile[] lastProfiles = new Profile[1];
	
	@Override
	public void sendProfiles(Profile[] profiles)
	{	lastProfiles = Arrays.copyOf(profiles,profiles.length);
		sendObject(profiles);
	}
	
	/**
	 * Renvoie la dernière liste de profils reçue.
	 */
	public void reSendProfiles()
	{	sendObject(lastProfiles);
	}
	
	@Override
	public void sendPointThreshold(int pointThreshold)
	{	sendObject(pointThreshold);
	}
	
	@Override
	public void sendBoard(Board board)
	{	sendObject(board);
	}

	@Override
	public void sendRound(Round round)
	{	sendObject(round);
	}

	/**
	 * Envoie un objet quelconque à tous les clients.
	 * 
	 * @param object
	 * 		L'objet à envoyer.
	 */
	public void sendObject(Object object)
	{	for(ServerWriteRunnable swr: swrs)
		{	if(swr!=null)
				swr.objects.offer(object);
		}
	}
}
