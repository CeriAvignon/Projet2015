package fr.univavignon.courbes.network.groupe06;

import fr.univavignon.courbes.physics.PhysicsEngine;
import fr.univavignon.courbes.physics.groupe04.MyPhysicsEngine;
import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Item;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;

public class MainServer {

	public static void main(String[] args) {
		
		int tab[]={1,2,3};		// profileIDs
		MyPhysicsEngine a = new MyPhysicsEngine();//  EASIER FOR COLLISIONS
		a.ourBoard = a.init(800,600,tab);
		
		Server S = new Server();
		S.launchServer();
		
		System.out.println("Angle en degré : "+a.ourBoard.snakes[0].currentAngle+
				"\nAngle en degré : "+a.ourBoard.snakes[1].currentAngle+"\nAngle en degré :"
						+ " "+a.ourBoard.snakes[2].currentAngle);
		while(true) {
			S.sendBoard(a.ourBoard);
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
