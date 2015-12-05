package fr.univavignon.courbes.ExemplePerso.echangeUDP;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerSocketConstructor {

   public static void main(String[] args) { // trouve un port de libre , s'arrete dessus ,
	   int port2=0;							//l'affiche (?) et créer un serveur dessus. 
      for(int port = 1; port <= 3000; port++){
         try {
            ServerSocket sSocket = new ServerSocket(2345);
            port2=2345;
            break;
         } catch (IOException e) {
            System.err.println("Le port " + port + " est déjà utilisé ! ");
         }
      }
      System.out.println(port2);
   }
}
