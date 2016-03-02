package fr.univavignon.courbes.network.kryonet;

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

import java.awt.EventQueue;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.common.UpdateInterface;
import fr.univavignon.courbes.inter.ClientConnectionHandler;
import fr.univavignon.courbes.inter.ClientGameHandler;
import fr.univavignon.courbes.inter.ClientProfileHandler;
import fr.univavignon.courbes.inter.ErrorHandler;

import fr.univavignon.courbes.network.ClientCommunication;
import fr.univavignon.courbes.network.kryonet.NetworkConstants;

/**
 * Implémentation de la classe {@link ClientCommunication}. Elle repose
 * sur la bibliothèque <a href="https://github.com/EsotericSoftware/kryonet">Kryonet</>.
 * 
 * @author L3 Info UAPV 2015-16
 */
public class ClientCommunicationKryonetImpl extends Listener implements ClientCommunication {
	////////////////////////////////////////////////////////////////
	//// ADRESSE IP
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
	//// PORT
	////////////////////////////////////////////////////////////////
	/** Variable qui contient le port du serveur */
	private int port = Constants.DEFAULT_PORT;// 2345;

	@Override
	public int getPort()
	{	return port;
	}

	@Override
	public void setPort(int port)
	{	this.port = port;
	}

	////////////////////////////////////////////////////////////////
	//// HANDLER DE CONNEXION
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
	{	if (connectionHandler != null)
			connectionHandler.gotAccepted();
//		else
//			System.err.println("Le handler de connexion n'a pas été renseigné !");
	}

	/**
	 * Transmet cet appel au handler concerné.
	 */
	protected void gotRefused()
	{	closeClient();
		if (connectionHandler != null)
			connectionHandler.gotRefused();
//		else
//			System.err.println("Le handler de connexion n'a pas été renseigné !");
	}

	////////////////////////////////////////////////////////////////
	//// HANDLER DE PROFILS
	////////////////////////////////////////////////////////////////
	/** Handler de profils */
	private ClientProfileHandler profileHandler;

	@Override
	public void setProfileHandler(ClientProfileHandler configHandler)
	{	this.profileHandler = configHandler;
	}

	/**
	 * Transmet cet appel au handler concerné.
	 */
	protected void gotKicked()
	{	closeClient();
		if (profileHandler != null)
			profileHandler.gotKicked();
//		else
//			System.err.println("Le handler de profils n'a pas été renseigné !");
	}

	/**
	 * Transmet cet appel au handler concerné.
	 * 
	 * @param profiles
	 *            Tableau de profils à transmettre au handler.
	 */
	protected void updateProfiles(Profile[] profiles)
	{	/* If the handler is not defined */
		int iteration = 0;

		/*
		 * Wait to give enough time for method <init> from ClientGameWaitPanel
		 * to be called to set the handler
		 */
		while (profileHandler == null && iteration < 100)
		{	try
			{	Thread.sleep(10);
			}
			catch (InterruptedException e)
			{	e.printStackTrace();
			}
			iteration++;
		}
		
		if (profileHandler != null)
			profileHandler.updateProfiles(profiles);
//		else
//			System.err.println("Le handler de profils n'a pas été renseigné !");
	}

	/**
	 * Transmet cet appel au handler concerné.
	 * 
	 * @param round
	 *            Manche à transmettre au handler.
	 */
	protected void startGame(Round round)
	{	if (profileHandler != null)
			profileHandler.startGame(round);
//		else
//			System.err.println("Le handler de profils n'a pas été renseigné !");
	}

	/**
	 * Transmet cet appel au handler concerné.
	 */
	public void connectionLost()
	{	if (profileHandler != null)
			profileHandler.connectionLost();
//		else
//			System.err.println("Le handler de profils n'a pas été renseigné !");
		
		if(gameHandler!=null)
			gameHandler.connectionLost();
//		else
//			System.err.println("Le handler de partie n'a pas été renseigné !");
	}

	////////////////////////////////////////////////////////////////
	//// HANDLER DE LA PARTIE
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
	 *            Manche à transmettre au handler.
	 */
	public void fetchRound(Round round)
	{	if (gameHandler != null)
			gameHandler.fetchRound(round);
//		else
//			System.err.println("Le handler de partie n'a pas été renseigné !");
	}

	////////////////////////////////////////////////////////////////
	//// HANDLER D'ERREUR
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
	 *            Message à transmettre au handler.
	 */
	public void displayError(String message)
	{	if (errorHandler != null)
			errorHandler.displayError(message);
//		else
//			System.err.println("Le handler d'erreur n'a pas été renseigné !");
	}

	////////////////////////////////////////////////////////////////
	//// CONNEXION
	////////////////////////////////////////////////////////////////
	/**
	 * Objet de la librairie kryonet représentant le client
	 */
	private Client client;

	/** Indentifie la premier manche reçue */
	private boolean firstRound;

	@Override
	public synchronized boolean launchClient()
	{	boolean result = true;

		client = new Client(60000, 60000);
		client.start();

		firstRound = true;

		ClassRegisterer.register(client);

		client.addListener(this);

		int timeout = 5000;
		try
		{	client.connect(timeout, ip, port, port + 1);
		}
		catch (IOException e)
		{	result = false;
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public synchronized void closeClient()
	{	client.stop();
	}

	@Override
	public synchronized boolean isConnected()
	{	return client != null && client.isConnected();
	}

	/**
	 * Méthode appelée quand la connexion avec le serveur est perdue
	 * accidentellement.
	 */
	protected synchronized void lostConnection()
	{	if(client != null)
		{	connectionLost();
			closeClient();
		}
	}

	////////////////////////////////////////////////////////////////
	//// ENTREES
	////////////////////////////////////////////////////////////////
	@Override
	public Integer retrievePointThreshold()
	{	Integer result = pointsLimits.poll();
		return result;
	}

	@Override
	public UpdateInterface retrieveUpdate()
	{	UpdateInterface result = updateData.poll();
		return result;
	}

	////////////////////////////////////////////////////////////////
	//// SORTIES
	////////////////////////////////////////////////////////////////
	@Override
	public void sendCommand(Direction direction)
	{	Integer intToSend = new Integer(0);
		
		switch (direction)
		{	case LEFT:
				intToSend = new Integer(-1);
				break;
			case RIGHT:
				intToSend = new Integer(1);
				break;
		}
		client.sendTCP(intToSend);
	}

	@Override
	public void sendProfile(Profile profile)
	{	client.sendTCP(profile);
	}

	@Override
	public void sendAcknowledgment()
	{	client.sendTCP(NetworkConstants.ANNOUNCE_ACKNOWLEDGMENT);
	}

	////////////////////////////////////////////////////////////////
	//// FILES DE DONNEES
	////////////////////////////////////////////////////////////////
	/**
	 * File des aires de jeu reçues du serveur et en attente de récupération par
	 * l'Interface Utilisateur
	 */
	protected Queue<UpdateInterface> updateData = new ConcurrentLinkedQueue<UpdateInterface>();

	/**
	 * File des limites de points reçues du serveur et en attente de
	 * récupération par l'Interface Utilisateur
	 */
	protected Queue<Integer> pointsLimits = new ConcurrentLinkedQueue<Integer>();

	/**
	 * Method called when the connection with the server is created
	 */
	@Override
	public void connected(Connection connection)
	{	/* Send a Boolean so that the server can send back the ANNOUNCE_ACCEPTED or ANNOUNCE_REJECTED String */
		client.sendTCP(new Boolean(true));
	}

	/**
	 * Method called when an object is received from the connection
	 */
	@Override
	public void received(Connection connection, Object object)
	{	if (object instanceof String)
		{	String string = (String) object;

			if (string.equals(NetworkConstants.ANNOUNCE_REJECTED_CONNECTION))
				gotRefused();
			else if (string.equals(NetworkConstants.ANNOUNCE_ACCEPTED_CONNECTION))
				gotAccepted();
			else if (string.equals(NetworkConstants.ANNOUNCE_REJECTED_PROFILE))
				gotKicked();
		}

		else if (object instanceof UpdateInterface)
		{	UpdateInterface ud = (UpdateInterface) object;
			updateData.offer(ud);
		}
		else if (object instanceof Integer)
		{	Integer integer = (Integer) object;
			pointsLimits.offer(integer);
		}

		else if (object instanceof Round)
		{	Round round = (Round) object;
			if(firstRound)
			{	startGame(round);
				firstRound = false;
			}
			else
				fetchRound(round);
		}
		else if (object instanceof Profile[])
		{	Profile[] profiles = (Profile[]) object;
			updateProfiles(profiles);
		}
	}

	/**
	 * Method called when the connection with the server is lost
	 * @param connection the connection
	 */
	@Override
	public void disconnected(Connection connection)
	{	EventQueue.invokeLater(new Runnable()
		{	@Override
			public void run()
			{	lostConnection();
			}
		});
	}

	@Override
	public void finalizeRound()
	{	// Nothing to do
	}
}
