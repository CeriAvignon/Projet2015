package fr.univavignon.courbes.network.groupe06;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;


/**
 * @author loic
 *	
 *	Cette classe a pour objectif de gérer la réception des différents messages du client et des les 
 *	transférer dans les buffers équivalent dans la classe Server.
 */
public class ClientProcessor extends Server implements Runnable{

	/**
    * La socket du client.
 	*/
	private Socket sock;
	/**
    * Flux d'écriture.
 	*/
	private PrintWriter writer = null;
	/**
	 * Flux de Lecture.
	 */
	private BufferedInputStream reader = null;
   
	/**
	 * @param pSock
	 * 	Nouveau client.
	 */	
	public ClientProcessor(Socket pSock){
		sock = pSock;
	}
   
	//Le traitement lancé dans un thread séparé
	@Override
   	public void run(){
      //tant que la connexion est active, on traite les demandes
         
			try {
				writer = new PrintWriter(sock.getOutputStream());
				reader = new BufferedInputStream(sock.getInputStream());
				boolean connexion = true;
				System.out.println("La connexion cliente est bien prise en compte ! ");
				while(connexion == true) {
				
					String response = read();
					
					switch(response){
	            	case "/quit":
	            		connexion = false;
	            		int i=0;
	            		
	            		while(Server.socketArray[i] != sock )
	            			i++;
	            		Server.socketArray[i].close();
	            		sock.close();
	            		Server.socketArray[i] = Server.socketArray[Server.nbClients - 1];        		
	            		Server.nbClients--;
	            		System.err.println("LA CONNEXION A ETE INTERROMPUE ! ");
	            		break;
	            	default :
	            		System.out.println(response);
	            		break;
	            	}
				}
			} catch(SocketException e){
				System.err.println("LA CONNEXION A ETE INTERROMPUE ! ");
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}
   
   /**
 * @return le message lu
 * @throws IOException En cas d'échec de lecture.
 */
   private String read() throws IOException {      
      String response = "";
      int stream;
      byte[] b = new byte[4096];
      stream = reader.read(b);
      response = new String(b, 0, stream);
      return response;
   }
  
   
}
