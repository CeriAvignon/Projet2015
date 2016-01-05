package fr.univavignon.courbes.network.groupe15;

import java.io.IOException;

import fr.univavignon.courbes.common.Board;


/**
 * @author uapv1301073
 */
public class TestServer {

	/**
	 * @param args Arguments de la classe main
	 * @throws IOException Gestion des erreurs
	 * @throws ClassNotFoundException Classe non trouvée
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
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
