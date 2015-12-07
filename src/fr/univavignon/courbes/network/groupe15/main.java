package fr.univavignon.courbes.network.groupe15;

public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Client c = new Client();
		c.initConnection("10.122.46.39", 3615);
		//c.initConnection("127.0.0.1", 3615);

	}

}
