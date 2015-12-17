package fr.univavignon.courbes.network.groupe05;


/**
 * @author uapv1504059
 */
public class main {

	/**
	 * @param args Arguments de la classe main.
	 */
	public static void main(String[] args) {
		Server s = new Server();
		s.setPort(3615);
		s.launchServer();
		for(int i = 0; i<1; i++){
			Client c = new Client();
			c.setIp("127.0.0.1");
			//c.setIp("10.122.46.39");
			c.setPort(3615);
			c.launchClient();
		}
	}

}
