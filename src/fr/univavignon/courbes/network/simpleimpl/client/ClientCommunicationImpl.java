package fr.univavignon.courbes.network.simpleimpl.client;

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

import fr.univavignon.courbes.network.ClientCommunication;
import fr.univavignon.courbes.network.simpleimpl.NetworkConstants;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.common.UpdateInterface;
import fr.univavignon.courbes.inter.ClientConnectionHandler;
import fr.univavignon.courbes.inter.ClientGameHandler;
import fr.univavignon.courbes.inter.ClientProfileHandler;
import fr.univavignon.courbes.inter.ErrorHandler;

/**
 * Implémentation de la classe {@link ClientCommunication}. Elle se repose
 * sur deux autres classes pour les entrées ({@link ClientReadRunnable}) et 
 * les sorties ({@link ClientWriteRunnable}).
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ClientCommunicationImpl implements ClientCommunication
{	
	////////////////////////////////////////////////////////////////
	////	ADRESSE IP
	////////////////////////////////////////////////////////////////
	/** Variable qui contient l'adresse ip du serveur */
	private String ip;

	@Override
	public String getIp()
	{	return ip;
	}

	@Override
	public void setIp(String ip)
	{	this.ip = ip;
	}

	////////////////////////////////////////////////////////////////
	////	PORT
	////////////////////////////////////////////////////////////////
	/** Variable qui contient le port du serveur */
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
	////	HANDLER DE CONNEXION
	////////////////////////////////////////////////////////////////
	/** Handler de connexion */
	public ClientConnectionHandler connectionHandler;

	@Override
	public void setConnectionHandler(ClientConnectionHandler connectionHandler)
	{	this.connectionHandler = connectionHandler;
	}
	
	/**
	 * Transmet cet appel au handler concerné.
	 */
	protected void gotAccepted()
	{	if(connectionHandler!=null)
			connectionHandler.gotAccepted();
//		else
//			System.err.println("Le handler de connexion n'a pas été renseigné !");
	}
	
	/**
	 * Transmet cet appel au handler concerné.
	 */
	protected void gotRefused()
	{	closeClient();
		if(connectionHandler!=null)
			connectionHandler.gotRefused();
//		else
//			System.err.println("Le handler de connexion n'a pas été renseigné !");
	}
	
	////////////////////////////////////////////////////////////////
	////	HANDLER DE PROFILS
	////////////////////////////////////////////////////////////////
	/** Handler de profils */
	public ClientProfileHandler profileHandler;

	@Override
	public void setProfileHandler(ClientProfileHandler configHandler)
	{	this.profileHandler = configHandler;
	}
	
	/**
	 * Transmet cet appel au handler concerné.
	 */
	protected void gotKicked()
	{	closeClient();
		if(profileHandler!=null)
			profileHandler.gotKicked();
//		else
//			System.err.println("Le handler de profils n'a pas été renseigné !");
	}
	
	/**
	 * Transmet cet appel au handler concerné.
	 * 
	 * @param profiles
	 * 		Tableau de profils à transmettre au handler.
	 */
	protected void updateProfiles(Profile[] profiles)
	{	if(profileHandler!=null)
			profileHandler.updateProfiles(profiles);
//		else
//			System.err.println("Le handler de profils n'a pas été renseigné !");
	}
	
	/**
	 * Transmet cet appel au handler concerné.
	 * 
	 * @param round
	 * 		Manche à transmettre au handler.
	 */
	protected void startGame(Round round)
	{	if(profileHandler!=null)
			profileHandler.startGame(round);
//		else
//			System.err.println("Le handler de profils n'a pas été renseigné !");
	}
	
	/**
	 * Transmet cet appel au handler concerné.
	 */
	public void connectionLost()
	{	if(profileHandler!=null)
			profileHandler.connectionLost();
//		else
//			System.err.println("Le handler de profils n'a pas été renseigné !");
		if(gameHandler!=null)
			gameHandler.connectionLost();
	}
	
	////////////////////////////////////////////////////////////////
	////	HANDLER DE LA PARTIE
	////////////////////////////////////////////////////////////////
	/** Handler de la partie */
	public ClientGameHandler gameHandler;

	@Override
	public void setGameHandler(ClientGameHandler gameHandler)
	{	this.gameHandler = gameHandler;
	}
	
	/**
	 * Transmet cet appel au handler concerné.
	 * 
	 * @param round
	 * 		Manche à transmettre au handler.
	 */
	public void fetchRound(Round round)
	{	if(gameHandler!=null)
			gameHandler.fetchRound(round);
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
	/** Socket du client connecté au serveur */
	public Socket socket = null;
	
	@Override
	public synchronized boolean launchClient()
	{	boolean result = true;
	
		try
		{	// on ouvre le socket
			socket = new Socket(ip, port);
			
			// on crée un thread pour s'occuper des sorties
			cwr = new ClientWriteRunnable(this);
			Thread outThread = new Thread(cwr,"Courbes-Client-Out");
			outThread.start();
			
			// on crée un thread pour s'occuper des entrées
			crr = new ClientReadRunnable(this);
			Thread inThread = new Thread(crr,"Courbes-Client-In");
			inThread.start();
		}
		catch(ConnectException e)
		{	result = false;
		}
		catch(UnknownHostException e)
		{	result = false;
			errorHandler.displayError("Impossible de se connecter au serveur.");
			e.printStackTrace();
		}
		catch(IOException e)
		{	result = false;
			errorHandler.displayError("Impossible de se connecter au serveur.");
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public synchronized void closeClient()
	{	if(socket!=null)
		{	
//			// on indique qu'on se déconnecte
//			cwr.objects.offer(NetworkConstants.ANNOUNCE_DISCONNECTION);
			
			// on indique aux deux threads de se terminer (proprement)
			crr.setActive(false);
			crr = null;
			cwr.setActive(false);
			cwr = null;
			
			// on ferme la socket
			try
			{	socket.close();
				socket = null;
			}
			catch (IOException e)
			{	e.printStackTrace();
				errorHandler.displayError("Erreur lors de la fermeture du socket.");
			}
		}
	}

	@Override
	public synchronized boolean isConnected()
	{	boolean result = socket!=null && socket.isConnected() && !socket.isClosed();
		return result;
	}
	
	/**
	 * Méthode appelée quand la connexion avec le serveur est perdue accidentellement.
	 */
	protected synchronized void lostConnection()
	{	if(socket!=null)
		{	connectionLost();
			closeClient();
		}
	}
	
	////////////////////////////////////////////////////////////////
	////	ENTREES
	////////////////////////////////////////////////////////////////
	/** Objet chargé de la communication en entrée avec le serveur */
	private ClientReadRunnable crr;

	@Override
	public Integer retrievePointThreshold()
	{	Integer result = null;
		if(crr!=null)
			result = crr.pointsLimits.poll();
		return result;
	}

	@Override
	public UpdateInterface retrieveUpdate()
	{	UpdateInterface result = null;
		if(crr!=null)
			result = crr.updateData.poll();
		return result;
	}
	
	@Override
	public synchronized void finalizeRound()
	{	
//		while(clientCom.retrieveUpdate()!=null);
		if(crr!=null)
			crr.updateData.clear();
	}
	
	////////////////////////////////////////////////////////////////
	////	SORTIES
	////////////////////////////////////////////////////////////////
	/** Objet chargé de la communication en sortie avec le serveur */
	private ClientWriteRunnable cwr;
	
	@Override
	public void sendCommand(Direction direction)
	{	if(cwr!=null)
			cwr.objects.offer(direction);
	}

	@Override
	public void sendProfile(Profile profile)
	{	if(cwr!=null)
			cwr.objects.offer(profile);
	}
	
	@Override
	public void sendAcknowledgment()
	{	if(cwr!=null)
			cwr.objects.offer(NetworkConstants.ANNOUNCE_ACKNOWLEDGMENT);
	}
}
