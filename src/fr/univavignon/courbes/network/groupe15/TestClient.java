package fr.univavignon.courbes.network.groupe15;

import java.io.IOException;

import fr.univavignon.courbes.common.Board;


/**
 * @author uapv1504059
 */
public class TestClient {

	/** Message à envoyer */
	public static String message;
	/** Le client lancé */
	public static Client c;
	
	/**
	 * @param args Arguments de la classe main
	 * @throws IOException Gestion des erreurs
	 * @throws ClassNotFoundException Classe non trouvée
	 */
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
		
		
		
		
		for(int i = 0; i<1; i++){
			c = new Client();
			c.setIp("10.122.39.43");
			c.setPort(3615);
			c.launchClient();
			Board b = c.retrieveBoard();
			
			System.out.println(b.height + " " + b.width);
			
		}
		
		//boolean serverClosed = false;
		//boolean clientClosed = false;
			
		
		
		
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
