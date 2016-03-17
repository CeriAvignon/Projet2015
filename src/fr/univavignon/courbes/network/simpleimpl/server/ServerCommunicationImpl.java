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
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Enumeration;

import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.common.UpdateInterface;
import fr.univavignon.courbes.inter.ErrorHandler;
import fr.univavignon.courbes.inter.ServerGameHandler;
import fr.univavignon.courbes.inter.ServerProfileHandler;

/**
 * Implémentation de la classe {@link ServerCommunication}. Elle se repose
 * sur deux autres classes pour les entrées ({@link ServerReadRunnable}) et 
 * les sorties ({@link ServerWriteRunnable}) associées à chaque client.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ServerCommunicationImpl implements ServerCommunication, Runnable
{	
	////////////////////////////////////////////////////////////////
	////	ADRESSE IP
	////////////////////////////////////////////////////////////////
	/** Variable qui contient l'adresse ip de ce serveur */
	private String ip = null;
	
	@Override
	public String getIp()
	{	if(ip==null)
		{	try
			{	Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
				while(nis.hasMoreElements() && ip==null)
				{	NetworkInterface ni = nis.nextElement();
					Enumeration<InetAddress> ias = ni.getInetAddresses();
					while(ias.hasMoreElements() && ip==null)
					{	InetAddress ia = ias.nextElement();
						String iaStr = ia.getHostAddress();
						int i = 0;
						while(ip==null && i<Constants.IP_PREFIXES.length)
						{	if(iaStr.startsWith(Constants.IP_PREFIXES[i]))
								ip = iaStr;
							i++;
						}
					}
				}
			}
			catch (SocketException e)
			{	e.printStackTrace();
			}
		}
		return ip;
	}

	////////////////////////////////////////////////////////////////
	////	PORT
	////////////////////////////////////////////////////////////////
	/** Variable qui contient le port de ce serveur */
	private int port = Constants.DEFAULT_PORT;//2345;

	@Override
	public int getPort()
	{	return port;
	}

	@Override
	public void setPort(int port)
	{	this.port = port;
	}
	
	////////////////////////////////////////////////////////////////
	////	HANDLER DE PROFILS
	////////////////////////////////////////////////////////////////
	/** Handler de profils */
	public ServerProfileHandler profileHandler;

	@Override
	public void setProfileHandler(ServerProfileHandler configHandler)
	{	this.profileHandler = configHandler;
	}
	
	/**
	 * Transmet cet appel au handler concerné.
	 * 
	 * @param profile
	 * 		Le profil à transmettre.
	 * @param index
	 * 		Le numéro de client à transmettre.
	 */
	protected void fetchProfile(Profile profile, int index)
	{	if(profileHandler!=null)
			profileHandler.fetchProfile(profile,index);
//		else
//			System.err.println("Le handler de profils n'a pas été renseigné !");
	}

	/**
	 * Transmet cet appel au handler concerné.
	 * 
	 * @param index
	 * 		Le numéro de client à transmettre.
	 */
	public void connectionLost(int index)
	{	if(profileHandler!=null)
			profileHandler.connectionLost(index);
//		else
//			System.err.println("Le handler de profils n'a pas été renseigné !");
		
		if(gameHandler!=null)
			gameHandler.connectionLost(index);
//		else
//			System.err.println("Le handler de la partie n'a pas été renseigné !");
	}
	
	////////////////////////////////////////////////////////////////
	////	HANDLER DE LA PARTIE
	////////////////////////////////////////////////////////////////
	/** Handler de la partie */
	public ServerGameHandler gameHandler;

	@Override
	public void setGameHandler(ServerGameHandler gameHandler)
	{	this.gameHandler = gameHandler;
	}
	
	/**
	 * Transmet cet appel au handler concerné.
	 * 
	 * @param index
	 * 		Le numéro de client à transmettre.
	 */
	public void fetchAcknowledgment(int index)
	{	if(gameHandler!=null)
			gameHandler.fetchAcknowledgment(index);
//		else
//			System.err.println("Le handler de partie n'a pas été renseigné !");
	}
	
	////////////////////////////////////////////////////////////////
	////	HANDLER D'ERREUR
	////////////////////////////////////////////////////////////////
	/** Handler d'erreurs */
	public ErrorHandler errorHandler;

	@Override
	public void setErrorHandler(ErrorHandler errorHandler)
	{	this.errorHandler = errorHandler;
	}
	
	/**
	 * Transmet cet appel au handler concerné.
	 * 
	 * @param message
	 * 		Message à transmettre au handler.
	 */
	public void displayError(String message)
	{	if(errorHandler!=null)
			errorHandler.displayError(message);
//		else
//			System.err.println("Le handler d'erreur n'a pas été renseigné !");
	}
	
	////////////////////////////////////////////////////////////////
	////	CONNEXION
	////////////////////////////////////////////////////////////////
	/** Processus principal du serveur (seulement pour la configuration= */
	private Thread mainThread;
	/** Socket de connexion */
	private ServerSocket serverSocket;
	/** Socket du client connecté au serveur */
	public Socket[] sockets = null;
	
	@Override
	public void launchServer()
	{	mainThread = new Thread(this, "Courbes-ServerThread-Main");
		mainThread.start();
	}
	
	@Override
	public void run()
	{	try
		{	try
			{	serverSocket = new ServerSocket(port);
			}
			catch (IOException e1)
			{	e1.printStackTrace();
			}
		
			Socket socket;
			do
			{	try
				{	socket = serverSocket.accept();
					processClient(socket);
				}
				catch(UnknownHostException e)
				{	
//					errorHandler.displayError("Impossible de se connecter au client.");
//					e.printStackTrace();
				}
				catch(IOException e)
				{	// si c'est une SocketException, on la re-lève
					if(e instanceof SocketException)
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
	private synchronized void processClient(Socket socket) throws UnknownHostException, IOException
	{	// on détermine le premier slot dispo
		int index = 0;
		if(sockets==null)
		{	sockets = new Socket[1];
			srrs = new ServerReadRunnable[1];
			swrs = new ServerWriteRunnable[1];
		}
		else
		{	while(index<sockets.length && sockets[index]!=null)
				index++;
		}
		
		// cas où il reste de la place sur le serveur
		if(index<sockets.length)
		{	// on ouvre le socket
			sockets[index] = socket;
			
			// on crée un thread pour s'occuper des sorties
			swrs[index] = new ServerWriteRunnable(this,index);
			Thread outThread = new Thread(swrs[index],"Courbes-Server-"+index+"-Out");
			outThread.start();
			
			// on crée un thread pour s'occuper des entrées
			srrs[index] = new ServerReadRunnable(this,index);
			Thread inThread = new Thread(srrs[index],"Courbes-Server-"+index+"-In");
			inThread.start();
			
			// on répond favorablement au client
			swrs[index].objects.offer(NetworkConstants.ANNOUNCE_ACCEPTED_CONNECTION);
		}
		
		// cas où le serveur n'a plus de place
		else
		{	// on récupère le flux de sortie
			OutputStream os = socket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.flush();
			
//			// celui d'entrée (pour rien)
//			socket.getInputStream();
			
			// on répond défavorablement au client
			oos.writeObject(NetworkConstants.ANNOUNCE_REJECTED_CONNECTION);
			
			// on ferme la connexion
			socket.close();
		}
	}
	
	@Override
	public synchronized void closeServer()
	{	
//		// prévient les clients de la prochaine fermeture
//		for(int i=0;i<sockets.length;i++)
//		{	if(swrs[i]!=null)
//				swrs[i].objects.offer(NetworkConstants.ANNOUNCE_DISCONNECTION);
//		}	
//		
//		// on attend que ces messages soient transmis
//		try
//		{	Thread.sleep(500);
//		}
//		catch(InterruptedException e)
//		{	e.printStackTrace();
//		}
		
		// ferme le socket de connexion, ce qui doit entrainer la fin du thread associé
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
	protected synchronized void closeConnection(int index)
	{	if(sockets[index]!=null)
		{	// on indique aux deux threads de se terminer (proprement)
			srrs[index].setActive(false);
			srrs[index] = null;
			swrs[index].setActive(false);
			swrs[index] = null;
			
			// on ferme la socket
			try
			{	sockets[index].close();
				sockets[index] = null;
			}
			catch (IOException e)
			{	//e.printStackTrace();
				//errorHandler.displayError("Erreur lors de la fermeture du socket numéro "+index);
			}
			
			// on indique à l'IU qu'on a perdu un client
		}
	}
	
	/**
	 * Méthode appelée quand la connexion avec un client est perdue
	 * accidentellement.
	 * 
	 * @param index
	 * 		Numéro du client concerné.
	 */
	protected synchronized void lostConnection(int index)
	{	if(sockets[index]!=null)
		{	connectionLost(index);
			closeConnection(index);
		}
	}
	
	@Override
	public synchronized void setClientNumber(int clientNumber)
	{	// si pas de clients pour l'instant
		if(sockets==null)
		{	sockets = new Socket[clientNumber];
			srrs = new ServerReadRunnable[clientNumber];
			swrs = new ServerWriteRunnable[clientNumber];
			lastProfiles = new Profile[clientNumber];
	
		}
		
		// s'il y a déjà des clients
		else
		{	// si le nombre diminue, il faut en fermer certains	
			if(clientNumber<sockets.length)
			{	// on prévient les clients rejetés
				boolean announce = false;
				for(int i=sockets.length-1;i>=clientNumber;i--)
				{	if(swrs[i]!=null)
					{	swrs[i].objects.offer(NetworkConstants.ANNOUNCE_REJECTED_PROFILE);
						announce = true;
					}
				}
				
				// on attend un peu que les messages soient transmis
				if(announce)
				{	try
					{	Thread.sleep(250);
					}
					catch (InterruptedException e)
					{	e.printStackTrace();
					}
				}
				
				// on ferme les clients concernés
				for(int i=sockets.length-1;i>=clientNumber;i--)
					closeConnection(i);
			}
			
			// dans tous les cas, on redimensionne les tableaux
			sockets = Arrays.copyOf(sockets, clientNumber);
			srrs = Arrays.copyOf(srrs, clientNumber);
			swrs = Arrays.copyOf(swrs, clientNumber);
			lastProfiles = Arrays.copyOf(lastProfiles, clientNumber);
		}
	}
	
	////////////////////////////////////////////////////////////////
	////	ENTREES
	////////////////////////////////////////////////////////////////
	/** Objet chargé de la communication en entrée avec le serveur */
	private ServerReadRunnable[] srrs;
	
	@Override
	public synchronized Direction[] retrieveCommands()
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
		
		// si aucune direction autre que NONE, on renvoie null
		boolean none = true;
		int i = 0;
		while(none && i<result.length)
		{	none = result[i]==Direction.NONE;
			i++;
		}
		if(none)
			result = null;
		
		return result;
	}
	
	@Override
	public synchronized void finalizeRound()
	{	
//		while(serverCom.retrieveCommands()!=null);
		for(ServerReadRunnable srr: srrs)
		{	if(srr!=null)
				srr.directions.clear();
		}
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
	
//	/**
//	 * Renvoie la dernière liste de profils reçue.
//	 */
//	public void reSendProfiles()
//	{	sendObject(lastProfiles);
//	}
	
	@Override
	public void sendPointThreshold(int pointThreshold)
	{	Integer value = new Integer(pointThreshold);
		sendObject(value);
	}
	
	@Override
	public void sendUpdate(UpdateInterface updateData)
	{	sendObject(updateData);
	}
	
	@Override
	public void sendRound(Round round)
	{	sendObject(round);
	}
	
	@Override
	public void kickClient(int index)
	{	// on prévient le client
		if(swrs[index]!=null)
			swrs[index].objects.offer(NetworkConstants.ANNOUNCE_REJECTED_PROFILE);
		
		// on attend un peu quel le message lui parvienne
		try
		{	Thread.sleep(250);
		}
		catch(InterruptedException e)
		{	e.printStackTrace();
		}
		
		// on ferme la connexion
		closeConnection(index);
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
