package fr.univavignon.courbes.network.groupe15;

/**
 * @author uapv1504059
 */
public class main {

	/**
	 * @param args Arguments de la classe main.
	 */
	public static void main(String[] args) {
		Client c = new Client();
		//c.setIp("127.0.0.1");
		c.setIp("10.122.46.39");
		c.setPort(3615);
		c.launchClient();

	}

}
