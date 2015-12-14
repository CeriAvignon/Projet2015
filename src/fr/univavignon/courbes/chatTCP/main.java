package fr.univavignon.courbes.chatTCP;


public class main {

	public static void main(String[] args) {
		 String host = "192.168.0.14";
	     int port = 2345;

	      ServerTCP ts = new ServerTCP(host, port);
	      ts.open();

	      /*System.out.println("Serveur initialisé.");*/

	         Thread t = new Thread(new clientTCP(host, port));
	         t.start();
	}

}
