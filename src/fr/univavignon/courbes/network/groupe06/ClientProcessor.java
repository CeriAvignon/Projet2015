package fr.univavignon.courbes.network.groupe06;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Map;

import fr.univavignon.courbes.common.Profile;

/**
 * @author loic
 *	
 *	Cette classe a pour objectif de gérer la réception des différents messages du client et des les 
 *	transférer dans les buffers équivalent dans la classe Server.
 */
public class ClientProcessor extends Server implements Runnable{

	/** La socket du client. */
	private Socket sock;
   
	/**
	 * @param pSock
	 * 		Nouveau client.
	 */	
	public ClientProcessor(Socket pSock){
		sock = pSock;
	}
   
	@Override
   	public void run(){
		ObjectInputStream ois;
		while(isRunning) {
			try {
				ois = null;
				ois = new ObjectInputStream(sock.getInputStream());
				Object objet = ois.readObject();
				if (objet instanceof Map<?,?>) ;
				//	profiles = (Map<Integer,Direction>)objet;
				else if (objet instanceof String); 
				//	messageText = (String)objet;
				else if (objet instanceof Profile);
				//	pointThreshold = (Profile)objet;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
