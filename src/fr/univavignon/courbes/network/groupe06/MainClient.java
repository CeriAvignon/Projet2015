package fr.univavignon.courbes.network.groupe06;

import fr.univavignon.courbes.physics.PhysicsEngine;
import fr.univavignon.courbes.physics.groupe04.MyPhysicsEngine;
import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Item;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;

public class MainClient {

	public static void main(String[] args) {
		
		int tab2[]={1,2,3};		// profileIDs
		MyPhysicsEngine b = new MyPhysicsEngine();//  EASIER FOR COLLISIONS
		b.ourBoard = b.init(800,600,tab2);
		b.ourBoard.snakes[0].currentAngle = 180;
		b.ourBoard.snakes[1].currentAngle = 0;
		b.ourBoard.snakes[2].currentAngle = 90;
		
		Client C = new Client();
		C.setIp("192.168.0.14");
		C.setPort(2345);
		C.launchClient();
		
		System.out.println("Angle en degré : "+b.ourBoard.snakes[0].currentAngle+
				"\nAngle en degré : "+b.ourBoard.snakes[1].currentAngle+"\nAngle en degré :"
						+ " "+b.ourBoard.snakes[2].currentAngle);

		while(true) {
			b.ourBoard = C.retrieveBoard();
			if (b.ourBoard != null) {
				System.out.println("Angle en degré : "+b.ourBoard.snakes[0].currentAngle+
				"\nAngle en degré : "+b.ourBoard.snakes[1].currentAngle+"\nAngle en degré :"
						+ " "+b.ourBoard.snakes[2].currentAngle);
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		//C.sendText("/quit");
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
