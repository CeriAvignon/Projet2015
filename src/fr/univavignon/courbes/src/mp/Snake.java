package fr.univavignon.courbes.src.mp;

import java.util.ArrayList;

public class Snake {
	private int idPlayer;
	private Position head;
	private float direction;
	private double ray; // radius head
	private double speed;
	private boolean state; // isAlive
	private boolean crashmode; // Nocollide
	private boolean invertmode;
	private boolean planemode;
	private double holerate;
	ArrayList changes; 

	public Snake(int id)
	{
		idPlayer 	= id;
		head 		= new Position(0,0);
		direction   = 0; 		
		ray 		= 1;  	
		speed 		= 1;		
		state 		= true;
		crashmode 	= false;
		invertmode  = false;
		planemode   = false;
		holerate 	= 1.0;			
		changes = new ArrayList();
	}
	
	/**
	 * @param strength Force sur un un coté ou l'autre du snake
	 */
	public void changeDirection(float strength)
	{
		
	}
	
	/**
	 * @param posSnake Position du Snake a tester
	 * @param posItem  Position de l'item a tester
	 * @return true si le snake est sur l'objet, sinon false
	 */
	public boolean snakeOnItem(Position posSnake, Position posItem)
	{
		return false;

	}
}

