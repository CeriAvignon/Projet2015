package fr.univavignon.courbes.inter;

import java.util.ArrayList;
import java.util.List;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.network.groupe20.Server;

public class MainServer {

	public static void main(String[] args) {
		Profile p1 = new Profile();
		List<Profile> profiles = new ArrayList<>();
		
			p1.profileId = 1;
			p1.score = 100;
			p1.email = "fff";
			p1.userName = "alaoui";
			p1.password = "1";
			p1.country = "maroc";
			p1.timeZone = "";
		
		profiles.add(p1);
		Server s = new Server(1117);
		s.launchServer();
		while(true){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			s.sendPlayers(profiles);
			
		}

	}

}
