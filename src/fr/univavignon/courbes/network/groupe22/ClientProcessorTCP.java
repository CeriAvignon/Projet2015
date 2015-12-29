package fr.univavignon.courbes.network.groupe22;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.util.Date;

public class ClientProcessorTCP extends ServerCom implements Runnable{

   private Socket sock;
   private PrintWriter writer = null;
   private BufferedInputStream reader = null;
   boolean connexion = true;

   public ClientProcessorTCP(Socket pSock){
      sock = pSock;
   }

   //Le traitement lancé dans un thread séparé
   @Override
   public void run(){
     //tant que la connexion est active, on traite les demandes

     try {
       writer = new PrintWriter(sock.getOutputStream());
       reader = new BufferedInputStream(sock.getInputStream());

       while(this.connexion) {

         String reponse = read();

         switch(reponse){
               case "/q":
                 connexion = false;
                 int i=0;

                 //On recherche dans le tableau le client concerné
                 while(ServerCom.tableauSocketClients[i] != sock )
                   i++;

                  //On le ferme
                 ServerCom.tableauSocketClients[i].close();
                 sock.close();
                 ServerCom.tableauSocketClients[i] = ServerCom.tableauSocketClients[ServerCom.nbClient - 1];
                 ServerCom.nbClient--;
                 System.err.println("LA CONNEXION A ETE INTERROMPUE ! ");
                 break;

               default :
                 System.out.println(reponse);
                 //writer.write(response);
                 break;
               }
       }
     } catch(SocketException e){
       System.err.println("LA CONNEXION A ETE INTERROMPUE ! ");

     } catch (IOException e) {
       e.printStackTrace();
     }

 }

   //La méthode que nous utilisons pour lire les réponses
   private String read() throws IOException{
      String reponse = "";
      int stream;
      byte[] b = new byte[4096];
      stream = reader.read(b);
      reponse = new String(b, 0, stream);
      return reponse;
   }

}
