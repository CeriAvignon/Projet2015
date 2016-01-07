package fr.univavignon.courbes.network.groupe06;

import java.util.ArrayList;
import java.util.List;

import fr.univavignon.courbes.common.Profile;



public class MainServer {

	public static void main(String[] args) {
		
		Server S = new Server();
		S.launchServer();
		
		/*int tab[]={1,2,3};		// profileIDs
		MyPhysicsEngine a = new MyPhysicsEngine();
		a.ourBoard = a.init(800,600,tab);

		System.out.println("Angle en degré : "+a.ourBoard.snakes[0].currentAngle+
				"\nAngle en degré : "+a.ourBoard.snakes[1].currentAngle+"\nAngle en degré :"
						+ " "+a.ourBoard.snakes[2].currentAngle);
		while(true) {
			S.sendBoard(a.ourBoard);
			//a = new MyPhysicsEngine();
			a.ourBoard = a.init(800,600,tab);
			try {
				Thread.sleep(0050);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
		List<Profile> profiles = new ArrayList<Profile>();
		 Profile J1 = new Profile();
		 
		 profiles.add(J1);
		 profiles.add(J1);
		 String text="i";
		while(true){
			 text = text+text;
			 S.sendText(text);
		 try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		 S.sendProfiles(profiles);
		 profiles.add(J1);

		}
		
		
	}

}
