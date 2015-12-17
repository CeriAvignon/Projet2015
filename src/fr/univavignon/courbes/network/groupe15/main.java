package fr.univavignon.courbes.network.groupe15;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;

import fr.univavignon.courbes.common.Board;


/**
 * @author uapv1504059
 */
public class main {

	/**
	 * @param args Arguments de la classe main.
	 */
	
	public static String message;
	public static Client c;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		/*System.out.println("Receiver Start");

        Socket sChannel = new Socket("10.122.13.24", 12345);
        if (sChannel != null) {

            ObjectInputStream ois = 
                     new ObjectInputStream(sChannel.getInputStream());

            String s = (String)ois.readObject();
            int i = (int)ois.readObject();
            System.out.println("String is: '" + s + "' " + i);
        }

        System.out.println("End Receiver");*/
		
		
		
		c = new Client();
		for(int i = 0; i<1; i++){
			c.setIp("10.122.13.24");
			c.setPort(3615);
			c.launchClient();
			
		}
		
		//boolean serverClosed = false;
		//boolean clientClosed = false;
			
		Board b = c.retrieveBoard();
		
		System.out.println(b.height + " " + b.width);
		
		
		/*Thread t = new Thread(new Runnable(){
			public void run(){
				Scanner sc = new Scanner(System.in);
				while(sc != null) {
					c.sendText(sc.nextLine());
				}
				sc.close();
			}
		});
		t.start();
		while(!serverClosed && !clientClosed){
			try{
				Thread.sleep(1000);
				message = c.retrieveText();
				if(!message.equals(null)){
					if(!message.equals("") && !message.equals("logout")){
						System.out.println(message);						
					} else if(message.equals("logout")) {
						c.closeClient();
						clientClosed = true;
					}
				} else {
					serverClosed = true;
				}
			} catch(Exception e){
				
			}
			
			
		}*/
	}

}
