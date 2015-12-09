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
		
		MyPhysicsEngine a = new MyPhysicsEngine();
		int tab[]={1,2,3};
		Board board = new Board();
		board = a.init(800,600,tab);
		
		long t = 15;
		
		a.updateSnakesPositions(t);
	}
	

}
