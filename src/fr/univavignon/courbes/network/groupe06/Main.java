package fr.univavignon.courbes.network.groupe06;

public class Main {

	public static void main(String[] args) {
		
		Server S = new Server();
		S.launchServer();
		
		Client C = new Client();
		C.setIp(S.getIp());
		C.setPort(S.getPort());
		C.launchClient();
		C.sendText("lol");
		System.out.println("test");
		C.sendText("/quit");
		System.out.println("test2");
		//C.closeClient();

		
		
		/*Client C1 = new Client();
		C1.setIp(S.getIp());
		C1.setPort(S.getPort());
		C1.launchClient();
		C1.sendText("/quit");
		
		Client C2 = new Client();
		C2.setIp(S.getIp());
		C2.setPort(S.getPort());
		C2.launchClient();
		C2.sendText("/quit");*/
		

	}

}
