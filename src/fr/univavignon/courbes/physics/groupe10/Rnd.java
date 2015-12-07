package fr.univavignon.courbes.physics.groupe10;

import java.util.HashMap;
import java.util.Map;

import fr.univavignon.courbes.physics.PhysicsEngine;

public class Rnd implements PhysicsEngine {
	
	Board br;
	
	
	@Override
	public Board init(int width, int height, int[] profileIds)
	{
		
		
		/** J'instancie un Board en utilisant le constructeur */
		br = new Board(width, height, profileIds);
		
		return br;
	}

	
	@Override
	public void update(long elapsedTime, Map<Integer,Direction> commands)
	{
		// Deplacer snakes "mettre à jour les snakes"
		//moveSnake(long, commands);
		
		//on trace le tracé et on test si il 

	}

	@Override
	public void forceUpdate(Board board) {
		// TODO Auto-generated method stub
		
	}

}
