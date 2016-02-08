package fr.univavignon.courbes.network.simpleimpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;

/**
 * @author TORO Loïc
 *	
 *	Cette classe a pour objectif de gérer la réception des différents messages du client et des les 
 *	transférer dans les buffers équivalent dans la classe Server.
 */
public class ClientProcessor extends Server implements Runnable{

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
