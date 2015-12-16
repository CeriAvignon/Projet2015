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
		int idPlayers[] = {0,1};
		Round newRound = new Round(500,500,idPlayers);
		Map<Integer, Direction> command = new HashMap<Integer, Direction>();
		long timeRound = 1;
		
		for(int i = 0 ; i < 1000 ; i++ )
		{
			newRound.update(timeRound,command);
		}
	}
}
