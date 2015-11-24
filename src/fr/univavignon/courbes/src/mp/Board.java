package fr.univavignon.courbes.src.mp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board {
	
	HashMap<Position, Integer > mapTrace = new HashMap<Position, Integer >(); 
	HashMap<Position, Item > mapItem = new HashMap<Position, Item >();
	
	public ArrayList<Snake> listSnake = new ArrayList<Snake>();
	
	public HashMap<Position, Integer > getMapTrace() { return mapTrace; }
	public HashMap<Position, Item > getMapItem() { return mapItem; }
	
	void buildSnakeList(int nbSnake)
	{

		for (int i = 1; i <= nbSnake; i++)
		{
			Position posSpawn = new Position(200+(25*i),200);
			listSnake.add(new Snake(i, posSpawn));
			updateSnakePosition()
		}
	}
	
	void spawnSnakeOnBoard(ArrayList<Snake> listSnake)
	{
		
	}
	
	
	/**
	 * @param snake
	 * @param currentPos
	 */
	void updateSnakePosition(Snake snake)
	{
		//Utiliser la speed,direction, et pos  pour faire evoluer la Pos
		//Mettre la old Pos du Snake dans la hash map tracé
		
	}
	
	/**
	 * @param posSnake Position du Snake a tester
	 * @return l'item si le snake est sur un item, sinon null
	 */
	public Item snakeOnItem(Position posSnake)
	{
		return mapItem.get(posSnake);
	}
}
