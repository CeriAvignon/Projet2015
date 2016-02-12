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

import fr.univavignon.courbes.network.simpleimpl.NetworkConstants;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.inter.ClientConfigHandler;
import fr.univavignon.courbes.inter.ErrorHandler;

/**
 * Classe chargée de lire en permanence sur le flux d'entrée du client.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ClientReadRunnable implements Runnable
{	
	/**
	 * Crée un objet chargé de la communication en entrée avec le serveur.
	 * 
	 * @param clientCom
	 * 		Client concerné.
	 */
	public ClientReadRunnable(ClientCommunicationImpl clientCom)
	{	ois = clientCom.ois;
		configHandler = clientCom.configHandler;
		errorHandler = clientCom.errorHandler;
	}
	
	////////////////////////////////////////////////////////////////
	////	TRANSMISSION
	////////////////////////////////////////////////////////////////
	/** Flux d'entrée utilisé pour communiquer avec le serveur */
	private ObjectInputStream ois;
	/** Handler chargé de la configuration du client */
	private ClientConfigHandler configHandler;
	/** Handler chargé des messages d'erreur */
	private ErrorHandler errorHandler;

	@Override
	public void run()
	{	setActive(true);
		
		try
		{	do
			{	Object object = ois.readObject();

				// objets mis en tampon
				if(object instanceof String)
				{	String string = (String)object;
					if(string.equals(NetworkConstants.ANNOUNCE_DISCONNECTION))
						setActive(false);//TODO probablement d'autres choses à faire à la suite d'une déconnexion ?
					else if(string.equals(NetworkConstants.ANNOUNCE_REJECTED_PROFILE))
						configHandler.disconnection();
				}
				else if(object instanceof Board)
				{	Board board = (Board)object;
					boards.offer(board);
				}
				else if(object instanceof Integer)
				{	Integer integer = (Integer)object;
					pointsLimits.offer(integer);
				}
				
				// objets passés au client handler
				else if(object instanceof Round)
				{	Round round = (Round)object;
					configHandler.startGame(round);
				}
				else if(object instanceof Profile[])
				{	Profile[] profiles = (Profile[])object;
					configHandler.updateProfiles(profiles);
				}
				
			}
			while(isActive());
		}
		catch (ClassNotFoundException e)
		{	e.printStackTrace();
		}
		catch (IOException e)
		{	e.printStackTrace();
			errorHandler.displayError("Erreur lors de la réception de données.");
		}
		finally
		{	try
			{	ois.close();
			}
			catch (IOException e)
			{	e.printStackTrace();
			}
		}
	}
	
	////////////////////////////////////////////////////////////////
	////	ETAT
	////////////////////////////////////////////////////////////////
	/** Indique si le thread est actif */
	private boolean active = true;
	
	/**
	 * Change l'état d'activité du thread (généralement utilisé
	 * pour stopper le thread, et donc se déconnecter du serveur).
	 *  
	 * @param active
	 * 		Nouvel état d'activité.
	 */
	public void setActive(boolean active)
	{	this.active = active;
	}
	
	/**
	 * Indique si le thread est actif.
	 * 
	 * @return
	 * 		{@code true} ssi le thread est actuellement actif.
	 */
	private boolean isActive()
	{	return active;
	}
	
	////////////////////////////////////////////////////////////////
	////	FILES DE DONNEES
	////////////////////////////////////////////////////////////////
	/** File des aires de jeu reçues du serveur et en attente de récupération par l'Interface Utilisateur */
	protected Queue<Board> boards = new ConcurrentLinkedQueue<Board>();
	/** File des limites de points reçues du serveur et en attente de récupération par l'Interface Utilisateur */
	protected Queue<Integer> pointsLimits = new ConcurrentLinkedQueue<Integer>();
}
