package fr.univavignon.courbes.chatTCP;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.util.Date;

public class ClientProcessor implements Runnable{

   private Socket sock;
   private PrintWriter writer = null;
   private BufferedInputStream reader = null;
   
   public ClientProcessor(Socket pSock){
      sock = pSock;
   }
   
   //Le traitement lancé dans un thread séparé
   public void run(){
      System.err.println("Lancement du traitement de la connexion cliente");

      boolean closeConnexion = false;
      //tant que la connexion est active, on traite les demandes
      while(!sock.isClosed()){
         
         try {
            
            //Ici, nous n'utilisons pas les mêmes objets que précédemment
            //Je vous expliquerai pourquoi ensuite
            writer = new PrintWriter(sock.getOutputStream());
            reader = new BufferedInputStream(sock.getInputStream());
            writer.write("Vous etes bien connecte(e) sur le serveur 192.168.0.11.\n Bonjour.");
			writer.flush();
            boolean connexion = true;
            while(connexion == true)
            {	
            	//On attend la demande du client            
            	String response = read();
            
            	//On traite la demande du client en fonction de la commande envoyée
            	String toSend = response;
            
            	switch(response){
            	case "/who":
            		//toSend = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM).format(new Date());
            		break;
            	case "/quit":
            		connexion = false;
            		break;
            	default :
            		break;
            	}
            	//On envoie la réponse au client
            	
            
            	if(!connexion){
            	System.err.println("COMMANDE CLOSE DETECTEE ! ");
               	writer = null;
               	reader = null;
               	sock.close();
               	break;
            	}
            	int i=0;
            	while(i<ServerTCP.nbClient){
            		Socket sock1=ServerTCP.tableauSocket[i];
            		writer = new PrintWriter(sock1.getOutputStream());
            		if(sock != sock1){
            			writer.write(toSend);
            			writer.flush();
            		}
            		i++;
            	}
            }
         }
            catch(SocketException e){
        	 System.err.println("LA CONNEXION A ETE INTERROMPUE ! ");
            	break;
         	} catch (IOException e) {
        	 e.printStackTrace();
         	}
      }
   }
   
   //La méthode que nous utilisons pour lire les réponses
   private String read() throws IOException{      
      String response = "";
      int stream;
      byte[] b = new byte[4096];
      stream = reader.read(b);
      response = new String(b, 0, stream);
      return response;
   }
   
}
