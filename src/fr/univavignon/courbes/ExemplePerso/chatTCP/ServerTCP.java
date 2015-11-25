package fr.univavignon.courbes.chatTCP;

import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetSocketAddress;

public class ServerTCP {
	 //On initialise des valeurs par défaut
	   private int port = 2345;
	   private String host = "192.168.0.14";
	   private ServerSocket server = null;
	   private boolean isRunning = true;
	   public static Socket tableauSocket[] = new Socket[100];
	   public static int nbClient=0;
	   
	   public ServerTCP(){
	      try {
	         server = new ServerSocket(port, 100, InetAddress.getByName(host));
	      } catch (UnknownHostException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      }
	   }
	   
	   public ServerTCP(String pHost, int pPort){
	      host = pHost;
	      port = pPort;
	      try {
	         server = new ServerSocket(port, 100, InetAddress.getByName(host));
	      } catch (UnknownHostException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      }
	   }
	   
	   
	   //On lance notre serveur
	   public void open(){
	      
	      //Toujours dans un thread à part vu qu'il est dans une boucle infinie
	      Thread t = new Thread(new Runnable(){
	         public void run(){
	            while(isRunning == true){
	               
	               try {
	                  //On attend une connexion d'un client
	                  Socket client = server.accept();
	                  
	                  //Une fois reçue, on la traite dans un thread séparé
	                  System.out.println("Connexion cliente reçue.");
	                  
	                  tableauSocket[nbClient]=client;
	                  nbClient++;
	                  
	                  Thread t = new Thread(new ClientProcessor(client));
	                  t.start();
	                  
	               } catch (IOException e) {
	                  e.printStackTrace();
	               }
	            }
	            
	            try {
	               server.close();
	            } catch (IOException e) {
	               e.printStackTrace();
	               server = null;
	            }
	         }
	      });
	      
	      t.start();
	   }
	   
	   public void close(){
	      isRunning = false;
	   }  

}
