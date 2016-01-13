package fr.univavignon.courbes.network.groupe22;

import java.io.IOException;
import java.io.ObjectInputStream;

import java.net.Socket;
import java.net.SocketException;

import java.util.Map;

import fr.univavignon.courbes.common.Profile;

/**
 * @author Gael Cuminal
 * La classe permettant de gérer les clients connectés au serveur
 */
public class ClientProcessor extends ServerCom implements Runnable{

	   /**
	 * The client socket to handle
	 */
	private Socket client;

	   /**
	 * @param pSock
	 * The constructor of the client processor
	 */
	public ClientProcessor(Socket pSock){
	      client = pSock;
	   }

	   //Le traitement lancé dans un thread séparé
	   @Override
	public void run(){
	      //tant que la connexion est active, on traite les demandes
	      ObjectInputStream ois;
	      while(!client.isClosed()){
	         try {
	            ois = new ObjectInputStream(this.client.getInputStream());

	            Object sentObject = ois.readObject();

	            if(sentObject instanceof Map<?,?>)
	            {
	              //TODO: handle map elements
	            }

	            else if (sentObject instanceof Profile) {
	              //TODO: handle profiles
	            }

	         }catch(SocketException e){
	            System.err.println("LA CONNEXION A ETE INTERROMPUE ! ");
	            break;
	         } catch (IOException e) {
	            e.printStackTrace();
	         } catch (ClassNotFoundException e) {
				e.printStackTrace();
			} 
	      }
	   }

}
