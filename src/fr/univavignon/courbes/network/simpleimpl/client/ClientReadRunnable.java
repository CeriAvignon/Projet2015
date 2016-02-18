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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.common.UpdateInterface;

/**
 * Classe chargée de lire en permanence sur le flux d'entrée du client.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ClientReadRunnable implements Runnable
{	/**  Indique s'il faut logger les échanges réseaux (debug) */
	private final static boolean LOG = false;
	
	/**
	 * Crée un objet chargé de la communication en entrée avec le serveur.
	 * 
	 * @param clientCom
	 * 		Client concerné.
	 */
	public ClientReadRunnable(ClientCommunicationImpl clientCom)
	{	this.clientCom = clientCom;
		socket = clientCom.socket;
		firstRound = true;
	}
	
	////////////////////////////////////////////////////////////////
	////	TRANSMISSION
	////////////////////////////////////////////////////////////////
	/** Socket utilisé pour communiquer avec le serveur */
	private Socket socket;
	/** Classe principale du client */
	private ClientCommunicationImpl clientCom;
	/** Indentifie la premier manche reçue */
	private boolean firstRound;

	@Override
	public void run()
	{	setActive(true);
		ObjectInputStream ois = null;
	
		try
		{	// on récupère le flux d'entrée
			InputStream is = socket.getInputStream();
			ois = new ObjectInputStream(is);
			
			do
			{	Object object = ois.readObject();
				if(LOG)
					System.out.println("CLIENT<<< "+object.toString());
				// objets mis en tampon
				if(object instanceof String)
				{	String string = (String)object;
					if(string.equals(NetworkConstants.ANNOUNCE_REJECTED_CONNECTION))
						clientCom.gotRefused();
					else if(string.equals(NetworkConstants.ANNOUNCE_ACCEPTED_CONNECTION))
						clientCom.gotAccepted();
					
					else if(string.equals(NetworkConstants.ANNOUNCE_REJECTED_PROFILE))
						clientCom.gotKicked();
				}
				else if(object instanceof UpdateInterface)
				{	UpdateInterface ud = (UpdateInterface)object;
					updateData.offer(ud);
					//System.out.println(boards.size());					
				}
				else if(object instanceof Integer)
				{	Integer integer = (Integer)object;
					pointsLimits.offer(integer);
				}
				
				// objets passés au client handler
				else if(object instanceof Round)
				{	Round round = (Round)object;
					if(firstRound)
					{	clientCom.startGame(round);
						firstRound = false;
					}
					else
						clientCom.fetchRound(round);
				}
				else if(object instanceof Profile[])
				{	Profile[] profiles = (Profile[])object;
				clientCom.updateProfiles(profiles);
				}
				
			}
			while(isActive());
		}
		catch(EOFException | SocketException e)
		{	// connexion probablement fermée par l'autre thread ou le serveur
			clientCom.lostConnection();
		}
		catch(ClassNotFoundException e)
		{	e.printStackTrace();
		}
		catch(IOException e)
		{	e.printStackTrace();
			clientCom.displayError("Erreur lors de la réception de données.");
		}
		finally
		{	try
			{	ois.close();
			}
			catch (IOException e)
			{	
//				e.printStackTrace();
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
	protected Queue<UpdateInterface> updateData = new ConcurrentLinkedQueue<UpdateInterface>();
	/** File des limites de points reçues du serveur et en attente de récupération par l'Interface Utilisateur */
	protected Queue<Integer> pointsLimits = new ConcurrentLinkedQueue<Integer>();
}
