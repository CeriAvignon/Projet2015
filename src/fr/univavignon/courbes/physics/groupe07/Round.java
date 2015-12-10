package fr.univavignon.courbes.physics.groupe07;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Item;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;
import fr.univavignon.courbes.physics.PhysicsEngine;

public class Round 
{
	public void createSnake(Snake snake, int id, Position spawnLocation)
	{
		snake.currentItems = new HashMap<Item,Long>();
		snake.playerId = id;
		snake.currentX = spawnLocation.x;
		snake.currentY = spawnLocation.y;
	}
}
