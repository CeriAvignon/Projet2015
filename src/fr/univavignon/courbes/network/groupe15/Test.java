package fr.univavignon.courbes.network.groupe15;

import java.io.IOException;
import java.net.Socket;

import fr.univavignon.courbes.common.Board;


/**
 * @author uapv1504059
 */
public class Test {

	/**
	 * @param args Arguments de la classe main.
	 */
	public static void main(String[] args) throws IOException {
		Server s = new Server();
		s.setPort(3615);
		s.launchServer();
		
		Board b = new Board();
		b.width = 1920;
		b.height = 1080;
		
		while(s.getSockets().isEmpty());
		System.out.println("Board sent");
		s.sendBoard(b);
		// s.sendText(s.retrieveText()[0]);
		/*for(int i = 0; i<1; i++){
			Client c = new Client();
			c.setIp("127.0.0.1");
			//c.setIp("10.122.46.39");
			c.setPort(3615);
			c.launchClient();
		}*/
		
		/*System.out.println("Sender Start");

        int port = 12345;
        String obj = "yolo swag";
        
        ServerSocket ssChannel = new ServerSocket(port);
        
        while (true) {
            Socket sChannel = ssChannel.accept();

            ObjectOutputStream  oos = new ObjectOutputStream(sChannel.getOutputStream());
            oos.writeObject(obj);
            oos.flush();
            oos.writeObject(42);
            oos.flush();
            oos.close();

            System.out.println("Connection ended");
        }*/
	}

}
