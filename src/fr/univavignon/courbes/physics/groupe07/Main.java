package fr.univavignon.courbes.physics.groupe07;

import java.util.HashMap;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Item;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;
import fr.univavignon.courbes.physics.PhysicsEngine;
import fr.univavignon.courbes.physics.groupe07.Round;

public class Main
{
	
	public static void main(String[] args)
	
	{
		int players[] = {1,2,3};
		Round firstRound = new Round(100,100,players);
		Map<Integer,Direction> command = new HashMap<Integer,Direction>();
		
		command.put(0, Direction.LEFT);
	}
}
