package fr.univavignon.courbes.echangeUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ClientUDP {

   public final static int port = 2345;
   
   public static void main(String[] args){
    
      
      Thread cli1 = new Thread(new UDPClient("Cysboy", 1000));
      Thread cli2 = new Thread(new UDPClient("John-John", 10000));
      Thread cli3 = new Thread(new UDPClient("Loic", 10000));
      
      cli1.start();
      cli2.start();
      cli3.start();
      
   }
   
   public static synchronized void print(String str){
      System.out.print(str);
   }
   public static synchronized void println(String str){
      System.err.println(str);
   }
   
   
  public static class UDPClient implements Runnable{
      String name = "";
      long sleepTime = 1000;
      
      public UDPClient(String pName, long sleep){
         name = pName;
         sleepTime = sleep;
      }
      
      public void run(){
         int nbre = 0;
         while(true){
            String envoi = name + "-" + (++nbre);
            byte[] buffer = envoi.getBytes();
            
            try {
               //On initialise la connexion c�t� client
               DatagramSocket client = new DatagramSocket();
               
               //On cr�e notre datagramme
               InetAddress adresse = InetAddress.getByName("10.104.29.115");
               DatagramPacket packet = new DatagramPacket(buffer, buffer.length, adresse, port);
               
               //On lui affecte les donn�es � envoyer
               packet.setData(buffer);
               
               //On envoie au serveur
               client.send(packet);
               
               //Et on r�cup�re la r�ponse du serveur
               byte[] buffer2 = new byte[8196];
               DatagramPacket packet2 = new DatagramPacket(buffer2, buffer2.length, adresse, port);
               client.receive(packet2);
               print(envoi + " a re�u une r�ponse du serveur : ");
               println(new String(packet2.getData()));
               
               try {
                  Thread.sleep(sleepTime);
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
               
            } catch (SocketException e) {
               e.printStackTrace();
            } catch (UnknownHostException e) {
               e.printStackTrace();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }      
   }   
}