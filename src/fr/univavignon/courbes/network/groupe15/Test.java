package fr.univavignon.courbes.network.groupe15;

import fr.univavignon.courbes.common.Board;


/**
 * @author uapv1504059
 */
public class Test {

	/**
	 * @param args Arguments de la classe main.
	 */
	public static void main(String[] args) {
		Server s = new Server();
		s.setPort(3615);
		s.launchServer();
		
		Board b = new Board();
		b.width = 1920;
		b.height = 1080;
		
		while(s.getSockets().isEmpty());

		s.sendBoard(b);
		// s.sendText(s.retrieveText()[0]);
		/*for(int i = 0; i<1; i++){
			Client c = new Client();
			//c.setIp("10.122.46.39");
			c.setPort(3615);
			c.launchClient();
		}*/
	}

}
