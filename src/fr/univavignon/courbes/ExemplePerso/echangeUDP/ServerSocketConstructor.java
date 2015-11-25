package fr.univavignon.courbes.echangeUDP;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerSocketConstructor {

   public static void main(String[] args) { // trouve un port de libre , s'arrete dessus ,
	   										//l'affiche (?) et créer un serveur dessus. 
      for(int port = 1; port <= 3000; port++){
         try {
            ServerSocket sSocket = new ServerSocket(port);
         } catch (IOException e) {
            System.err.println("Le port " + port + " est déjà utilisé ! ");
         }
      }
   }
}
