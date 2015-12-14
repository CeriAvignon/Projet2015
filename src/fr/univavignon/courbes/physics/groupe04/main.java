package fr.univavignon.courbes.physics.groupe04;


import java.awt.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Item;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;
import fr.univavignon.courbes.physics.PhysicsEngine;
import fr.univavignon.courbes.physics.groupe04.MyPhysicsEngine;


public class main {


	
	public static void main(String[] args) {
		int tab[]={1,2,3};		// profileIDs
		MyPhysicsEngine a = new MyPhysicsEngine(800,600,tab);//  EASIER FOR COLLISIONS
		Map<Integer,Direction> com = new HashMap<Integer,Direction>();
		com.put(1, Direction.LEFT);
		com.put(2, Direction.NONE);
		com.put(3, Direction.RIGHT);
		a.ourBoard.snakes[0].currentAngle = 180;
		a.ourBoard.snakes[1].currentAngle = 0;
		a.ourBoard.snakes[2].currentAngle = 90; 
		System.out.println("Angle en degré : "+a.ourBoard.snakes[0].currentAngle+"\nAngle en degré : "+a.ourBoard.snakes[1].currentAngle+"\nAngle en degré : "+a.ourBoard.snakes[2].currentAngle);
		long t = 1;
		
		for(int i = 0 ; i < 50 ; i++ )
		{
			a.update(t,com);
		}
		
		System.out.println("Angle en degré : "+a.ourBoard.snakes[0].currentAngle+"\nAngle en degré : "+a.ourBoard.snakes[1].currentAngle+"\nAngle en degré : "+a.ourBoard.snakes[2].currentAngle);
	}
	

}
