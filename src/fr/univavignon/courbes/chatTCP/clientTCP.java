package fr.univavignon.courbes.chatTCP;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

public class clientTCP implements Runnable{

   private Socket connexion = null;
   private PrintWriter writer = null;
   private BufferedInputStream reader = null;
   
   private static int count = 0;
   private String name = "";   
   
   public clientTCP(String host, int port){
	  System.out.println("Entrez Votre nom:");
	  Scanner sc = new Scanner(System.in);
	  name = sc.nextLine();
      try {
         connexion = new Socket(host, port);
      } catch (UnknownHostException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   
   
   public void run(){
	   String message = "";
	   Thread t = new Thread(new Runnable(){
		   public void run(){
			   while(true){
			   try{
				   String response = read();
				   System.out.println(response);
			   }catch (IOException e1) {
               e1.printStackTrace();
			   }
			   }
		   }
	   });
	   
	   
	  
         try {
            writer = new PrintWriter(connexion.getOutputStream(), true);
            reader = new BufferedInputStream(connexion.getInputStream());
            t.start();
         } catch (IOException e1) {
             e1.printStackTrace();
          }
            
            while(message != "/quit"){
     		   Scanner sc = new Scanner(System.in);
     		   message= name+" :";
     		   message += sc.nextLine();
            //On envoie le message au serveur
            
            
            writer.write(message);
            writer.flush();  
          //  String response = read();
			 //  System.out.println(response);
           // System.out.println("Commande " + commande + " envoyée au serveur");
            
            
        
	   }
   
      writer.close();
   }
   
   //Méthode pour lire les réponses du serveur
   private String read() throws IOException{      
      String response = "";
      int stream;
      byte[] b = new byte[4096];
      stream = reader.read(b);
      response = new String(b, 0, stream);      
      return response;
   }   
}
