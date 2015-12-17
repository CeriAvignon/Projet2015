package fr.univavignon.courbes.network.groupe06;

import fr.univavignon.courbes.physics.groupe04.MyPhysicsEngine;


public class MainServer {

	public static void main(String[] args) {
		
		int tab[]={1,2,3};		// profileIDs
		MyPhysicsEngine a = new MyPhysicsEngine();
		a.ourBoard = a.init(800,600,tab);
		
		Server S = new Server();
		S.launchServer();
		
		System.out.println("Angle en degré : "+a.ourBoard.snakes[0].currentAngle+
				"\nAngle en degré : "+a.ourBoard.snakes[1].currentAngle+"\nAngle en degré :"
						+ " "+a.ourBoard.snakes[2].currentAngle);
		while(true) {
			S.sendBoard(a.ourBoard);
			a = new MyPhysicsEngine();
			a.ourBoard = a.init(800,600,tab);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
