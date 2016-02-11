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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;

/**
 *	Cette classe a pour objectif de gérer la réception des différents messages du client et des les 
 *	transférer dans les buffers équivalent dans la classe Server.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ClientProcessor extends ServerCommunicationImpl implements Runnable{

	/** La socket du client. */
	private Socket sock;
	/**La liste des identifiants de tous les joueurs sur un seul client. */
	private ArrayList<Integer> arrayOfId;
   
	/**
	 *  Constructeur de la classe ClientProcessor.
	 * @param pSock
	 * 		Nouveau client.
	 */	
	public ClientProcessor(Socket pSock){
		this.sock = pSock;
	}
   
	@Override
   	public void run(){
		ObjectInputStream ois;
		while(isRunning) {
			try {
				ois = null;
				ois = new ObjectInputStream(this.sock.getInputStream());
				Object objet = ois.readObject();
				
				boolean test;
				if (objet instanceof Map<?,?>) 
					commands.putAll((Map<Integer, Direction>)objet);
				else if (objet instanceof String) {
					String message = (String)objet; 
					if (message == "/quit") {
						nbClients--;
						socketArray.remove(this.sock);
						arrayOfIp.remove(this.sock.getInetAddress().getHostAddress());
						messageError.displayError("L'ip "+this.sock.getInetAddress().getHostAddress()+" a quitté le serveur.");
						sock.close();
						break;
					}
				}
				else if (objet instanceof Profile) {
					test = profileHandler.fetchProfile((Profile)objet);
					if (test) {
						Profile profil = (Profile)objet;
						arrayOfId.add(profil.profileId);
					}
					else {
						ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
						oos.writeObject("/reject");
						oos.flush();
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				messageError.displayError("Un client a été déconnecté.");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
