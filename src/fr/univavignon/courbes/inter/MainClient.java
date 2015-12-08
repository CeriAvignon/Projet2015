package fr.univavignon.courbes.inter;

import fr.univavignon.courbes.network.groupe20.Client;

public class MainClient {
	public static void main(String[] args) {
		final Client c = new Client();
		c.launchClient();
		new Thread(new Runnable() {
			public void run() {
				while(true){
					if(c.retrieveProfiles() != null){
						System.out.println(c.retrieveProfiles().size());
					}else
						break;
				}
			}
		}).start();
		
	}
}
