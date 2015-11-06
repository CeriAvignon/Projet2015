package fr.univavignon.courbes.src.mp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board {
	
	HashMap<Position, Integer > mapTrace = new HashMap<Position, Integer >(); 
	HashMap<Position, Item > mapItem = new HashMap<Position, Item >();
	
	public ArrayList<Snake> listSnake = new ArrayList<Snake>();
	
	
	void buildSnakeList()
	{
		
	}
	
	void spawnSnakeOnBoard(ArrayList<Snake> listSnake)
	{
		
	}
	
	
	/**
	 * @param snake
	 * @param currentPos
	 */
	void updateSnakePosition(Snake snake, Position currentPos)
	{
		//Utiliser la speed,direction, et pos  pour faire evoluer la Pos
		//Mettre la old Pos du Snake dans la hash map tracé
	}
}
